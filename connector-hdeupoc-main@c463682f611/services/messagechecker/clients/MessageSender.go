package clients

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
)

// TODO: Do this with gRPC
type MessageSender struct {
}

func (ms MessageSender) SendMessage(msg clients.MessageList, targetProtocolAndIp string) error {
	jsonBytes, err := json.Marshal(msg)
	if err != nil {
		return err
	}
	req, err := http.NewRequest("POST", targetProtocolAndIp+"/message", bytes.NewBuffer(jsonBytes))
	if err != nil {
		return err
	}
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	fmt.Println("Message receiver responded with code: ", resp.Status)

	return nil
}
