package com.skytech.moa.model;

/**
 * Created by Administrator on 2015/6/5.
 */
public class NoticeInfo {

    //通知公告id
    private String nid;
    //通知公告标题
    private String title;
    //通知公告部门
    private String dept;
    //通知公告时间
    private String releaseTime;
    //通知公告详细地址
    private String url;
    //通知公告是否有附件
    private boolean hasAttachment;

    public String getId() {
        return nid;
    }

    public void setId(String nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
}
