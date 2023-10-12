package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"strings"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermitcentralservice/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermitcentralservice/services"

	"github.com/gorilla/mux"
)

var dataPermitCentralService services.DataPermitCentralServiceInterface

var dataPermitCentralController controller.DataPermitCentralController

var soapClient clients.SoapClient

func main() {
	conf := config.ReadConfig()
	fmt.Printf("Config: %v\n", conf)
	soapClient = clients.SoapClient{
		Url:      conf.Domibus.Url,
		Backend:  "/domibus/services/backend",
		Username: conf.Domibus.Username,
		Port:     conf.Domibus.Port,
	}

	catByAP, err := readAPCatalog()
	if err != nil {
		fmt.Println(err)
	}

	createServices(conf, catByAP)
	createControllers()

	r := mux.NewRouter()
	dataPermitCentralController.Attach(r.PathPrefix("/datapermit").Subrouter())
	r.PathPrefix("/").HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
		fmt.Printf("%v", r.URL)
	})

	fmt.Println("Starting the server")
	log.Fatal(http.ListenAndServe(":"+strings.TrimSpace(conf.DataPermitCentralService.Port), r))
	fmt.Println("The server is dying")
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

	// Invert the map.
	catByAP := make(map[string]string)
	for ap, catalog := range apByCatalog {
		catByAP[catalog] = ap
	}

	return catByAP, nil
}

func createControllers() {
	dataPermitCentralController = controller.DataPermitCentralController{
		DataPermitCentralService: dataPermitCentralService,
	}
}

func createServices(conf *config.Config, catByAP map[string]string) {
	fmt.Printf("createServices %v\n", catByAP)
	dataPermitCentralService = services.CreateDataPermitCentralService(conf, catByAP, soapClient)

}
