package services

import (
	"fmt"

	libs_clients "code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/messagechecker/clients"
)

type MessageCheckerServiceInterface interface {
	Check() error
}

type MessageCheckerService struct {
	SoapClient          libs_clients.SoapClient
	SubscriptionService SubscriptionService
}

var _ MessageCheckerServiceInterface = MessageCheckerService{}

func (messageCheckerService MessageCheckerService) Check() error {

	resp, pendingMessagesError := messageCheckerService.SoapClient.ListPendingMessages()
	if pendingMessagesError != nil {
		fmt.Printf("ListPendingMessages Response error: %v\n", pendingMessagesError)
		return pendingMessagesError
	}

	for _, msgId := range resp.MessageID {
		fmt.Printf("MsgId: %v\n", *msgId)
		msgResp, from, errResp := messageCheckerService.SoapClient.RetrieveMessage(*msgId)
		if errResp != nil {
			fmt.Printf("RetrieveMessage Response error: %v\n", errResp)
			return errResp
		}
		fmt.Printf("MessageCheckerService msgResp: %v, from:%v\n", msgResp, from)
		for _, payloadValue := range msgResp.Payload {
			//TODO: This should not be fully parsed here. Only the outer shell, with the topic
			messageList, err := libs_clients.ReadMessageList(string(payloadValue.Value))
			if err != nil {
				fmt.Println(err)
				return err
			}
			fmt.Printf("MessageCheckerService messageList: %v\n", messageList)
			fmt.Printf("MessageCheckerService messageList.Type: %v\n", messageList.Type)
			messageList.From = from
			subscribers := messageCheckerService.SubscriptionService.GetSubscribersForTopic(Topic(messageList.Type))
			fmt.Printf("MessageCheckerService subscribers: %v\n", subscribers)
			sender := clients.MessageSender{}
			for subscriber, _ := range subscribers {
				// TODO: What happens if one of the subscribers fails?
				err := sender.SendMessage(messageList, string(subscriber))
				if err != nil {
					fmt.Printf("Error occured when sending message %v to subscriber %v\n", messageList, subscriber)
					fmt.Printf("%v\n", err)
					// TODO: Probably exponential backoff or something
					return nil
				}
				fmt.Printf("Message sent successfully to subscriber %v\n", subscriber)
			}
		}
	}
	return nil
}
