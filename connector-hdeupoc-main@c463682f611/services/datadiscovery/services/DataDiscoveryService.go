package services

import (
	"fmt"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
)

type DataDiscoveryServiceInterface interface {
	Add(message string) error
	Delete(message string) error
	Update(message string) error
}

type DataDiscoveryService struct {
	SoapClient      clients.SoapClient
	From            string
	To              string
	MessageTopicMap map[clients.Recipient]map[clients.MessageType][]string
	MessageListSize int
}

var _ DataDiscoveryServiceInterface = DataDiscoveryService{}

func CreateDataDiscoveryService(conf *config.Config, soapClient clients.SoapClient) *DataDiscoveryService {
	messageTopicMap := make(map[clients.Recipient]map[clients.MessageType][]string)
	return &DataDiscoveryService{
		SoapClient:      soapClient,
		From:            conf.Domibus.From,
		To:              conf.Domibus.To,
		MessageTopicMap: messageTopicMap,
		MessageListSize: conf.Gateway.MessageListSize,
	}

}

func (dds DataDiscoveryService) Add(payload string) error {
	message, err := clients.CreateUpdateCentralEuCatalogMessage(clients.CreateDataset, payload)
	if err != nil {
		fmt.Printf("[ERROR] on Add CreateJsonMessage %v\n", err)
		return err
	}
	to := clients.Recipient(dds.To)
	if dds.MessageTopicMap[to] == nil {
		dds.MessageTopicMap[to] = make(map[clients.MessageType][]string)
	}
	dds.MessageTopicMap[clients.Recipient(dds.To)][clients.UpdateCentralEuCatalog] = append(dds.MessageTopicMap[to][clients.UpdateCentralEuCatalog], string(message))
	return dds.SendAllByTopic()
}

func (dds DataDiscoveryService) Delete(message string) error {
	message, err := clients.CreateUpdateCentralEuCatalogMessage(clients.DeleteDataset, message)
	if err != nil {
		fmt.Printf("[ERROR] on Delete CreateJsonMessage %v\n", err)
		return err
	}
	to := clients.Recipient(dds.To)
	if dds.MessageTopicMap[to] == nil {
		dds.MessageTopicMap[to] = make(map[clients.MessageType][]string)
	}
	dds.MessageTopicMap[to][clients.UpdateCentralEuCatalog] = append(dds.MessageTopicMap[to][clients.UpdateCentralEuCatalog], string(message))
	return dds.SendAllByTopic()
}

func (dds DataDiscoveryService) Update(message string) error {
	message, err := clients.CreateUpdateCentralEuCatalogMessage(clients.UpdateDataset, message)
	if err != nil {
		fmt.Printf("[ERROR] on Update CreateJsonMessage %v\n", err)
		return err
	}
	to := clients.Recipient(dds.To)
	if dds.MessageTopicMap[to] == nil {
		dds.MessageTopicMap[to] = make(map[clients.MessageType][]string)
	}
	dds.MessageTopicMap[to][clients.UpdateCentralEuCatalog] = append(dds.MessageTopicMap[to][clients.UpdateCentralEuCatalog], string(message))
	return dds.SendAllByTopic()
}

func (dds DataDiscoveryService) SendAllByTopic() error {
	return clients.SendAllByTopic(&dds.MessageTopicMap, dds.MessageListSize, dds.From, dds.SoapClient.SubmitMessage)
}
