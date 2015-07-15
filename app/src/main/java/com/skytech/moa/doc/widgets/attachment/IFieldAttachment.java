package com.skytech.moa.doc.widgets.attachment;

public interface IFieldAttachment {
    void onUploadSuccess(String response);

    void onUploadFailure();

    void onDownloadProgress(int index, String speed);

    void onDownloadSuccess(int index, String filePath);

    void onDownloadFailure(int index, String error);
}
