package services

type Topic string
type IPAddress string
type SubscriptionService struct {
	Subscribers map[Topic]map[IPAddress]struct{}
}

func (ss *SubscriptionService) Subscribe(who IPAddress, topic Topic) error {
	if _, exists := ss.Subscribers[topic]; !exists {
		ss.Subscribers[topic] = map[IPAddress]struct{}{}
	}
	ss.Subscribers[topic][who] = struct{}{}
	return nil
}
func (ss SubscriptionService) GetSubscribersForTopic(topic Topic) map[IPAddress]struct{} {
	if subscribers, exists := ss.Subscribers[topic]; !exists {
		return nil
	} else {
		return subscribers
	}
}
