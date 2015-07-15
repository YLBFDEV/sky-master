package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.skytech.moa.R;

/**
 * Created by huangzf on 2015/4/29.
 */
public class FieldTextView extends BaseFieldView {
    private TextView textView;

    public FieldTextView(Context context) {
        this(context, null, -1);
    }

    public FieldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (getOrientation() == VERTICAL) {
            LayoutInflater.from(context).inflate(R.layout.doc_field_edit_ver, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.doc_field_text, this);
        }
        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        textView = (TextView) valueView;
        initStyles(attrs);
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            textView.setText((String) object);
        }
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setHint(String hint) {

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (null == textView) {
            super.setOnClickListener(l);
        } else {
            textView.setOnClickListener(l);
        }
    }
}
