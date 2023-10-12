// Package v1 contains the full set of handler functions and routes
// supported by the v1 web api.
package v1

import (
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscovery/services"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/controller"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/gateway/handlers/discoverygrp"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/gateway/handlers/permitgrp"

	"github.com/gorilla/mux"
)

// Config contains all the mandatory systems required by handlers.
type Config struct {
	Discovery services.DataDiscoveryService
	Permit    controller.PermitAddress
}

// Routes binds all routes.
func Routes(router *mux.Router, cfg Config) {
	//==========================================================================
	// DataDiscovery

	dgh := discoverygrp.New(cfg.Discovery)
	dRouter := router.PathPrefix("/discovery/dataset").Subrouter()

	dRouter.HandleFunc("", dgh.Create).Methods(http.MethodPost)
	dRouter.HandleFunc("", dgh.Update).Methods(http.MethodPut)
	dRouter.HandleFunc("/delete", dgh.Delete).Methods(http.MethodPost)
	dRouter.HandleFunc("/debug/sendallbytopic", dgh.SendAllByTopic).Methods(http.MethodGet)

	//==========================================================================
	// DataPermit

	pgh := permitgrp.New(cfg.Permit.URL, cfg.Permit.Port, cfg.Permit.UseHttps)
	pRouter := router.PathPrefix("/permit/datapermit").Subrouter()

	pRouter.HandleFunc("", pgh.Retrieve).Methods(http.MethodGet)
}
