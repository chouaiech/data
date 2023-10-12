package clients

import (
	"context"
	"encoding/xml"
	"time"

	"github.com/hooklift/gowsdl/soap"
)

// against "unused imports"
var _ time.Time
var _ xml.Name

type AnyType struct {
	InnerXML string `xml:",innerxml"`
}

type AnyURI string

type NCName string

type Max255nonemptystring string

type Max1024nonemptystring string

type Messaging struct {
	XMLName        xml.Name     `xml:"ns:Messaging"`
	XmlNS          string       `xml:"xmlns:ns,attr"`
	UserMessage    *UserMessage `xml:"ns:UserMessage,omitempty" json:"UserMessage,omitempty"`
	MustUnderstand bool         `xml:"mustUnderstand,attr,omitempty" json:"mustUnderstand,omitempty"`
}

type UserMessage struct {
	MessageInfo       *MessageInfo       `xml:"ns:MessageInfo,omitempty" json:"MessageInfo,omitempty"`
	PartyInfo         *PartyInfo         `xml:"ns:PartyInfo,omitempty" json:"PartyInfo,omitempty"`
	CollaborationInfo *CollaborationInfo `xml:"ns:CollaborationInfo,omitempty" json:"CollaborationInfo,omitempty"`
	MessageProperties *MessageProperties `xml:"ns:MessageProperties,omitempty" json:"MessageProperties,omitempty"`
	PayloadInfo       *PayloadInfo       `xml:"ns:PayloadInfo,omitempty" json:"PayloadInfo,omitempty"`
	Mpc               AnyURI             `xml:"mpc,attr,omitempty" json:"mpc,omitempty"`
}

type MessageInfo struct {
	Timestamp      soap.XSDDateTime      `xml:"ns:Timestamp,omitempty" json:"Timestamp,omitempty"`
	MessageId      *Max255nonemptystring `xml:"ns:MessageId,omitempty" json:"MessageId,omitempty"`
	RefToMessageId *Max255nonemptystring `xml:"ns:RefToMessageId,omitempty" json:"RefToMessageId,omitempty"`
}

type PartyInfo struct {
	From *From `xml:"ns:From,omitempty" json:"From,omitempty"`
	To   *To   `xml:"ns:To,omitempty" json:"To,omitempty"`
}

type PartyId struct {
	Value *Max255nonemptystring `xml:",chardata" json:"-,"`
	Type  *Max255nonemptystring `xml:"type,attr,omitempty" json:"type,omitempty"`
}

type From struct {
	PartyId *PartyId              `xml:"ns:PartyId,omitempty" json:"PartyId,omitempty"`
	Role    *Max255nonemptystring `xml:"ns:Role,omitempty" json:"Role,omitempty"`
}

type To struct {
	PartyId *PartyId              `xml:"ns:PartyId,omitempty" json:"PartyId,omitempty"`
	Role    *Max255nonemptystring `xml:"ns:Role,omitempty" json:"Role,omitempty"`
}

type CollaborationInfo struct {
	AgreementRef   *AgreementRef `xml:"AgreementRef,omitempty" json:"AgreementRef,omitempty"`
	Service        *Service      `xml:"ns:Service,omitempty" json:"Service,omitempty"`
	Action         string        `xml:"ns:Action,omitempty" json:"Action,omitempty"`
	ConversationId string        `xml:"ConversationId,omitempty" json:"ConversationId,omitempty"`
}

type Service struct {
	Value *Max255nonemptystring `xml:",chardata" json:"-,"`
	Type  *Max255nonemptystring `xml:"type,attr,omitempty" json:"type,omitempty"`
}

type AgreementRef struct {
	Value *Max255nonemptystring `xml:",chardata" json:"-,"`
	Type  *Max255nonemptystring `xml:"type,attr,omitempty" json:"type,omitempty"`
	Pmode *Max255nonemptystring `xml:"pmode,attr,omitempty" json:"pmode,omitempty"`
}

type PayloadInfo struct {
	PartInfo []*PartInfo `xml:"ns:PartInfo,omitempty" json:"PartInfo,omitempty"`
}

type PartInfo struct {
	PartProperties *PartProperties `xml:"ns:PartProperties,omitempty" json:"PartProperties,omitempty"`
	Href           string          `xml:"href,attr,omitempty" json:"href,omitempty"`
}

