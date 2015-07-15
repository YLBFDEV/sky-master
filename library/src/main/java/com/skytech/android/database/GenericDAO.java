package com.skytech.android.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: sunjiejing Date: 13-6-5 Time: 上午9:37
 */

public abstract class GenericDAO {
    protected SQLiteDatabase db;

    abstract protected String getTableName();

    protected String getPKName() {
        return "id";
    }

    public JSONObject load(String id) {
        Cursor c = db.query(getTableName(), null, getPKName() + "=?",
                new String[]{id}, null, null, null);
        if (c.moveToNext()) {
            return wrapCursor(c);
        } else {
            return null;
        }
    }

    protected List<JSONObject> list(String[] columns, String selection,
                                    String[] selectionArgs, String orderBy) {
        return list(getTableName(), columns, selection, selectionArgs, orderBy);
    }

    protected List<JSONObject> list(String sql, String[] selectionArgs) {
        List<JSONObject> result = new ArrayList<JSONObject>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            result.add(wrapCursor(c));
        }
        return result;
    }

    protected List<JSONObject> list(String tableName, String[] columns,
                                    String selection, String[] selectionArgs, String orderBy) {
        List<JSONObject> result = new ArrayList<JSONObject>();
        Cursor c = db.query(tableName, columns, selection, selectionArgs, null,
                null, orderBy);
        while (c.moveToNext()) {
            result.add(wrapCursor(c));
        }
        return result;
    }

    protected List<String> listString(String[] columns, String selection, String[] selectionArgs) {
        List<String> result = new ArrayList<String>();
        Cursor c = db.query(getTableName(), columns, selection, selectionArgs, null, null, null);
        while (c.moveToNext()) {
            result.add(c.getString(0));
        }
        return result;
    }

    protected List<String> listString(String sql, String[] selectionArgs) {
        List<String> result = new ArrayList<String>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            result.add(c.getString(0));
        }
        return result;
    }

    private JSONObject wrapCursor(Cursor c) {
        JSONObject obj = new JSONObject();
        for (String name : c.getColumnNames()) {
            try {
                obj.putOpt(name, c.getString(c.getColumnIndex(name)));
            } catch (JSONException e) {
                Log.e(getLogTag(), e.getMessage());
            }
        }
        return obj;
    }

    private String getLogTag() {
        return this.getClass().getName() + "[" + getTableName() + "]";
    }

    protected void insert(ContentValues values) {
        db.insert(getTableName(), null, values);
    }

    protected Cursor query() {
        return db.query(getTableName(), null, null, null, null, null, null);
    }

    protected Cursor query(String[] columns, String selection,
                           String[] selectionArgs, String groupBy, String having,
                           String orderBy) {
        return db.query(getTableName(), columns, selection, selectionArgs,
                groupBy, having, orderBy);
    }

    protected Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public void update(String sql, Object[] selectionArgs) {
        db.execSQL(sql, selectionArgs);
    }

    public void update(JSONObject updated) {
        if (updated == null)
            return;

        ContentValues cv = new ContentValues();
        Iterator<String> keys = updated.keys();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            if (key.equals(getPKName()))
                continue;
            cv.put(key, updated.optString(key));
        }

        db.update(getTableName(), cv, getPKName() + "=?",
                new String[]{updated.optString(getPKName())});
    }

    public void del(String id) {
        db.delete(getTableName(), getPKName() + "=?", new String[]{id});
    }

    public void delAll() {
        db.delete(getTableName(), null, null);
    }

    public int countAll() {
        return query().getCount();
    }

    public void expire(String version) {
        db.execSQL("delete from " + getTableName() + " where version < ?",
                new String[]{version});
    }
}
