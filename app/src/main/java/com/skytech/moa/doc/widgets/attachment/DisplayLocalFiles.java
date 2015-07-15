package com.skytech.moa.doc.widgets.attachment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import com.skytech.android.ArkActivity;
import com.skytech.android.util.FileUtils;
import com.skytech.moa.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DisplayLocalFiles extends ArkActivity implements View.OnClickListener {
    private Button confirmBtn;
    private GridView gv;
    private DisplayLocalFilesAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.display_local_files));

        confirmBtn = (Button) findViewById(R.id.button);
        gv = (GridView) findViewById(R.id.file_gridView);

        confirmBtn.setOnClickListener(this);
        adapter = new DisplayLocalFilesAdapter(this);
        gv.setAdapter(adapter);
        new LoadImageTask().execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("imagePaths", adapter.getSelected());
                setResult(RESULT_OK, intent);
                finish();
        }
    }

    private class LoadImageTask extends AsyncTask {
        private ProgressDialog dialog;
        private List allFilePaths = new ArrayList();

        @Override
        protected Void doInBackground(Object[] params) {
            getFilePath(Environment.getExternalStorageDirectory());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            adapter.setPaths(allFilePaths);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(DisplayLocalFiles.this, null, "正在加载，请稍候...", true, true);
        }

        private void getFilePath(File file) {//file = Environment.getExternalStorageDirectory()
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.getName().substring(0, 1).equals(".")) {
                    continue;
                }
                String path = f.getPath();
                if (f.isFile() && (FileUtils.isImage(path) || FileUtils.isAudio(path))) {
                    allFilePaths.add(f.getAbsolutePath());
                } else if (f.isDirectory() && f.canRead()) {
                    getFilePath(f);
                }
            }
        }
    }
}

