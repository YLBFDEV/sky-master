package com.ntko.app.office.documents;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class SendToUpload implements Parcelable {
    //文件路来源
    public String sResultFrom;
    //文件路径
    public String sResultFile;
    //文件
    public File sFile;
    //文件名
    public String sFileName;
    public CustomFields sCustomUploadFormFields;
    public String sURL;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sResultFrom);
        dest.writeString(this.sResultFile);
        dest.writeSerializable(this.sFile);
        dest.writeString(this.sFileName);
        dest.writeParcelable(this.sCustomUploadFormFields, 0);
        dest.writeString(this.sURL);
    }

    public SendToUpload() {
    }

    private SendToUpload(Parcel in) {
        this.sResultFrom = in.readString();
        this.sResultFile = in.readString();
        this.sFile = (File) in.readSerializable();
        this.sFileName = in.readString();
        this.sCustomUploadFormFields = in.readParcelable(CustomFields.class.getClassLoader());
        this.sURL = in.readString();
    }

    public static final Parcelable.Creator<SendToUpload> CREATOR = new Parcelable.Creator<SendToUpload>() {
        public SendToUpload createFromParcel(Parcel source) {
            return new SendToUpload(source);
        }

        public SendToUpload[] newArray(int size) {
            return new SendToUpload[size];
        }
    };
}