type Property struct {
	Value *Max1024nonemptystring `xml:",chardata" json:"-,"`
	Name  *Max255nonemptystring  `xml:"name,attr,omitempty" json:"name,omitempty"`
	Type  *Max255nonemptystring  `xml:"type,attr,omitempty" json:"type,omitempty"`
}

type PartProperties struct {
	Property []*Property `xml:"ns:Property,omitempty" json:"Property,omitempty"`
}

type MessageProperties struct {
	Property []*Property `xml:"ns:Property,omitempty" json:"Property,omitempty"`
}

type MessageStatus string

const (
	MessageStatusREADY_TO_SEND             MessageStatus = "READY_TO_SEND"
	MessageStatusREADY_TO_PULL             MessageStatus = "READY_TO_PULL"
	MessageStatusBEING_PULLED              MessageStatus = "BEING_PULLED"
	MessageStatusSEND_ENQUEUED             MessageStatus = "SEND_ENQUEUED"
	MessageStatusSEND_IN_PROGRESS          MessageStatus = "SEND_IN_PROGRESS"
	MessageStatusWAITING_FOR_RECEIPT       MessageStatus = "WAITING_FOR_RECEIPT"
	MessageStatusACKNOWLEDGED              MessageStatus = "ACKNOWLEDGED"
	MessageStatusACKNOWLEDGED_WITH_WARNING MessageStatus = "ACKNOWLEDGED_WITH_WARNING"
	MessageStatusSEND_ATTEMPT_FAILED       MessageStatus = "SEND_ATTEMPT_FAILED"
	MessageStatusSEND_FAILURE              MessageStatus = "SEND_FAILURE"
	MessageStatusNOT_FOUND                 MessageStatus = "NOT_FOUND"
	MessageStatusWAITING_FOR_RETRY         MessageStatus = "WAITING_FOR_RETRY"
	MessageStatusRECEIVED                  MessageStatus = "RECEIVED"
	MessageStatusRECEIVED_WITH_WARNINGS    MessageStatus = "RECEIVED_WITH_WARNINGS"
	MessageStatusDELETED                   MessageStatus = "DELETED"
	MessageStatusDOWNLOADED                MessageStatus = "DOWNLOADED"
)

type ErrorCode string

const (
	ErrorCodeEBMS_0001 ErrorCode = "EBMS_0001"
	ErrorCodeEBMS_0002 ErrorCode = "EBMS_0002"
	ErrorCodeEBMS_0003 ErrorCode = "EBMS_0003"
	ErrorCodeEBMS_0004 ErrorCode = "EBMS_0004"
	ErrorCodeEBMS_0005 ErrorCode = "EBMS_0005"
	ErrorCodeEBMS_0006 ErrorCode = "EBMS_0006"
	ErrorCodeEBMS_0007 ErrorCode = "EBMS_0007"
	ErrorCodeEBMS_0008 ErrorCode = "EBMS_0008"
	ErrorCodeEBMS_0009 ErrorCode = "EBMS_0009"
	ErrorCodeEBMS_0010 ErrorCode = "EBMS_0010"
	ErrorCodeEBMS_0011 ErrorCode = "EBMS_0011"
	ErrorCodeEBMS_0101 ErrorCode = "EBMS_0101"
	ErrorCodeEBMS_0102 ErrorCode = "EBMS_0102"
	ErrorCodeEBMS_0103 ErrorCode = "EBMS_0103"
	ErrorCodeEBMS_0201 ErrorCode = "EBMS_0201"
	ErrorCodeEBMS_0202 ErrorCode = "EBMS_0202"
	ErrorCodeEBMS_0301 ErrorCode = "EBMS_0301"
	ErrorCodeEBMS_0302 ErrorCode = "EBMS_0302"
	ErrorCodeEBMS_0303 ErrorCode = "EBMS_0303"
	ErrorCodeEBMS_0020 ErrorCode = "EBMS_0020"
	ErrorCodeEBMS_0021 ErrorCode = "EBMS_0021"
	ErrorCodeEBMS_0022 ErrorCode = "EBMS_0022"
	ErrorCodeEBMS_0023 ErrorCode = "EBMS_0023"
	ErrorCodeEBMS_0030 ErrorCode = "EBMS_0030"
	ErrorCodeEBMS_0031 ErrorCode = "EBMS_0031"
	ErrorCodeEBMS_0040 ErrorCode = "EBMS_0040"
	ErrorCodeEBMS_0041 ErrorCode = "EBMS_0041"
	ErrorCodeEBMS_0042 ErrorCode = "EBMS_0042"
	ErrorCodeEBMS_0043 ErrorCode = "EBMS_0043"
	ErrorCodeEBMS_0044 ErrorCode = "EBMS_0044"
	ErrorCodeEBMS_0045 ErrorCode = "EBMS_0045"
	ErrorCodeEBMS_0046 ErrorCode = "EBMS_0046"
	ErrorCodeEBMS_0047 ErrorCode = "EBMS_0047"
	ErrorCodeEBMS_0048 ErrorCode = "EBMS_0048"
	ErrorCodeEBMS_0049 ErrorCode = "EBMS_0049"
	ErrorCodeEBMS_0050 ErrorCode = "EBMS_0050"
	ErrorCodeEBMS_0051 ErrorCode = "EBMS_0051"
	ErrorCodeEBMS_0052 ErrorCode = "EBMS_0052"
	ErrorCodeEBMS_0053 ErrorCode = "EBMS_0053"
	ErrorCodeEBMS_0054 ErrorCode = "EBMS_0054"
	ErrorCodeEBMS_0055 ErrorCode = "EBMS_0055"
	ErrorCodeEBMS_0060 ErrorCode = "EBMS_0060"
	ErrorCodeEBMS_0065 ErrorCode = "EBMS_0065"
)

