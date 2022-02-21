package kg.megacom.referralsystem.services.impl;

import kg.megacom.referralsystem.dao.SubscriberRepo;
import kg.megacom.referralsystem.mappers.SubscriberMapper;
import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;
import kg.megacom.referralsystem.models.entities.Subscriber;
import kg.megacom.referralsystem.services.SubscriberService;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private SubscriberRepo subscriberRepo;

    public SubscriberServiceImpl(SubscriberRepo subscriberRepo) {
        this.subscriberRepo = subscriberRepo;
    }

    @Override
    public SubscriberEntityDto findByPhone(String senderPhone) {
        Subscriber subscriber = subscriberRepo.findByPhone(senderPhone);
        SubscriberEntityDto subscriberEntityDto = SubscriberMapper.INSTANCE.SubscriberToEntityDto(subscriber);
        return subscriberEntityDto;
    }

    @Override
    public SubscriberEntityDto createSubscriber(String phone) {
        Subscriber subscriber = new Subscriber();
        subscriber.setPhone(phone);
        subscriber = subscriberRepo.save(subscriber);
        SubscriberEntityDto subscriberEntityDto = SubscriberMapper.INSTANCE.SubscriberToEntityDto(subscriber);
        return new SubscriberEntityDto();
    }
}
