package main

import (
	"fmt"
	"log"
	"log/slog"
	"net/http"
	"os"
	"strings"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/apikey"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/mid"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscovery/services"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/gateway/handlers"
)

func main() {
	slog := slog.New(slog.NewTextHandler(os.Stdout, nil))
	conf := config.ReadConfig()

	soapClient := clients.SoapClient{
		Url:      conf.Domibus.Url,
		Backend:  "/domibus/services/backend",
		Username: conf.Domibus.Username,
		Port:     conf.Domibus.Port,
	}

	discoveryConfig := services.CreateDataDiscoveryService(conf, soapClient)

	permitConfig := controller.PermitAddress{
		URL:      conf.DataPermit.Url,
		Port:     conf.DataPermit.Port,
		UseHttps: conf.DataPermit.UseHttps,
	}

	root, err := os.Getwd()
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}

	path := root + "/keys/apikeys.json"
	authorizedKeys, err := apikey.ReadAll(path)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}

	auth := mid.Auth{
		Keys: authorizedKeys,
	}

	cfgMux := handlers.APIMuxConfig{
		Log:       slog,
		Auth:      auth,
		Discovery: *discoveryConfig,
		Permit:    permitConfig,
	}

	apiMux := handlers.APIMux(cfgMux)

	slog.Info("starting server...")

	if conf.Gateway.UseHttps {
		fmt.Println("[LOG] Starting the server (https)")
		var crt = "/etc/ssl/localcerts/" + conf.Gateway.CrtFileName
		var key = "/etc/ssl/localcerts/" + conf.Gateway.KeyFileName
		log.Fatal(http.ListenAndServeTLS(":"+strings.TrimSpace(conf.Gateway.Port), crt, key, apiMux))
	} else {
		fmt.Println("[LOG] Starting the server (http)")
		log.Fatal(http.ListenAndServe(":"+strings.TrimSpace(conf.Gateway.Port), apiMux))
	}
	if err != nil {
		log.Fatalf("server down: %v", err)
	}
}
