package services

import (
	"fmt"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
)

type DataDiscoveryCentralServiceInterface interface {
	Add(payload string, catalogID string) error
	Delete(uuid string) error
	Update(uuid string, message string) error
	Search(dcterm_identifier string) ([]byte, error)
}

type DataDiscoveryCentralService struct {
	FairdatapointClient clients.FairDataPointClient
}

var _ DataDiscoveryCentralServiceInterface = DataDiscoveryCentralService{}

func CreateDataDiscoveryCreateCentralService(fairdatapointClient clients.FairDataPointClient) *DataDiscoveryCentralService {
	centralService := &DataDiscoveryCentralService{
		FairdatapointClient: fairdatapointClient,
	}
	return centralService

}

func (dds DataDiscoveryCentralService) Add(payload string, catalogID string) error {

	_, err := dds.FairdatapointClient.Add(payload, catalogID)
	if err != nil {
		fmt.Print(err)
	}
	return nil
}

func (dds DataDiscoveryCentralService) Delete(uuid string) error {
	response, err := dds.FairdatapointClient.Delete(uuid)
	if err != nil {
		fmt.Print(err)
	}
	fmt.Print(response)
	return nil
}

func (dds DataDiscoveryCentralService) Update(uuid string, payload string) error {

	response, err := dds.FairdatapointClient.Update(uuid, payload)
	if err != nil {
		fmt.Print(err)
	}
	fmt.Print(response)
	return nil
}

func (dds DataDiscoveryCentralService) Search(dcterm_identifier string) ([]byte, error) {
	return dds.FairdatapointClient.Search(dcterm_identifier)

}
