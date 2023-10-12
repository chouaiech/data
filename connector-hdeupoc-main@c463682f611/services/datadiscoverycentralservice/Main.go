package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"strings"
	"time"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
	lib_controller "code.europa.eu/healthdataeu-nodes/hdeupoc/libs/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscoverycentralservice/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscoverycentralservice/services"

	"github.com/gorilla/mux"
)

var dataDiscoveryCentralService services.DataDiscoveryCentralService
var dataDiscoveryCentralController controller.DataDiscoveryCentralController

var fairdatapointClient clients.FairDataPointClient

var conf *config.Config
var topicSubscriber clients.TopicSubscriber
var messageControler lib_controller.MessageController

func main() {
	conf = config.ReadConfig()
	fairdatapointClient = clients.FairDataPointClient{
		UseHttps: conf.Fairdatapoint.UseHttps,
		Url:      strings.TrimSpace(conf.Fairdatapoint.Url),
		Username: strings.TrimSpace(conf.Fairdatapoint.Username),
		Port: 	  strings.TrimSpace(conf.Fairdatapoint.Port),
	}

	catByAP, err := readAPCatalog()
	if err != nil {
		fmt.Printf("[LOG] read AP Catalog error: %+v\n", err)
		return
	}

	createServices()
	createControllers(catByAP)

	r := mux.NewRouter()
	messageControler.Attach(r)
	r.PathPrefix("/").HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
		fmt.Printf("%v", r.URL)
	})

	subscribeToTopics(conf)
	log.Fatal(http.ListenAndServe(":"+strings.TrimSpace(conf.DataDiscoveryCentralService.Port), r))
}

func createServices() {
	service := services.CreateDataDiscoveryCreateCentralService(fairdatapointClient)
	dataDiscoveryCentralService = *service
}

func createControllers(catByAp map[string]string) {
	dataDiscoveryCentralController, err := controller.CreateDataDiscoveryCentralController(dataDiscoveryCentralService, catByAp)
	if err != nil {
		fmt.Printf("[ERROR] creating controller: %v\n", err)
		os.Exit(2)
	}
	messageControler = lib_controller.MessageController{
		MessageHandler: dataDiscoveryCentralController.MessageHandler,
	}
}

func subscribeToTopics(conf *config.Config) {
	go func() {
		for {
			// TODO: Get the message checker ip from the service discovery
			subscriptionUrl := strings.TrimSpace(conf.MessageChecker.Url) + ":" + strings.TrimSpace(conf.MessageChecker.Port)
			// TODO: Uncomment, if centralservice should support https
			// var protocol string
			// if conf.DataDiscoveryCentralService.UseHttps {
			// 	protocol = "https://"
			// } else {
			// 	protocol = "http://"
			// }
			selfUrl := "http://" + strings.TrimSpace(conf.DataDiscoveryCentralService.Url) + ":" + strings.TrimSpace(conf.DataDiscoveryCentralService.Port)
			fmt.Printf("Subscribe %v to message checker: %v\n", selfUrl, subscriptionUrl)
			err := topicSubscriber.SubscribeToTopic(clients.UpdateCentralEuCatalog, subscriptionUrl, selfUrl, conf.MessageChecker.UseHttps)
			if err == nil {
				fmt.Printf("Successfully subscribed to topic\n")
				break
				// TODO: Handle what happens if the message checker dies- then we never receive a message and we dont know
			}

			fmt.Printf("Couldnt subscribe to topic because %v\n", err)

			time.Sleep(5 * time.Second)
		}
	}()
}

func readAPCatalog() (map[string]string, error) {
	root, err := os.Getwd()
	if err != nil {
		return nil, fmt.Errorf("getting working directory: %w", err)
	}

	path := root + "/ap_catalog.json"
	b, err := os.ReadFile(path)
	if err != nil {
		return nil, fmt.Errorf("reading %q: %w", path, err)
	}

	apByCatalog := make(map[string]string)
	err = json.Unmarshal(b, &apByCatalog)
	if err != nil {
		return nil, fmt.Errorf("unmarshaling %q: %w", path, err)
	}
	fmt.Printf("[LOG] APCatalog: %v\n", apByCatalog)
	return apByCatalog, nil
}
