package com.skytech.android.draft;

import com.skytech.android.exception.BusinessLogicalException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Draft implements Serializable {
    /**
     * draft`s local id in database
     */
    private long id;
    /**
     * draft`s relative parent id
     */
    private String pkId;
    /**
     * entity`s content
     */
    private String content;

    /**
     * draft`s update time
     */
    private String updateTime;

    public Draft(long id, String pkId, String updateTime, String content)
            throws BusinessLogicalException {
        this.id = id;
        setPkId(pkId);
        setUpdateTime(updateTime);
        this.content = content;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPkId() {
        return pkId;
    }

    public synchronized void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public JSONObject getContentJson() {
        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
