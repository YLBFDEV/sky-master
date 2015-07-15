package com.skytech.android.draft;

/**
 * to used to wrap records for UI to handle
 */
public class ModuleRecordWrapper {
    /**
     * record type definition
     */
    public enum RecordType {
        RECORD_TYPE_DRAFT,      // draft record
        RECORD_TYPE_SERVER      // server record
    }

    /**
     * record type
     */
    private RecordType type;
    /**
     * record content
     */
    private Object obj;

    public ModuleRecordWrapper(RecordType type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    private ModuleRecordWrapper() {
    }

    public boolean isDraft() {
        return type == RecordType.RECORD_TYPE_DRAFT;
    }

    public Object getContent() {
        return obj;
    }
}
