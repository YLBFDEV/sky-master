package com.skytech.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper;
    private static final int VERSION = 2;
    private Context context;
    private SQLiteDatabase database;

    public static DBHelper getInstance() {
        return dbHelper;
    }

    public static void initialize(Context context, String dbName) {
        dbHelper = new DBHelper(context, dbName);
    }

    public DBHelper(Context c, String dbName) {
        super(c, dbName, null, VERSION);
        context = c;
        database = getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void closeDB() {
        database.close();
        database.getPath();
    }

    /*当数据库被首次创建时执行该方法，一般将创建表等初始化操作在该方法中执行。*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("persistence init", "creating table draftbox");
        db.execSQL("create table if not exists draftbox(id integer primary key autoincrement,owner varchar(50),module varchar(50),parentid varchar(50),pkid varchar(50),updatetime datetime,content text)");
        db.execSQL("create table if not exists category(key varchar(50),context text) ");
        db.execSQL("create table if not exists url(key varchar(50),context text) ");
        db.execSQL("create table if not exists notice(id integer primary key autoincrement,noticeid varchar(50)) ");
        db.execSQL("create table if not exists news(id integer primary key autoincrement,newsid varchar(50)) ");
    }

    /*当打开数据库时传入的版本号与当前的版本号不同时会调用该方法。*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("persistence upgrade", "123");
    }
}