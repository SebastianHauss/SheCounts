package at.technikum.backend.mapper;

import at.technikum.backend.dto.UserDto;
import at.technikum.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = UserMapperHelper.class)
public interface UserMapper {

    @Mapping(target = "profileId", source = "profile", qualifiedByName = "getUUIDFromProfile")
    UserDto toDto(User user);

    @Mapping(target = "profile", source = "profileId", qualifiedByName = "getProfileFromUUID")
    User toEntity(UserDto userDto);

}
