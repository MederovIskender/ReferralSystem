package kg.megacom.referralsystem.services.impl;

import kg.megacom.referralsystem.dao.InviteRepo;
import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.mappers.InviteMapper;
import kg.megacom.referralsystem.mappers.SubscriberMapper;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.models.entities.Subscriber;
import kg.megacom.referralsystem.services.InviteService;
import kg.megacom.referralsystem.services.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Invite invite = new Invite();
        inviteRepo.save(invite);
        Date startOfMonth = convertToDateViaInstant(invite.getStartDate());
        Integer countOfInvitesPerMonth = inviteRepo.getCountOfInvitesPerMonth(sender.getId(), startOfMonth);
        if (countOfInvitesPerMonth>=30){
            return new ResponseEntity<>("limit of 30 invites per month is reached", HttpStatus.CONFLICT);
        }
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        Integer countPerDay=inviteRepo.geCountPerDay(sender.getId(), startOfDay, endOfDay);
        if (countPerDay>=5){
            return new ResponseEntity<>("limit of 5 invites per day is reached", HttpStatus.CONFLICT);
        }
        invite.setSender(SubscriberMapper.INSTANCE.EntityDtoToSubscriber(sender));
        invite.setReceiver(SubscriberMapper.INSTANCE.EntityDtoToSubscriber(receiver));
        invite.setInviteStatus(InviteStatus.NEW);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("invite sent", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> acceptInvite(InviteRequestDto inviteRequestDto) {
        findSubscriber(inviteRequestDto);
        SubscriberEntityDto senderDto = subscriberService.findByPhone(inviteRequestDto.getSenderPhone());
        SubscriberEntityDto receiverDto = subscriberService.findByPhone((inviteRequestDto.getReceiverPhone()));
        Subscriber sender = SubscriberMapper.INSTANCE.EntityDtoToSubscriber(senderDto);
        Subscriber receiver = SubscriberMapper.INSTANCE.EntityDtoToSubscriber(receiverDto);
        Invite invite = inviteRepo.findInviteByReceiverAndSenderAndInviteStatus(receiver,sender,InviteStatus.NEW);
        invite.setInviteStatus(InviteStatus.ACCEPTED);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("Invite from subscriber " + senderDto.getPhone() + " is accepted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?>findSubscriber(InviteRequestDto inviteRequestDto){
        SubscriberEntityDto receiver1 = subscriberService.findByPhone(inviteRequestDto.getReceiverPhone());
        if (Objects.isNull(receiver1)){
            return new ResponseEntity<>("receiver not found", HttpStatus.NOT_FOUND);
        }
        return null;
    }
    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

}


