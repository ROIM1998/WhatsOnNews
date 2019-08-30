package com.java.whatsonnews;

import com.orm.SugarRecord;
import java.io.Serializable;

public class News extends SugarRecord implements Serializable {
    private String title;
    private String category;
    private String content;
    private String imgPath;
    private String newsID;

    public News(){}
    public News(String title,String category,String content){
        this.title=title;
        this.category=category;
        this.content=content;
        this.imgPath=null;
    }
    public News(String title,String category,String content,String imgPath){
        this.title=title;
        this.category=category;
        this.content=content;
        this.imgPath=imgPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setNewsID(String newsID){
        this.newsID=newsID;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsID(){
        return newsID;
    }
}