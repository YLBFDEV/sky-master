package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.moa.services.INewWorkLogsService;
import com.skytech.moa.services.NewWorkLogsService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.INewWorkLogsView;

public class NewWorkLogsPresenter {
    private INewWorkLogsView view;
    private INewWorkLogsService service;

    private NewWorkLogsPresenter() {
        service = new NewWorkLogsService();
    }

    public NewWorkLogsPresenter(INewWorkLogsView view) {
        this();
        this.view = view;
    }

    public void fetchNewWorkLogsForm() {
        RequestParams params = new RequestParams();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.AFFAIR_TYPE, "");
    }
}
