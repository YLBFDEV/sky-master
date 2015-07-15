package com.skytech.android.draft;

import java.util.List;

public interface IDraftManager {

    public List<Draft> list(String module);

    /**
     * to save a form draft
     *
     * @param entityId entity`s id, null or empty if it is a local draft
     * @param content  record`s content
     * @return
     */
    public String saveDraft(String draftId, String moduleCode, String entityId, String content);


    /**
     * to get a form draft
     *
     * @param id draft`s id
     * @return draft
     */
    public Draft getDraft(String id);

    /**
     * to update a form draft
     *
     * @param draftId
     * @param content
     */
    public void update(String draftId, String content);


    /**
     * to delete a form draft
     *
     * @param draftId draft to be deleted
     */
    public void delete(String draftId);

    /**
     * to delete a form draft
     *
     * @param draft draft to be deleted
     */
    public void delete(Draft draft);
}
