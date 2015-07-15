package com.skytech.android.cache;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.skytech.android.Logging;
import com.skytech.android.SkyInitializer;
import com.skytech.android.util.FileUtils;
import com.skytech.android.util.NetworkUtils;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ConfigCache {
    private static final String TAG = ConfigCache.class.getName();
    private static final int CONFIG_CACHE_MOBILE_TIMEOUT_LONG = 86400000;//24 hour
    private static final int CONFIG_CACHE_MOBILE_TIMEOUT_SHORT = 60000;//1 minute
    private static final int CONFIG_CACHE_WIFI_TIMEOUT_LONG = 3600000;//1 hour
    private static final int CONFIG_CACHE_WIFI_TIMEOUT_SHORT = 5000; //5 second

    //bt字节参考量
    public static final long SIZE_BT = 1024L;
    //KB字节参考量
    public static final long SIZE_KB = SIZE_BT * 1024L;
    //MB字节参考量
    public static final long SIZE_MB = SIZE_KB * 1024L;
    //GB字节参考量
    public static final long SIZE_GB = SIZE_MB * 1024L;
    //TB字节参考量
    public static final long SIZE_TB = SIZE_GB * 1024L;

    public static final int SACLE = 2;

    private static final int MB = 1024 * 1024;
    private static final int CACHE_SIZE = 10;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
    private static final String WHOLESALE_CONV = ".cache";

    /**
     * 根据URL获取文本文件缓存
     *
     * @param cacheType
     * @return
     */
    public static String getCacheByUrlOfTimeout(CacheType cacheType, String cacheFileName) {
        if (cacheType.equals(CacheType.NOCACHE)) {
            return null;
        }
        String result = null;
        File file = new File(PathUtil.getInstance().getCacheDir() + "/" + cacheFileName);
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            if (Logging.DEBUG) {
                Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
            }
            //1. in case the system time is incorrect (the time is turn back long ago)
            //2. when the network is invalid, you can only read the cache
            if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_NO_NETWORK) || expiredTime < 0) {
                //不联网
                return FileUtils.readString(file);
            }
            if (cacheType.equals(CacheType.SHORT)) {
                if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT_SHORT) {
                    return null;    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime <= CONFIG_CACHE_WIFI_TIMEOUT_SHORT) {
                    return FileUtils.readString(file);    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT_SHORT) {
                    return null;    //移动网络
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime <= CONFIG_CACHE_MOBILE_TIMEOUT_SHORT) {
                    return FileUtils.readString(file);   //移动网络
                }
            } else if (cacheType.equals(CacheType.LONG)) {
                if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT_LONG) {
                    return null;    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime <= CONFIG_CACHE_WIFI_TIMEOUT_LONG) {
                    return FileUtils.readString(file);    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT_LONG) {
                    return null;    //移动网络
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime <= CONFIG_CACHE_MOBILE_TIMEOUT_LONG) {
                    return FileUtils.readString(file);   //移动网络
                }
            } else if (cacheType.equals(CacheType.PERMANENT)) {
                result = FileUtils.readString(file);
            }
        }
        return result;
    }

    /**
     * 根据URL获取文件流
     *
     * @param cacheType
     * @return
     */
    public static byte[] getCacheByteByUrlOfTimeout(CacheType cacheType, String cacheFileName) {
        if (cacheType.equals(CacheType.NOCACHE)) {
            return null;
        }
        byte[] result = null;
        File file = new File(PathUtil.getInstance().getCacheDir() + "/" + cacheFileName);
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            if (Logging.DEBUG) {
                Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
            }
            //1. in case the system time is incorrect (the time is turn back long ago)
            //2. when the network is invalid, you can only read the cache
            if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_NO_NETWORK) || expiredTime < 0) {
                //不联网
                return FileUtils.readBytes(file);
            }
            if (cacheType.equals(CacheType.SHORT)) {
                if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT_SHORT) {
                    return null;    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime <= CONFIG_CACHE_WIFI_TIMEOUT_SHORT) {
                    return FileUtils.readBytes(file);    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT_SHORT) {
                    return null;    //移动网络
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime <= CONFIG_CACHE_MOBILE_TIMEOUT_SHORT) {
                    return FileUtils.readBytes(file);   //移动网络
                }
            } else if (cacheType.equals(CacheType.LONG)) {
                if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT_LONG) {
                    return null;    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_WIFI)
                        && expiredTime <= CONFIG_CACHE_WIFI_TIMEOUT_LONG) {
                    return FileUtils.readBytes(file);    //WIFI
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT_LONG) {
                    return null;    //移动网络
                } else if (SkyInitializer.getInstance().getNetWorkState().equals(NetworkUtils.NET_TYPE_MOBILE)
                        && expiredTime <= CONFIG_CACHE_MOBILE_TIMEOUT_LONG) {
                    return FileUtils.readBytes(file);   //移动网络
                }
            } else if (cacheType.equals(CacheType.PERMANENT)) {
                result = FileUtils.readBytes(file);
            }
        }
        return result;
    }

    //从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = null;
        try {
            InputStream in = context.getResources().getAssets().open(PathUtil.CACHEDIR + "/" + fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getCacheByUrl(String cacheFileName) {
        if (cacheFileName == null) {
            return null;
        }
        String result = null;
        File file = new File(PathUtil.getInstance().getCacheDir() + "/" + cacheFileName);
        if (file.exists() && file.isFile()) {
            result = FileUtils.readString(file);
        }
        if (result == null) {
            return null;
        }
        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            Log.e(Logging.LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 将数据缓存至以URL命名的文本文件
     *
     * @param data
     * @param cacheFileName
     */
    public static void setUrlCache(String data, String cacheFileName) {
        File file = new File(PathUtil.getInstance().getCacheDir() + "/" + cacheFileName);
        if (Logging.DEBUG) {
            Log.d(TAG, "setUrlCache getPath+" + file.getPath());
        }
        try {
            //创建缓存数据到磁盘，就是创建文件
            FileUtils.writeString(file, data);
        } catch (Exception e) {
            Log.e(TAG, "write " + file.getAbsolutePath() + " data failed!");
        }
    }


    /**
     * * 计算存储目录下的文件大小，
     * * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * * 那么删除40%最近没有被使用的文件
     *   
     */
    private static boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件夹大小
     *
     * @param dirPath File实例
     * @return long 单位为M
     * @throws Exception
     */
    private static long getFolderSize(String dirPath) {
        int dirSize = 0;
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return 0;
        }

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                dirSize += getFolderSize(files[i].getAbsolutePath());
            } else {
                dirSize += files[i].length();
            }
        }
        return dirSize;
    }

    /**
     * 计算文件夹大小
     *
     * @return
     */
    public static String calculateCache() {
        try {
            //调用计算文件或目录大小方法
            long longSize = getFolderSize(PathUtil.getInstance().getCacheDir().getPath());

            if (longSize >= 0 && longSize < SIZE_BT) {
                return longSize + "B";
            } else if (longSize >= SIZE_BT && longSize < SIZE_KB) {
                return longSize / SIZE_BT + "KB";
            } else if (longSize >= SIZE_KB && longSize < SIZE_MB) {
                return longSize / SIZE_KB + "MB";
            } else if (longSize >= SIZE_MB && longSize < SIZE_GB) {
                BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "").toString());
                BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_MB + "").toString());
                String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
                //double result=this.longSize/(double)SIZE_MB;
                return result + "GB";
            } else {
                BigDecimal longs = new BigDecimal(Double.valueOf(longSize + "").toString());
                BigDecimal sizeMB = new BigDecimal(Double.valueOf(SIZE_GB + "").toString());
                String result = longs.divide(sizeMB, SACLE, BigDecimal.ROUND_HALF_UP).toString();
                return result + "TB";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new RuntimeException(ex.getMessage());
            return "KB";
        }
    }

    /**
     * 修改文件的最后修改时间
     */
    public static void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 计算sdcard上的剩余空间 *
     */
    private static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 将url转成文件名 *
     */
    private static String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private static class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 查找文件。
     *
     * @param baseDirName    待查找的目录
     * @param targetFileName 目标文件名，支持通配符形式
     * @param count          期望结果数目，如果畏0，则表示查找全部。
     * @return 满足查询条件的文件名列表
     */
    public static List findFiles(String baseDirName, String targetFileName, int count) {
        List fileList = new ArrayList();
        //判断目录是否存在
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            Log.e(Logging.LOG_TAG, "文件查找失败:" + baseDirName + "不是一个目录！");
            return fileList;
        }


        File files[] = baseDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(targetFileName) && files[i].getName().contains(".")) {
                //匹配成功，将文件名添加到结果集
                fileList.add(files[i].getAbsoluteFile());
                //如果已经达到指定的数目，则退出循环
                if ((count != 0) && (fileList.size() >= count)) {
                    return fileList;
                }
            }
        }

        return fileList;
    }
}