type MshRole string

const (
	MshRoleSENDING   MshRole = "SENDING"
	MshRoleRECEIVING MshRole = "RECEIVING"
)

type FaultDetail struct {
	XMLName xml.Name `xml:"http://org.ecodex.backend/1_1/ FaultDetail"`
	Code    string   `xml:"code,omitempty" json:"code,omitempty"`
	Message *string  `xml:"message,omitempty" json:"message,omitempty"`
}

type RetrieveMessageRequest struct {
	XMLName   xml.Name              `xml:"http://org.ecodex.backend/1_1/ retrieveMessageRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type RetrieveMessageResponse struct {
	XMLName  xml.Name            `xml:"http://org.ecodex.backend/1_1/ retrieveMessageResponse"`
	Bodyload *LargePayloadType   `xml:"bodyload,omitempty" json:"bodyload,omitempty"`
	Payload  []*LargePayloadType `xml:"payload,omitempty" json:"payload,omitempty"`
}

type RetrieveMessageRequestEu struct {
	XmlNS     string                `xml:"xmlns:eu,attr"`
	XMLName   xml.Name              `xml:"eu:retrieveMessageRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type RetrieveMessageResponseEu struct {
	XMLName  xml.Name            `xml:"http://eu.domibus.wsplugin/ retrieveMessageResponse"`
	Bodyload *LargePayloadType   `xml:"bodyload,omitempty" json:"bodyload,omitempty"`
	Payload  []*LargePayloadType `xml:"payload,omitempty" json:"payload,omitempty"`
}

type ListPendingMessagesRequest struct {
	XMLName xml.Name `xml:"http://org.ecodex.backend/1_1/ listPendingMessagesRequest"`
}

