package controller

import (
	"fmt"
	"io"
	"log"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/messagechecker/services"

	"github.com/gorilla/mux"
)

type SubscriptionController struct {
	SubscriptionService services.SubscriptionService
}

func (sc SubscriptionController) Attach(r *mux.Router) {
	routerSet := r.PathPrefix("/topics/{topic}").Subrouter()
	routerSet.HandleFunc("", sc.subscribeTopic).Methods("POST")
	// routerSet.HandleFunc("", sc.unsubscribeTopic).Methods("DELETE")
}

func (sc SubscriptionController) subscribeTopic(rw http.ResponseWriter, req *http.Request) {
	params := mux.Vars(req)
	topic := params["topic"]
	//TODO: Some validation

	body, err := io.ReadAll(req.Body)
	if err != nil {
		log.Printf("Error reading body: %v", err)
		return
	}

	//TODO: It shouldnt be IP address, instead some identifier that can be resolved by service discovery
	err = sc.SubscriptionService.Subscribe(services.IPAddress(body), services.Topic(topic))
	if err != nil {
		fmt.Printf("Something bad happened during the subscription process on topic %v\n", topic)
		fmt.Println(err)
		rw.WriteHeader(http.StatusBadRequest)
	}
}
