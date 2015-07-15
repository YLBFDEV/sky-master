package com.skytech.android.util;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.skytech.android.Logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {
    public static boolean delete(String path) {
        return new File(path).delete();
    }

    public static boolean exist(File file) {
        return file.exists();
    }

    public static String getFileName(String path) {
        int i = path.lastIndexOf(File.separator);
        if (i == -1) {
            return path;
        } else {
            return path.substring(i);
        }
    }

    public static String getMIMEType(File f) {
        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1,
                f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
                || end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")) {
            type = "image";
        } else if (end.equals("doc") || end.equals("docx") || end.equals("pdf")
                || end.equals("txt")) {
            type = "application/msword";
            return type;
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }

    /**
     * 判断是否是wps能打开的文件
     *
     * @param fileName
     * @return
     */
    public static boolean isWPSFile(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("doc") || end.equals("docx") || end.equals("wps")
                || end.equals("dot") || end.equals("wpt")
                || end.equals("xls") || end.equals("xlsx") || end.equals("et")
                || end.equals("ppt") || end.equals("pptx") || end.equals("dps")
                || end.equals("txt") || end.equals("pdf"))
            return true;

        return false;
    }

    /**
     * 判断是否是word文件
     *
     * @param fileName
     * @return
     */
    public static boolean isWordFile(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("doc") || end.equals("docx") || end.equals("wps")
                || end.equals("dot") || end.equals("wpt"))
            return true;

        return false;
    }

    /**
     * 判断是否是word文件
     *
     * @param fileName
     * @return
     */
    public static boolean isExcFile(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("xls") || end.equals("xlsx") || end.equals("et"))
            return true;

        return false;
    }

    /**
     * 判断是否是pdf文件
     *
     * @param fileName
     * @return
     */
    public static boolean isPDFFile(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("pdf"))
            return true;

        return false;
    }

    public static int writeString(File file, String str) {
        //mode:0:create
        //1:overwrite
        /*if (file.exists() && mode == 0) {
            return -1;
        }*/
        java.io.PrintWriter output;
        try {
            output = new java.io.PrintWriter(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return -2;
        }
        output.print(str);
        output.close();
        return 0;
    }

    public static String readString(File file) {
        String inputStr = "";
        java.util.Scanner scanner;
        try {
            scanner = new java.util.Scanner(file);
            scanner.useDelimiter("^&*");
            while (scanner.hasNext()) {
                inputStr += scanner.next();
            }
        } catch (FileNotFoundException e) {
            Log.e(Logging.LOG_TAG, e.getMessage());
            return null;
        }
        scanner.close();
        return inputStr;
    }

    public static int writeBytes(File file, byte[] bytes) {
        java.io.ObjectOutputStream out;
        try {
            out = new java.io.ObjectOutputStream(
                    new java.io.FileOutputStream(file));
            out.writeObject(bytes);
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -2;
        }

        return 0;
    }

    public static byte[] readBytes(File file) {
        byte[] r = null;
        java.io.ObjectInputStream in;
        try {
            in = new java.io.ObjectInputStream(
                    new java.io.FileInputStream(file));
            r = (byte[]) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    public static boolean isImage(String path) {
        String type = getSuffix(path);//文件后缀名
        if (type != null
                && (type.equals("JPG") || type.equals("GIF")
                || type.equals("PNG") || type.equals("JPEG")
                || type.equals("BMP") || type.equals("WBMP")
                || type.equals("ICO") || type.equals("JPE"))) {
            return true;
        }
        return false;
    }

    public static boolean isAudio(String path) {
        String type = getSuffix(path);//文件后缀名
        if (type != null
                && (type.equals("WAV")
                || type.equals("MP3") || type.equals("MIDI")
                || type.equals("WMA") || type.equals("APE")
                || type.equals("VQF") || type.equals("ACC")
                || type.equals("M4A") || type.equals("AMR")
                || type.equals("3GPP"))) {
            return true;
        }
        return false;
    }

    public static String getSuffix(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot < 0)
            return "";
        return fileName.substring(lastDot + 1).toUpperCase();//文件后缀名
    }

    public static Intent openFile(String filePath) {

        File file = new File(filePath);
        if (!file.exists()) return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
