package main

import (
	"fmt"
	"log"
	"net/http"
	"strings"
	"time"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/messagechecker/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/messagechecker/services"

	"github.com/gorilla/mux"
)

var subscriptionController controller.SubscriptionController

var messageCheckerService services.MessageCheckerServiceInterface
var subscriptionService services.SubscriptionService //TODO: do we need an interface for this?

var soapClient clients.SoapClient

var conf *config.Config

func main() {
	conf = config.ReadConfig()
	soapClient = clients.SoapClient{
		Url:      conf.Domibus.Url,
		Backend:  "/domibus/services/wsplugin",
		Username: conf.Domibus.Username,
		Port:     conf.Domibus.Port,
	}
	createServices()
	createControllers()

	go func() {
		for {
			checkError := messageCheckerService.Check()
			if checkError != nil {
				fmt.Printf("Main Response error: %v\n", checkError)
			}
			time.Sleep(1000 * time.Millisecond)
		}
	}()

	r := mux.NewRouter()
	subscriptionController.Attach(r)

	fmt.Printf("Config: %v\n", conf)
	fmt.Printf("Starting the messagechecker server. Port: %s\n", conf.MessageChecker.Port)

	if conf.MessageChecker.UseHttps {
		var crt = "/etc/ssl/localcerts/" + conf.MessageChecker.CrtFileName
		var key = "/etc/ssl/localcerts/" + conf.MessageChecker.KeyFileName
		log.Fatal(http.ListenAndServeTLS(":"+strings.TrimSpace(conf.MessageChecker.Port), crt, key, r))
	} else {
		log.Fatal(http.ListenAndServe(":"+strings.TrimSpace(conf.MessageChecker.Port), r))
	}
	fmt.Printf("The messagechecker server is dying\n")
}

func createControllers() {
	subscriptionController = controller.SubscriptionController{
		SubscriptionService: subscriptionService,
	}
}

func createServices() {
	subscriptionService = services.SubscriptionService{
		Subscribers: map[services.Topic]map[services.IPAddress]struct{}{},
	}

	messageCheckerService = services.MessageCheckerService{
		SoapClient:          soapClient,
		SubscriptionService: subscriptionService,
	}
}