type ListPendingMessagesResponse struct {
	XMLName   xml.Name  `xml:"http://org.ecodex.backend/1_1/ listPendingMessagesResponse"`
	MessageID []*string `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

// Eu -> Wsplugin
type ListPendingMessagesRequestEu struct {
	XMLName xml.Name `xml:"http://eu.domibus.wsplugin/ listPendingMessagesRequest"`
}

type ListPendingMessagesResponseEu struct {
	XMLName   xml.Name  `xml:"http://eu.domibus.wsplugin/ listPendingMessagesResponse"`
	MessageID []*string `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type MessageErrorsRequest struct {
	XMLName   xml.Name              `xml:"http://org.ecodex.backend/1_1/ messageErrorsRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type MessageStatusRequest struct {
	XMLName   xml.Name              `xml:"http://org.ecodex.backend/1_1/ messageStatusRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type SubmitRequest struct {
	XMLName  xml.Name            `xml:"_l:submitRequest"`
	XmlNS    string              `xml:"xmlns:_l,attr"`
	Bodyload *LargePayloadType   `xml:"bodyload,omitempty" json:"bodyload,omitempty"`
	Payload  []*LargePayloadType `xml:"payload,omitempty" json:"payload,omitempty"`
}

type SubmitResponse struct {
	XMLName   xml.Name  `xml:"http://org.ecodex.backend/1_1/ submitResponse"`
	MessageID []*string `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type GetStatusRequest struct {
	XMLName   xml.Name              `xml:"http://org.ecodex.backend/1_1/ getStatusRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type StatusRequest struct {
	XMLName   xml.Name              `xml:"_l:statusRequest"`
	XmlNS     string                `xml:"xmlns:_l,attr"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type GetStatusResponse MessageStatus

type GetErrorsRequest struct {
	XMLName   xml.Name              `xml:"http://org.ecodex.backend/1_1/ getErrorsRequest"`
	MessageID *Max255nonemptystring `xml:"messageID,omitempty" json:"messageID,omitempty"`
}

type GetMessageErrorsResponse ErrorResultImplArray

type PayloadType struct {
	Value     []byte `xml:",chardata" json:"-,"`
	PayloadId string `xml:"payloadId,attr,omitempty" json:"payloadId,omitempty"`
}

type LargePayloadType struct {
	Value       []byte `xml:"value,omitempty" json:"value,omitempty"`
	PayloadId   string `xml:"payloadId,attr,omitempty" json:"payloadId,omitempty"`
	ContentType string `xml:"contentType,attr,omitempty" json:"contentType,omitempty"`
}

type ErrorResultImpl struct {
	XMLName          xml.Name         `xml:"http://org.ecodex.backend/1_1/ item"`
	ErrorCode        *ErrorCode       `xml:"errorCode,omitempty" json:"errorCode,omitempty"`
	ErrorDetail      string           `xml:"errorDetail,omitempty" json:"errorDetail,omitempty"`
	MessageInErrorId string           `xml:"messageInErrorId,omitempty" json:"messageInErrorId,omitempty"`
	MshRole          *MshRole         `xml:"mshRole,omitempty" json:"mshRole,omitempty"`
	Notified         soap.XSDDateTime `xml:"notified,omitempty" json:"notified,omitempty"`
	Timestamp        soap.XSDDateTime `xml:"timestamp,omitempty" json:"timestamp,omitempty"`
}

type PayloadURLType struct {
	Value     string `xml:",chardata" json:"-,"`
	PayloadId string `xml:"payloadId,attr,omitempty" json:"payloadId,omitempty"`
}

type ErrorResultImplArray struct {
	XMLName xml.Name           `xml:"http://org.ecodex.backend/1_1/ getMessageErrorsResponse"`
	Item    []*ErrorResultImpl `xml:"item,omitempty" json:"item,omitempty"`
}

type Base64Binary struct {
	XMLName     xml.Name `xml:"http://www.w3.org/2005/05/xmlmime value"`
	Value       []byte   `xml:",chardata" json:"-,"`
	ContentType string   `xml:"contentType,attr,omitempty" json:"contentType,omitempty"`
}

type HexBinary struct {
	Value       []byte `xml:",chardata" json:"-,"`
	ContentType string `xml:"contentType,attr,omitempty" json:"contentType,omitempty"`
}

type BackendInterface interface {
	ListPendingMessages(request *AnyType) (*ListPendingMessagesResponse, error)
	ListPendingMessagesContext(ctx context.Context, request *AnyType) (*ListPendingMessagesResponse, error)
	GetMessageErrors(request *GetErrorsRequest) (*ErrorResultImplArray, error)
	GetMessageErrorsContext(ctx context.Context, request *GetErrorsRequest) (*ErrorResultImplArray, error)
	// Error can be either of the following types:
	//
	//   - StatusFault
	GetStatus(request *StatusRequest) (*MessageStatus, error)
	GetStatusContext(ctx context.Context, request *StatusRequest) (*MessageStatus, error)
	// Error can be either of the following types:
	//
	//   - SubmitMessageFault
	SubmitMessage(request *SubmitRequest) (*SubmitResponse, error)
	SubmitMessageContext(ctx context.Context, request *SubmitRequest) (*SubmitResponse, error)
	// Error can be either of the following types:
	//
	//   - RetrieveMessageFault
	RetrieveMessageEu(request *RetrieveMessageRequestEu) (*RetrieveMessageResponseEu, string, error)

	ListPendingMessagesEu(request *ListPendingMessagesRequestEu) (*ListPendingMessagesResponseEu, error)
	ListPendingMessagesContextEu(ctx context.Context, request *ListPendingMessagesRequestEu) (*ListPendingMessagesResponseEu, error)
	GetClient() (client *soap.Client)
}

type backendInterface struct {
	client *soap.Client
}

func NewBackendInterface(client *soap.Client) BackendInterface {
	return &backendInterface{
		client: client,
	}
}

func (service *backendInterface) GetClient() (client *soap.Client) {
	return service.client
}

func (service *backendInterface) ListPendingMessagesContext(ctx context.Context, request *AnyType) (*ListPendingMessagesResponse, error) {
	response := new(ListPendingMessagesResponse)
	err := service.client.CallContext(ctx, "''", request, response)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (service *backendInterface) ListPendingMessages(request *AnyType) (*ListPendingMessagesResponse, error) {
	return service.ListPendingMessagesContext(
		context.Background(),
		request,
	)
}

func (service *backendInterface) ListPendingMessagesContextEu(ctx context.Context, request *ListPendingMessagesRequestEu) (*ListPendingMessagesResponseEu, error) {
	response := new(ListPendingMessagesResponseEu)
	messagingResponse := new(MessagingResponse)
	err := service.client.CallContextRetrieve(ctx, "''", request, response, messagingResponse)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (service *backendInterface) ListPendingMessagesEu(request *ListPendingMessagesRequestEu) (*ListPendingMessagesResponseEu, error) {
	return service.ListPendingMessagesContextEu(
		context.Background(),
		request,
	)
}

func (service *backendInterface) GetMessageErrorsContext(ctx context.Context, request *GetErrorsRequest) (*ErrorResultImplArray, error) {
	response := new(ErrorResultImplArray)
	err := service.client.CallContext(ctx, "''", request, response)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (service *backendInterface) GetMessageErrors(request *GetErrorsRequest) (*ErrorResultImplArray, error) {
	return service.GetMessageErrorsContext(
		context.Background(),
		request,
	)
}

func (service *backendInterface) GetStatusContext(ctx context.Context, request *StatusRequest) (*MessageStatus, error) {
	response := new(MessageStatus)
	err := service.client.CallContext(ctx, "''", request, response)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (service *backendInterface) GetStatus(request *StatusRequest) (*MessageStatus, error) {
	return service.GetStatusContext(
		context.Background(),
		request,
	)
}

func (service *backendInterface) SubmitMessageContext(ctx context.Context, request *SubmitRequest) (*SubmitResponse, error) {
	response := new(SubmitResponse)
	err := service.client.CallContext(ctx, "''", request, response)
	if err != nil {
		return nil, err
	}

	return response, nil
}

func (service *backendInterface) SubmitMessage(request *SubmitRequest) (*SubmitResponse, error) {
	return service.SubmitMessageContext(
		context.Background(),
		request,
	)
}

func (service *backendInterface) RetrieveMessageEu(request *RetrieveMessageRequestEu) (*RetrieveMessageResponseEu, string, error) {
	return service.RetrieveMessageContextEu(
		context.Background(),
		request,
	)
}

func (service *backendInterface) RetrieveMessageContextEu(ctx context.Context, request *RetrieveMessageRequestEu) (*RetrieveMessageResponseEu, string, error) {
	response := new(RetrieveMessageResponseEu)
	messagingResponse := new(MessagingResponse)
	err := service.client.CallContextRetrieve(ctx, "''", request, response, messagingResponse)
	if err != nil {
		return nil, "", err
	}
	if messagingResponse.UserMessage == nil {
		return response, "", nil
	}
	userMessage := *messagingResponse.UserMessage
	if userMessage.PartyInfo == nil {
		return response, "", nil
	}
	partyInfo := *userMessage.PartyInfo
	if partyInfo.From == nil {
		return response, "", nil
	}
	from := *partyInfo.From
	if from.PartyId == nil {
		return response, "", nil
	}
	fromId := *from.PartyId
	if fromId.Value == nil {
		return response, "", nil
	}
	fromIdValue := *fromId.Value
	return response, string(fromIdValue), nil
}
