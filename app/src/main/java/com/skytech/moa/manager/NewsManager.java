package com.skytech.moa.manager;

import com.skytech.android.database.DBHelper;
import com.skytech.moa.database.NewsDbHelper;

import java.util.List;

/**
 * Created by Administrator on 2015/6/10.
 */
public class NewsManager {

    private static NewsManager manager;
    private List<String> readNewsIds;
    private NewsDbHelper dbHelper;

    public static NewsManager getInstance() {
        if (null == manager) {
            manager = new NewsManager();
        }
        return manager;
    }

    public NewsManager() {
        try {
            dbHelper = new NewsDbHelper(DBHelper.getInstance().getDatabase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.readNewsIds = dbHelper.listAll();
    }

    public boolean isRead(String noticeId) {
        return readNewsIds.contains(noticeId);
    }

    public void addNews(String newsId) {
        if (!isRead(newsId)) {
            dbHelper.insert(newsId);
            readNewsIds.add(newsId);
        }
    }

}
