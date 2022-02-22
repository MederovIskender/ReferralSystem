package kg.megacom.referralsystem.controllers;

import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import kg.megacom.referralsystem.services.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/invite")
public class InviteController {

    private InviteService inviteService;

    public InviteController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping("/send")
    public ResponseEntity<?>sendInvite(@RequestBody InviteRequestDto inviteSendDto){
        return inviteService.sendInvite(inviteSendDto);
    }
    @PutMapping("/accept")
    public ResponseEntity<?> acceptInvite(@RequestBody InviteRequestDto inviteRequestDto){
        return inviteService.acceptInvite(inviteRequestDto);
    }
    @PutMapping("/reject")
    public ResponseEntity<?>rejectInvite(@RequestBody InviteRequestDto inviteRequestDto) {
        return inviteService.rejectInvite(inviteRequestDto);
    }
}
