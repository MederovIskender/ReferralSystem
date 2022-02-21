package kg.megacom.referralsystem.services;

import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;

public interface SubscriberService {
    SubscriberEntityDto findByPhone(String senderPhone);

    SubscriberEntityDto createSubscriber(String senderPhone);
}
