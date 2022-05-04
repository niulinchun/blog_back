package com.example.demo.service;

import com.example.demo.dao.ArticleMapper;
import com.example.demo.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService_Impl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

//    @Autowired
//    public ArticleService_Impl(ArticleMapper articleMapper) {
//        this.articleMapper = articleMapper;
//    }
//    @Autowired
//    public void setArticleMapper (ArticleMapper articleMapper) {
//        this.articleMapper = articleMapper;
//    }

    @Override
    public ArrayList<Article> get_article_info() {
        return articleMapper.get_article_info();
    }

    @Override
    public ArrayList<String> get_article_category() {
        return articleMapper.get_article_category();
    }

    @Override
    public ArrayList<String> get_article_title(String category) {
        return articleMapper.get_article_title(category);
    }

    @Override
    public ArrayList<String> get_all_title() {
        return articleMapper.get_all_title();
    }

    @Override
    public List<Map<String, Object>> getAllCategoryAndTitle() {
        return articleMapper.getAllCategoryAndTitle();
    }

    @Override
    public String get_article_content(String category, String title) {
        return articleMapper.get_article_content(category, title);
    }
    @Override
    public int insert_content(String encode_category, String encode_title, String encode_content) {
        return articleMapper.insert_content(encode_category, encode_title, encode_content);
    }

    @Override
    public int update_content(String content, String category, String title, String encode_old_category, String encode_old_title) {
        return articleMapper.update_content(content, category, title, encode_old_category, encode_old_title);
    }

    @Override
    public int delArticle(String category, String title) {
        return articleMapper.delArticle(category, title);
    }
    @Override
    public ArrayList<String> searchTitle(String keyWords) {
        return articleMapper.searchTitle(keyWords);
    }
}
