package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private List<Article> allArticle = new ArrayList<>();


    public List<Article> getAllArticle() {
        return this.allArticle;
    }

    public Article getArticle(int id) {
        for (int i = 0; i < allArticle.size(); i++) {
            if (allArticle.get(i).getId() == id) {
                return allArticle.get(i);
            }
        }
        return null;
    }


    public Article addArticle(Article article) {
        if (article.getArticle() == null) {
            //message: You can't create an empty article
            return null;
        }
        allArticle.add(article); //add article to DB
        return article;
    }

    public Article updateArticle(Article article, String newText) {
        //check if article id is in DB
        for (int i = 0; i < allArticle.size(); i++) {
            if (allArticle.get(i).getId() == article.getId()) {

                //if article in DB set new StringContent
                allArticle.get(i).setArticle(newText);
                return allArticle.get(i);
            }
        }
        return null;
    }

    public boolean deleteArticle(Article article) {
        for (int i = 0; i < allArticle.size(); i++) {
            if (allArticle.get(i).getId() == article.getId()) {
                allArticle.remove(article);
                return true;
            }
        }
        return false;
    }


}