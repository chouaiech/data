package controller

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"

	"github.com/gorilla/mux"
)

type MessageController struct {
	// TODO: The Message Struct should not be defined in clients
	MessageHandler func(clients.MessageList) error
}

func (mc MessageController) Attach(r *mux.Router) {
	routerSet := r.PathPrefix("/message").Subrouter()
	routerSet.HandleFunc("", mc.receiveMessage).Methods("POST")
}

func (mc MessageController) receiveMessage(rw http.ResponseWriter, req *http.Request) {
	body, err := io.ReadAll(req.Body)
	if err != nil {
		log.Printf("Error reading body: %v", err)
		rw.WriteHeader(http.StatusBadRequest)
		return
	}
	//TODO: This should not be fully parsed here. Only the outer shell, with the topic
	var messageList clients.MessageList
	err = json.Unmarshal(body, &messageList)
	if err != nil {
		fmt.Println(err)
		rw.WriteHeader(http.StatusBadRequest)
		return
	}

	mc.MessageHandler(messageList)
}
