package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.skytech.moa.R;

import java.util.LinkedList;
import java.util.List;


public class FieldCheckbox extends BaseFieldView {
    private TableLayout tl;
    private TextView image;
    private String[] options;
    private List<CheckBox> checkBoxes;
    private boolean clickable;

    private int columnCount;
    private int divide;
    private int textColor;

    public FieldCheckbox(Context context) {
        this(context, null, -1);
    }

    public FieldCheckbox(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (getOrientation() == VERTICAL) {
            LayoutInflater.from(context).inflate(R.layout.doc_field_checkbox_ver, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.doc_field_checkbox, this);
        }

        keyNameView = (TextView) findViewById(R.id.field_title);
        tl = (TableLayout) findViewById(R.id.tablelayout);
        //设置属性
        initStyles(attrs);

        checkBoxes = new LinkedList<CheckBox>();
        if (null != options) {
            int cc = 0;
            TableRow row = new TableRow(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            for (String option : options) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(option);
                if (textColor != -1) checkBox.setTextColor(textColor);
                else {
                    checkBox.setTextColor(getResources().getColorStateList(R.color.textcolor));
                }
                // checkBox.setButtonDrawable(R.drawable.checkbox_selector);
                checkBox.setClickable(clickable);
                checkBoxes.add(checkBox);
                row.addView(checkBox, params);
                cc++;
                if (cc == columnCount) {
                    tl.addView(row);
                    //  addLine(divide);
                    cc = 0;
                    row = new TableRow(context);
                }
            }
            if (cc != 0) {
                tl.addView(row);
                //  addLine(divide);
                cc = 0;
            }
        }
    }

    private void addLine(int resid) {
        if (resid != -1) {
            int margin = (int) getResources().getDimension(R.dimen.main_content_margin);
            TableLayout.LayoutParams lineParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
            lineParams.setMargins(0, margin, 0, margin);
            View line = new View(this.getContext());
            line.setBackgroundResource(resid);
            tl.addView(line, lineParams);
        }
    }

    @Override
    protected void initStyles(AttributeSet attrs) {
        super.initStyles(attrs);
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);
        if (ta.hasValue(R.styleable.doc_options)) options = ta.getString(R.styleable.doc_options).split(",");
        columnCount = ta.getInt(R.styleable.doc_columnsNum, 3);
        divide = ta.getResourceId(R.styleable.doc_divider, -1);
        textColor = ta.getColor(R.styleable.doc_textColor, -1);
        clickable = ta.getBoolean(R.styleable.doc_clickable, true);
        ta.recycle();
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            String selected = (String) object;
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setChecked(selected.equals("1"));
            }
        }
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        String result = "";
        for (CheckBox checkBox : checkBoxes) {
            if (!checkBox.isChecked()) {
                result = "0";
                continue;
            }
            if (TextUtils.isEmpty(result)) {
                /*result = checkBox.getText().toString();*/
                result = "1";
            } else {
                result += ",1";
            }
        }
        return result;
    }

    @Override
    public void setHint(String hint) {

    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setClickable(editable);
            checkBox.setEnabled(editable);
        }
    }
}
