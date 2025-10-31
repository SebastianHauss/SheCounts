package at.technikum.backend.mapper;

import at.technikum.backend.dto.ProfileDto;
import at.technikum.backend.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProfileMapperHelper.class)
public interface ProfileMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUUIDFromUser")
    ProfileDto toDto(Profile profile);

    @Mapping(target = "user", source = "userId", qualifiedByName = "getUserFromUUID")
    Profile toEntity(ProfileDto profileDto);
}
