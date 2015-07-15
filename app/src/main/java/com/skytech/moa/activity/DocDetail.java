package com.skytech.moa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.handmark.pulltorefresh.PullToRefreshBase;
import com.handmark.pulltorefresh.PullToRefreshListView;
import com.skytech.android.ArkActivity;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.manager.DocDetailModel;
import com.skytech.moa.model.DocumentDetail;
import com.skytech.moa.services.LoadDocDetailService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.utils.DocTimeSort;
import com.skytech.moa.view.IDocDetailView;
import com.skytech.moa.widgets.doclibrary.DocDetailAdapter;

import java.util.ArrayList;

public class DocDetail extends ArkActivity implements IDocDetailView, DocDetailItemListener.ClickHappend {
    private PullToRefreshListView docDetailList;
    private DocDetailAdapter docDetailAdapter;
    private ArrayList<DocumentDetail> docDetails = new ArrayList<>();
    private ImageView back;
    private RadioButton timeSoft;
    private RadioButton nameSoft;
    private LoadDocDetailService loadDocDetailService;
    private String sortType = Constant.SortByTime;
    private DocDetailItemListener docDetailItemListener;
    private DocDetailModel docDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_doc);
        initData();
        initView();
        action();
    }

    public void initView() {
        docDetailList = (PullToRefreshListView) findViewById(R.id.detail_doc);
        back = (ImageView) findViewById(R.id.doc_back);
        timeSoft = (RadioButton) findViewById(R.id.time_soft);
        nameSoft = (RadioButton) findViewById(R.id.name_soft);
        docDetailAdapter = new DocDetailAdapter(this);
        docDetailList.setOnRefreshListener(refreshListener);
        docDetailItemListener = new DocDetailItemListener(docDetailAdapter, this, this);
        docDetailList.setOnItemClickListener(docDetailItemListener);
        docDetailModel = new DocDetailModel(docDetailItemListener);
    }

    public void initData() {
        loadDocDetailService = new LoadDocDetailService(this);
        loadDocDetailService.loadDocDetails(123, 1, false);
    }

    public void action() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        timeSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByTime();
            }
        });
        nameSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void loadAllDocDetail(ArrayList<DocumentDetail> documentDetails) {
        this.docDetails = documentDetails;
        refresh();
        docDetailList.onRefreshComplete();
    }

    @Override
    public void pullToRefresh(ArrayList<DocumentDetail> documentDetails) {
        this.docDetails = documentDetails;
        refresh();
        docDetailList.onRefreshComplete();
        CustomToast.show(this, getResources().getString(R.string.updated));
    }

    @Override
    public void failure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT);
    }

    public void sortByTime() {
        this.docDetails = DocTimeSort.docDetailsTimeSort(docDetails);
        refresh();
    }

    public void More() {
        loadDocDetailService.loadDocDetails(123, 1, true);
    }

    @Override
    public void more(ArrayList<DocumentDetail> documents) {
        if (documents.size() != 0) {
            for (DocumentDetail documentDetail : documents) {
                this.docDetails.add(documentDetail);
            }
        } else {
            CustomToast.show(this, getResources().getString(R.string.no_more_data));
            System.out.println("no more");
        }
        docDetailAdapter.setDocDetails(docDetails);
        docDetailList.onRefreshComplete();
        docDetailAdapter.notifyDataSetChanged();
    }

    public void refresh() {
        docDetailAdapter.setDocDetails(docDetails);
        docDetailAdapter.notifyDataSetChanged();
        docDetailList.setAdapter(docDetailAdapter);
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            loadDocDetailService.pullToRefresh(123, 1);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            loadDocDetailService.loadDocDetails(123, 1, true);
        }
    };


    @Override
    public void downloadAttachment(String fileName) {
        docDetailModel.downloadFile(fileName);
    }
}
