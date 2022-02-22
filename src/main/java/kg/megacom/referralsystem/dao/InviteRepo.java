package kg.megacom.referralsystem.dao;

import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.models.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteRepo extends JpaRepository<Invite,Long> {


    @Query ("select new kg.megacom.referralsystem.models.dtos.InviteEntityDto(p.id, p.sender, p.receiver, p.startDate, p.endDate, p.inviteStatus) from Invite p where p.sender.phone=?1 " +
            "AND p.receiver.phone =?2 and current_date between p.startDate and p.endDate")
    InviteEntityDto getInvites(String sender, String receiver);

    Invite findInviteByReceiverAndInviteStatusIs(Subscriber receiver, InviteStatus inviteStatus);
}
