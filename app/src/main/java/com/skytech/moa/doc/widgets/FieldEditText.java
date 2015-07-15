package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import com.skytech.moa.R;

/**
 * 文本输入框
 */
public class FieldEditText extends BaseFieldView {
    private EditText editText;

    public FieldEditText(Context context) {
        this(context, null, -1);
    }

    public FieldEditText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (getOrientation() == VERTICAL) {
            LayoutInflater.from(context).inflate(R.layout.doc_field_edit_ver, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.doc_field_edit, this);
        }
        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        editText = (EditText) valueView;
        initStyles(attrs);
    }

    @Override
    protected void initStyles(AttributeSet attrs) {
        super.initStyles(attrs);
        //设置属性
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);
        editText.setMinLines(ta.getInt(R.styleable.doc_minLines, 1));
        int inputType = ta.getInt(R.styleable.doc_inputType, 0);
        switch (inputType) {
            case 1://identityCard
                editText.setKeyListener(new NumberKeyListener() {
                    protected char[] getAcceptedChars() {
                        char[] numberChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
                        return numberChars;
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_URI;
                    }
                });
                break;
            case 0:
                break;
            default:
                editText.setInputType(inputType);
                break;
        }
        ta.recycle();
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            editText.setText(((String) object).replace("null", ""));
        }
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        return editText.getText().toString();
    }

    @Override
    public void setHint(String hint) {
        editText.setHint(hint);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        editText.setFocusable(editable);
        editText.setEnabled(editable);
    }

    public void setInputType(String inputType) {
        switch (inputType) {
            case "identity"://identityCard
                editText.setKeyListener(new NumberKeyListener() {
                    protected char[] getAcceptedChars() {
                        char[] numberChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
                        return numberChars;
                    }

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_URI;
                    }
                });
                break;
            case "none":
                break;
            case "phone":
                editText.setInputType(3);
                break;
            case "number":
                editText.setInputType(2);
                break;
            case "email":
                editText.setInputType(32);
                break;
        }
    }

    public void setMaxLength(int maxLength) {
        if (maxLength > 50) {
            editText.setLines(5);
        }
        editText.setMaxEms(maxLength);
    }
}
