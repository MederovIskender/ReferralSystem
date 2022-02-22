package kg.megacom.referralsystem.dao;

import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.models.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InviteRepo extends JpaRepository<Invite,Long> {


//    @Query ("select new kg.megacom.referralsystem.models.dtos.InviteEntityDto(p.id, p.sender, p.receiver, p.startDate, p.endDate, p.inviteStatus) from Invite p where p.sender.phone=?1 " +
//            "AND p.receiver.phone =?2 and current_date between p.startDate and p.endDate")
//    InviteEntityDto getInvites(String sender, String receiver);

    Invite findInviteByReceiverAndInviteStatusIs(Subscriber receiver, InviteStatus inviteStatus);

    @Query(value = "Select count sender_subs_id from Invite where sender_subs_id=?1 and status = NEW and period between ?2 and ?3", nativeQuery = true)
    int getCountOfInvitesPerMonth(Long id, LocalDateTime initialTime, LocalDateTime endTimeThirtyDays);

    @Query(value ="select startDate from Invite where InviteStatus = NEW and sender_subs_id=?1 order by startDate asc limit 1", nativeQuery = true)
    LocalDateTime getFirstTime(Long id);

    @Query(value = "Select count sender_subs_id from Invite where sender_subs_id=?1 and status = NEW and period between ?2 and ?3", nativeQuery = true)
    int geCountPerDay(Long id, LocalDateTime initialTime, LocalDateTime endOfTheDay);
}
