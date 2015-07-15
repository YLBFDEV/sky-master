package com.skytech.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArkAdapter extends BaseAdapter {
    public interface AdapterHandler {
        public View getView(int i, View view, ViewGroup viewGroup);

        public int getCount();
    }

    private AdapterHandler mAdapterHandler;

    public void setAdapterHandler(AdapterHandler adapterHandler) {
        mAdapterHandler = adapterHandler;
    }

    @Override
    public int getCount() {
        if (mAdapterHandler == null) {
            return 0;
        }
        return mAdapterHandler.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (mAdapterHandler == null) {
            return null;
        } else {
            return mAdapterHandler.getView(i, view, viewGroup);
        }
    }
}
