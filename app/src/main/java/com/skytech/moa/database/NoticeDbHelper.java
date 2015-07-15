package com.skytech.moa.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.skytech.android.database.GenericDAO;

import java.util.List;

/**
 * Created by Administrator on 2015/6/9.
 */
public class NoticeDbHelper extends GenericDAO {

    public NoticeDbHelper(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    protected String getTableName() {
        return "notice";
    }

    public List<String> listAll() {
        return listString(new String[]{"noticeid"}, null, null);
    }

    public boolean insert(String noticeId) {
        try {

            ContentValues values = new ContentValues();
            values.put("noticeid", noticeId);
            insert(values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
