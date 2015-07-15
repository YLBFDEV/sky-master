package com.skytech.moa.activity;

import android.app.Activity;
import android.os.Bundle;
import com.skytech.moa.doc.FormView;
import com.skytech.android.draft.Draft;
import com.skytech.android.util.CustomProgress;
import com.skytech.moa.R;
import com.skytech.moa.presenter.AffairPresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.IApplyAffairView;
import org.json.JSONObject;

public class ApplyAffair extends Activity implements IApplyAffairView {

    private AffairPresenter presenter;
    private FormView form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        initView();
        presenter = new AffairPresenter(this);
        presenter.fetchAffairForm();
    }

    private void initView() {
        setContentView(R.layout.apply_affair);
        form = (FormView)findViewById(R.id.apply_affair_form);//form
    }

    @Override
    public void loadForm(JSONObject data) {
        form.setAddable(data.optBoolean(Constant.JSONKEY_ADDABLE, false));
        form.setEditable(data.optBoolean(Constant.JSONKEY_EDITABLE, false));
        form.initFieldViews(data.optJSONArray(Constant.DOC_FIELDS), null);
        form.setData(data);
    }
}
