package com.skytech.moa.database;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.skytech.android.database.GenericDAO;

import java.util.List;

public class NewsDbHelper extends GenericDAO {

    public NewsDbHelper(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    protected String getTableName() {
        return "news";
    }

    public List<String> listAll() {
        return listString(new String[]{"newsid"}, null, null);
    }

    public boolean insert(String noticeId) {
        try {

            ContentValues values = new ContentValues();
            values.put("newsid", noticeId);
            insert(values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
