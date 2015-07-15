package com.skytech.moa.utils;

public class Constant {
    public static final String USER_ID = "uid";
    public static final String AFFAIR_TYPE = "type";
    /**
     * input intent`s parameter names
     */
    public static final String EXTRA_CONTACT = "contact";

    public final static String EXTRA_MODULE_CODE = "module_code";
    public final static String EXTRA_MODULE_NAME = "module_title";
    public static final String PARAMETER_STARTTIME = "starttime";
    public static final String PARAMETER_ENDTIME = "endtime";
    public static final String DOCUMENT_TITLE = "title";
    public static final String CURRENT_STEP = "currentstep";
    public static final String RELEASE_TIME = "releasetime";
    public static final String NOTICES = "list";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_LATITUDE = "latitude";
    public static final String PARAM_LONGITUDE = "longitude";
    public static final String PARAM_ADDRESS = "address";

    public static final String ATTACHMENT_ID = "id";
    public static final String ATTACHMENT_NAME = "name";
    public static final String ATTACHMENT_PATH = "path";

    public static final String IS_TODO = "istodo";

    public static final int REQUEST_ATTACHMENT = 101;

    public static final int ATTENDANCE_TYPE_UP_WORK = 0;
    public static final int ATTENDANCE_TYPE_DOW_WORK = 1;

    public static final String MODULE_KEY_ATTENDANCE_EXCEPTION = "kqyc";


    /*handler msg code*/
    public static final int START_CACHE = 90;
    public static final int START_DRAFTS = 91;
    public static final int START_LOADING = 100;
    public static final int LIST_SUCCESS = 200;
    public static final int LIST_FAILURE = 400;
    public static final int DETAIL_SUCCESS = 201;
    public static final int DETAIL_FAILURE = 401;
    public static final int BUTTON_OK = 202;
    public static final int BUTTON_FAILURE = 402;
    public static final int NEXT_USER_OK = 203;
    public static final int NEXT_USER_FAILURE = 403;
    public static final int NEXT_STEP_OK = 204;
    public static final int NEXT_STEP_FAILURE = 404;
    public static final int DWONLOAD_FILE_OK = 205;
    public static final int DWONLOAD_FILE_FAILURE = 405;
    /*文档类型 */
    public static final int DOC = 0;
    public static final int PDF = 1;
    public static final int PPT = 2;
    public static final int ZIP = 3;
    /*排序规则*/
    public static final String SortByTime = "sortbytime";
    /**
     * action
     */
    public static final String ACTION_LOGIN_FAIL = "com.skytech.action.LOGIN_FAIL";
    public static final String ACTION_NETWORK_TIMEDOUT = "com.skytech.action.NETWORK_TIMEDOUT";


    /**
     * input intent`s parameter names
     */
    public static final String EXTRA_REFRESH = "isRefresh";
    public static final String EXTRA_DRAFT = "draftId";
    public static final String EXTRA_LAYOUT_RESID = "resId";

    public final static String PARAM_SUCCESS = "success";
    public final static String PARAM_MESSAGE = "msg";
    public final static String PARAM_BUTTONS = "buttons";
    public final static String PARAM_USERID = "uid";
    public final static String PARAM_PAGESIZE = "pagesize";
    public final static String PARAM_PAGENUM = "pagenum";
    public final static String PARAM_PKID = "pkid";
    public final static String PARAM_ID = "id";
    public final static String PARAM_CONID = "conid";
    public final static String JSONKEY_ITEMS = "items";
    public final static String JSONKEY_AFFAIRS = "affairs";
    public static final String JSONKEY_ADDABLE = "isaddable";
    public static final String JSONKEY_EDITABLE = "iseditable";


    /*请求码-REQUEST*/
    public static final int REQUEST_LIST = 112;

    /**
     * time interval for emit stop loading
     */
    public static final int LOADING_INTERVAL = 200;


    /* doc start */
    public static final String DOCUMENT_IS_SIGNATURE = "issignature";
    public static final String DOC_TITLE = "title";
    public static final String DOC_FIELDS = "fields";
    public final static String DOC_ATTACHMENTS = "attachments";
    public static final String DOC_BUTTON_NAME = "buttonname";
    public static final String DOC_BUTTON_ACTION = "action";
    public static final String DOC_BUTTON_ACTION_DRAFT = "draft";
    public static final String DOC_BUTTON_ACTION_SUBMIT = "submit";
    public static final String FIELD_TYPE = "valuetype";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_INPUT = "input";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_KEYID = "keyid";
    public static final String FIELD_KEYNAME = "keyname";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_EDITABLE = "editable";
    public static final String FIELD_OPINION = "choice";
    public static final String FIELD_VALUES = "values";
    public static final String FIELD_REQUIRED = "required";
    public static final String SUB_FORM = "subform";
    public static final String FILE = "file";
   /* doc end */

    public static final String BUTTON_ACTION_SAVE = "save";

    /* activity_login */
    public static final String OAUTH_REQUEST_PARAM_LOGIN_KEY = "loginKey";
    public static final String OAUTH_REQUEST_PARAM_CLIENT_SIGN_DATA = "signDataFromClient";
    public static final String OAUTH_RESPONSE_NONCE_TOKEN = "data";
    public static final String OAUTH_RESPONSE_ACCESS_TOKEN = "data";
    public static final String OAUTH_RESPONSE_USER_INFO = "data";

    /* Module Definition */
    public static final String MODULE_NEWS = "news";//申请事务6.1
    public static final String MODULE_JFTJ = "jftj";//纠纷调解
    public static final String MODULE_DBSX = "dbsx";
    public static final String MODULE_TZGG = "tzgg";
    public static final String MODULE_RCAP = "rcap";
    public static final String MODULE_GZYJ = "gzyj";
    public static final String MODULE_TXL = "txl";
    public static final String MODULE_RCSW = "rcsw";//日常事务
    public static final String MODULE_RCSW_SWSQ = "swsq";//申请事务
    public static final String MODULE_KHGL = "khgl";
    public static final String MODULE_WDK = "wdk";
    public static final String MODULE_CLDT = "cldt";
    public static final String MODULE_TJGN = "tjgn";
    public static final String MODULE_EXPENSE = "fysq";//费用申请
    public static final String MODULE_NEWWORKLOG = "gzrz";//增加工作日志
    public static final String MODULE_NEWMEETING = "hysq";//会议申请
    public static final String MODULE_NEWWORKPLAN = "gzjh";//新增工作任务安排
    public static final String MODULE_NEWMEMO = "grbw";//新增备忘
}
