package com.ntko.app.office.documents;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 上传文件的时机
 * 解决关闭时保存会上传两次的BUG,
 * 注意：该方法一般作为内部调用
 */
public class UploadOptions implements Parcelable {
    enum Activation {ON_DOCUMENT_SAVED, ON_DOCUMENT_CLOSED}

    //失败重传
    private boolean retry;
    //激活时机
    private Activation activation;

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(retry ? (byte) 1 : (byte) 0);
        dest.writeInt(this.activation == null ? -1 : this.activation.ordinal());
    }

    public UploadOptions() {
    }

    private UploadOptions(Parcel in) {
        this.retry = in.readByte() != 0;
        int tmpActivation = in.readInt();
        this.activation = tmpActivation == -1 ? null : Activation.values()[tmpActivation];
    }

    public static final Creator<UploadOptions> CREATOR = new Creator<UploadOptions>() {
        public UploadOptions createFromParcel(Parcel source) {
            return new UploadOptions(source);
        }

        public UploadOptions[] newArray(int size) {
            return new UploadOptions[size];
        }
    };
}