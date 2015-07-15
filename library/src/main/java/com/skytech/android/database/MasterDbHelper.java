package com.skytech.android.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class MasterDbHelper extends GenericDAO {

    public MasterDbHelper(SQLiteDatabase database) {
        db = database;
    }

    @Override
    protected String getTableName() {
        return "sqlite_master";
    }

    public List<String> listTableName() {
        return listString(
                "select name from sqlite_master where type=? order by name",
                new String[]{"table"});
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }
}
