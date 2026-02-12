package at.technikum.backend.mapper;

import at.technikum.backend.dto.NotificationDto;
import at.technikum.backend.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = NotificationMapperHelper.class)
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUUIDFromUser")
    NotificationDto toDto(Notification notification);

    @Mapping(target = "user", source = "userId", qualifiedByName = "getUserFromUUID")
    Notification toEntity(NotificationDto notificationDto);

}
