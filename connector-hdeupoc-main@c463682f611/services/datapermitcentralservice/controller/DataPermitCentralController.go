package controller

import (
	"fmt"
	"io"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermitcentralservice/services"

	"github.com/gorilla/mux"
)

type DataPermitCentralController struct {
	DataPermitCentralService services.DataPermitCentralServiceInterface
}

func CreateDataPermitCentralController(dataPermitCentralService services.DataPermitCentralServiceInterface) (*DataPermitCentralController, error) {

	dataPermitCentralController := &DataPermitCentralController{
		DataPermitCentralService: dataPermitCentralService,
	}
	return dataPermitCentralController, nil

}

func (controller DataPermitCentralController) Attach(r *mux.Router) {
	routerSet := r.PathPrefix("/permit").Subrouter()
	routerSet.HandleFunc("", controller.addHandler).Methods("POST")
	routerSet.HandleFunc("/debug/sendallbytopic", controller.sendAllByTopicHandler).Methods("GET")	
    routerSet.HandleFunc("", controller.optionsHandler).Methods("OPTIONS")
}
func (controller DataPermitCentralController) addHandler(rw http.ResponseWriter, req *http.Request) {
	body, err := io.ReadAll(req.Body)
	if err != nil {
		fmt.Printf("Error: %s", err)
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	rw.Header().Set("Access-Control-Allow-Origin", "*")
	controller.DataPermitCentralService.Add(body)
}

func (controller DataPermitCentralController) sendAllByTopicHandler(rw http.ResponseWriter, req *http.Request) {
	controller.DataPermitCentralService.SendAllByTopic()
}

func (controller DataPermitCentralController) optionsHandler(rw http.ResponseWriter, req *http.Request) {

    rw.Header().Set("Access-Control-Allow-Origin", "*")

    rw.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS, GET")

    rw.Header().Set("Access-Control-Allow-Headers", "Content-Type")

    rw.WriteHeader(http.StatusNoContent)  

}
