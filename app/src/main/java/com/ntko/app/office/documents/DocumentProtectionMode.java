package com.ntko.app.office.documents;

public enum DocumentProtectionMode {
    ON(0), OFF(1), UNKNOWN(-1);

    public int value = -1;

    DocumentProtectionMode(int value) {
        this.value = value;
    }
}