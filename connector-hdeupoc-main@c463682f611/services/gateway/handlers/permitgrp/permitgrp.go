// Package permitgrp maintains the group of handlers for permit access.
package permitgrp

import (
	"fmt"
	"io"
	"log/slog"
	"net/http"
	"os"
)

type Handler struct {
	// TODO put those fields on the business layer.
	URL      string
	Port     string
	UseHttps bool
}

// New constructs a handlers for a route access.
func New(url, port string, useHttps bool) Handler {
	return Handler{
		URL:      url,
		Port:     port,
		UseHttps: useHttps,
	}
}

func (h Handler) Retrieve(w http.ResponseWriter, r *http.Request) {

	slog := slog.New(slog.NewTextHandler(os.Stdout, nil))
	var protocol string
	if h.UseHttps {
		protocol = "https://"
	} else {
		protocol = "http://"
	}

	url := protocol + h.URL + ":" + h.Port + "/datapermit"

	resp, err := http.Get(url)
	slog.Info("DataPermitController get request forwarded to :" + url)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadGateway)
		slog.Error("DataPermitController get:" + err.Error())
		return
	}

	fmt.Println(url)

	defer resp.Body.Close()
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		slog.Error("DataPermitController read body: " + err.Error())
		return
	}

	w.Header().Set("Content-Type", "application/text")
	w.Write(body)
}
