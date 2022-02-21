package kg.megacom.referralsystem.services;

import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import org.springframework.http.ResponseEntity;

public interface InviteService {
    ResponseEntity<?> sendInvite(InviteRequestDto inviteSendDto);

    ResponseEntity<?> AcceptInvite(InviteRequestDto inviteRequestDto);

    ResponseEntity<?> rejectInvite(InviteRequestDto inviteRequestDto);
}
