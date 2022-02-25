package kg.megacom.referralsystem.dao;

import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.entities.Invite;
import kg.megacom.referralsystem.models.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface InviteRepo extends JpaRepository<Invite,Long> {

    Invite findInviteByReceiverAndSenderAndInviteStatus(Subscriber receiver, Subscriber sender, InviteStatus inviteStatus);

    @Query(value = "Select count (id) from Invite where sender_subs_id=?1 and ?2>=(select date_trunc('month', current_date))", nativeQuery = true)
    int getCountOfInvitesPerMonth(Long id, Date date);

    @Query("select count (p.id) from Invite p where p.sender.id=?1 and p.startDate between ?2 and ?3")
    int geCountPerDay(Long id, LocalDateTime startOfDay, LocalDateTime endOfTheDay);
}
