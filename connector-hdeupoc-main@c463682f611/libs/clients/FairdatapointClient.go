package clients

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"time"

	genericpass "github.com/JamMasterVilua/genericpass"
)

type FairDataPointClient struct {
	UseHttps    bool
	Url         string
	Username    string
	Port		string
	bearertoken *string
}

type RequestObject struct {
	url         string
	method      string
	message     []byte
	contentType *string
}

type SearchResponse struct {
	Uri         string   `json:"uri"`
	Types       []string `json:"types"`
	Title       string   `json:"title"`
	Description string   `json:"description"`
	Relations   []string `json:"relations"`
}

func (fairdatapointClient FairDataPointClient) Search(dcterm_identifier string) ([]byte, error) {

	fmt.Printf("[LOG] search for dcterms:identifier: %s\n", dcterm_identifier)
	values := map[string]string{"prefixes": "PREFIX dcterms: <http://purl.org/dc/terms/>", "graphPattern": "?entity dcterms:identifier <" + dcterm_identifier + ">", "ordering": "ASC(?title)"}

	fmt.Printf("[LOG] query: %s\n", values)
	json_data, err := json.Marshal(values)
	if err != nil {
		fmt.Printf("[ERROR] on json marshal: %s \n", err)
		return nil, err
	}
	fmt.Printf("[LOG] json data : %s \n", json_data)

	var contentType = "application/json"
	resp, err := fairdatapointClient.request(RequestObject{
		url:         fairdatapointClient.protocol() + fairdatapointClient.Url + "/search/query",
		method:      "POST",
		message:     json_data,
		contentType: &contentType,
	})
	if err != nil {
		fmt.Printf("[ERROR] on search response: %s \n", err)
		return nil, err
	}
	responseData, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("[ERROR] on read response data: %s \n", err)
		return nil, err
	}
	return responseData, nil
}

func (fairdatapointClient FairDataPointClient) Add(payload string, catalogID string) (*http.Response, error) {
	fmt.Printf("[LOG] FairDataPointClient - trying to create dataset\n")
	var contentType = "application/rdf+xml"
	resp, err := fairdatapointClient.request(RequestObject{
		url:         fairdatapointClient.protocol() + fairdatapointClient.Url + ":" + fairdatapointClient.Port +"/catalogues/"+catalogID+"/datasets",
		method:      "POST",
		message:     []byte(payload),
		contentType: &contentType,
	})
	if err != nil {
		fmt.Printf("[ERROR] response %v.\n", err)
		return nil, err
	}
	return resp, nil
}

func (fairdatapointClient FairDataPointClient) Delete(uuid string) (*http.Response, error) {
	fmt.Printf("[LOG] FairDataPointClient - trying to delete dataset\n")
	resp, err := fairdatapointClient.request(RequestObject{
		url:    fairdatapointClient.protocol() + fairdatapointClient.Url + "/dataset/" + uuid,
		method: "DELETE",
	})
	if err != nil {
		fmt.Printf("[ERROR] response %v.\n", err)
		return nil, err
	}

	return resp, nil
}

func (fairdatapointClient FairDataPointClient) Update(uuid string, message string) (*http.Response, error) {
	fmt.Printf("[LOG] FairDataPointClient - trying to update dataset\n")
	var contentType = "application/rdf+xml"
	resp, err := fairdatapointClient.request(RequestObject{
		url:         fairdatapointClient.protocol() + fairdatapointClient.Url + "/dataset/" + uuid,
		method:      "PUT",
		message:     []byte(message),
		contentType: &contentType,
	})
	if err != nil {
		fmt.Printf("[ERROR] response %v.\n", err)
		return nil, err
	}
	return resp, nil
}

func bearerToken(username string, host string) (string, error) {
	f, err := genericpass.OpenDefault(".fairdatapointpass")
	if err != nil {
		log.Printf("[ERROR] opening file: %v", err)
		return "", err
	}
	defer f.Close()
	pass, err := genericpass.PasswordFrom([]string{username, host}, f)
	if err != nil {
		log.Printf("[ERROR] finding password: %v for %+v", err, []string{username, host})
		return "", err
	}
	return pass, nil
}

func (fairdatapointClient FairDataPointClient) request(requestObject RequestObject) (*http.Response, error) {

	fmt.Printf("[LOG] FairDataPointClient - create request\n")
	var body *bytes.Buffer
	if requestObject.message != nil {
		body = bytes.NewBuffer(requestObject.message)
	}
	fmt.Printf("[LOG] FairDataPointClient - request object body: %s\n", body)
	fmt.Printf("[LOG] FairDataPointClient: %s url %v\n", requestObject.method, requestObject.url)
	var request *http.Request
	var err error
	if body == nil {
		request, err = http.NewRequest(requestObject.method, requestObject.url, nil)
	} else {
		request, err = http.NewRequest(requestObject.method, requestObject.url, body)
	}

	if err != nil {
		fmt.Printf("[ERROR] FairDataPointClient - request error: %v.\n", err)
		return nil, err
	}

	if fairdatapointClient.bearertoken == nil {
		token, err := bearerToken(fairdatapointClient.Username, fairdatapointClient.Url)
		if err != nil {
			fmt.Printf("[ERROR] on getting token: %s\n", err)
			return nil, err
		}
		fairdatapointClient.bearertoken = &token
	}
	//fmt.Printf("[LOG] Authorization Header: %s\n", "Bearer "+*fairdatapointClient.bearertoken)
	//request.Header.Add("Authorization", "Bearer "+*fairdatapointClient.bearertoken)
	fmt.Printf("[LOG] X-API-Key: %s\n", *fairdatapointClient.bearertoken)
	request.Header.Add("X-API-Key", *fairdatapointClient.bearertoken)
	if requestObject.contentType != nil {
		fmt.Printf("[LOG] content type: %s\n", *requestObject.contentType)
		request.Header.Add("Content-Type", *requestObject.contentType)
	} else {
		fmt.Printf("[LOG] No contentType given. Using default content type: %s\n", "x-www-form-urlencoded")
		request.Header.Add("Content-Type", "x-www-form-urlencoded")
	}
	client := &http.Client{
		Timeout: 5 * time.Second,
	}
	response, err := client.Do(request)
	if err != nil {
		fmt.Printf("[ERROR] FairDataPointClient - response: %v.\n", err)
		return nil, err
	}
	return response, nil
}

func (fairdatapointClient FairDataPointClient) protocol() string {
	if fairdatapointClient.UseHttps {
		return "https://"
	}
	return "http://"
}
