package com.ntko.app.office.documents;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WB
 * on 2014/11/6.
 */
public class CustomFields implements Parcelable {

    public List<CustomFieldKeyPair> fieldsList = new ArrayList<CustomFieldKeyPair>();

    public List<CustomFieldKeyPair> getFieldsList() {
        return fieldsList;
    }

    public int size() {
        return fieldsList.size();
    }

    @Override
    public String toString() {
        return "CustomFields{ " + fieldsList + " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(fieldsList);
    }

    public CustomFields() {
    }

    private CustomFields(Parcel in) {
        in.readTypedList(fieldsList, CustomFieldKeyPair.CREATOR);
    }

    public static final Creator<CustomFields> CREATOR = new Creator<CustomFields>() {
        public CustomFields createFromParcel(Parcel source) {
            return new CustomFields(source);
        }

        public CustomFields[] newArray(int size) {
            return new CustomFields[size];
        }
    };
}
