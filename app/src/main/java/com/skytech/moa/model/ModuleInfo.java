package com.skytech.moa.model;

public class ModuleInfo {
    private int image;
    private Class<?> cls;
    private String moduleName;
    private String listUrl, detailUrl, buttonsUrl, submitUrl;

    public ModuleInfo(int image, Class<?> cls) {
        this.image = image;
        this.cls = cls;
    }

    public ModuleInfo(String listUrl, String detailUrl, String buttonsUrl, String submitUrl) {
        this.listUrl = listUrl;
        this.detailUrl = detailUrl;
        this.buttonsUrl = buttonsUrl;
        this.submitUrl = submitUrl;
    }

    public ModuleInfo(int image, Class<?> cls, String listUrl, String detailUrl, String buttonsUrl, String submitUrl) {
        this.image = image;
        this.cls = cls;
        this.listUrl = listUrl;
        this.detailUrl = detailUrl;
        this.buttonsUrl = buttonsUrl;
        this.submitUrl = submitUrl;
    }

    public String getListUrl() {
        return listUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public String getButtonsUrl() {
        return buttonsUrl;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public Class<?> getCls() {
        return cls;
    }

    public int getImage() {
        return image;
    }
}
