package com.skytech.moa;

import com.skytech.android.cache.CacheType;
import com.skytech.android.http.UrlCache;

public class API {
    // Login
    public static String LOGIN_GET_REQUEST_TOKEN = "login/getRequestToken.do";
    public static String LOGIN_GET_ACCESS_TOKEN = "login/getAccessToken.do";
    public static String LOGIN_GET_USER_INFO = "login/getUserInfo.do";

    // Get All Modules
    public static UrlCache GET_ALL_MODULES = new UrlCache("getModules.do", CacheType.PERMANENT);

    // Get to do number
    public static UrlCache GET_TODO_NUM = new UrlCache("updateTodoNumber.do", CacheType.NOCACHE);

    // Get new docs
    public static UrlCache GET_NEWDOCS = new UrlCache("AppInterface/getNewestGW.do", CacheType.NOCACHE);
    // Get Documents
    public static UrlCache GET_DOCUMENTS = new UrlCache("AppInterface/getOfficialFileList.do", CacheType.NOCACHE);
    public static UrlCache GET_LEADWORK = new UrlCache("AppInterface/LoadRcPageList.do", CacheType.NOCACHE);
    public static UrlCache GET_WORKLOG = new UrlCache("AppInterface/getRzList.do", CacheType.NOCACHE);

    // Get Notices
    public static UrlCache GET_NOTICES = new UrlCache("AppInterface/LoadNoticeByPage.do", CacheType.NOCACHE);
    // Get SCHEDULES
    public static UrlCache GET_SCHEDULES = new UrlCache("AppInterface/getLeaderweekplanList.do", CacheType.NOCACHE);
    // Get NODES
    public static UrlCache GET_NODES = new UrlCache("AppInterface/getNodesList.do", CacheType.PERMANENT);
    //Post Add Note
    public static UrlCache POST_ADD_NODE = new UrlCache("AppInterface/nodeTransData.do", CacheType.NOCACHE);
    // Get workStepMonitor
    public static UrlCache POST_WORK_STEP_MONITOR = new UrlCache("AppInterface/workStepMonitor.do", CacheType.NOCACHE);

    public static UrlCache POST_DOC_DETAIL = new UrlCache("AppInterface/getOfficialFileDetail.do", CacheType.NOCACHE);
    public static UrlCache POST_DOC_BUTTON = new UrlCache("AppInterface/getOfficialFileBtn.do", CacheType.NOCACHE);
    public static UrlCache GET_NEXT_USER = new UrlCache("AppInterface/getNextUsrList.do", CacheType.NOCACHE);
    public static UrlCache GET_DOWNLOADFILE = new UrlCache("AppInterface/LoadFile.do", CacheType.NOCACHE);
    public static UrlCache POST_UPLOADFILE = new UrlCache("AppInterface/UploadFileAPI.do", CacheType.NOCACHE);
    public static UrlCache POST_DOC_NEXTSTEP = new UrlCache("AppInterface/btnTransData.do", CacheType.NOCACHE);

    /*notice detail*/
    public static UrlCache GET_NOTICE_DETAIL = new UrlCache("AppInterface/GetNoticeById.do", CacheType.NOCACHE);
    /*node detail*/
    public static UrlCache GET_NOTE_DETAIL = new UrlCache("AppInterface/getNodeDetail.do", CacheType.PERMANENT);
    /*contacts list*/
    public static UrlCache GET_CONTACTS = new UrlCache("contact/getContacts.do?uid=%s", CacheType.NOCACHE);

    /*documents library*/
    public static UrlCache GET_ALL_DOCUMENTS = new UrlCache("document/getDocuments.do", CacheType.PERMANENT);
    public static UrlCache DOWNLOAD_DOC_DETAILS = new UrlCache("attachment/download.do", CacheType.PERMANENT);
    /*附件下载*/
    public static String GET_FILE = "file/download.do?fileName=%s";
    /*附件上传*/
    public static String POST_FILE = "attachment/file";
    public static String NEWS = "news/getNews.do";

    public static String ATTENDANCE_LIST = "attendance/list.do";        //考勤
    public static String ATTENDANCE_EXCEPTION_LIST = "attendance/exceptionList.do";  //考勤异常
    public static String ATTENDANCE_EXCEPTION_DETAIL = "attendance/exception.do";  //考勤异常详情
    public static String ATTENDANCE_EXCEPTION_SAVE = "attendance/exception.do";  //考勤异常登记
    public static String ATTENDANCE_SIGNIN = "attendance/signin.do";    //考勤签到
    public static String AFFAIR_LIST = "affair/affairApplyList.do";//事务申请显示列表
    public static String MEETING_LIST = "affair/MeetingManageList.do";//会议申请显示列表
    /*获取事务申请表单*/
    public static String AFFAIR_FORM = "affair/getAffairForm.do";
    /*获取费用申请表单*/
    public static String EXPENSE_LIST = "";
    public static String EXPENSE_FORM = "affair/getExpenseForm.do";
    /*获取事务申请按钮*/
    public static String AFFAIR_FORM_BUTTONS = "affair/fetchAffairFormButtons.do";
    public static String AFFAIR_FORM_SAVE = "affair/fetchAffairFormButtons.do";
    /*获取费用申请按钮*/
    public static String EXPENSE_FORM_BUTTONS = "affair/fetchExpenseFormButtons.do";
    public static String EXPENSE_FORM_SAVE = "affair/fetchExpenseFormButtons.do";
    /*获取新增日志表单*/
    public static String NEWWORKLOGS_FORM = "affair/getNewWorkLogsForm.do";
    /*获取新增日志按钮*/
    public static String NEWWORKLOGS_FORM_BUTTONS = "affair/fetchNewWorkLogsFormButtons.do";
    public static String NEWWORKLOGS_FORM_SAVE = "affair/fetchNewWorkLogsFormButtons.do";
    /*获取新增会议表单*/
    public static String NEWMEETING_FORM = "affair/getNewMeetingForm.do";
    /*获取新增会议按钮*/
    public static String NEWMEETING_FORM_BUTTONS = "affair/fetchNewMeetingFormButtons.do";
    public static String NEWMEETING_FORM_SAVE = "affair/fetchNewMeetingFormButtons.do";
    /*获取新增任务安排表单*/
    public static String NEWWORKPLAN_FORM = "affair/getNewWorkplanForm.do";
    /*获取新增任务安排按钮*/
    public static String NEWWORKPLAN_FORM_BUTTONS = "affair/fetchNewWorkplanFormButtons.do";
    public static String NEWWORKPLAN_FORM_SAVE = "affair/fetchNewWorkplanFormButtons.do";
    /*获取新增个人备忘表单*/
    public static String NEWMEMO_FORM = "affair/getNewMemoForm.do";
    /*获取新增个人备忘按钮*/
    public static String NEWMEMO_FORM_BUTTONS = "affair/fetchNewMemoFormButtons.do";
    public static String NEWMEMO_FORM_SAVE = "affair/fetchNewMemoFormButtons.do";
    public static String NOTICE = "notice/getNotices.do";

    public static UrlCache GET_NOTICE_ATTACHMENT = new UrlCache("notice/getNoticeAttachment.do", CacheType.SHORT);
    public static UrlCache GET_ALL_DOC_DETAILS = new UrlCache("document/getDocumentDetail.do", CacheType.PERMANENT);
}

