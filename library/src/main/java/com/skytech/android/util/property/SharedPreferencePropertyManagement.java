package com.skytech.android.util.property;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencePropertyManagement implements PropertyManagement {
    private SharedPreferences applicationPreferences;
    private static PropertyManagement pm;

    public static PropertyManagement getInstance() {
        return pm;
    }

    public static void initialize(Context context) {
        pm = new SharedPreferencePropertyManagement(context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE));
    }

    public SharedPreferencePropertyManagement(SharedPreferences preferences) {
        applicationPreferences = preferences;
    }

    @Override
    public String getString(String name) {
        return applicationPreferences.getString(name, "");
    }

    @Override
    public boolean setString(String name, String val) {
        SharedPreferences.Editor editor = applicationPreferences.edit();
        editor.putString(name, val);
        return editor.commit();
    }


    @Override
    public boolean remove(String name) {
        SharedPreferences.Editor editor = applicationPreferences.edit();
        editor.remove(name);
        return editor.commit();
    }


    @Override
    public boolean contains(String name) {
        return applicationPreferences.contains(name);
    }


}