package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.skytech.moa.R;

import java.util.LinkedList;
import java.util.List;

public class FieldRadioButton extends BaseFieldView {
    private ViewGroup group;
    private String[] options;
    private List<RadioButton> radioButtons;
    private boolean clickable;

    private int columnCount;
    private int textColor;

    public FieldRadioButton(Context context) {
        this(context, null, -1);
    }

    public FieldRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(this.getContext()).inflate(R.layout.doc_field_radiobutton, this);

        keyNameView = (TextView) findViewById(R.id.field_title);
        group = (ViewGroup) findViewById(R.id.tablelayout);
        //设置属性
        initStyles(attrs);
    }

    public void setOptions(String[] options) {
        this.options = options;
        radioButtons = new LinkedList<RadioButton>();
        if (null != options) {
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            for (String option : options) {
                RadioButton radioButton = new RadioButton(this.getContext());
                radioButton.setText(option);
                radioButton.setTextColor(textColor);
                // checkBox.setButtonDrawable(R.drawable.checkbox_selector);
                radioButton.setClickable(clickable);
                radioButtons.add(radioButton);
                group.addView(radioButton, params);
            }

        }
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            int selected = Integer.parseInt(object.toString()) - 1;
            if (selected >= 0 && selected < radioButtons.size()) {
                radioButtons.get(selected).setChecked(true);
            }
            /*for (RadioButton radioButton : radioButtons) {
                radioButton.setChecked(selected.indexOf(radioButton.getText().toString()) != -1);
            }*/
        }
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        String result = "";
        for (RadioButton radioButton : radioButtons) {
            if (!radioButton.isChecked()) continue;
            result = "" + (radioButtons.indexOf(radioButton) + 1);
            /*if (TextUtils.isEmpty(result)) {
                result = radioButton.getText().toString();
            } else {
                result += "," + radioButton.getText().toString();
            }*/
        }
        return result;
    }

    @Override
    public void setHint(String hint) {

    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        for (RadioButton radioButton : radioButtons) {
            radioButton.setClickable(editable);
            radioButton.setEnabled(editable);
        }
    }

    @Override
    protected void initStyles(AttributeSet attrs) {
        super.initStyles(attrs);
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);
        columnCount = ta.getInt(R.styleable.doc_columnsNum, 3);
        textColor = ta.getColor(R.styleable.doc_textColor, getResources().getColor(R.color.black));
        clickable = ta.getBoolean(R.styleable.doc_clickable, true);
        if (ta.hasValue(R.styleable.doc_options)) {
            setOptions(ta.getString(R.styleable.doc_options).split(","));
        }
        ta.recycle();
    }
}
