package com.skytech.moa.model;

public class Attachment {
    private String id, fileName, type;
    private boolean editable;

    public Attachment(String id, String fileName, String type) {
        this.id = id;
        this.fileName = fileName;
        this.type = type.replace(".", "");
    }

    public Attachment(String id, String fileName, String type, boolean editable) {
        this(id, fileName, type);
        this.editable = editable;
    }

    public String getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getType() {
        return type;
    }

    public boolean isEditable() {
        return editable;
    }

/*    public int getFileIco() {
        int imageId;
        String extension = getType().toLowerCase().replace(".", "");
        if (extension.equals("zip") || extension.equals("rar") || extension.equals("7z")) {
            imageId = R.drawable.attachment_file_zip;
        } else if (extension.equals("xls") || extension.equals("xlsx")) {
            imageId = R.drawable.attachment_file_xlsx;
        } else if (extension.equals("doc") || extension.equals("docx")) {
            imageId = R.drawable.attachment_file_doc;
        } else if (extension.equals("jpg") || extension.equals("png") || extension.equals("bmp") || extension.equals("gif")) {
            imageId = R.drawable.attachment_file_png;
        } else if (extension.equals("pdf")) {
            imageId = R.drawable.attachment_file_pdf;
        } else if (extension.equals("txt")) {
            imageId = R.drawable.attachment_file_txt;
        } else if (extension.equals("iso")) {
            imageId = R.drawable.attachment_file_iso;
        } else if (extension.equals("mov") || extension.equals("mp4")) {
            imageId = R.drawable.attachment_file_mov;
        } else if (extension.equals("ced")) {
            imageId = R.drawable.attachment_file_ceb;
        } else {
            imageId = R.drawable.attachment_file_setting;
        }
        return imageId;
    }*/
}
