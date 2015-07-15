package com.skytech.moa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.moa.R;
import com.skytech.moa.services.NewsCommentCommitService;
import com.skytech.moa.services.NewsPraiseService;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/1.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private final static String TAG = NewsDetailActivity.class.getSimpleName();

    private WebView wvNewsContent;
    private ImageView ivPublicBack;
    private TextView tvPublicTitle;
    private Button btnViewComment;
    private RelativeLayout rlFooter;

    private Button btnCommitComment;
    private Button btnPraise;
    private EditText etComment;

    private int praiseNum;
    private String comment;
    private String webUrl;
    private String newsId;
    private boolean isPraised;

    private Context context;
    private NewsCommentCommitService commentService;
    private NewsPraiseService praiseService;

    private View noNetWork;
    private final static String errorUrl = "about:blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        initData();
        initView();
        loadWeb(webUrl);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        newsId = bundle.getString("id");
        webUrl = bundle.getString("webUrl");
        praiseNum = bundle.getInt("praiseNum");
        isPraised = bundle.getBoolean("isPraised");
        webUrl = SkyHttpClient.getInstance().getAppBaseUrl() + webUrl;
    }

    private void initView() {

        ivPublicBack = (ImageView) findViewById(R.id.back);
        ivPublicBack.setOnClickListener(this);
        tvPublicTitle = (TextView) findViewById(R.id.title);
        tvPublicTitle.setText("新闻详情");
        rlFooter = (RelativeLayout) findViewById(R.id.rl_news_detail_footer);
        btnViewComment = (Button) findViewById(R.id.comment);
        btnViewComment.setVisibility(View.VISIBLE);
        btnViewComment.setOnClickListener(this);
        wvNewsContent = (WebView) findViewById(R.id.wv_news_detail_content);
        btnCommitComment = (Button) findViewById(R.id.btn_news_comment_commit);
        btnCommitComment.setOnClickListener(this);
        btnPraise = (Button) findViewById(R.id.btn_news_praise);
        if (isPraised) {
            btnPraise.setClickable(false);
            btnPraise.setText("已");
        } else {
            btnPraise.setOnClickListener(this);
        }
        etComment = (EditText) findViewById(R.id.et_news_comment);
        noNetWork = findViewById(R.id.no_network);
        noNetWork.setOnClickListener(this);

    }

    /**
     * 加载网页
     */
    private void loadWeb(String webUrl) {

        wvNewsContent.loadUrl(webUrl);
        wvNewsContent.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = wvNewsContent.getSettings();
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
            Log.i(TAG, "........................加载网页成功: url : " + url + "     errorUrl : " + errorUrl );
            if ( url.equals(errorUrl)) {
                Log.i(TAG, "editview 获得不了焦点");
                etComment.setFocusable(false);
                btnViewComment.setClickable(false);
            } else {
                Log.i(TAG, "editview 获得焦点");
                btnViewComment.setClickable(true);
                etComment.setFocusable(true);
                etComment.setFocusableInTouchMode(true);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "........................加载网页失败");
            //当加载失败的时候，加载一个空白页面
            loadWeb(errorUrl);
            showNoNetWork();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch(i) {
            case R.id.btn_news_comment_commit:

                commitComment();

                break;
            case R.id.btn_news_praise:

                praiseNum ++;
                praiseNews(praiseNum);
                break;

            case R.id.comment:

                Intent intent = new Intent();
                intent.putExtra("newsId", newsId);
                intent.setClass(this, NewsCommentDetail.class);
                startActivity(intent);
                break;

            case R.id.back:
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Intent intent1 = new Intent();
                intent1.setClass(this, NewsActivity.class);
                setResult(RESULT_OK, intent1);
                finish();
                break;

            case R.id.no_network:
                loadWeb(webUrl);
                break;
        }
    }

    /**
     * 提交评论
     */
    private void commitComment() {
        /**
         * newsId 新闻id
         * uid    用户id
         * uname  用户名
         * uPicUrl 用户头像
         * comment 用户评论内容
         */
        int uid = 1;
        String uname = "用户名1";
        String uPicUrl = "http://xxxxxx";
        comment = etComment.getText().toString().trim();
        if (null == comment || !TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "输入评论不准为空", Toast.LENGTH_SHORT).show();
        } else {
            commentService = new NewsCommentCommitService();
            commentService.commitComment(newsId, uid, uname, uPicUrl, comment, new ArkHttpClient.HttpHandler(){
                @Override
                public void onSuccess(JSONObject response, boolean isInCache) {
                    super.onSuccess(response, isInCache);
                    showCommitSuccess();
                }

                @Override
                public void onFailure(String statusCode, String error) {
                    super.onFailure(statusCode, error);
                    showCommitFailure();
                }
            });
        }
    }

    private void showCommitSuccess() {
        Toast.makeText(this, "提交评论成功", Toast.LENGTH_SHORT).show();
    }

    private void showCommitFailure() {
        Toast.makeText(this, "提交评论失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 点赞
     * @param praiseNum 点赞数
     */
    private void praiseNews(int praiseNum){
        /**
         * newsId 新闻id
         * uid 用户id
         * praiseNum 点赞数量
         */
        int uid = 1;
        praiseService = new NewsPraiseService();
        praiseService.praiseNews(newsId, uid, praiseNum, new ArkHttpClient.HttpHandler(){
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                showPraiseSuccess();
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
            }
        });
    }

    private void showPraiseSuccess() {
        btnPraise.setText("已");
    }

    private void showNoNetWork(){
        noNetWork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetWork() {
        noNetWork.setVisibility(View.GONE);
    }

}
