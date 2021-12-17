package com.example.demo.service;

import com.example.demo.entity.Article;

import java.util.List;

public interface ArticleService {
    public List<Article> get_article_info();

    public List<String> get_article_category();

    public List<String> get_article_title(String category);

    public String get_article_content(String category, String title);

    public int insert_content(String encode_category, String encode_title, String encode_content);

    public int update_content(String content, String category, String title, String encode_old_category, String encode_old_title);

    public int delArticle(String category, String title);

    public List<String> searchTitle(String keyWords);
}
