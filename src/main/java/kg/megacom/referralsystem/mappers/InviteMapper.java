package kg.megacom.referralsystem.mappers;

import kg.megacom.referralsystem.models.dtos.InviteEntityDto;
import kg.megacom.referralsystem.models.dtos.InviteRequestDto;
import kg.megacom.referralsystem.models.entities.Invite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InviteMapper {
    InviteMapper INSTANCE = Mappers.getMapper(InviteMapper.class);

    @Mappings({
            @Mapping(target="sender.phone", source="senderPhone"),
            @Mapping(target="receiver.phone", source="receiverPhone")
    })
    Invite InviteRequestDtoToInvite(InviteRequestDto inviteRequestDto);

    @Mappings({
            @Mapping(target="senderPhone", source="sender.phone"),
            @Mapping(target="receiverPhone", source="receiver.phone")
    })
    InviteRequestDto InviteToInviteRequestDto(Invite Invite);

    Invite InviteEntityDtoToInvite(InviteEntityDto inviteEntityDto);
    InviteEntityDto InviteToInviteEntityDto(Invite invite);



}
