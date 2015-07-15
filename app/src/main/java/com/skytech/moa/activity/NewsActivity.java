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
import com.skytech.android.util.CustomProgress;
import com.skytech.moa.R;
import com.skytech.moa.adapter.NewsListViewAdapter;
import com.skytech.moa.manager.NewsManager;
import com.skytech.moa.model.NewsInfo;
import com.skytech.moa.services.NewsService;
import com.skytech.moa.utils.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final static String TAG = NewsActivity.class.getSimpleName();

    private ImageView ivPublicBack;
    private TextView tvPublicTitle;

    private ArrayList<NewsInfo> newsInfos;
    private NewsInfo newsInfo;

    private NewsListViewAdapter newsListViewAdapter;
    private NewsService newsService;
    private PullToRefreshListView newsListView;

    private View noNetwork;
    private View noData;
    private Handler handler1 = new Handler();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case Constant.START_LOADING:
//                    CustomProgress.showProgress(NewsActivity.this, "正在加载……");
//                    break;
                case Constant.START_CACHE:
                    loadNewsSuccess((ArrayList<NewsInfo>) msg.obj, false);
                    break;
                case Constant.LIST_SUCCESS:
                    //如果 msg.arg1 不是1的话，累加
                    loadNewsSuccess((ArrayList<NewsInfo>) msg.obj, msg.arg1 != 1);
                    break;
                case Constant.LIST_FAILURE:
                    loadNewsFailure((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);
        initView();
        initData();
    }

    private void initData() {

        newsService = new NewsService(handler);
        newsListViewAdapter = new NewsListViewAdapter(this);
        newsListView.setAdapter(newsListViewAdapter);
        newsService.load(getRequestParams());

    }

    private JSONObject getRequestParams() {
        JSONObject params = new JSONObject();
        /**
         * 2，类型：type（0，全部；1，公告；2，通知）
         * 3，开始时间：timeFrom
         * 4，结束时间：timeTo
         * 5，页码：pagenum
         * 6，每页显示数：pagesize
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
        tvPublicTitle.setText("公司动态");
        newsListView = (PullToRefreshListView) findViewById(R.id.lv_news);
        newsListView.setOnRefreshListener(refreshListener);
        newsListView.setOnItemClickListener(this);

        noNetwork = findViewById(R.id.no_network);
        noData = findViewById(R.id.no_data);

        noNetwork.setOnClickListener(errorViewClick());
        noData.setOnClickListener(errorViewClick());

    }

    public void loadNewsSuccess(ArrayList<NewsInfo> list, boolean more) {
        int size = list.size();
        if (more) {
            if (0 == size) {
                Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
            } else {
                this.newsInfos.addAll(list);
            }
        } else {

            this.newsInfos = list;
        }

        hideNoNetWork();
        hideNoDataView();
        CustomProgress.hideProgress();
        Log.i(TAG, "......................." + newsInfos.toString());
        newsListViewAdapter.setNewsInfos(newsInfos);
        refreshComplete();
        newsListViewAdapter.notifyDataSetChanged();
    }

    public void loadNewsFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        CustomProgress.hideProgress();
        refreshComplete();
        showNoNetWork();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        newsInfo = newsInfos.get(position - 1);
        Intent intent = new Intent();
        intent.putExtra("id", newsInfo.getId());
        intent.putExtra("webUrl", newsInfo.getWebUrl());
        intent.putExtra("praiseNum", newsInfo.getPraiseNum());
        intent.putExtra("isPraised", newsInfo.getIsPraise());
        intent.setClass(this, NewsDetailActivity.class);
        /*将新闻id存入数据库中*/
        NewsManager.getInstance().addNews(newsInfo.getId());

        startActivityForResult(intent, 1);
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            newsService.refresh(getRequestParams());
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            newsService.more(getRequestParams());
        }
    };

    private void hideNoDataView() {
        noData.setVisibility(View.GONE);
    }

    private void showNoNetWork() {
        noNetwork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetWork() {
        noNetwork.setVisibility(View.GONE);
    }

    private View.OnClickListener errorViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsService.refresh(getRequestParams());
            }
        };
    }

    /**
     * 重新开线程结束加载，不然可能会出错，而且结束不了
     */
    private void refreshComplete() {
        handler1.postDelayed(stopRefreshRunnable, Constant.LOADING_INTERVAL);
    }

    private Runnable stopRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            newsListView.onRefreshComplete();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            newsListViewAdapter.notifyDataSetChanged();
        }
    }
}
