package com.skytech.android.model;

/**
 * Created by huangzf on 2015/4/2.
 */
public class User {
    private String id;
    private String name;
    private String section;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String id, String name, String section) {
        this.id = id;
        this.name = name;
        this.section = section;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }
}
