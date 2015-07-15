package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubFormView extends BaseFieldView {
    private Context context;
    private TextView addView;
    private LinearLayout container;
    private boolean isAddable = false;

    private List<FormView> formViews = new ArrayList<FormView>();

    private JSONArray formViewFields;


    public SubFormView(Context context) {
        this(context, null, -1);
        this.context = context;
    }

    public SubFormView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        this.context = context;
    }

    public SubFormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.doc_sub_form, this);
        keyNameView = (TextView) findViewById(R.id.title);
        addView = (TextView) findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);

        addView.setOnClickListener(addClick());
        initStyles(attrs);
    }

    public void setAddable(boolean isAddable) {
        this.isAddable = isAddable;
        if (isAddable) {
            container.addView(addFormView(null));
        }
    }

    private View.OnClickListener addClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.addView(addFormView(null));
            }
        };
    }

    private View addFormView(JSONObject fieldViewData) {
        final ViewGroup rowView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sub_form_row, null);

        FormView formView = new FormView(context);
        formView.setOrientation(VERTICAL);
        formView.setEditable(isEditable());
        formView.initFieldViews(formViewFields, null);
        if (null != fieldViewData) {
            formView.setFieldViewsData(fieldViewData);
        }


        TextView title = (TextView) rowView.findViewById(R.id.title);
        title.setText(getKeyName() + (container.getChildCount() + 1));
        if (isEditable()) {
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.removeView(rowView);
                }
            });
        } else {
            title.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        ((ViewGroup) rowView.findViewById(R.id.groupView)).addView(formView);
        return rowView;
    }

    @Override
    protected void initStyles(AttributeSet attrs) {
        super.initStyles(attrs);
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);


        ta.recycle();
    }

    public void setFormViewFields(JSONArray formViewFields) {
        this.formViewFields = formViewFields;
    }

    @Override
    public void setKeyName(String text) {
        keyNameView.setText(text);
        addView.setText("新增" + text);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        keyNameView.setVisibility(GONE);
        if (editable) {
            addView.setVisibility(VISIBLE);
        } else {
            addView.setVisibility(GONE);
        }
    }

    public void setFormViewsData(Object object) {
        if (object instanceof JSONArray) {
            JSONArray formViewData = ((JSONArray) object);
            int length = formViewData.length();
            //if no data ,to add new FormView
            if (length == 0 && isRequired() && isEditable()) {
                container.addView(addFormView(null));
            }
            for (int i = 0; i < length; i++) {
                container.addView(addFormView(formViewData.optJSONObject(i)));
            }
        }
    }

    @Override
    public void setValue(Object object) {

    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        JSONArray array = new JSONArray();
        int rowCount = container.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            ViewGroup childLayout = (ViewGroup) container.getChildAt(i).findViewById(R.id.groupView);
            FormView formView = (FormView) childLayout.getChildAt(0);
            array.put(formView.getFieldsData());
        }
        return array;
    }

    @Override
    public void setHint(String hint) {

    }

    @Override
    public String requiredName() {
        return checkRequired();
    }

    @Override
    public boolean isNeed2Validate() {
        if (isEditable()) {
            return !TextUtils.isEmpty(checkRequired());
        } else {
            return false;
        }
    }

    public String checkRequired() {
        String result = "";
        int rowCount = container.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            if (TextUtils.isEmpty(result))
                result = checkChildRequired((ViewGroup) container.getChildAt(i));
            else
                result += "、" + checkChildRequired((ViewGroup) container.getChildAt(i));
        }
        return result;
    }

    public String checkChildRequired(ViewGroup childLayout) {
        String mess = "";
        childLayout = (ViewGroup) ((ViewGroup) childLayout.findViewById(R.id.groupView)).getChildAt(0);
        int childCount = childLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = childLayout.getChildAt(i);
            if (v instanceof BaseFieldView) {
                BaseFieldView fv = (BaseFieldView) v;
                if (fv.isRequired() && v.getVisibility() == View.VISIBLE
                        && (null == fv.getValue() || fv.getValue().equals(""))) {
                    mess += String.format("(%s)", fv.getKeyName());
                }
            }
        }
        return mess;
    }
}
