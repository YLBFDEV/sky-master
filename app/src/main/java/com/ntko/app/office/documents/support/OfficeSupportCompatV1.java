package com.ntko.app.office.documents.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface OfficeSupportCompatV1 {
    static enum OfficeCompatCode {

        OFFICE_PRO("PDF", "OFFICE_PRO"),
        OFFICE_STD("PDF", "OFFICE_STD"),
        PDF_STANDALONE("PDF");

        private List<String> codes = new ArrayList<String>();

        OfficeCompatCode(String... types) {
            Collections.addAll(codes, types);
        }

        public List<String> getCodes() {
            return codes;
        }
    }

    OfficeCompatCode getCompatCode();
}
