package com.skytech.moa.widgets.doclibrary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.skytech.moa.model.Document;

import java.util.ArrayList;

public class DocBaseAdapter extends BaseAdapter {
    private ArrayList<Document> docLibraries;
    public void setDocLibraries(ArrayList<Document> docLibraries) {
        this.docLibraries = docLibraries;
    }

    public ArrayList<Document> getDocLibraries(){
        return docLibraries;
    }
    @Override
    public int getCount() {
        return docLibraries.size();
    }

    @Override
    public Object getItem(int i) {
        return docLibraries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
