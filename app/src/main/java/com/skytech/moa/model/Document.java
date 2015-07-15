package com.skytech.moa.model;


public class Document {
    private int id;
    private String name;
    private int type;
    private String createTime;
    private int number;

    public Document(int id, String name, int type, String createTime, int number) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createTime = createTime;
        this.number = number;
    }

    public Document() {

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
