package com.skytech.moa.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Module implements Serializable {
    private String key;
    private String name;
    private String num;
    private List<Module> children = new ArrayList<Module>();

    public Module(String key, String name, String num) {
        this.key = key;
        this.name = name;
        this.num = num;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<Module> getChildren() {
        return children;
    }

    public void setChildren(List<Module> modules) {
        children = modules;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public String toString() {
        return "Key = " + key + ", Name = " + name + ", NUM = " + num;
    }
}
