package com.skytech.moa.view;

import com.skytech.moa.model.DocumentDetail;

import java.util.ArrayList;

public interface IDocDetailView {
    public void loadAllDocDetail(ArrayList<DocumentDetail> documents);
    public void more(ArrayList<DocumentDetail> documents);
    public void failure(String error);
    public void pullToRefresh(ArrayList<DocumentDetail> documents);
}
