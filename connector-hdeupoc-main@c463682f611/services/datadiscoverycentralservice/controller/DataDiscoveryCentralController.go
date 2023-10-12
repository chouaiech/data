package controller

import (
	"encoding/json"
	"errors"
	"fmt"
	"regexp"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/clients"
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datadiscoverycentralservice/services"
)

type DataDiscoveryCentralController struct {
	DataDiscoveryCentralService services.DataDiscoveryCentralServiceInterface
	IdentifierRegex             *regexp.Regexp
	IsPartOfRegex               *regexp.Regexp
	CatByAP                     map[string]string
}

func CreateDataDiscoveryCentralController(dataDiscoveryCentralService services.DataDiscoveryCentralServiceInterface, catByAP map[string]string) (*DataDiscoveryCentralController, error) {
	identifierRegex, err := regexp.Compile("<dcterms:identifier rdf:resource=\"(.*?)\"\\/>")
	if err != nil {
		return nil, err
	}
	ispartofRegex, err := regexp.Compile("<dcterms:isPartOf rdf:resource=(.*?)\\/>")
	if err != nil {
		return nil, err
	}

	centralController := &DataDiscoveryCentralController{
		DataDiscoveryCentralService: dataDiscoveryCentralService,
		IdentifierRegex:             identifierRegex,
		IsPartOfRegex:               ispartofRegex,
		CatByAP:                     catByAP,
	}
	return centralController, nil

}

var replacement = []byte("<dcterms:isPartOf rdf:resource=\"http://metadatabroker.healthdataportal.eu/catalog/979e9731-46f4-40f0-ae4e-73ee3b86a024\"/>")

type SearchResponse struct {
	Uri         string   `json:"uri"`
	Types       []string `json:"types"`
	Title       string   `json:"title"`
	Description string   `json:"description"`
	Relations   []string `json:"relations"`
}

func (ddc DataDiscoveryCentralController) Add(body, catalogID string) {
	fmt.Printf("Add Handler")
	fmt.Printf("Body: %v\n", body)
	//result := ddc.replaceIsPartOfResource([]byte(body), catalogID)
	//ddc.DataDiscoveryCentralService.Add(string(result))
	ddc.DataDiscoveryCentralService.Add(string(body), catalogID)
}

func (ddc DataDiscoveryCentralController) Update(body, catalogID string) {
	fmt.Println("[LOG] Update Handler")
	fmt.Printf("[LOG] Body: %s\n", body)
	fmt.Println()
	identifier := ddc.findIdentifier([]byte(body))
	fmt.Printf("[LOG] Found <dcterms:identifier>: %s\n\n", identifier)
	if len(identifier) > 0 {
		fmt.Printf("[LOG] search for uuid from Identifier %s\n", identifier[1])
		uuid, uri, err := ddc.findUriAndUuid(identifier[1])
		if err != nil {
			fmt.Printf("[ERROR] on uri and uuid search: %s\n\n", err)
			return
		}
		fmt.Printf("[LOG] found uri: %v\n\n", uri)
		fmt.Printf("[LOG] found uuid: %v\n\n", uuid)
		result := ddc.replaceIsPartOfResource([]byte(body), catalogID)
		fmt.Printf("[LOG] xml afer catalog replacement: %v\n", string(result))
		about := ddc.replaceAbout(identifier[1], []byte(uri), []byte(result))
		fmt.Printf("[LOG] xml after about replacement: %s\n\n", about)
		ddc.DataDiscoveryCentralService.Update(uuid, string(about))

	} else {
		fmt.Printf("[LOG] Identifier not found")
	}

}

func (ddc DataDiscoveryCentralController) Delete(body string) error {
	fmt.Printf("[LOG] Delete Handler\n")
	fmt.Printf("[LOG] Payload:\n")
	fmt.Printf("%s\n", body)
	identifier := ddc.findIdentifier([]byte(body))
	fmt.Printf("[LOG] Found <dcterms:identifier>: %s\n\n", identifier)
	if len(identifier) > 0 {
		fmt.Printf("[LOG] search for uuid from Identifier %s\n", identifier[1])
		uuid, _, err := ddc.findUriAndUuid(identifier[1])
		if err != nil {
			fmt.Printf("[ERROR] on find uuid: %s", err)
			return err
		}
		fmt.Printf("[LOG] uuid: %s\n", uuid)
		ddc.DataDiscoveryCentralService.Delete(uuid)
		return nil
	}
	return errors.New("[LOG] identifier not found in xml body")
}

