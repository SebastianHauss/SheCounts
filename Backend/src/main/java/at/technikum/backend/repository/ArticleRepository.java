package at.technikum.backend.repository;

import at.technikum.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String > {

}
