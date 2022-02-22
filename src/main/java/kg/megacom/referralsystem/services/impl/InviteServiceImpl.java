package kg.megacom.referralsystem.services.impl;

import kg.megacom.referralsystem.dao.InviteRepo;
import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.mappers.InviteMapper;
import kg.megacom.referralsystem.mappers.SubscriberMapper;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.services.InviteService;
import kg.megacom.referralsystem.services.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
public class InviteServiceImpl implements InviteService {
    private InviteRepo inviteRepo;
    private SubscriberService subscriberService;

    public InviteServiceImpl(InviteRepo inviteRepo, SubscriberService subscriberService) {
        this.inviteRepo = inviteRepo;
        this.subscriberService = subscriberService;
    }

    @Override
    public ResponseEntity<?> sendInvite(InviteRequestDto inviteRequestDto) {
        SubscriberEntityDto sender = subscriberService.findByPhone(inviteRequestDto.getSenderPhone());
        if(Objects.isNull(sender)){
            sender = subscriberService.createSubscriber(inviteRequestDto.getSenderPhone());
        }
        SubscriberEntityDto receiver = subscriberService.findByPhone(inviteRequestDto.getReceiverPhone());
        if (Objects.isNull(receiver)){
            receiver = subscriberService.createSubscriber(inviteRequestDto.getReceiverPhone());
        }

        LocalDateTime initialTime = inviteRepo.getFirstTime(sender.getId());
        LocalDateTime endTimeThirtyDays= initialTime.plusDays(30);
        int countOfInvitesPerMonth = inviteRepo.getCountOfInvitesPerMonth(sender.getId(), initialTime, endTimeThirtyDays);
        if (countOfInvitesPerMonth>=30){
            return new ResponseEntity<>("limit of 30 invites per month is reached", HttpStatus.CONFLICT)
        }
        LocalDateTime endOfTheDay = initialTime.plusDays(1);
        int countPerDay=inviteRepo.geCountPerDay(sender.getId(),initialTime,endOfTheDay);
        if (countPerDay>=5){
            return new ResponseEntity<>("limit of 5 invites per day is reached", HttpStatus.CONFLICT)
        }


        Invite invite = new Invite();
        invite.setSender(SubscriberMapper.INSTANCE.EntityDtoToSubscriber(sender));
        invite.setReceiver(SubscriberMapper.INSTANCE.EntityDtoToSubscriber(receiver));
        invite.setInviteStatus(InviteStatus.NEW);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("invite sent", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> acceptInvite(InviteRequestDto inviteRequestDto) {
        findSubscriber(inviteRequestDto);
        String senderPhone = inviteRequestDto.getSenderPhone();
        String receiverPhone = inviteRequestDto.getReceiverPhone();
        InviteEntityDto entity = inviteRepo.getInvites(senderPhone,receiverPhone);
        entity.setInviteStatus(InviteStatus.ACCEPTED);
        Invite invite = InviteMapper.INSTANCE.InviteEntityDtoToInvite(entity);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("Invite from subscriber " + senderPhone+ " is accepted", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> rejectInvite(InviteRequestDto inviteRequestDto) {
        findSubscriber(inviteRequestDto);
        String senderPhone = inviteRequestDto.getSenderPhone();
        String receiverPhone = inviteRequestDto.getReceiverPhone();
        InviteEntityDto entity = inviteRepo.getInvites(senderPhone,receiverPhone);
        entity.setInviteStatus(InviteStatus.CANCELLED);
        Invite invite = InviteMapper.INSTANCE.InviteEntityDtoToInvite(entity);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("Invite from subscriber " + senderPhone+ " is accepted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> findSubscriber(InviteRequestDto inviteRequestDto){
        SubscriberEntityDto receiver1 = subscriberService.findByPhone(inviteRequestDto.getReceiverPhone());
        if (Objects.isNull(receiver1)){
            return new ResponseEntity<>("receiver not found", HttpStatus.NOT_FOUND);
        }
        return null;
    }

}


