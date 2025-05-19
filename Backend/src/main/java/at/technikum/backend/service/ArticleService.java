package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    /*
    TODO: allArticles -> plural, enhanced for-loops (forEach), logs/messages in methods, error handling
     */

    private List<Article> articles = new ArrayList<>();


    public List<Article> getArticles() {
        return this.articles;
    }

    public Article getArticle(int id) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == id) {
                return articles.get(i);
            }
        }
        return null;
    }


    public Article addArticle(Article article) {
        if (article.getArticle() == null) {
            //message: You can't create an empty article
            return null;
        }
        articles.add(article); //add article to DB
        return article;
    }

    public Article updateArticle(Article article, String newText) {
        //check if article id is in DB
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == article.getId()) {

                //if article in DB set new StringContent
                articles.get(i).setArticle(newText);
                return articles.get(i);
            }
        }
        return null;
    }

    public boolean deleteArticle(Article article) {
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == article.getId()) {
                articles.remove(article);
                return true;
            }
        }
        return false;
    }


}