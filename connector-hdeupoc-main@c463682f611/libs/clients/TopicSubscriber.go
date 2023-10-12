package clients

import (
	"bytes"
	"fmt"
	"net/http"
)

// TODO: Do this with gRPC
type TopicSubscriber struct {
}

func (ts TopicSubscriber) SubscribeToTopic(topic MessageType, messageCheckerIp, subscriberIp string, messageCheckerUseHttps bool) error {
	//TODO: Dont send subscribedIp. Instead send the name of current service, so that the messagem checker
	// can resolve the actual IP using service discovery

	var protocol string
	if messageCheckerUseHttps {
		protocol = "https://"
	} else {
		protocol = "http://"
	}

	fmt.Printf("[LOG] Subscribe to topic %s with subscriberUrl: %s \n", topic, subscriberIp)
	req, err := http.NewRequest("POST", protocol+messageCheckerIp+"/topics/"+string(topic), bytes.NewBuffer([]byte(subscriberIp)))
	if err != nil {
		return err
	}

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	fmt.Println("Message checker responded with code: ", resp.Status)

	return nil
}
