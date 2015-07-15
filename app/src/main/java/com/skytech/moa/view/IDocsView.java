package com.skytech.moa.view;

import com.skytech.moa.model.Document;

import java.util.ArrayList;

public interface IDocsView {
    public void LoadAllDocuments(ArrayList<Document> documents);
    public void failure(String error);
}
