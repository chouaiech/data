package main

import (
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
	lib_controller "code.europa.eu/healthdataeu-nodes/hdeupoc/libs/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/repository"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/services"

	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
)

var conf *config.Config
var topicSubscriber clients.TopicSubscriber
var messageControler lib_controller.MessageController

var dataPermitService services.DataPermitServiceInterface
var dataPermitController controller.DataPermitController
var dataPermitRepository repository.DataPermitRepositoryInterface

func main() {
	conf = config.ReadConfig()
	createRepositories()
	dataPermitRepository.CreateTableIfNotExists()
	defer dataPermitRepository.CloseDb()
	createServices()
	createControllers()

	r := mux.NewRouter()
	messageControler.Attach(r)
	dataPermitController.Attach(r)
	r.PathPrefix("/").HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
		fmt.Printf("%v", r.URL)
	})

	subscribeToTopics(conf)
	if conf.DataPermit.UseHttps {
		var crt = "/etc/ssl/localcerts/" + conf.DataPermit.CrtFileName
		var key = "/etc/ssl/localcerts/" + conf.DataPermit.KeyFileName
		fmt.Printf("[LOG] crt file: %s:\n", crt)
		fmt.Printf("[LOG] key file: %s:\n", key)
		log.Fatal(http.ListenAndServeTLS(":"+strings.TrimSpace(conf.DataPermit.Port), crt, key, r))
	} else {
		log.Fatal(http.ListenAndServe(":"+strings.TrimSpace(conf.DataPermit.Port), r))
	}

}

func subscribeToTopics(conf *config.Config) {
	go func() {
		for {
			// TODO: Get the message checker ip from the service discovery
			subscription := strings.TrimSpace(conf.MessageChecker.Url) + ":" + strings.TrimSpace(conf.MessageChecker.Port)

			var protocol string
			if conf.DataPermit.UseHttps {
				protocol = "https://"
			} else {
				protocol = "http://"
			}
			myurl := protocol + strings.TrimSpace(conf.DataPermit.Url) + ":" + strings.TrimSpace(conf.DataPermit.Port)
			fmt.Printf("Subscribe to message checker: %s\n", subscription)
			fmt.Printf("My url (DataPermit): %s\n", myurl)
			err := topicSubscriber.SubscribeToTopic(clients.DataPermit, subscription, myurl, conf.MessageChecker.UseHttps)
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

func createRepositories() {
	dataPermitRepository = repository.CreateDataPermitRepository()
}

func createServices() {
	dataPermitService = services.CreateDataPermitService(dataPermitRepository)
}

func createControllers() {
	dataPermitController = controller.CreateDataPermitController(dataPermitService)
	messageControler = lib_controller.MessageController{
		MessageHandler: dataPermitController.MessageHandler,
	}
}
