package com.skytech.android.util.property;

public interface PropertyManagement {
    public String getString(String name);

    public boolean setString(String name, String val);

    public boolean remove(String name);

    public boolean contains(String name);
}
