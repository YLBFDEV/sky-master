package com.skytech.moa.doc.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.skytech.android.Logging;
import com.skytech.moa.doc.widgets.slate.SlateView;
import com.skytech.android.util.DateUtils;
import com.skytech.moa.App;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huangzf on 2015/1/19.
 * 意见输入框
 */
public class FieldOpinionText extends BaseFieldView {
    private EditText opinionEdit;
    private ViewGroup body;
    private SlateView slateView;
    private View signature;
    private Button btnPen, btnKeyboard, btnErase, btnUndo, btnRedo;
    private View split;
    private String signatureText;
    private boolean isSignature;

    public FieldOpinionText(Context context) {
        this(context, null, -1);
    }

    public FieldOpinionText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldOpinionText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (getOrientation() == VERTICAL) {
            LayoutInflater.from(context).inflate(R.layout.doc_field_opinion_ver, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.doc_field_opinion, this);
        }
        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        opinionEdit = (EditText) findViewById(R.id.field_edit);
        //    slateView = (SlateView) findViewById(R.id.slate);
        body = (ViewGroup) findViewById(R.id.opinion_body);
        signature = findViewById(R.id.signature);
        split = findViewById(R.id.split);
        initStyles(attrs);
        //   initSignature();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        signatureText = String.format("%s_签批意见：%s", App.getInstance().getUser().getId(), format.format(new Date()));
    }

    public void setSignature(boolean isSignature) {
        this.isSignature = isSignature;
        if (isSignature) {
            //           slateView.setVisibility(View.VISIBLE);
            findViewById(R.id.slateControlPanel).setVisibility(View.VISIBLE);
            opinionEdit.setVisibility(View.GONE);
        } else {
//            slateView.setVisibility(View.GONE);
            opinionEdit.setVisibility(View.VISIBLE);
            findViewById(R.id.slateControlPanel).setVisibility(View.GONE);
        }
    }

    private void initSignature() {
        slateView.setPenColor(getResources().getColor(R.color.black));
        slateView.setPenSize(0.8f, 12f);
        slateView.setPenType(SlateView.TYPE_WHITEBOARD);
        ViewTreeObserver vto2 = slateView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                slateView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //设置手写区域大小
                if (slateView.getWidth() <= 0 || slateView.getHeight() <= 0) {
                    return;
                }
                slateView.initCanvas();
                slateView.setSlateHandler(new SlateView.SlateHandler() {
                    @Override
                    public void onUndoEnabledChanged(boolean enabled) {
                        btnUndo.setEnabled(enabled);
                    }

                    @Override
                    public void onRedoEnabledChanged(boolean enabled) {
                        btnRedo.setEnabled(enabled);
                    }
                });
            }
        });
        btnPen = (Button) findViewById(R.id.btn_pen);
        btnErase = (Button) findViewById(R.id.btn_erase);
        btnKeyboard = (Button) findViewById(R.id.btn_text);
        btnUndo = (Button) findViewById(R.id.btn_undo);
        btnRedo = (Button) findViewById(R.id.btn_redo);
        btnPen.setSelected(true);
        btnUndo.setEnabled(false);
        btnRedo.setEnabled(false);
        btnPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPen.setClickable(false);
                btnKeyboard.setClickable(true);
                btnErase.setClickable(true);
                btnUndo.setClickable(true);
                btnRedo.setClickable(true);
                btnPen.setSelected(true);
                btnKeyboard.setSelected(false);
                btnErase.setSelected(false);

                opinionEdit.setVisibility(GONE);
                slateView.setVisibility(VISIBLE);

                slateView.setPenColor(getResources().getColor(R.color.black));
                slateView.setPenSize(0.5f, 12f);
            }
        });
        btnKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPen.setClickable(true);
                btnKeyboard.setClickable(false);
                btnErase.setClickable(true);
                btnUndo.setClickable(false);
                btnRedo.setClickable(false);
                btnPen.setSelected(false);
                btnKeyboard.setSelected(true);
                btnErase.setSelected(false);

                opinionEdit.setVisibility(VISIBLE);
                slateView.setVisibility(GONE);
            }
        });
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPen.setClickable(true);
                btnKeyboard.setClickable(true);
                btnErase.setClickable(false);
                btnUndo.setClickable(true);
                btnRedo.setClickable(true);

                btnPen.setSelected(false);
                btnKeyboard.setSelected(false);
                btnErase.setSelected(true);

                slateView.setPenColor(Color.WHITE);
                slateView.setPenSize(60f, 60f);
            }
        });
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slateView.undo();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slateView.redo();
            }
        });
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof JSONArray) {
            JSONArray values = (JSONArray) object;
            for (int i = 0; i < values.length(); i++) {
                JSONObject json = values.optJSONObject(i);
                if (json.optBoolean("iscurrent") && isEditable()) {
                    if (json.optBoolean(Constant.DOCUMENT_IS_SIGNATURE)) {
                        //当前步骤 审批意见
                        addImageSignature(json.optString("opinion"));
                        slateView.setTag(json.optString("opinion"));
                        btnPen.performClick();
                    } else {
                        opinionEdit.setText(json.optString("opinion"));
                        //   btnKeyboard.performClick();
                    }
                } else {
                    if (json.optBoolean(Constant.DOCUMENT_IS_SIGNATURE)) {
                        addImageSignature(json.optString("opinion"));
                    } else {
                        addTextSignature(json.optString("opinion"), json.optString("uname"), json.optString("handlingtime"));
                    }
                }
            }
        } else if (object instanceof JSONObject) {
            JSONObject json = (JSONObject) object;
            if (isEditable()) {
                opinionEdit.setText(json.optString(Constant.FIELD_OPINION));
                signature.setVisibility(VISIBLE);
            } else {
                addTextSignature(json.optString(Constant.FIELD_OPINION), json.optString("uname"), json.optString("handlingtime"));
            }
        }
    }

    public void addTextSignature(String opinion, String uname, String handlingtime) {
        TextView tv = new TextView(getContext());
        tv.setText(opinion);
        tv.setTextColor(getResources().getColorStateList(R.color.textcolor));
        body.addView(tv, body.getChildCount() - 2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView user = new TextView(getContext());
        user.setText(uname + " " + DateUtils.preciseMinute(handlingtime));
        user.setTextColor(getResources().getColorStateList(R.color.textcolor));
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParams.gravity = Gravity.RIGHT;
        body.addView(user, body.getChildCount() - 2, lParams);
    }

    private void addImageSignature(String posinion) {
        TextView tv = new TextView(getContext());
        tv.setText(posinion);
        body.addView(tv, body.getChildCount() - 2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageView imageView = new ImageView(getContext());
        imageView.setTag(posinion);
        imageView.setImageResource(R.drawable.ic_loading);
        body.addView(imageView, body.getChildCount() - 2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setValueId(Object object) {

    }

    @Override
    public Object getValue() {
        if (null != btnPen && btnPen.isSelected() && isSignature) {
            return signatureText;
        }
        return opinionEdit.getText().toString();
    }

    @Override
    public void setHint(String hint) {
        opinionEdit.setHint(hint);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        opinionEdit.setFocusable(editable);
        opinionEdit.setEnabled(editable);
        if (editable) {
            signature.setVisibility(VISIBLE);
            //  split.setVisibility(GONE);
        } else {
            signature.setVisibility(GONE);
        }
    }

    /**
     * any signature needs to save
     *
     * @return
     */
    public boolean need2Save() {
        return isEditable() && !slateView.isEmpty() && btnUndo.isEnabled();
    }

    protected void setBackground() {
        if (null == opinionEdit) return;
        if (isEditable()) {
            if (isRequired()) {
                opinionEdit.setBackgroundColor(getResources().getColor(R.color.edit_required));
            } else {
                opinionEdit.setBackgroundColor(getResources().getColor(R.color.edit_editable));
            }
        } else {
            opinionEdit.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    public void addSignImage(String position, Bitmap bitmap) {
        if (null != slateView.getTag() && slateView.getTag().equals(position)) {
            Log.d(Logging.LOG_TAG, "latest saved signature found ");
            slateView.setRawBitmap(bitmap);
            slateView.invalidate();
        } else {
            int count = body.getChildCount();
            for (int i = 0; i < count; i++) {
                if (body.getChildAt(i) instanceof ImageView && body.getChildAt(i).getTag().equals(position)) {
                    ((ImageView) body.getChildAt(i)).setImageBitmap(bitmap);
                    Log.d(Logging.LOG_TAG, "signature bitmap set ");
                }
            }
        }
    }

    private Bitmap resize(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float scaleRatio = Float.parseFloat(getResources().getString(R.string.signature_scale_ratio));
        matrix.postScale(scaleRatio, scaleRatio); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        // slateView.setParentScrollView(parentScrollView);
    }

    public Bitmap getSignature2BeSaved() {
        if (!slateView.isEmpty()) {
            return resize(slateView.getBitmap());
        }
        return null;
    }
}
