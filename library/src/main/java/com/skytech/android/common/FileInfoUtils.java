package com.skytech.android.common;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * @Description TAFileInfoUtil是一个文件的操作类
 * @version V1.0
 * Created by yikai on 2014/11/18.
 */
public class FileInfoUtils {

    /**
     * 返回自定文件或文件夹的大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) throws Exception
    {// 取得文件大小
        long s = 0;
        if (f.exists())
        {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else
        {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    // 递归
    public static long getFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getFileSize(flist[i]);
            } else
            {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormatFileSize(long fileS)
    {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#0.00");
        String fileSizeString = "";
        if (fileS < 1024)
        {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576)
        {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824)
        {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else
        {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static long getList(File f)
    {// 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++)
        {
            if (flist[i].isDirectory())
            {
                size = size + getList(flist[i]);
                size--;
            }
        }
        return size;

    }
}
