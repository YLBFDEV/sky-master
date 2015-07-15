package com.skytech.moa.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.R;

public class NoticeDetailActivity extends Activity implements View.OnClickListener {

    private final static String TAG = NoticeDetailActivity.class.getSimpleName();
    private final static String errorUrl = "about:blank";

    private ImageView ivPublicBack;
    private TextView tvPublicTitle;
    private ImageView ivAttachment;
    private Button btnAttachment;

    private WebView wvNoticeContent;

    private String url;
    private String id;
    private boolean hasAttachment;

    private View noNetWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail);
        initView();
        initData();
        loadWeb(url);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
        id = bundle.getString("nid");
        hasAttachment = bundle.getBoolean("hasAttachment");
        url = SkyHttpClient.getInstance().getAppBaseUrl() + url;
        /**
         * 通知公告附件
         */
        if (hasAttachment) {
            btnAttachment.setText("查看附件");
            btnAttachment.setVisibility(View.VISIBLE);
            btnAttachment.setOnClickListener(this);
        }
    }

    private void initView() {
        ivPublicBack = (ImageView) findViewById(R.id.back);
        ivPublicBack.setOnClickListener(this);
        tvPublicTitle = (TextView) findViewById(R.id.title);
        tvPublicTitle.setText("公告详情");
        ivAttachment = (ImageView) findViewById(R.id.attachment);
        ivAttachment.setOnClickListener(this);
        btnAttachment = (Button) findViewById(R.id.comment);
        wvNoticeContent = (WebView) findViewById(R.id.wv_notice_detail_content);
        noNetWork = findViewById(R.id.no_network);
        noNetWork.setOnClickListener(this);
    }

    private void loadWeb(String webUrl) {
        wvNoticeContent.loadUrl(webUrl);
        wvNoticeContent.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = wvNoticeContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    /**
     * 对加载网页中出现的问题 进行处理
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i(TAG, "........................开始加载网页成功");
            hideNoNetWork();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i(TAG, "........................加载网页成功");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "........................加载网页失败");
            loadWeb(errorUrl);
            showNoNetWork();
        }
    }

    private void showNoNetWork() {
        noNetWork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetWork() {
        noNetWork.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.back:
                Intent intent = new Intent(this, NoticeActivity.class);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.comment:
                Intent intent1 = new Intent();
                intent1.putExtra("nid", id);
                intent1.setClass(this, NoticeAttachmentActivity.class);
                startActivity(intent1);
                break;
            case R.id.no_network:
                loadWeb(url);
                break;
            default:
                break;
        }
    }

}
