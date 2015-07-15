package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.skytech.moa.R;

/**
 * base class of custom component
 */
public abstract class BaseFieldView extends LinearLayout {
    protected TextView keyNameView;
    protected View valueView;
    protected String name;
    private boolean required = false;
    private boolean editable = false;

    public BaseFieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setText(String keyName, Object value) {
        setKeyName(keyName);
        setValue(value);
    }

    public String getKeyId() {
        return name;
    }

    public void setKeyId(String keyId) {
        name = keyId;
    }

    public void setKeyName(String text) {
        if (null != keyNameView && keyNameView.getText().length() == 0) {
            text = text.trim();
            switch (text.length()) {
                case 0:
                    keyNameView.setVisibility(GONE);
                    break;
                case 2:
                    keyNameView.setText(text.substring(0, 1) + "　　" + text.substring(1, 2));
                    break;
                case 3:
                    keyNameView.setText(text.substring(0, 1) + "  " + text.substring(1, 2) + "  " + text.substring(2, 3));
                    break;
                default:
                    keyNameView.setText(text);
                    break;
            }
        }
        //setHint(text);
    }

    public String getKeyName() {
        if (null != keyNameView) return keyNameView.getText().toString();
        return "";
    }

    public abstract void setValue(Object object);

    public abstract void setValueId(Object object);

    public void setEditable(boolean editable) {
        this.editable = editable;
        //   setBackground();
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
        if (null != keyNameView) {
            if (required) {
                keyNameView.setText(Html.fromHtml(String.format("<font color='#C80600'>*</font> %s", keyNameView.getText().toString().replaceAll(" ", "&nbsp;"))));
            } else
                keyNameView.setText(keyNameView.getText().toString().replace("* ", "").replaceAll("&nbsp;", " "));
        }
    }

    public abstract Object getValue();

    public Object getValueId() {
        return getValue();
    }

    public abstract void setHint(String hint);

    public String requiredName() {
        return getKeyName();
    }

    /**
     * validate the field weather need to verify
     * return true or false
     */
    public boolean isNeed2Validate() {
        return isEditable() && isRequired()
                && null != getValueId() && getValueId().toString().trim().length() == 0;
    }

    protected void setBackground() {
        if (null == valueView) return;
        if (isEditable()) {
            if (isRequired()) {
                valueView.setBackgroundColor(getResources().getColor(R.color.edit_required));
            } else {
                valueView.setBackgroundColor(getResources().getColor(R.color.edit_editable));
            }
        } else {
            valueView.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    /**
     * init the components styles
     *
     * @param attrs
     */
    protected void initStyles(AttributeSet attrs) {
        //设置属性
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.doc);
        name = ta.getString(R.styleable.doc_keyid);
        editable = ta.getBoolean(R.styleable.doc_editable, false);
        if (null == getBackground()) {
            if (ta.hasValue(R.styleable.doc_position) == false) {
                setBackgroundResource(ta.getResourceId(R.styleable.doc_leftbottomfield_background, R.color.transparent));
            } else if (ta.getString(R.styleable.doc_position).equals("1")) {
                setBackgroundResource(ta.getResourceId(R.styleable.doc_righttopfield_background, R.color.transparent));
            } else if (ta.getString(R.styleable.doc_position).equals("2")) {
                setBackgroundResource(ta.getResourceId(R.styleable.doc_leftbottomfield_background, R.color.transparent));
            } else if (ta.getString(R.styleable.doc_position).equals("3")) {
                setBackgroundResource(ta.getResourceId(R.styleable.doc_rightbottomfield_background, R.color.transparent));
            } else {
                setBackgroundResource(ta.getResourceId(R.styleable.doc_lefttopfield_background, R.color.transparent));
            }
        }
        if (ta.hasValue(R.styleable.doc_keyname)) {
            setKeyName(ta.getString(R.styleable.doc_keyname));
        }
        View divider = findViewById(R.id.divider);
        if (ta.hasValue(R.styleable.doc_divider) && null != divider) {
            divider.setBackgroundResource(ta.getResourceId(R.styleable.doc_divider, R.color.transparent));
        }
        if (ta.hasValue(R.styleable.doc_keyNameTextColor)) {
            keyNameView.setTextColor(getResources().getColor(ta.getResourceId(R.styleable.doc_keyNameTextColor, R.color.black)));
        }
        if (ta.getBoolean(R.styleable.doc_hideKeyName, false)) {
            keyNameView.setVisibility(GONE);
            divider.setVisibility(GONE);
        }
        if (ta.hasValue(R.styleable.doc_drawableLeft) || ta.hasValue(R.styleable.doc_drawableRight)) {
            ((TextView) valueView).setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(ta.getResourceId(R.styleable.doc_drawableLeft, R.color.transparent)), null,
                    getResources().getDrawable(ta.getResourceId(R.styleable.doc_drawableRight, R.color.transparent)), null);
            valueView.setPadding(0, 0, 0, 0);
            valueView.setBackgroundResource(R.color.transparent);
        }
        if (ta.hasValue(R.styleable.doc_hint)) {
            setHint(ta.getString(R.styleable.doc_hint));
        }
        setValue(ta.getString(R.styleable.doc_value));
        ta.recycle();
    }


}