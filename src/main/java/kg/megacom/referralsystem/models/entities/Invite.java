package kg.megacom.referralsystem.models.entities;

import kg.megacom.referralsystem.enums.InviteStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Invite")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "sender_subs_id")
    Subscriber sender;

    @ManyToOne
    @JoinColumn(name = "receiver_subs_id")
    Subscriber receiver;

    @CreationTimestamp
    Date startDate;
    Date endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    InviteStatus inviteStatus;


}