func (ddc DataDiscoveryCentralController) findUriAndUuid(identifier string) (string, string, error) {

	responseData, err := ddc.DataDiscoveryCentralService.Search(identifier)
	if err != nil {
		fmt.Printf("[ERROR] on search: %s\n\n", err)
		return "", "", err
	}
	fmt.Printf("[LOG] Response Data: %s\n", string(responseData))
	var searchResponse []SearchResponse
	err = json.Unmarshal([]byte(responseData), &searchResponse)
	if err != nil {
		fmt.Printf("[ERROR] on json unmarshall: %s \n", err)
		return "", "", err
	}
	fmt.Printf("[LOG] Search Response: %s\n", searchResponse)
	if len(searchResponse) > 0 {
		uri := searchResponse[0].Uri
		r, err := regexp.Compile(".*\\/(.*?)$")
		if err != nil {
			fmt.Printf("[ERROR] on regexp compile : %s \n", err)
			return "", "", err
		}
		uuid := r.FindStringSubmatch(string([]byte(uri)))
		if len(uuid) > 0 {
			fmt.Printf("[LOG] uuid: %v\n", uuid)
			return uuid[1], uri, nil
		}
		fmt.Printf("[LOG] uuid not found\n")
		return "", "", nil

	}
	return "", "", err
}

func (ddc DataDiscoveryCentralController) findIdentifier(dat []byte) []string {
	return ddc.IdentifierRegex.FindStringSubmatch(string(dat))
}

func (ddc DataDiscoveryCentralController) replaceIsPartOfResource(dat []byte, catalogID string) []byte {
	var replacement = []byte("<dcterms:isPartOf rdf:resource=\"http://metadatabroker.healthdataportal.eu/catalog/" + catalogID + "\"/>")
	return ddc.IsPartOfRegex.ReplaceAll(dat, replacement)
}

func (ddc DataDiscoveryCentralController) replaceAbout(identifier string, uri []byte, dat []byte) []byte {
	fmt.Printf("[LOG] ReplaceAbout identifier: %s\n\n", identifier)
	fmt.Printf("[LOG] ReplaceAbout uri: %s\n\n", uri)
	replacement := "<rdf:Description rdf:about=\"" + string(uri) + "\">"
	regex := "<rdf:Description rdf:about=\"" + identifier + "\"\\/?>"
	fmt.Printf("[LOG] regex: %s\n\n", regex)
	aboutRegex, err := regexp.Compile(regex)
	if err != nil {
		return dat
	}
	return aboutRegex.ReplaceAll(dat, []byte(replacement))
}

func (ddc DataDiscoveryCentralController) MessageHandler(messageList clients.MessageList) error {
	fmt.Printf("DataDiscovery received message. Type: %v\n", messageList.Type)
	fmt.Printf("Received message: %v\n", messageList)
	if messageList.Type == clients.UpdateCentralEuCatalog {
		clients.ProcessMessageList(messageList, ddc.ProcessMessage)
	}
	return nil
}

func (ddc DataDiscoveryCentralController) ProcessMessage(message, from string) error {

	var updateCentralEuCatalogMessage clients.UpdateCentralEuCatalogMessage
	err := json.Unmarshal([]byte(message), &updateCentralEuCatalogMessage)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return err
	}
	catalogID, exists := ddc.CatByAP[from]
	if !exists {
		return fmt.Errorf("CatalogID does not exists in CatByAP")
	}
	switch updateCentralEuCatalogMessage.Operation {
	case clients.CreateDataset:
		ddc.Add(updateCentralEuCatalogMessage.Payload, catalogID)
	case clients.DeleteDataset:
		ddc.Delete(updateCentralEuCatalogMessage.Payload)
	case clients.UpdateDataset:
		ddc.Update(updateCentralEuCatalogMessage.Payload, catalogID)
	default:
		fmt.Printf("Unknown operation %v\n", message)
	}
	return nil
}
