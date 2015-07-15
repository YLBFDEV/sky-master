package com.skytech.android.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.skytech.android.draft.Draft;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzf on 2015/5/4.
 */
public abstract class SkyAdapter extends BaseAdapter {
    protected List<Object> mapData = new ArrayList<>();

    protected JSONArray selectedData;
    protected Context context;
    protected ListView listView;

    public SkyAdapter(Context context) {
        this.context = context;
    }

    public void setSelectedData(JSONArray selectedData) {
        this.selectedData = selectedData;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public int getCount() {
        if (null == mapData)
            return 0;
        return mapData.size();
    }

    @Override
    public Object getItem(int i) {
        return mapData.get(i);
    }

    public JSONObject getJSONObject(int i) {
        return (JSONObject) mapData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    protected abstract void draft2Json(List<Draft> drafts);

    public void setData(JSONArray array) {
        this.setData(JSONArray2List(array));
    }

    public void setData(List<Object> list) {
        mapData = list;
        notifyDataSetChanged();
    }

    public void setDrafts(List<Draft> drafts) {
        draft2Json(drafts);
        notifyDataSetChanged();
    }

    public void add(List<Object> list) {
        mapData.addAll(list);
    }

    public void removeItemByIndex(int index) {
        mapData.remove(index);
    }

    private List<Object> JSONArray2List(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            list.add(jsonArray.optJSONObject(i));
        }
        return list;
    }
}
