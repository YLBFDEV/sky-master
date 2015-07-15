package com.skytech.moa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.skytech.moa.R;
import com.skytech.moa.adapter.NoticeListViewAdapter;
import com.skytech.moa.manager.NoticeManager;
import com.skytech.moa.model.NoticeInfo;
import com.skytech.moa.services.NoticesService;
import com.skytech.moa.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static String TAG = NoticeActivity.class.getSimpleName();

    private ImageView ivPublicBack;
    private TextView tvPublicTitle;
    private ImageView ivPublicSearch;

    private ArrayList<NoticeInfo> noticeInfos;

    private NoticeListViewAdapter noticeListViewAdapter;
    private NoticesService noticesService;
    private PullToRefreshListView noticeListView;

    private View noNetwork;
    private View noData;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case Constant.START_LOADING:
//                    CustomProgress.showProgress(NoticeActivity.this, "正在加载……");
//                    break;
                case Constant.START_CACHE:
                    loadNoticeSuccess((ArrayList<NoticeInfo>) msg.obj, false);
                    break;
                case Constant.LIST_SUCCESS:
                    //如果 msg.arg1 不是1的话，累加
                    loadNoticeSuccess((ArrayList<NoticeInfo>) msg.obj, msg.arg1 != 1);
                    break;
                case Constant.LIST_FAILURE:
                    loadNoticeFailure("失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_main);
        initView();
        initData();
    }

    private JSONObject getRequestParams() {
        JSONObject params = new JSONObject();
        /**
         * 2，类型：type（0，全部；1，公告；2，通知）
         * 3，开始时间：timeFrom
         * 4，结束时间：timeTo
         * 5，页码：page
         * 6，每页显示数：size
         */
        int type = 0;
        try {
            params.put("type", type);
            params.put("timeFrom", "");
            params.put("timeTo", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    private void initView() {
        ivPublicBack = (ImageView) findViewById(R.id.back);
        ivPublicBack.setOnClickListener(this);
        tvPublicTitle = (TextView) findViewById(R.id.title);
        tvPublicTitle.setText("通知公告");
        ivPublicSearch = (ImageView) findViewById(R.id.search);
        ivPublicSearch.setVisibility(View.VISIBLE);
        ivPublicSearch.setOnClickListener(this);

        noticeListView = (PullToRefreshListView) findViewById(R.id.lv_notice);
        noticeListView.setMode(PullToRefreshBase.Mode.BOTH);
        noticeListView.setOnRefreshListener(refreshListener);
        noticeListView.setOnItemClickListener(this);

        noNetwork = findViewById(R.id.no_network);
        noData = findViewById(R.id.no_data);

        noNetwork.setOnClickListener(errorViewClick());
        noData.setOnClickListener(errorViewClick());
    }

    private void initData() {
        noticesService = new NoticesService(handler);
        noticeListViewAdapter = new NoticeListViewAdapter(this);
        noticeListView.setAdapter(noticeListViewAdapter);
        Log.i(TAG, "...................................INIT DATA ()");
        noticesService.load(getRequestParams());
    }

    public void loadNoticeSuccess(ArrayList<NoticeInfo> list, boolean isMore) {
        int size = list.size();
        if (isMore) {
            if (0 == size) {
                Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
            } else {
                this.noticeInfos.addAll(list);
            }
        } else {
            this.noticeInfos = list;
        }

        hideNoNetWork();
        hideNoDataView();
        noticeListViewAdapter.setNoticeInfos(noticeInfos);
        refreshComplete();
        noticeListViewAdapter.notifyDataSetChanged();
    }

    public void loadNoticeFailure(String error) {
        Log.i(TAG, "请求失败。。。。。。。。。。。。。");
        showNoNetWork();
        refreshComplete();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.back:
                finish();
                break;
            case R.id.search:
                break;
        }
    }

    private void hideNoDataView() {
        noData.setVisibility(View.GONE);
    }

    private void showNoNetWork() {
        noNetwork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetWork() {
        noNetwork.setVisibility(View.GONE);
    }

    /**
     * 重新开线程结束加载，不然可能会出错，而且结束不了
     */
    private void refreshComplete() {
        handler.postDelayed(stopRefreshRunnable, Constant.LOADING_INTERVAL);
    }

    private Runnable stopRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            noticeListView.onRefreshComplete();
        }
    };

    /**
     * 上拉刷新 下拉加载
     */
    private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            noticesService.refresh(getRequestParams());
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            noticesService.more(getRequestParams());
        }
    };

    private View.OnClickListener errorViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticesService.refresh(getRequestParams());
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      NoticeInfo  noticeInfo = noticeInfos.get(position - 1);
        Intent intent = new Intent();
        intent.putExtra("url", noticeInfo.getUrl());
        intent.putExtra("nid", noticeInfo.getId());
        intent.putExtra("hasAttachment", noticeInfo.getHasAttachment());
        intent.setClass(this, NoticeDetailActivity.class);
        /**
         * 将新闻id存入数据库
         */
        NoticeManager.getInstance().addNotice(noticeInfo.getId());

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            noticeListViewAdapter.notifyDataSetChanged();
        }
    }

}
