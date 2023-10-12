package controller

import (
	"fmt"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/services"

	"github.com/gorilla/mux"
)

// A PermitAddress contains the URL and the port of a datapermit.
type PermitAddress struct {
	URL      string
	Port     string
	UseHttps bool
}

type DataPermitController struct {
	DataPermitService services.DataPermitServiceInterface
}

func CreateDataPermitController(dataPermitService services.DataPermitServiceInterface) DataPermitController {

	dataPermitController := DataPermitController{
		DataPermitService: dataPermitService,
	}
	return dataPermitController

}

func (dpc DataPermitController) Attach(r *mux.Router) {
	routerSet := r.PathPrefix("/datapermit").Subrouter()
	routerSet.HandleFunc("", dpc.getHandler).Methods("GET")
}

func (dpc DataPermitController) getHandler(rw http.ResponseWriter, req *http.Request) {
	json, err := dpc.DataPermitService.GetAllDataPermits()
	fmt.Printf("[LOG] DataPermitController json: %s\n", json)
	rw.Header().Set("Content-Type", "application/json")
	if err != nil {
		rw.WriteHeader(http.StatusNoContent)
		return
	}
	rw.Header().Set("Content-Type", "application/json")
	rw.Write(json)
}

func (dpc DataPermitController) Add(json_form string) {
	dpc.DataPermitService.AddDataPermit(json_form)
}

func (dpc DataPermitController) MessageHandler(messageList clients.MessageList) error {
	fmt.Printf("[LOG] Received message.  Type: %v\n", messageList.Type)
	if messageList.Type == clients.DataPermit {
		clients.ProcessMessageList(messageList, dpc.ProcessMessage)
	}
	return nil
}

func (dpc DataPermitController) ProcessMessage(message, from string) error {
	dpc.Add(string(message))
	return nil
}
