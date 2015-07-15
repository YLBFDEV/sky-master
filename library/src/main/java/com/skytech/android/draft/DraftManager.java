package com.skytech.android.draft;

import android.util.Log;
import com.skytech.android.Logging;
import com.skytech.android.database.DBHelper;
import com.skytech.android.util.StringUtils;
import com.skytech.android.exception.BusinessLogicalException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DraftManager implements IDraftManager {
    private DraftDBHelper draftDBHelper;

    public DraftManager() {
        draftDBHelper = new DraftDBHelper(DBHelper.getInstance().getDatabase());
    }

    @Override
    public List<Draft> list(String module) {
        List<JSONObject> listJson = draftDBHelper.listDraftsByModule(module);
        List<Draft> list = new LinkedList<>();
        for (JSONObject object : listJson) {
            try {
                list.add(new Draft(object.optLong("id"), object.optString("pkid"), object.optString("updatetime"), object.optString("content")));
            } catch (BusinessLogicalException ble) {
                Log.e(Logging.LOG_TAG, ble.toString());
            }
        }
        return list;
    }

    /**
     * to save a form draft
     *
     * @param entityId entity`s id, null or empty if it is a local draft
     * @param content  record`s content
     * @return
     */
    @Override
    public String saveDraft(String draftId, String moduleCode, String entityId, String content) {
        if (StringUtils.isEmpty(draftId)) {
            return draftDBHelper.save(moduleCode, entityId, "", content);
        } else {
            update(draftId, content);
            return draftId;
        }
    }

    /**
     * to get a form draft
     *
     * @param draftId draft`s parent id
     * @return draft
     */
    @Override
    public Draft getDraft(String draftId) {
        if (null == draftId) {
            Log.w(Logging.LOG_TAG, "draft id is null");
            return null;
        }
        JSONObject object = draftDBHelper.load(draftId);
        if (null == object) {
            Log.w(Logging.LOG_TAG, "can not find form draft, id : " + draftId);
            return null;
        }

        Draft draft = null;
        try {
            draft = new Draft(object.optLong("id"), object.optString("pkid"), object.optString("updatetime"), object.optString("content"));
        } catch (BusinessLogicalException ble) {
            Log.e(Logging.LOG_TAG, ble.toString());
        }

        return draft;
    }

    /**
     * to update a form draft
     *
     * @param draftId
     * @param content
     */
    @Override
    public void update(String draftId, String content) {
        draftDBHelper.update(content, draftId);
    }

    /**
     * to update a form draft
     *
     * @param draft new draft
     */
    public void update(Draft draft) {
        if ((null == draft) || (0 == draft.getId())) {
            throw new IllegalArgumentException("parameter error while updating a form draft");
        }
        draftDBHelper.update(draft.getContent().toString(), "" + draft.getId());
    }

    /**
     * to delete a form draft
     *
     * @param draftId draft to be deleted
     */
    @Override
    public void delete(String draftId) {
        if (draftId.isEmpty()) {
            throw new IllegalArgumentException("parameter error while deleting a form draft");
        }

        draftDBHelper.del(draftId);
    }

    /**
     * to delete a form draft
     *
     * @param draft draft to be deleted
     */
    @Override
    public void delete(Draft draft) {
        if ((null == draft) || (0 == draft.getId())) {
            throw new IllegalArgumentException("parameter error while deleting a form draft");
        }

        draftDBHelper.del("" + draft.getId());
    }
}
