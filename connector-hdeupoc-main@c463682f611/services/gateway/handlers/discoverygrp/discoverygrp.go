// Package discoverygrp maintains the group of handlers for datadiscovery
// access.
package discoverygrp

import (
	"io"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscovery/services"
)

type Handler struct {
	discovery services.DataDiscoveryService
}

// New constructs a handlers for a route access.
func New(discovery services.DataDiscoveryService) Handler {
	return Handler{
		discovery: discovery,
	}
}

// Create creates a dataset.
func (h Handler) Create(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	body, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = h.discovery.Add(string(body))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

// Update a dataset.
func (h Handler) Update(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	body, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = h.discovery.Update(string(body))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

// Delete a dataset.
func (h Handler) Delete(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	body, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = h.discovery.Delete(string(body))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (h Handler) SendAllByTopic(w http.ResponseWriter, r *http.Request) {
	err := h.discovery.SendAllByTopic()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}
