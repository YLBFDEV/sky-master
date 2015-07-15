package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.moa.services.INewMemoService;
import com.skytech.moa.services.NewMemoService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewMemoView;
public class NewMemoPresenter {
    private INewMemoView view;
    private INewMemoService service;

    private NewMemoPresenter() {
        service = new NewMemoService();
    }

    public NewMemoPresenter(INewMemoView view) {
        this();
        this.view = view;
    }

    public void fetchNewMemoForm() {
        RequestParams params = new RequestParams();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.AFFAIR_TYPE, "");
    }
}
