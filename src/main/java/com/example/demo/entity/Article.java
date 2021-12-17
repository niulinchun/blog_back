package com.example.demo.entity;

public class Article {

    private String article_title;
    private String article_category;
    private String article_content;

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_category() {
        return article_category;
    }

    public void setArticle_category(String article_category) {
        this.article_category = article_category;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public Article(String article_title, String article_category, String article_content) {
        this.article_title = article_title;
        this.article_category = article_category;
        this.article_content = article_content;
    }
}
