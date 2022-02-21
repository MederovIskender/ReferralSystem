package kg.megacom.referralsystem.models.dtos;

import kg.megacom.referralsystem.enums.InviteStatus;
import kg.megacom.referralsystem.models.entities.Subscriber;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InviteEntityDto {
    long id;
    Subscriber sender;
    Subscriber receiver;
    Date startDate;
    Date endDate;
    InviteStatus inviteStatus;
}
