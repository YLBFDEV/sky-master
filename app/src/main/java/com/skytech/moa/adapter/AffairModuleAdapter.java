package com.skytech.moa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.android.adapter.SkyAdapter;
import com.skytech.android.draft.Draft;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class AffairModuleAdapter extends SkyAdapter {
    public AffairModuleAdapter(Context context) {
        super(context);
        initData();
    }

    @Override
    protected void draft2Json(List<Draft> drafts) {

    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_affair_entry, null);
        }
        JSONObject rowJson = getJSONObject(i);
        ((TextView) view.findViewById(R.id.title)).setText(rowJson.optString(Constant.EXTRA_MODULE_NAME));
        ((TextView) view.findViewById(R.id.description)).setText(rowJson.optString("description"));
        ((ImageView) view.findViewById(R.id.icon)).setImageResource(rowJson.optInt("ico"));
        return view;
    }

    private void initData() {
        mapData.clear();
        try {
            mapData.add(newAffairModule(context.getString(R.string.title_affair_apply_affair), context.getString(R.string.desc_affair_apply_affair), R.drawable.icon_affair_apply_affair, Constant.MODULE_RCSW_SWSQ));
            mapData.add(newAffairModule(context.getString(R.string.title_affair_meeting_management), context.getString(R.string.desc_affair_meeting_management), R.drawable.icon_affair_meeting, Constant.MODULE_NEWMEETING));
            mapData.add(newAffairModule(context.getString(R.string.title_affair_memo), context.getString(R.string.desc_affair_memo), R.drawable.icon_affair_memo, Constant.MODULE_NEWMEMO));
            mapData.add(newAffairModule(context.getString(R.string.title_affair_task), context.getString(R.string.desc_affair_task), R.drawable.icon_affair_task, Constant.MODULE_NEWWORKPLAN));
            mapData.add(newAffairModule(context.getString(R.string.title_affair_work_log), context.getString(R.string.desc_affair_work_log), R.drawable.icon_affair_work_log, Constant.MODULE_NEWWORKLOG));
            mapData.add(newAffairModule(context.getString(R.string.title_affair_fee), context.getString(R.string.desc_affair_fee), R.drawable.icon_affair_fee, Constant.MODULE_EXPENSE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject newAffairModule(String title, String description, int resId, String moduleCode) throws JSONException {
        JSONObject module = new JSONObject();
        module.put(Constant.EXTRA_MODULE_NAME, title);
        module.put("description", description);
        module.put("ico", resId);
        module.put(Constant.EXTRA_MODULE_CODE, moduleCode);
        return module;
    }
}
