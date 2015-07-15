package com.skytech.moa.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.skytech.android.adapter.SkyAdapter;
import com.skytech.android.draft.Draft;
import com.skytech.android.draft.DraftManager;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeneralListView implements View.OnClickListener {
    public interface OnClickHappened {
        public void onAdd();

        public void onRefresh();

        public void onMore();

        public void onOpenDetail(JSONObject json);
    }

    private Activity activity;
    private OnClickHappened dlg;
    private Handler handler = new Handler();
    private PullToRefreshListView issueList;
    private View noNetwork;
    private View add;
    private SkyAdapter adapter;

    public GeneralListView(Activity activity, OnClickHappened delegate) {
        this.activity = activity;
        dlg = delegate;
        init();
    }

    public void setTitle(String title) {
        try {
            ((TextView) activity.findViewById(R.id.title)).setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        noNetwork = activity.findViewById(R.id.no_network);
        issueList = (PullToRefreshListView) activity.findViewById(R.id.issue_list);
        issueList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                dlg.onRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                dlg.onMore();
            }
        });
        issueList.getRefreshableView().setOnItemClickListener(issueListItemClickListener);
        issueList.getRefreshableView().setOnItemLongClickListener(issueListItemLongClickListener);
        noNetwork.setOnClickListener(this);
    }

    public void showButtonAdd() {
        add = activity.findViewById(R.id.add);
        add.setOnClickListener(this);
        add.setVisibility(View.VISIBLE);
    }

    public void setAdapter(SkyAdapter adapter) {
        this.adapter = adapter;
        issueList.setAdapter(this.adapter);
    }

    public JSONObject getParams() {
        JSONObject json = new JSONObject();
        return json;
    }

    /**
     * issue list`s item click listener
     */
    private AdapterView.OnItemClickListener issueListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dlg.onOpenDetail(adapter.getJSONObject(position - 1));
        }
    };

    /**
     * issue list`s item long click listener
     */
    private AdapterView.OnItemLongClickListener issueListItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            final JSONObject item = adapter.getJSONObject(i - 1);
            if (item.optLong(Constant.EXTRA_DRAFT, 0) == 0)
                return true;
            new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("删除该草稿？")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DraftManager draftManager = new DraftManager();
                                    draftManager.delete(item.optString(Constant.EXTRA_DRAFT, ""));
                                    adapter.removeItemByIndex(i - 1);
                                    adapter.notifyDataSetChanged();
                                    CustomToast.show(activity, "已删除！");
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            });
                    return builder.create();
                }
            }.show(activity.getFragmentManager(), "dialog");
            return true;
        }
    };

    private void showList() {
        if (noNetwork.getVisibility() == View.VISIBLE) {
            noNetwork.setVisibility(View.GONE);
        }
    }

    public void showError(String msg) {
        if (adapter.getCount() == 0) {
            noNetwork.setVisibility(View.VISIBLE);
        }
        CustomToast.show(activity, msg);
        refreshComplete();
        CustomProgress.hideProgress();
    }

    public void showDrafts(List<Draft> drafts) {
        adapter.setDrafts(drafts);
        showList();
    }

    public void refreshList(JSONObject data) {
        if (null == data) return;
        JSONArray items;
        if (data.has(Constant.JSONKEY_AFFAIRS)) {
            items = data.optJSONArray(Constant.JSONKEY_AFFAIRS);
        } else if (data.has(Constant.NOTICES)) {
            items = data.optJSONArray(Constant.NOTICES);
        } else {
            items = data.optJSONArray(Constant.JSONKEY_ITEMS);
        }
        if (data.optInt(Constant.PARAM_PAGENUM, 1) == 1) {
            if (null == items || items.length() == 0) {
                CustomToast.show(activity, "囧，当前没有任何记录耶");
            } else {
                CustomToast.show(activity, "已更新");
            }
            adapter.setData(JSONArray2List(items));
        } else {
            if (null == items || items.length() == 0) {
                CustomToast.show(activity, "没有了");
            } else {
                adapter.add(JSONArray2List(items));
                adapter.notifyDataSetChanged();
            }
        }
        refreshComplete();
        CustomProgress.hideProgress();
    }

    private List<Object> JSONArray2List(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            list.add(jsonArray.optJSONObject(i));
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_network:
                dlg.onRefresh();
                break;
            case R.id.add:
                dlg.onAdd();
                break;
        }
    }

    private void refreshComplete() {
        handler.postDelayed(stopRefreshRunnable, Constant.LOADING_INTERVAL);
    }

    private Runnable stopRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            issueList.onRefreshComplete();
        }
    };
}
