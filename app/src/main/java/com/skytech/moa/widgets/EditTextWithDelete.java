package com.skytech.moa.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.skytech.moa.R;

/**
 * Created by huangzf on 2014/12/25.
 */
public class EditTextWithDelete extends EditText implements View.OnFocusChangeListener {
    private Drawable drawableLeft, imgEnable, imgEnablePress;
    private Context context;
    private OnDeleteCallBack callBack;

    public interface OnDeleteCallBack {
        void onContentClickListener();

        void onDeleteClickListener();
    }

    public EditTextWithDelete(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EditTextWithDelete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public EditTextWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.doc);
        int drawableLeftRes = ta.getResourceId(R.styleable.doc_drawableLeft, -1);
        if (drawableLeftRes != -1) {
            drawableLeft = getResources().getDrawable(drawableLeftRes);
        }

        ta.recycle();

        init();
    }

    public void setCallBack(OnDeleteCallBack callBack) {
        this.callBack = callBack;
    }

    private void init() {
        setPadding(5, 5, 5, 5);
        //获取图片资源
        imgEnable = getResources().getDrawable(R.drawable.clear);
        imgEnablePress = getResources().getDrawable(R.drawable.clear_press);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
                //   Toast.makeText(context, getText(), 10).show();
            }
        });
        setDrawable();
    }

    private void setDrawable() {
        setDrawable(false);
    }

    /**
     * .
     * 设置删除图片.
     */
    private void setDrawable(boolean isPress) {
        if (length() == 0) {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], isPress ? imgEnablePress : imgEnable, getCompoundDrawables()[3]);
        }
    }

    /**
     * .
     * event.getX() 获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgEnable != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    int x = (int) event.getX();
                    //判断触摸点是否在水平范围内
                    boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
                            (x < (getWidth() - getPaddingRight()));
                    //获取删除图标的边界，返回一个Rect对象
                    Rect rect = imgEnable.getBounds();
                    //获取删除图标的高度
                    int height = rect.height();
                    int y = (int) event.getY();
                    //计算图标底部到控件底部的距离
                    int distance = (getHeight() - height) / 2;
                    //判断触摸点是否在竖直范围内(可能会有点误差)
                    // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
                    boolean isInnerHeight = (y > distance) && (y < (distance + height));
                    if (isInnerWidth && isInnerHeight) {
                        setText("");
                        if (null != callBack) {
                            callBack.onDeleteClickListener();
                        }
                    } else {
                        setDrawable(false);
                        if (null != callBack) {
                            callBack.onContentClickListener();
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    setDrawable(true);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setDrawable();
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }
}