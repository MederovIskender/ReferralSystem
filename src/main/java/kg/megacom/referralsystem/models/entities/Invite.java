package kg.megacom.referralsystem.models.entities;

import kg.megacom.referralsystem.enums.InviteStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    LocalDateTime startDate;
    LocalDateTime endDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    InviteStatus inviteStatus;

}
