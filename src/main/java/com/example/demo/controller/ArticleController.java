package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;

import org.apache.ibatis.annotations.Param;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Controller
@RequestMapping("")
public class ArticleController {

    @Value("${file-save-path}")
    private String fileSavePath;

    @Autowired
    private ArticleService articleService;

//    @Autowired
//    public ArticleController(ArticleService articleService) {
//        this.articleService = articleService;
//    }
//    @Autowired
//    public void setArticleService (ArticleService articleService) {
//        this.articleService = articleService;
//    }

//    @RequestMapping("/myBlog")
//    public String page() {
//        return "article";
//    }



    @RequestMapping("/queryArticleInfo")
    public String getArticleInfo(Model model) throws IOException {

        List<Article> lists = articleService.get_article_info();
        ArrayList<Article> decode_lists = new ArrayList<Article>();
        BASE64Decoder decoder = new BASE64Decoder();
        for (Article article : lists) {
            String category = new String(decoder.decodeBuffer(article.getArticle_category()), "UTF-8");
            String title = new String(decoder.decodeBuffer(article.getArticle_title()), "UTF-8");
            String content = new String(decoder.decodeBuffer(article.getArticle_content()), "UTF-8");
            Article a = new Article(title, category, content);
            decode_lists.add(a);
        }
        model.addAttribute("article_info", decode_lists);
        return "index";
    }
    @ResponseBody
    @RequestMapping("/queryCategory")
    public ArrayList<String> getCategory(Model model) throws IOException {
        List<String> lists = articleService.get_article_category();
        ArrayList<String> decode_lists = new ArrayList<String>();
        BASE64Decoder decoder = new BASE64Decoder();
        for (String category: lists){
            String tmp = new String(decoder.decodeBuffer(category), "UTF-8");
            decode_lists.add(tmp);
        }
        model.addAttribute("category_info", decode_lists);
        return decode_lists;
    }

    @ResponseBody
    @RequestMapping("/queryTitle")
    public ArrayList<String> getTitle(@Param("category") String category) throws IOException {
        ArrayList<String> decode_lists = new ArrayList<>();
        BASE64Decoder decoder = new BASE64Decoder();
//        String decode_cat = new String(decoder.decodeBuffer(category), "utf-8");
        List<String> lists = articleService.get_article_title(category);
        for (String title: lists){
            String tmp = new String(decoder.decodeBuffer(title), "UTF-8");
            decode_lists.add(tmp);
        }
        return decode_lists;
    }

    @ResponseBody
    @RequestMapping("/searchTitle")
    public ArrayList<String> searchTitle(@Param("keyWords") String keyWords) throws IOException {
        ArrayList<String> decode_lists = new ArrayList<>();
        String de = URLDecoder.decode(keyWords, "utf-8");

        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        String encodeKeyWords = new String(de.getBytes(StandardCharsets.UTF_8));

        List<String> lists = articleService.searchTitle(encodeKeyWords);
        for (String title: lists) {
            String tmp = new String(decoder.decodeBuffer(title), "utf-8");
            decode_lists.add(tmp);
        }
        return decode_lists;
    }

    @ResponseBody
    @RequestMapping("/queryContent")
    public String getContent(@Param("category") String category,
                                        @Param("title") String title) throws IOException {
//        ArrayList<String> decode_lists = new ArrayList<>();
        String title_new = title.replace('-','+');
        String category_new = category.replace('-','+');
        BASE64Decoder decoder = new BASE64Decoder();
//        BASE64Encoder encoder = new BASE64Encoder();
//        String encode_category = encoder.encode(category.getBytes(StandardCharsets.UTF_8));
        String content = articleService.get_article_content(category_new, title_new);
        if(content != null) {
            byte[] mid = decoder.decodeBuffer(content);
            String tmp = new String(mid, "UTF-8");
            return tmp;
        }
        return null;

    }

//    @ResponseBody
//    @RequestMapping(value = "/submitContent", method = RequestMethod.POST)
//    public int submitContent(@RequestParam(value = "articleInfo") String JsonObj) throws JSONException {
//        JSONObject articleInfo = new JSONObject(JsonObj);
//        String category = articleInfo.getString("category");
//        String title = articleInfo.getString("title");
//        String content = articleInfo.getString("content");
//        System.out.println(category+title+content);
//        BASE64Encoder encoder = new BASE64Encoder();
//        String encode_category = encoder.encode(category.getBytes(StandardCharsets.UTF_8));
//        String encode_title = encoder.encode(title.getBytes(StandardCharsets.UTF_8));
//        String encode_content = encoder.encode(content.getBytes(StandardCharsets.UTF_8));
//        return articleService.insert_content(encode_category, encode_title, encode_content);
//
//    }

