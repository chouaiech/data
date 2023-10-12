package clients

/* Response structs - xml without namespace. Namespace is not parsed by SOAP library */

import (
	"encoding/xml"

	"github.com/hooklift/gowsdl/soap"
)

type MessagingResponse struct {
	XMLName        xml.Name             `xml:"Messaging"`
	UserMessage    *UserMessageResponse `xml:"UserMessage,omitempty" json:"UserMessage,omitempty"`
	MustUnderstand bool                 `xml:"mustUnderstand,attr,omitempty" json:"mustUnderstand,omitempty"`
}

type UserMessageResponse struct {
	MessageInfo       *MessageInfoResponse       `xml:"MessageInfo,omitempty" json:"MessageInfo,omitempty"`
	PartyInfo         *PartyInfoResponse         `xml:"PartyInfo,omitempty" json:"PartyInfo,omitempty"`
	CollaborationInfo *CollaborationInfoResponse `xml:"CollaborationInfo,omitempty" json:"CollaborationInfo,omitempty"`
	MessageProperties *MessagePropertiesResponse `xml:"MessageProperties,omitempty" json:"MessageProperties,omitempty"`
	PayloadInfo       *PayloadInfoResponse       `xml:"PayloadInfo,omitempty" json:"PayloadInfo,omitempty"`
	Mpc               AnyURI                     `xml:"mpc,attr,omitempty" json:"mpc,omitempty"`
}

type MessageInfoResponse struct {
	Timestamp      soap.XSDDateTime      `xml:"Timestamp,omitempty" json:"Timestamp,omitempty"`
	MessageId      *Max255nonemptystring `xml:"MessageId,omitempty" json:"MessageId,omitempty"`
	RefToMessageId *Max255nonemptystring `xml:"RefToMessageId,omitempty" json:"RefToMessageId,omitempty"`
}

type PartyInfoResponse struct {
	From *FromResponse `xml:"From,omitempty" json:"From,omitempty"`
	To   *ToResponse   `xml:"To,omitempty" json:"To,omitempty"`
}

type FromResponse struct {
	PartyId *PartyId              `xml:"PartyId,omitempty" json:"PartyId,omitempty"`
	Role    *Max255nonemptystring `xml:"Role,omitempty" json:"Role,omitempty"`
}

type ToResponse struct {
	PartyId *PartyId              `xml:"PartyId,omitempty" json:"PartyId,omitempty"`
	Role    *Max255nonemptystring `xml:"Role,omitempty" json:"Role,omitempty"`
}

type CollaborationInfoResponse struct {
	AgreementRef   *AgreementRef `xml:"AgreementRef,omitempty" json:"AgreementRef,omitempty"`
	Service        *Service      `xml:"Service,omitempty" json:"Service,omitempty"`
	Action         string        `xml:"Action,omitempty" json:"Action,omitempty"`
	ConversationId string        `xml:"ConversationId,omitempty" json:"ConversationId,omitempty"`
}

type PayloadInfoResponse struct {
	PartInfo []*PartInfo `xml:"PartInfo,omitempty" json:"PartInfo,omitempty"`
}

type MessagePropertiesResponse struct {
	Property []*Property `xml:"Property,omitempty" json:"Property,omitempty"`
}
