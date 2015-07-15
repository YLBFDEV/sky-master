package com.ntko.app.office.documents.support;

public interface SaveCallback {
    public void callback(String path);

    public void failed();

    public void canceled();
}