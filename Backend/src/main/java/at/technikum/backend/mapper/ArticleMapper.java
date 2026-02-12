package at.technikum.backend.mapper;

import at.technikum.backend.dto.ArticleDto;
import at.technikum.backend.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toDto(Article article);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Article toEntity(ArticleDto dto);
}
