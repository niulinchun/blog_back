package com.example.demo.dao;

import com.example.demo.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
@Mapper
public interface ArticleMapper {

    List<Article> get_article_info();

    List<String> get_article_category();

    List<String> get_article_title(String category);

    String get_article_content(@Param("category") String category, @Param("title") String title);

    public int insert_content(@Param("encode_category") String encode_category,
                              @Param("encode_title") String encode_title,
                              @Param("encode_content") String encode_content);

    public int update_content(String content, String category, String title, String encode_old_category, String encode_old_title);

    public int delArticle(String category, String title);

    public List<String> searchTitle(String keyWords);
}
