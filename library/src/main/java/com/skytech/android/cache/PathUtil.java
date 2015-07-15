package com.skytech.android.cache;

import android.os.Environment;
import android.text.TextUtils;
import com.skytech.android.SkyInitializer;

import java.io.File;

/**
 * 文件路径
 */
public class PathUtil {
    private static PathUtil dirUtil;

    public static PathUtil getInstance() {
        if (null == dirUtil) {
            dirUtil = new PathUtil(SkyInitializer.getInstance().getPackageName());
        }
        return dirUtil;
    }

    private final String ROOT_DIRECTORY;
    public static final String CACHEDIR = "cache";
    private static final String ATTACHMENT = "attachment";
    private static final String LOG = "log";

    public PathUtil(String appName) {
        this.ROOT_DIRECTORY = appName;
    }

    public File getCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = SkyInitializer.getInstance().getContext().getExternalCacheDir().getPath();
        } else {
            cachePath = SkyInitializer.getInstance().getContext().getCacheDir().getPath();
        }
        File cacheDir = new File(cachePath + File.separator + uniqueName);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public String getBitmapCacheDir() {
        return getCacheDir("bitmap").getPath();
    }

    public File getCacheDir() {
        return getCacheDir("cache");
    }

    public String getExternalCacheDir() {
        return getRootDir() + File.separator + "" + CACHEDIR;
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        }
        if (sdDir != null) {
            return sdDir.getPath();
        } else {
            return "";
        }
    }

    public String getRootDir() {
        File destDir = new File(getSDPath() + File.separator + ROOT_DIRECTORY);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getPath();
    }

    /**
     * 获得附件缓存目录 *
     */
    public String getAttachmentDir() {
        String dir = getRootDir() + "/" + ATTACHMENT;
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getPath();
    }

    public String getLogDir() {
        String dir = getRootDir() + "/" + LOG;
        File destDir = new File(dir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir.getPath();
    }

    public void clearCache() {
        deleteFolderFile(getCacheDir().getPath(), true);
    }

    public void clearAttachments() {
        deleteFolderFile(getAttachmentDir(), true);
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {// 处理目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
                if (deleteThisPath && file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                    file.delete();
                }
            } else {// 如果是文件，删除
                file.delete();
            }
        }
    }
}

