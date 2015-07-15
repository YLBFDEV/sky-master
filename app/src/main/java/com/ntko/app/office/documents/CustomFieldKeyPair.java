package com.ntko.app.office.documents;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WB
 * on 2014/11/6.
 */
public class CustomFieldKeyPair implements Parcelable {
    private String key;
    private String value;

    public CustomFieldKeyPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    private CustomFieldKeyPair(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Creator<CustomFieldKeyPair> CREATOR = new Creator<CustomFieldKeyPair>() {
        public CustomFieldKeyPair createFromParcel(Parcel source) {
            return new CustomFieldKeyPair(source);
        }

        public CustomFieldKeyPair[] newArray(int size) {
            return new CustomFieldKeyPair[size];
        }
    };
}
