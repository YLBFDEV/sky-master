package com.skytech.android.draft;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.skytech.android.Logging;
import com.skytech.android.SkyInitializer;
import com.skytech.android.database.GenericDAO;
import com.skytech.android.util.TaskDateTime;
import org.json.JSONObject;

import java.util.List;

public class DraftDBHelper extends GenericDAO {
    @Override
    protected String getTableName() {
        return "draftbox";
    }

    public DraftDBHelper(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * to save a form draft
     *
     * @param entityId entity`s id, null or empty if it is added locally
     * @param content  entity`s content
     * @return
     */
    public String save(String moduleCode, String entityId, String parentId, String content) {
        ContentValues values = new ContentValues();
        values.put("owner", SkyInitializer.getInstance().getUser().getId());
        values.put("module", moduleCode);
        values.put("pkid", entityId);
        values.put("parentid", parentId);
        values.put("updatetime", (new TaskDateTime()).toString());
        values.put("content", content);
        insert(values);

        Cursor c = rawQuery("select * from " + getTableName() +
                " order by " + getPKName() + " desc", null);
        if (c.moveToNext()) {
            return c.getString(c.getColumnIndex(getPKName()));
        } else {
            Log.e(Logging.LOG_TAG, "failed to insert form draft, no record found");
        }
        return null;
    }


    /**
     * to update a form draft
     *
     * @param content draft`s content
     * @param draftId draft`s local id
     */
    public void update(String content, String draftId) {
        update("update " + getTableName() + " set updatetime=?,content=? where id=? ",
                new String[]{(new TaskDateTime()).toString(), content, draftId}
        );
    }


    /**
     * to list service process drafts by entity specified
     *
     * @param userId user`s id
     * @param formId entity`s id
     * @return
     */
    public List<JSONObject> listProcessDraftsByEntity(String userId, String formId) {
        return list(getTableName(), null, "owner=? and record=?", new String[]{userId, formId}, "updatetime desc");
    }


    /**
     * to list drafts by module specified
     *
     * @param module module
     * @return
     */
    public List<JSONObject> listDraftsByModule(String module) {
        return list(getTableName(), null, "owner=? and module=?", new String[]{SkyInitializer.getInstance().getUser().getId(), module}, "updatetime desc");
    }


    /**
     * to list entity drafts (edit detail) by module given
     *
     * @param userId user`s id
     * @param module module
     * @return
     */
    public List<JSONObject> listServerDraftsByModule(String userId, String module) {
        return list(getTableName(), null, "owner=? and module=? and record is null and pkid is not null", new String[]{userId, module}, "updatetime desc");
    }


    /**
     * to list service process drafts by entity specified, used when someone wanna to modify his service record submitted.
     *
     * @param userId user`s id
     * @param formId entity`s id
     * @return
     */
    public List<JSONObject> listServerProcessDraftsByEntityProcessed(String userId, String formId) {
        return list(getTableName(), null, "owner=? and record=? and pkid is not null", new String[]{userId, formId}, "updatetime desc");
    }
}
