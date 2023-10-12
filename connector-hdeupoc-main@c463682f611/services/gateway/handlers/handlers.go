// Package handlers manages the different versions of the API.
package handlers

import (
	"log/slog"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/mid"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscovery/services"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/controller"
	v1 "code.europa.eu/healthdataeu-nodes/hdeupoc/services/gateway/handlers/v1"

	"github.com/gorilla/mux"
)

// APIMuxConfig contains all the mandatory systems required by handlers.
type APIMuxConfig struct {
	Log       *slog.Logger
	Auth      mid.Auth
	Discovery services.DataDiscoveryService
	Permit    controller.PermitAddress
}

// APIMux constructs a http.Handler with all application routes defined.
func APIMux(cfg APIMuxConfig) *mux.Router {
	router := mux.NewRouter()

	log := mid.Logger{
		Log: cfg.Log,
	}

	auth := mid.Auth{
		Keys: cfg.Auth.Keys,
	}

	router.Use(log.Middleware, auth.Middleware)

	v1.Routes(router, v1.Config{
		Discovery: cfg.Discovery,
		Permit:    cfg.Permit,
	})

	return router
}
