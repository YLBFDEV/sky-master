package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.R;
import com.skytech.moa.presenter.ExpensePresenter;
import com.skytech.moa.utils.Constant;

import com.skytech.moa.view.IApplyExpenseView;
import org.json.JSONObject;

public class ApplyExpense extends Activity implements IApplyExpenseView {

    private ExpensePresenter presenter;
    private FormView form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        initView();
        presenter = new ExpensePresenter(this);
        presenter.fetchExpenseForm();
    }

    private void initView() {
        setContentView(R.layout.apply_expense);
        form = (FormView)findViewById(R.id.apply_expense_form);
    }

    @Override
    public void loadForm(JSONObject data) {
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), null);
        form.setData(data);
    }
}
