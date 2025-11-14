package at.technikum.backend.mapper;

import at.technikum.backend.dto.ArticleDto;
import at.technikum.backend.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUUIDFromUser")
    ArticleDto toDto(Article article);

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUUIDFromUser")
    Article toEntity(ArticleDto articleDto);
}
