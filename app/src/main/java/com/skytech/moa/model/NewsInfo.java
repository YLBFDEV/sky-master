package com.skytech.moa.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/28.
 */
public class NewsInfo implements Serializable {

    //新闻id
    private String id;
    //新闻标题
    private String title;
    //发布时间
    private String time;
    //新闻图片
    private String picUrl;
    //点赞数
    private int praiseNum;
    //是否一赞
    private boolean isPraise;
    //详情网页
    private String webUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public boolean getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

}
