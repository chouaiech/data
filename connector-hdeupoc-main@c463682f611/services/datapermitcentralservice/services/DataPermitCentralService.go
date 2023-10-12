package services

import (
	"encoding/json"
	"fmt"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/config"
)

type DataPermitCentralServiceInterface interface {
	Add(payload []byte) error
	SendAllByTopic() error
}

type DataPermitCentralService struct {
	SoapClient      clients.SoapClient
	CatByAP         map[string]string
	From            string
	To              string // TODO - remove from config
	MessageTopicMap map[clients.Recipient]map[clients.MessageType][]string
	MessageListSize int
}

var _ DataPermitCentralServiceInterface = DataPermitCentralService{}

func CreateDataPermitCentralService(conf *config.Config, catByAP map[string]string, soapClient clients.SoapClient) *DataPermitCentralService {
	messageTopicMap := make(map[clients.Recipient]map[clients.MessageType][]string)
	return &DataPermitCentralService{
		SoapClient:      soapClient,
		CatByAP:         catByAP,
		From:            conf.Domibus.From,
		To:              conf.Domibus.To,
		MessageTopicMap: messageTopicMap,
		MessageListSize: conf.DataPermitCentralService.MessageListSize,
	}

}

func (dps DataPermitCentralService) Add(payload []byte) error {
	fmt.Printf("DataPermitCentralService Add\n")
	message, err := clients.CreateDataPermitMessage(payload)
	if err != nil {
		fmt.Printf("[ERROR] on Add CreateDataPermitMessage%v\n", err)
		return err
	}

	var application clients.Application
	if err := json.Unmarshal(payload, &application); err != nil {
		return fmt.Errorf("unmarshaling permit payload: %w", err)
	}

	target, exists := dps.CatByAP[application.CatalogID]
	if !exists {
		return fmt.Errorf("no access point for this catalog uuid %q", application.CatalogID)
	}
	dps.To = target
	to := clients.Recipient(dps.To)
	if dps.MessageTopicMap[to] == nil {
		dps.MessageTopicMap[to] = make(map[clients.MessageType][]string)
	}
	dps.MessageTopicMap[to][clients.DataPermit] = append(dps.MessageTopicMap[to][clients.DataPermit], string(message))
	return dps.SendAllByTopic()
}

func (dps DataPermitCentralService) SendAllByTopic() error {
	return clients.SendAllByTopic(&dps.MessageTopicMap, dps.MessageListSize, dps.From, dps.SoapClient.SubmitMessage)
}
