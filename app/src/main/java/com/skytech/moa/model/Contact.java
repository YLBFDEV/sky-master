package com.skytech.moa.model;

import com.skytech.moa.widgets.indelible.CharacterParser;
import com.skytech.moa.widgets.indelible.IIndelibleModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable ,IIndelibleModel{
    private String id;
    private String key;
    private String name;
    private String department;
    private String duty;
    private List<Phone> phoneList = new ArrayList<>();
    private String sortLetters;  //显示数据拼音的首字母
    private String photoId;

    public Contact() {
    }

    public Contact(JSONObject json) {
        id = json.optString("id");
        key = json.optString("key");
        name = json.optString("name");
        department = json.optString("department");
        duty = json.optString("job");
        photoId = json.optString("photoid");

        initPhones(json);

        initPinyin();
    }

    private void initPinyin() {
        //实例化汉字转拼音类
        CharacterParser characterParser = CharacterParser.getInstance();
        //汉字转换成拼音
        String pinyin = characterParser.getSelling(getName());
        String sortString = pinyin.substring(0, 1).toUpperCase();

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            setSortLetters(sortString.toUpperCase());
        } else {
            setSortLetters("#");
        }
    }

    private void initPhones(JSONObject json) {
        JSONArray array = json.optJSONArray("phones");
        for (int i = 0; i < array.length(); i++) {
            phoneList.add(new Phone(array.optJSONObject(i)));
        }
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDuty() {
        return duty;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getPhotoId() {
        return photoId;
    }
}