    @ResponseBody
    @RequestMapping(value = "/updateContent", method = RequestMethod.POST)
    public int updateContent(@RequestParam(value = "articleInfo") String JsonObj) throws JSONException {

        String category = new JSONObject(JsonObj).getString("category");
        String title = new JSONObject(JsonObj).getString("title");
        String content = new JSONObject(JsonObj).getString("content");
        String old_category = new JSONObject(JsonObj).getString("old_category");
        String old_title = new JSONObject(JsonObj).getString("old_title");
        String encode_category = new BASE64Encoder().encode(category.getBytes(StandardCharsets.UTF_8));
        String encode_title = new BASE64Encoder().encode(title.getBytes(StandardCharsets.UTF_8));
        String encode_content = new BASE64Encoder().encode(content.getBytes(StandardCharsets.UTF_8));
        String encode_old_title = new BASE64Encoder().encode(old_title.getBytes(StandardCharsets.UTF_8));
        String encode_old_category = new BASE64Encoder().encode(old_category.getBytes(StandardCharsets.UTF_8));

        return articleService.update_content(encode_content, encode_category, encode_title, encode_old_category, encode_old_title);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public String upload(MultipartFile file, Model model, HttpServletRequest request) {

        String fileName = file.getOriginalFilename();
        String cur_dir = System.getProperty("user.dir");

//        System.out.println("cur_dir:"+cur_dir);
        String path = fileSavePath;
//        String path = cur_dir+"/src/main/resources/static/dist/uploads/";
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFileName= UUID.randomUUID().toString().replaceAll("-", "")+suffix;

        File dest = new File(path + newFileName);
        try {
            file.transferTo(dest);
            String url = "/uploads/" + newFileName;
//            String url = request.getScheme() + "://" + request.getServerName() + ":8090/myBlog/images/" + fileName;
//            System.out.println(url);
//            model.addAttribute("success", true);
//            model.addAttribute("imageName", newFileName);
//            model.addAttribute("message","文件上传成功");
            return url;
        } catch (IllegalStateException | IOException e) {
//            model.addAttribute("success", false);
//            model.addAttribute("message","文件上传失败");
//            e.printStackTrace();
            return "error";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delImg", method = RequestMethod.GET)
    public int delImg(String imgName) throws UnsupportedEncodingException {
        String img_name = URLDecoder.decode(imgName, "utf-8");
        String cur_dir = System.getProperty("user.dir");
        String path = cur_dir+"/src/main/resources/static/dist/uploads/";
        File img = new File(path + img_name);
        if(img.exists() && img.isFile()) {
            boolean del = img.delete();
            if(del) {
                return 1;
            }
            else
                return 0;
        }

        return 0;

    }

    @ResponseBody
    @RequestMapping("getAllCategoryAndTitle")
    public HashMap<String,String> getAllCategoryAndTitle() throws IOException {
        List<String> lists = articleService.get_article_category();
        BASE64Decoder decoder = new BASE64Decoder();
        HashMap<String, String> map = new HashMap<>();
        for (String category : lists) {
            String tmp_category = new String(decoder.decodeBuffer(category), "UTF-8");
            List<String> title_list = articleService.get_article_title(category);
            for (String s : title_list) {
                String tmp_title = new String(decoder.decodeBuffer(s), "UTF-8");
                map.put(tmp_title, tmp_category);
            }
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/addNewArticle")
    public int addNewArticle(@RequestParam(value = "articleInfo") String JsonObj) throws JSONException {
        String category = new JSONObject(JsonObj).getString("category");
        String title = new JSONObject(JsonObj).getString("title");
        String content = new JSONObject(JsonObj).getString("content");
        String encode_category = new BASE64Encoder().encode(category.getBytes(StandardCharsets.UTF_8));
        String encode_title = new BASE64Encoder().encode(title.getBytes(StandardCharsets.UTF_8));
        String encode_content = new BASE64Encoder().encode(content.getBytes(StandardCharsets.UTF_8));
        return articleService.insert_content(encode_category, encode_title, encode_content);
    }

    @ResponseBody
    @RequestMapping("/delArticle")
    public int delArticle(@RequestParam(value = "articleInfo") String JsonObj) throws JSONException {

        String category = new JSONObject(JsonObj).getString("category");
        String title = new JSONObject(JsonObj).getString("title");

        String encode_category = new BASE64Encoder().encode(category.getBytes(StandardCharsets.UTF_8));
        String encode_title = new BASE64Encoder().encode(title.getBytes(StandardCharsets.UTF_8));

        return articleService.delArticle(encode_category, encode_title);
    }

    @ResponseBody
    @RequestMapping("/userLogin")
    public int userLogin(@RequestParam(value = "userData") String JsonObj) throws JSONException {
        String username = new JSONObject(JsonObj).getString("username");
        String password = new JSONObject(JsonObj).getString("password");
        if(username.equals("admin") && password.equals("admin")) {
            return 1;
        } else {
            return 0;
        }
    }
}
