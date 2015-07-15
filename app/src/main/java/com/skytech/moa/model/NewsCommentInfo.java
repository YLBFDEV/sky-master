package com.skytech.moa.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/6/2.
 */
public class NewsCommentInfo implements Serializable {
    //评论id
    private int _id;
    //用户id
    private int uid;
    //新闻id
    private int newsId;
    //用户头像
    private String uidPic;
    //用户姓名
    private String name;
    //用户评论
    private String content;
    //评论时间
    private String time;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUidPic() {
        return uidPic;
    }

    public void setUidPic(String uidPic) {
        this.uidPic = uidPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
