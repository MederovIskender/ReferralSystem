package kg.megacom.referralsystem.mappers;

import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;
import kg.megacom.referralsystem.models.entities.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriberMapper {
    SubscriberMapper INSTANCE = Mappers.getMapper(SubscriberMapper.class);

    Subscriber EntityDtoToSubscriber(SubscriberEntityDto subscriberEntityDto);
    SubscriberEntityDto SubscriberToEntityDto(Subscriber Subscriber);

}
