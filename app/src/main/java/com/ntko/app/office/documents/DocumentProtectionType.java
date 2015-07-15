package com.ntko.app.office.documents;

public enum DocumentProtectionType {
    TRACK_CHANGES(0), COMMENTS(1), FORMS(2), READONLY(3), NONE(7), UNKNOWN(-1);

    public int value = 7;

    DocumentProtectionType(int value) {
        this.value = value;
    }

    static DocumentProtectionType fromValue(int value) {
        switch (value) {
            case 0:
                return TRACK_CHANGES;
            case 1:
                return COMMENTS;
            case 2:
                return FORMS;
            case 3:
                return READONLY;
            case 7:
                return NONE;
            default:
                return UNKNOWN;
        }
    }
}