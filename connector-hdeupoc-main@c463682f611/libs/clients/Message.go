package clients

import (
	b64 "encoding/base64"
	"encoding/json"
	"fmt"
)

type Operation string

const (
	CreateDataset Operation = "createDataset"
	UpdateDataset Operation = "updateDataset"
	DeleteDataset Operation = "deleteDataset"

	ApplyPermit Operation = "applyPermit"
)

type MessageType string
type Recipient string

const (
	UpdateCentralEuCatalog MessageType = "updateCentralEUCatalog"
	DataPermit             MessageType = "dataPermit"
)

type MessageList struct {
	Type     MessageType `json:"type"`
	Messages []string    `json:"messages"`
	From     string      `json:"from"`
}

type Message struct {
	Type    MessageType `json:"type"`
	Content []byte      `json:"content"`
	From    string      `json:"from"`
}

type UpdateCentralEuCatalogMessage struct {
	Operation Operation `json:"operation"`
	Payload   string    `json:"payload"`
}

// An Application contains the data needed to apply for a permit.
type Application struct {
	ID        string         `json:"application_id"`
	CatalogID string         `json:"catalog_id"`
	Applicant map[string]any `json:"form"`
}

type DataPermitMessage struct {
	Operation   Operation   `json:"operation"`
	Application Application `json:"application"`
}

type Base64String string

func CreateDataPermitMessage(payload []byte) (Base64String, error) {
	var application Application
	if err := json.Unmarshal(payload, &application); err != nil {
		return "", fmt.Errorf("unmarshaling permit payload: %w", err)
	}
	
	
	content := DataPermitMessage{
		Operation:   ApplyPermit,
		Application: application,
	}
	marshalContent, err := json.Marshal(content)
	if err != nil {
		fmt.Printf("Error: %s", err)
		return "", err
	}
	message := Message{
		Type:    DataPermit,
		Content: marshalContent,
	}
	jsonmessage, err := json.Marshal(message)
	if err != nil {
		fmt.Printf("Error: %s", err)
		return "", err
	}
	fmt.Println(string(jsonmessage))
	
	return Base64String(b64.StdEncoding.EncodeToString([]byte(jsonmessage))), nil
}

func CreateUpdateCentralEuCatalogMessage(operation Operation, payload string) (string, error) {
	content := UpdateCentralEuCatalogMessage{
		Operation: operation,
		Payload:   payload,
	}
	marshalContent, err := json.Marshal(content)
	if err != nil {
		fmt.Printf("Error: %s", err)
		return "", err
	}
	message := Message{
		Type:    UpdateCentralEuCatalog,
		Content: marshalContent,
	}

	jsonmessage, err := json.Marshal(message)
	if err != nil {
		fmt.Printf("Error: %s", err)
		return "", err
	}
	fmt.Println(string(jsonmessage))
	return b64.StdEncoding.EncodeToString([]byte(jsonmessage)), nil
}

func ReadMessage(message string) (Message, error) {
	decoded, _ := b64.StdEncoding.DecodeString(message)

	var jsonmessage Message
	err := json.Unmarshal(decoded, &jsonmessage)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return jsonmessage, err
	}
	return jsonmessage, nil
}

func CreateMessageList(messages []string, messagesType MessageType) (string, error) {
	messageList := MessageList{
		Type:     messagesType,
		Messages: messages,
	}

	jsonmessagelist, err := json.Marshal(messageList)
	if err != nil {
		return "", fmt.Errorf("CreateMessageList Error: %s", err)
	}
	return b64.StdEncoding.EncodeToString([]byte(jsonmessagelist)), nil
}

func ReadMessageList(messageList string) (MessageList, error) {

	decoded, _ := b64.StdEncoding.DecodeString(messageList)

	var jsonmessagelist MessageList
	err := json.Unmarshal(decoded, &jsonmessagelist)
	if err != nil {
		var emptylist MessageList
		return emptylist, fmt.Errorf("error: %v", err)
	}
	jsonmessagelist.From = ""
	return jsonmessagelist, nil
}

func ChunkSlice(slice []string, chunkSize int) ([][]string, error) {

	var chunks [][]string
	if chunkSize < 1 {
		return chunks, fmt.Errorf("ChunkSize is less than one")
	}
	for i := 0; i < len(slice); i += chunkSize {
		end := i + chunkSize

		if end > len(slice) {
			end = len(slice)
		}

		chunks = append(chunks, slice[i:end])
	}
	return chunks, nil
}

type processMessage func(string, string) error

func ProcessMessageList(messageList MessageList, fn processMessage) error {
	fmt.Printf("Start processing message\n")
	fmt.Printf("MessageList: %v\n", messageList)
	for _, messageBase64 := range messageList.Messages {
		fmt.Printf("Before read\n")
		message, err := ReadMessage(string(messageBase64))
		fmt.Printf("After read: %v\n", message)
		if err != nil {
			fmt.Printf("Error : %v\n", err)
			fmt.Println(err)
			return err
		}
		fmt.Printf("Data Permit processed message : %v\n", message)

		fn(string(message.Content), messageList.From)
	}
	return nil
}

type submitMessage func(string, string, string) (*SubmitResponse, error)

func SendAllByTopic(messageTopicMap *map[Recipient]map[MessageType][]string, messageListSize int, from string, fn submitMessage) error {

	for destination, mapList := range *messageTopicMap {
		for topic, messageByTopicList := range mapList {
			payloads, err := ChunkSlice(messageByTopicList, messageListSize)
			if err != nil {
				return fmt.Errorf("error SendAllByTopic: %v", err)
			}
			for _, payload := range payloads {
				messageListPayload, err := CreateMessageList(payload, topic)
				if err != nil {
					fmt.Printf("[ERROR] on CreaetMessageList: %v\n", err)
					return err
				}
				resp, err := fn(string(messageListPayload), from, string(destination))
				if err != nil {
					fmt.Printf("[ERROR] on SubmitMessage: %v\n", err)
					return err
				}
				fmt.Printf("[LOG] response: %v\n", resp)
			}
			delete((*messageTopicMap)[destination], topic)
		}
	}
	return nil
}
