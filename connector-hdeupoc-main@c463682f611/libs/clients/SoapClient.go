package clients

import (
	"bytes"
	"encoding/xml"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"

	genericpass "github.com/JamMasterVilua/genericpass"

	soap "github.com/hooklift/gowsdl/soap"
	"github.com/motemen/go-loghttp"
)

type SoapClient struct {
	Url      string
	Backend  string
	Username string
	Port     string
	UseHttps bool
}

func (soapClient SoapClient) createUrl() string {

	var resulturl string
	if soapClient.UseHttps {
		resulturl = "https://"
	} else {
		resulturl = "http://"
	}
	return resulturl + soapClient.Url + ":" + soapClient.Port + soapClient.Backend
}

func (soapClient SoapClient) createClient() (*soap.Client, error) {

	auth, err := domibusAuth(soapClient.Username, soapClient.Url, soapClient.Port)
	if err != nil {
		return nil, err
	}
	sc := soap.NewClient(soapClient.createUrl(),
		auth,
		soap.WithHTTPClient(&http.Client{
			Transport: &loghttp.Transport{
				LogRequest: func(req *http.Request) {
					body, err := ioutil.ReadAll(req.Body)
					if err != nil {
						log.Printf("Error reading body: %v", err)
						return
					}

					req.Body = ioutil.NopCloser(bytes.NewBuffer(body))
					log.Printf("[%p] %s %s, %v", req, req.Method, req.URL, string(body))
				},
			},
		}))

	return sc, nil
}

func domibusAuth(username, url, port string) (soap.Option, error) {
	f, err := genericpass.OpenDefault(".domibuspass")
	if err != nil {
		log.Printf("Error opening file: %v", err)
		return nil, err
	}
	defer f.Close()
	pass, err := genericpass.PasswordFrom([]string{username, url, port}, f)
	if err != nil {
		log.Printf("Error finding password: %v for %+v", err, []string{username, url, port})
		log.Printf("Username: %+v", username)
		log.Printf("Url: %+v", url)
		log.Printf("Port: %+v", port)
		return nil, err
	}
	return soap.WithBasicAuth(username, pass), nil
}

type CustomHeader struct {
	XMLName   xml.Name  `xml:"soap:Header"`
	Messaging Messaging `xml:"ns:Messaging,omitempty" json:"Messaging,omitempty"`
}

func addSendMessageHeader(client *soap.Client, from string, to string) {
	client.AddHeader(CustomHeader{
		Messaging: Messaging{
			XmlNS: "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/",
			UserMessage: &UserMessage{
				PartyInfo: &PartyInfo{
					From: &From{
						PartyId: &PartyId{
							Value: make255PointerString(from),
							Type:  make255PointerString("urn:oasis:names:tc:ebcore:partyid-type:unregistered"),
						},
						Role: make255PointerString("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/initiator"),
					},
					To: &To{
						PartyId: &PartyId{
							Value: make255PointerString(to),
							Type:  make255PointerString("urn:oasis:names:tc:ebcore:partyid-type:unregistered"),
						},
						Role: make255PointerString("http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/responder"),
					},
				},
				CollaborationInfo: &CollaborationInfo{
					Service: &Service{
						Value: make255PointerString("bdx:noprocess"),
						Type:  make255PointerString("tc1"),
					},
					Action: "TC1Leg1",
				},
				MessageProperties: &MessageProperties{
					Property: []*Property{
						{
							Name:  make255PointerString("originalSender"),
							Value: make1024PointerString("urn:oasis:names:tc:ebcore:partyid-type:unregistered:C1"),
						},
						{
							Name:  make255PointerString("finalRecipient"),
							Value: make1024PointerString("urn:oasis:names:tc:ebcore:partyid-type:unregistered:C4"),
						},
					},
				},
				PayloadInfo: &PayloadInfo{
					PartInfo: []*PartInfo{
						{
							Href: "cid:message",
							PartProperties: &PartProperties{
								Property: []*Property{
									{
										Name:  make255PointerString("MimeType"),
										Value: make1024PointerString("text/xml"),
									},
								},
							},
						},
					},
				},
			},
		},
	})
}

func make255PointerString(str string) *Max255nonemptystring {
	stuff := Max255nonemptystring(str)
	return &stuff
}

func make1024PointerString(str string) *Max1024nonemptystring {
	stuff := Max1024nonemptystring(str)
	return &stuff
}

// TODO - use backend instead of wsplugin if possible.
func (soapClient SoapClient) ListPendingMessages() (*ListPendingMessagesResponseEu, error) {
	client, err := soapClient.createClient()
	if err != nil {
		return nil, err
	}
	backendInterface := NewBackendInterface(client)
	resp, err := backendInterface.ListPendingMessagesEu(&ListPendingMessagesRequestEu{})
	if err != nil {
		fmt.Printf("MsgId: %v\n", err)
	}
	return resp, err
}

func (soapClient SoapClient) GetStatus(msgId string) (*MessageStatus, error) {

	client, err := soapClient.createClient()
	if err != nil {
		return nil, err
	}
	backendInterface := NewBackendInterface(client)
	req := &StatusRequest{
		MessageID: make255PointerString(msgId),
		XmlNS:     "http://eu.domibus.wsplugin/",
	}
	return backendInterface.GetStatus(req)
}

func (soapClient SoapClient) RetrieveMessage(msgId string) (*RetrieveMessageResponseEu, string, error) {

	client, err := soapClient.createClient()
	if err != nil {
		return nil, "", err
	}
	backendInterface := NewBackendInterface(client)
	req := &RetrieveMessageRequestEu{
		MessageID: make255PointerString(msgId),
		XmlNS:     "http://eu.domibus.wsplugin/",
	}
	retMessage, from, err := backendInterface.RetrieveMessageEu(req)
	if err != nil {
		return nil, "", fmt.Errorf("retriving message %w", err)
	}

	return retMessage, from, nil
}

func (soapClient SoapClient) SubmitMessage(message string, from string, to string) (*SubmitResponse, error) {
	client, err := soapClient.createClient()
	if err != nil {
		return nil, err
	}
	addSendMessageHeader(client, from, to)
	backendInterface := NewBackendInterface(client)
	req := &SubmitRequest{
		Payload: []*LargePayloadType{
			{
				Value:       []byte(message),
				PayloadId:   "cid:message",
				ContentType: "text/xml",
			},
		},
		XmlNS: "http://org.ecodex.backend/1_1/",
	}

	return backendInterface.SubmitMessage(req)
}
