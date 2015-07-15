package com.skytech.moa.callback;

public interface HomeClickHappend {

    void onRefreshNewdoc(int pageNum);

    void onClickDocs(String docType, boolean isTodo);

    void onClickDoc(String docType, String docid);

    void onClickMessages(String module);

    void onClickLogout();
}
