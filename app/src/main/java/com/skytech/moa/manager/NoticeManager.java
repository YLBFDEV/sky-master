package com.skytech.moa.manager;

import com.skytech.android.database.DBHelper;
import com.skytech.moa.database.NoticeDbHelper;

import java.util.List;

public class NoticeManager {
    private static NoticeManager manager;
    private List<String> readNoticeIds;
    private NoticeDbHelper dbHelper;

    public static  NoticeManager getInstance() {
        if (null == manager) {
            manager = new NoticeManager();
        }
        return manager;
    }

    public NoticeManager() {
        try {
            dbHelper = new NoticeDbHelper(DBHelper.getInstance().getDatabase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.readNoticeIds = dbHelper.listAll();
    }

    public boolean isRead(String noticeId) {
        return readNoticeIds.contains(noticeId);
    }

    public void addNotice(String noticeId) {
        if (!isRead(noticeId)) {
            dbHelper.insert(noticeId);
            readNoticeIds.add(noticeId);
        }
    }
}
