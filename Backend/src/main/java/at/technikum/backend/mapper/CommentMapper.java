package at.technikum.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import at.technikum.backend.dto.CommentDto;
import at.technikum.backend.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(target = "profilePictureId", source = "user.profilePictureId")
    @Mapping(source = "article.id", target = "articleId")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "article", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toEntity(CommentDto dto);
}
