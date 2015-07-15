package com.skytech.moa.doc.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.skytech.moa.dialog.OpenFileDialog;
import com.skytech.moa.doc.widgets.attachment.AttachmentAdapter;
import com.skytech.moa.doc.widgets.attachment.AttachmentEntity;
import com.skytech.moa.doc.widgets.attachment.DisplayLocalFiles;
import com.skytech.android.util.DateUtils;
import com.skytech.android.util.FileUtils;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldFile extends BaseFieldView {
    public static final int RECORD = 2;
    public static final int CAMERA = 20;
    public static final int LOCAL = 200;

    public interface OnClickHappened {
        public void onClickFile(boolean isMainBody, String fileId, String conid, String fileName);
    }

    private OnClickHappened dlg;
    private LinearLayout layout;
    private GridView gridView;
    private boolean isMainBody;

    private AttachmentAdapter adapter;
    private List<AttachmentEntity> files = new ArrayList<AttachmentEntity>();

    public FieldFile(Context context) {
        this(context, null, -1);
    }

    public FieldFile(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldFile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.doc_field_file, this);
        keyNameView = (TextView) findViewById(R.id.field_title);
        layout = (LinearLayout) findViewById(R.id.layout);
        gridView = (GridView) findViewById(R.id.attachment_gridView);
        adapter = new AttachmentAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(getGridItemClickListener());
        initStyles(attrs);
    }

    private AdapterView.OnItemClickListener getGridItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditable() && position == adapter.getCount() - 1) {
                    Dialog alertDialog = new AlertDialog.Builder(getContext()).
                            setIcon(R.drawable.ic_launcher)
                            .setItems(new String[]{"拍照", "录音", "从本地选择"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    String state = Environment.getExternalStorageState();
                                    switch (j) {
                                        case 0:
                                            if (state.equals(Environment.MEDIA_MOUNTED)) {
                                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                ((Activity) getContext()).startActivityForResult(intent, FieldFile.CAMERA);
                                            } else {
                                                Toast.makeText(getContext(), "请插入SD卡", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        case 1:
                                            if (state.equals(Environment.MEDIA_MOUNTED)) {
                                                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                                ((Activity) getContext()).startActivityForResult(intent, FieldFile.RECORD);
                                            } else {
                                                Toast.makeText(getContext(), "请插入SD卡", Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                        case 2:
                                            Intent intent = new Intent(getContext(), DisplayLocalFiles.class);
                                            ((Activity) getContext()).startActivityForResult(intent, FieldFile.LOCAL);
                                            break;
                                    }
                                }
                            }).create();
                    alertDialog.show();
                } else {
                    AttachmentEntity attachmentEntity = (AttachmentEntity) adapter.getItem(position);
                    if (attachmentEntity.getFilePath() == null) return;
                    File file = new File(attachmentEntity.getFilePath());
                    if (file.exists()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        if (FileUtils.isImage(attachmentEntity.getFilePath())) {
                            intent.setDataAndType(Uri.fromFile(file), "image/*");
                        } else if (FileUtils.isAudio(attachmentEntity.getFilePath())) {
                            intent.setDataAndType(Uri.fromFile(file), "audio/*");
                        } else {
                            Toast.makeText(getContext(), "文件不存存！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        getContext().startActivity(intent);
                    }
                }
            }
        };
    }

    public void setOnClickHappened(OnClickHappened delegate) {
        dlg = delegate;
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof JSONArray) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lParams.gravity = Gravity.RIGHT;
            JSONArray files = (JSONArray) object;
            for (int i = 0; i < files.length(); i++) {
                JSONObject file = files.optJSONObject(i);
                TextView tv = new TextView(getContext());
                tv.setText(file.optString("name"));
                tv.setTag(file);
                tv.setTextColor(getResources().getColorStateList(R.color.textcolor));
                tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
                tv.setPadding(0, 5, 0, 5);
                tv.setBackgroundResource(R.drawable.back_button_selector);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != dlg) {
                            JSONObject json = (JSONObject) view.getTag();
                            dlg.onClickFile(isMainBody, json.optString(Constant.PARAM_ID), json.optString(Constant.PARAM_CONID), json.optString("name"));
                        }
                    }
                });
                layout.addView(tv);
                if (file.has("uname")) {
                    TextView user = new TextView(getContext());
                    user.setText(file.optString("uname") + " " + DateUtils.preciseMinute(file.optString("time")));
                    user.setTextColor(getResources().getColorStateList(R.color.textcolor));
                    layout.addView(user, lParams);
                }
            }
        }
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        if (editable) {
            addButton();
        }
    }

    private void addButton() {
        TextView add = new TextView(getContext());
        add.setText("添加附件");
        add.setPadding(0, 5, 0, 5);
        add.setEnabled(true);
        add.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        add.setBackgroundResource(R.drawable.back_button_selector);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Integer> images = new HashMap<String, Integer>();
                // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
                /* images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);   // 根目录图标
                images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);    //返回上一层的图标*/
                images.put(OpenFileDialog.sFolder, R.drawable.doc_ic_folder);   //文件夹图标
                images.put("doc", R.drawable.ic_document);   //doc文件图标
                images.put(OpenFileDialog.sEmpty, R.drawable.ic_empty);
                final Dialog dialog = OpenFileDialog.createDialog(10, getContext(), "打开文件", new OpenFileDialog.CallbackBundle() {
                            @Override
                            public void callback(Bundle bundle) {
                                String filepath = bundle.getString("path");
                                //setTitle(filepath); // 把文件路径显示在标题上

                            }
                        },
                        ".txt;.png;.jpg;.doc;.docx;.exl;.exlx;",
                        images);
                dialog.show();
            }
        });
        layout.addView(add);
    }

    public void setMainBody(boolean isMainbody) {
        this.isMainBody = isMainbody;
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
}
