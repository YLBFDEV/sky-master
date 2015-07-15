package com.skytech.android.database;

import android.content.Context;
import org.json.JSONObject;

import java.util.List;

public class DatabaseManager {
    private MasterDbHelper dbHelper;

    public DatabaseManager(Context c) {
        dbHelper = new MasterDbHelper(DBHelper.getInstance().getDatabase());
    }

    public List<String> listTableName() {
        return dbHelper.listTableName();
    }

    public List<JSONObject> queryTable(String tableName) {
        return dbHelper.list(tableName, null, null, null, null);
    }

    public List<JSONObject> query(String sql) {
        return dbHelper.list(sql, null);
    }
}
