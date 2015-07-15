package com.skytech.moa.model;

import com.skytech.moa.utils.Constant;
import org.json.JSONObject;

public class DocumentDetail {
    private int docId;
    private String name;
    private int type;
    private String releaseTime;
    private String publisher;
    private boolean isFavorite;
    private int docFormat;

    public DocumentDetail(int docId, String name, int type, String releaseTime, String publisher, boolean isFavorite, int docFormat) {
        this.docId = docId;
        this.name = name;
        this.type = type;
        this.publisher = publisher;
        this.releaseTime = releaseTime;
        this.isFavorite = isFavorite;
        this.docFormat = docFormat;
    }

    public DocumentDetail(JSONObject jsonObject) {
        this.docId = jsonObject.optInt("docId");
        this.name = jsonObject.optString("name");
        this.type = jsonObject.optInt("type");
        this.releaseTime = jsonObject.optString("releaseTime");
        this.publisher = jsonObject.optString("publisher");
        this.isFavorite = jsonObject.optBoolean("isFavorite");
        this.docFormat = DocFormat(jsonObject.optString("docFormat"));
    }


    public String getExtension() {
        switch (docFormat) {
            case Constant.PDF:
                return "pdf";
            case Constant.PPT:
                return "ppt";
            case Constant.ZIP:
                return "zip";
            case Constant.DOC:
                return "doc";
        }
        return null;
    }

    public DocumentDetail() {
    }

    public int getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(int docFormat) {
        this.docFormat = docFormat;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public int DocFormat(String format) {
        if (format.equals("pdf"))
            return Constant.PDF;
        if (format.equals("doc"))
            return Constant.DOC;
        if (format.equals("zip"))
            return Constant.ZIP;
        else {
            return Constant.PPT;
        }
    }
}
