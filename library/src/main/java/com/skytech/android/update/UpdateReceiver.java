package com.skytech.android.update;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.skytech.android.Logging;
import com.skytech.R;

import java.io.File;

/**
 * Created by huangzf on 2014/6/27.
 */
public class UpdateReceiver extends BroadcastReceiver {
    //    private DownloadManager downloadManager;
    private Context mContext;
    private static long queueId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (Logging.DEBUG) {
                Log.d(Logging.LOG_TAG, "下载完成了,downId" + downId);
            }
            if (downId != queueId) return;
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
            Query query = new Query();
            query.setFilterById(id);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);

            int columnCount = cursor.getColumnCount();
            String path = null;
            //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
            while (cursor.moveToNext()) {
                for (int j = 0; j < columnCount; j++) {
                    String columnName = cursor.getColumnName(j);
                    String string = cursor.getString(j);
                    if (columnName.equals("local_filename")) {
                        path = string;
                    }
                    if ((string != null) && Logging.DEBUG) {
                        Log.d(Logging.LOG_TAG, columnName + ":" + string);
                    }
                }
            }
            cursor.close();
            Log.d(Logging.LOG_TAG, "file path:" + path);
            if (path == null) return;
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            context.startActivity(installIntent);
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Log.d(Logging.LOG_TAG, "点击通知了..");
        } else if (action.equals(Constant.ACTION_NEWVERSION)) {
            downloadFile(intent.getStringExtra(Constant.EXTRA_APKURL));
        }
    }

    private void downloadFile(String apkurl) {
        if (Logging.DEBUG) {
            Log.d(Logging.LOG_TAG, "下载更新包,url:" + apkurl);
        }
        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(apkurl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(mContext.getString(R.string.app_name));
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);

        //不显示下载界面
        request.setVisibleInDownloadsUi(false);
        /*设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
               在/mnt/sdcard/Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，
               不设置，下载后的文件在/cache这个  目录下面*/
        //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
        queueId = downloadManager.enqueue(request);
        if (Logging.DEBUG) {
            Log.d(Logging.LOG_TAG, "开始下载了,queueId" + queueId);
        }
    }
}
