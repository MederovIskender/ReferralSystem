package kg.megacom.referralsystem.services.impl;

import kg.megacom.referralsystem.dao.InviteRepo;
import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.mappers.InviteMapper;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import kg.megacom.referralsystem.models.dtos.SubscriberEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.services.InviteService;
import kg.megacom.referralsystem.services.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

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
        Invite invite = InviteMapper.INSTANCE.InviteRequestDtoToInvite(inviteRequestDto);
        invite.setStartDate(convertToDateViaInstant(LocalDate.now()));
        invite.setEndDate(convertToDateViaInstant(LocalDate.now().plusDays(1)));
        invite.setInviteStatus(InviteStatus.NEW);
        invite = inviteRepo.save(invite);
        return new ResponseEntity<>("invite sent", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> AcceptInvite(InviteRequestDto inviteRequestDto) {
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


    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    private ResponseEntity<?> findSubscriber(InviteRequestDto inviteRequestDto){
        SubscriberEntityDto receiver1 = subscriberService.findByPhone(inviteRequestDto.getReceiverPhone());
        if (Objects.isNull(receiver1)){
            return new ResponseEntity<>("receiver not found", HttpStatus.NOT_FOUND);
        }
        return null;
    }

}


