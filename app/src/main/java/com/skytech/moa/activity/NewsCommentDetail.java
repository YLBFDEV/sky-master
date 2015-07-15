package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.skytech.android.util.CustomProgress;
import com.skytech.moa.R;
import com.skytech.moa.adapter.NewsCommentAdapter;
import com.skytech.moa.model.NewsCommentInfo;
import com.skytech.moa.presenter.NewsCommentPresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewsCommentView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/2.
 */
public class NewsCommentDetail extends Activity implements INewsCommentView, View.OnClickListener{

    private ImageView ivPublicBack;
    private TextView tvPublicTitle;

    private ArrayList<NewsCommentInfo> newsCommentInfos;
    private NewsCommentInfo newsCommentInfo;

    private NewsCommentAdapter newsCommentAdapter;
    private NewsCommentPresenter newsCommentPresenter;
    private PullToRefreshListView newsCommentListView;

    private View noNetwork;
    private View noData;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_comment_main);
        initView();
        initData();
        refresh();
    }

    private void refresh() {
        load(false);
    }

    private void load(boolean isMore) {
        newsCommentPresenter.loadNewsComment(isMore, getRequestParams());
    }

    private RequestParams getRequestParams(){
        RequestParams params = new RequestParams();
//        Bundle bundle = getIntent().getExtras();
//        params.put("newsId", bundle.getInt("newsId") + "");
        params.put("newsId", "1");
        return params;
    }

    private void initData() {

        newsCommentPresenter = new NewsCommentPresenter(this);
        newsCommentAdapter = new NewsCommentAdapter(this);
        newsCommentListView.setAdapter(newsCommentAdapter);
    }

    private void initView() {

        ivPublicBack = (ImageView) findViewById(R.id.back);
        ivPublicBack.setOnClickListener(this);
        tvPublicTitle = (TextView) findViewById(R.id.title);
        tvPublicTitle.setText("新闻评论");
        newsCommentListView = (PullToRefreshListView) findViewById(R.id.lv_news_comment);
        newsCommentListView.setMode(PullToRefreshBase.Mode.BOTH);
        newsCommentListView.setOnRefreshListener(refreshListener);

        noNetwork = findViewById(R.id.no_network);
        noData = findViewById(R.id.no_data);

        noNetwork.setOnClickListener(errorViewClick());
        noData.setOnClickListener(errorViewClick());

        CustomProgress.showProgress(NewsCommentDetail.this, "正在加载……");
    }

    private View.OnClickListener errorViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(false);
            }
        };
    }

    @Override
    public void loadNewsCommentSuccess(ArrayList<NewsCommentInfo> list, boolean isMore) {
        int size = list.size();
        if (isMore) {
            if (0 == size) {
                Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
            } else {
                this.newsCommentInfos.addAll(list);
            }
        } else {
            if (0 == size) {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            } else {
                this.newsCommentInfos = list;
            }
        }
        hideNoNetWork();
        hideNoDataView();
        CustomProgress.hideProgress();
        newsCommentAdapter.setNewsCommentInfos(newsCommentInfos);
        refreshComplete();
        newsCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadNewsCommentFailure(String error) {
        refreshComplete();
        Toast.makeText(this, "数据加载失败", Toast.LENGTH_SHORT).show();
        CustomProgress.hideProgress();
        showNoNetWork();
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            load(false);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            load(true);
        }
    };

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.back:
                finish();
                break;
            default:
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
     * 开启线程，结束加载
     */
    private void refreshComplete() {
        handler.postDelayed(stopRefreshRunnable, Constant.LOADING_INTERVAL);
    }

    private Runnable stopRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            newsCommentListView.onRefreshComplete();
        }
    };


}
