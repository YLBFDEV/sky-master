package com.skytech.moa.presenter;

import com.loopj.android.http.RequestParams;
import com.skytech.moa.services.AffairService;
import com.skytech.moa.services.ExpenseService;
import com.skytech.moa.services.IAffairService;
import com.skytech.moa.services.IExpenseService;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.IApplyAffairView;
import com.skytech.moa.view.IApplyExpenseView;

public class ExpensePresenter {
    private IApplyExpenseView view;
    private IExpenseService service;

    private ExpensePresenter() {
        service = new ExpenseService();
    }

    public ExpensePresenter(IApplyExpenseView view) {
        this();
        this.view = view;
    }

    public void fetchExpenseForm() {
        RequestParams params = new RequestParams();
        params.put(Constant.USER_ID, "1");
        params.put(Constant.AFFAIR_TYPE, "");
      /*  service.applyAffair(API.GET_AFFAIR_FORM, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                view.loadForm(response);
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
            }
        }, params);*/
    }
}
