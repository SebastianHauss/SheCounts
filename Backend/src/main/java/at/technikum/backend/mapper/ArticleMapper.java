package at.technikum.backend.mapper;

import at.technikum.backend.dto.ArticleDto;
import at.technikum.backend.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toDto(Article article);

    Article toEntity(ArticleDto articleDto);
}
