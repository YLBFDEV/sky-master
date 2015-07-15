package com.skytech.moa.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Phone implements Serializable {
    private String label;
    private String phone;

    public Phone(JSONObject json) {
        this(json.optString("label"), json.optString("phone"));
    }

    public Phone(String label, String phone) {
        this.label = label;
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }

    public String getPhone() {
        return phone;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
