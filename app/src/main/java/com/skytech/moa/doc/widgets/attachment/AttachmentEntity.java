package com.skytech.moa.doc.widgets.attachment;

public class AttachmentEntity {
    private String fileId;
    private String filePath;
    private String speed;
    private boolean isUpload;

    public AttachmentEntity(String fileId) {
        this(null, false);
        this.fileId = fileId;
    }

    public AttachmentEntity(String filePath, boolean isUpload) {
        this.filePath = filePath;
        this.isUpload = isUpload;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
