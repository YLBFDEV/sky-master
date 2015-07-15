package com.skytech.moa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.skytech.android.adapter.SkyAdapter;
import com.skytech.android.draft.Draft;
import com.skytech.moa.R;
import org.json.JSONObject;

import java.util.List;

/**
 * 多选
 */
public class MultipleChoiceAdapter extends SkyAdapter {
    private String itemValueKey;

    public MultipleChoiceAdapter(Context context, String itemValueKey) {
        super(context);
        this.itemValueKey = itemValueKey;
    }

    @Override
    protected void draft2Json(List<Draft> drafts) {

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_multiple_choice, viewGroup, false);
        }
        CheckedTextView text = (CheckedTextView) view;
        text.setCheckMarkDrawable(context.getResources().getDrawable(R.drawable.checkbox_selector));
        text.setTextColor(context.getResources().getColor(R.color.text_gray));
        text.setText(getJSONObject(i).optString(itemValueKey, ""));
        setItemChecked(i);
        return view;
    }

    private void setItemChecked(int position) {
        if (null == listView) return;
        listView.setItemChecked(position + 1, isSelected(position));
    }

    private boolean isSelected(int position) {
        if (null != selectedData && selectedData.length() > 0) {
            JSONObject item = getJSONObject(position);
            for (int i = 0; i < selectedData.length(); i++) {
                if (selectedData.optJSONObject(i).equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
