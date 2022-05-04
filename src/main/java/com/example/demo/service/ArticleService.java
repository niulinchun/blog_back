package com.example.demo.service;

import com.example.demo.entity.Article;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    public ArrayList<Article> get_article_info();

    public ArrayList<String> get_article_category();

    public ArrayList<String> get_article_title(String category);

    public ArrayList<String> get_all_title();

    public List<Map<String, Object>> getAllCategoryAndTitle();

    public String get_article_content(String category, String title);

    public int insert_content(String encode_category, String encode_title, String encode_content);

    public int update_content(String content, String category, String title, String encode_old_category, String encode_old_title);

    public int delArticle(String category, String title);

    public ArrayList<String> searchTitle(String keyWords);
}
