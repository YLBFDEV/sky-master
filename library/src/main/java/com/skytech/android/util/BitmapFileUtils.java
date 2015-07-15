package com.skytech.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapFileUtils {
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    /**
     * cache root
     */
    private static String mDataRootPath = null;
    /**
     * folder for savimg images
     */
    private final static String FOLDER_NAME = "/XZ12348Cache/Images";


    public BitmapFileUtils(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }


    /**
     * get folder path for saving images
     *
     * @return
     */
    private String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }

    /**
     * save bitmaps to sdcard if exists, or else save in phone
     *
     * @param fileName
     * @param bitmap
     * @throws java.io.IOException
     */
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * get bitmap from sdcard or phone
     *
     * @param fileName
     * @return
     */
    public Bitmap getBitmap(String fileName) {
        try {
            return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return null;
    }

    public boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * delete images cached
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }

        dirFile.delete();
    }
}
