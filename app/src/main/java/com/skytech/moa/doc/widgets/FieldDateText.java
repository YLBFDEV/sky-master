package com.skytech.moa.doc.widgets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import com.skytech.android.util.StringUtils;
import com.skytech.moa.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择框
 */
public class FieldDateText extends BaseFieldView implements View.OnClickListener {
    private Calendar c;
    private Dialog dialog;
    private DateFormat formatDate;
    private String defaultValue = "";

    public FieldDateText(Context context) {
        this(context, null, -1);
    }

    public FieldDateText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldDateText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.doc_field_date, this);
        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        //设置属性
        initStyles(attrs);
        valueView.setOnClickListener(this);
    }

    @Override
    protected void initStyles(AttributeSet attrs) {
        super.initStyles(attrs);
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);
        if (ta.hasValue(R.styleable.doc_dateFormat)) {
            formatDate = new SimpleDateFormat(ta.getString(R.styleable.doc_dateFormat));
        } else {
            formatDate = new SimpleDateFormat("yyyy-MM-dd");
        }
        if (ta.getBoolean(R.styleable.doc_showDefaultValue, true)) {
            c = Calendar.getInstance();
            setValue(c);
            defaultValue = (String) getValue();
        }
        ta.recycle();
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof Calendar) {
            ((TextView) valueView).setText(formatDate.format(c.getTime()));
        } else if (object instanceof String) {
            if (!StringUtils.isEmpty((String) object)) {
                try {
                    ((TextView) valueView).setText(formatDate.format(formatDate.parse((String) object)));
                } catch (ParseException e) {
                    try {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ((TextView) valueView).setText(formatDate.format(format.parse((String) object)));
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        return ((TextView) valueView).getText().toString();
    }

    public Date getDate() {
        return new Date();
    }

    @Override
    public void setHint(String hint) {
        ((TextView) valueView).setHint(hint);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        valueView.setEnabled(editable);
    }

    @Override
    public void onClick(View view) {
        if (isEnabled()) {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = createDialog();
            dialog.show();
        }
    }

    private Dialog createDialog() {
        if (null == c) c = Calendar.getInstance();
        return new DatePickerDialog(
                this.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setValue(c);
                    }
                },
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
    }
}
