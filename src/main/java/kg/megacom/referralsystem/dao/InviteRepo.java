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

    Invite findInviteByReceiverAndSenderAndInviteStatus(Subscriber receiver, Subscriber sender, InviteStatus inviteStatus);

    @Query(value = "Select count sender_subs_id from Invite where sender_subs_id=?1 between date_trunc('month', current_date) and date", nativeQuery = true)
    int getCountOfInvitesPerMonth(Long id,LocalDateTime date);

    @Query(value = "Select count sender_subs_id from Invite where sender_subs_id=?1 and status = NEW and period between ?2 and ?3", nativeQuery = true)
    int geCountPerDay(Long id, LocalDateTime initialTime, LocalDateTime endOfTheDay);
}
