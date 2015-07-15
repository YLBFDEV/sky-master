package com.skytech.moa.doc.widgets.attachment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.skytech.android.cache.PathUtil;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.doc.widgets.BaseFieldView;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.FileUtils;
import com.skytech.android.util.JsonUtil;
import com.skytech.moa.R;
import com.skytech.moa.utils.Constant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FieldAttachment extends BaseFieldView implements FormView.Callback, IFieldAttachment {

    public interface UploadHandler {
        /**
         * Invoked when upload failure
         */
        void onUploadFailure();

        /**
         * Invoked when upload success
         */
        void onUploadSuccess();
    }

    public static final int RECORD = 2;
    public static final int CAMERA = 20;
    public static final int LOCAL = 200;

    private static final String SEPARATOR = ",";

    private String value = "";

    private GridView gridView;
    private List serverPaths = new ArrayList();
    private UploadHandler uploadHandler;
    private AttachmentAdapter adapter;

    private AttachmentService service;
    private List<AttachmentEntity> files = new ArrayList<AttachmentEntity>();

    public FieldAttachment(Context context) {
        this(context, null, -1);
    }

    public FieldAttachment(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FieldAttachment(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.attachment_view, this);
        keyNameView = (TextView) findViewById(R.id.field_title);
        gridView = (GridView) findViewById(R.id.attachment_gridView);
        adapter = new AttachmentAdapter(context);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(getGridItemClickListener());
        service = new AttachmentService(this);
        initStyles(attrs);
    }

    public void setUploadHandler(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
        onUpload();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        adapter.setEditable(editable);
    }

    private AdapterView.OnItemClickListener getGridItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditable() && position == adapter.getCount() - 1) {
                    showAttachmentOptions();
                } else {
                    openAttachment(position);
                }
            }
        };
    }

    private void showAttachmentOptions() {
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
                                    ((Activity) getContext()).startActivityForResult(intent, FieldAttachment.CAMERA);
                                } else {
                                    Toast.makeText(getContext(), "请插入SD卡", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 1:
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                    ((Activity) getContext()).startActivityForResult(intent, FieldAttachment.RECORD);
                                } else {
                                    Toast.makeText(getContext(), "请插入SD卡", Toast.LENGTH_LONG).show();
                                }
                                break;
                            case 2:
                                Intent intent = new Intent(getContext(), DisplayLocalFiles.class);
                                ((Activity) getContext()).startActivityForResult(intent, FieldAttachment.LOCAL);
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }

    private void openAttachment(int position) {
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

    public List<String> getPaths() {
        List<String> paths = new ArrayList();
        List files = adapter.getFiles();
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            AttachmentEntity file = (AttachmentEntity) i.next();
            paths.add(file.getFilePath());
        }
        return paths;
    }

    @Override
    public boolean isNeed2Validate() {
        return isEditable() && isRequired() && getPaths().isEmpty();
    }

    public boolean need2Save() {
        return !serverPaths.equals(getPaths());
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            String result = (String) object;
            if (result.isEmpty()) return;

            String[] array = result.split(SEPARATOR);
            files.clear();
            int length = array.length;
            for (int i = 0; i < length; i++) {
                files.add(new AttachmentEntity(array[i]));
            }
            adapter.setFiles(files);

            for (int i = 0; i < files.size(); i++) {
                service.downLoad(files.get(i).getFileId(), i);
            }
        } else if (object instanceof JSONArray) {
            adapter.addFiles(JsonUtil.jsonArray2List((JSONArray) object));
        }
    }


    private List<JSONObject> jsonArray2List(JSONArray ja) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < ja.length(); i++) {
            if (ja.opt(i) instanceof JSONObject) {
                list.add(ja.optJSONObject(i));
            } else {
                try {
                    JSONObject jo = new JSONObject();
                    jo.put(Constant.ATTACHMENT_ID,  "");
                    jo.put(Constant.ATTACHMENT_NAME,  FileUtils.getFileName(ja.optString(i)));
                    jo.put(Constant.ATTACHMENT_PATH, ja.optString(i));
                    list.add(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void setValueId(Object object) {
        if (object instanceof String) {

        }
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setHint(String hint) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        Cursor cursor;
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case CAMERA:
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    FileOutputStream b = null;
                    String fileName = PathUtil.getInstance().getAttachmentDir() + "/" + name;
                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                        adapter.addFile(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bitmap.recycle();
                    }
                    break;
                case RECORD:
                    uri = data.getData();
                    String[] pro = {MediaStore.Audio.Media.DATA};
                    cursor = ((Activity) getContext()).managedQuery(uri, pro, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(column_index);
                        adapter.addFile(path);
                    }
                    break;
                case LOCAL:
                    List list = data.getStringArrayListExtra("imagePaths");
                    adapter.addFiles(list);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void onUpload() {
        if (getPaths().isEmpty()) {
            if (null != uploadHandler)
                uploadHandler.onUploadSuccess();
        } else {
            CustomProgress.showProgress(getContext(), "附件上传中");
            service.upload(getPaths());
        }
    }

    public void onUploadSuccess(String response) {
        CustomProgress.hideProgress();
        value = response;
        if (null != uploadHandler)
            uploadHandler.onUploadSuccess();
    }

    @Override
    public void onUploadFailure() {
        CustomProgress.hideProgress();
        if (null != uploadHandler)
            uploadHandler.onUploadFailure();
    }

    @Override
    public void onDownloadProgress(int index, String speed) {
        files.get(index).setSpeed(speed);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onDownloadSuccess(int index, String filePath) {
        serverPaths.add(filePath);
        files.get(index).setFilePath(filePath);
        adapter.setFiles(files);
    }

    @Override
    public void onDownloadFailure(int index, String error) {
        serverPaths.add(error);
        files.get(index).setFilePath(error);
        adapter.setFiles(files);
    }
}
