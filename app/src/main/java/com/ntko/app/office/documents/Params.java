package com.ntko.app.office.documents;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WB
 * on 2015/3/27.
 */
@SuppressWarnings("UnusedDeclaration")
public final class Params implements Parcelable {

    private final static int MATH = 'M' + 'D' + 'S' + 'V';
    private final static int CREATE = MATH + 332;
    private final static int OPEN = MATH + 322;
    private final static int VIEW = MATH + 312;
    private final static int EDIT_SERVER = MATH + 302;
    private final static int EDIT_PDF = MATH + 300;

    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    public final static int OBTAIN_REGISTER_CLIENT = MATH - 1;
    /**
     * Command to the service to unregister a client, ot stop receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client as previously given with MSG_REGISTER_CLIENT.
     */
    public final static int OBTAIN_UNREGISTER_CLIENT = MATH - 2;

    //CREATE 646, Local File
    public final static int OBTAIN_CREATE = CREATE + 1;
    //OPEN 636, Local File
    public final static int OBTAIN_EDIT_LOCAL_DOC = OPEN + 1;
    //VIEW 626, Web File
    public final static int OBTAIN_VIEW_REMOTE_DOC = VIEW + 1;
    //EDIT_SERVER 616, Web File
    public final static int OBTAIN_EDIT_REMOTE_DOC = EDIT_SERVER + 1;
    //EDIT PDF 614, Local or Web File
    public final static int OBTAIN_EDIT_PDF = EDIT_PDF + 1;
    public final static int OBTAIN_EDIT_REMOTE_PDF = EDIT_PDF + 2;
    //file type
    public final static String FILE_TYPE_PDF = "pdf";
    public final static String FILE_TYPE_WORD = "doc";
    public final static String FILE_TYPE_EXCEL = "xls";
    public final static String FILE_TYPE_PRESENTATION = "ppt";
    public final static String FILE_TYPE_MSO = "ppt|xls|doc";
    public static String FILE_TYPE = FILE_TYPE_WORD;

    //fix-#131 FILE_TYPE不能被内部应用作为单独的实例独立引用
    private String fileType = FILE_TYPE_WORD;

    //发送到服务的参数名称
    public final static String SVR_OFFICE_PRO = "RHOfficePro.DOC_SVC";//"com.ntko.app.office.service.WPSProService";//;"RHOfficePro.DOC_SVC";
    public final static String SVR_OFFICE_STD = "RHOfficeStd.DOC_SVC";//"com.ntko.app.office.service.YzStdService";//"RHOfficeStd.DOC_SVC";
    public final static String IFR_OFFICE_PRO = "RHOfficePro.ACT_IFR";
    public final static String PEM_OFFICE_PRO = "com.rh.security.DOC_MAN";
    public final static String ACTION_NAME = "ACTION_NAME";
    public final static String ACTION_DOWNLOAD = "DOWNLOAD";
    public final static String ACTION_CREATE = "CREATE";
    public final static String ACTION_OPEN = "OPEN";
    public final static String EDIT_FILE_PATH = "EDIT_FILE_PATH";
    public final static String EDIT_FILE_NAME = "EDIT_FILE_NAME";
    public final static String EDIT_FILE_PARAMS = "EDIT_FILE_PARAMS";
    public final static String EDIT_FILE_FORM_FIELDS = "EDIT_FILE_FORM_FIELDS";
    public final static String EDIT_FILE_DOWNLOAD_PATH = "EDIT_FILE_DOWNLOAD_PATH";
    public final static String EDIT_FILE_UPLOAD_PATH = "EDIT_FILE_UPLOAD_PATH";
    public final static String EDIT_FILE_UPLOAD_SETS = "EDIT_FILE_UPLOAD_SETS";

    //广播相关的参数
    public final static String InT_FILTER_CREATE = "OFFICE_OPS_CREATE";
    public final static String InT_FILTER_OPEN_DOC_RMT = "OFFICE_OPS_OPEN_DOC_1";
    public final static String InT_FILTER_OPEN_DOC_LCL = "OFFICE_OPS_OPEN_DOC_2";
    public final static String InT_FILTER_OPEN_PDF_RMT = "OFFICE_OPS_OPEN_PDF_1";
    public final static String InT_FILTER_OPEN_PDF_LCL = "OFFICE_OPS_OPEN_PDF_2";
    public final static String InT_FILTER_FIELDS_SETTER = "GLOBAL_FIELDS_SETTER";
    public final static String InT_FILTER_FIELDS_CLEAR = "GLOBAL_FIELDS_CLEAR";
    public final static String InT_FILTER_D_CTX_UPLOAD = "MSO_UPLOAD_EXT_CTX";
    public final static String InT_FILTER_MSO_DOC_OPENED = "MSO_DOC_OPENED";
    public final static String InT_FILTER_MSO_DOC_CLOSED = "MSO_DOC_CLOSED";

    /**
     * 创建/打开的文档名称，不同于文档的文件名称,这是显示在界面标题栏的名称
     */
    private String documentTitle;
    /**
     * 创建类型, 同时应用于打开文件的类型
     */
    private String createType;
    /**
     * @Type String
     * <p/>
     * 创建类型：2003兼容格式电子表格
     */
    public final static String MSO_CREATE_TYPE_SHEET = "CREATE_SS";
    /**
     * @Type String
     * <p/>
     * 创建类型：电子表格
     */
    public final static String MSO_CREATE_TYPE_SHEET_X = "CREATE_SS_X";
    /**
     * @Type String
     * <p/>
     * 创建类型：2003兼容格式电子文稿
     */
    public final static String MSO_CREATE_TYPE_WORD = "CREATE_WP";
    /**
     * @Type String
     * <p/>
     * 创建类型：电子文稿
     */
    public final static String MSO_CREATE_TYPE_WORD_X = "CREATE_WP_X";
    /**
     * @Type String
     * 创建类型：2003兼容格式演示文稿
     */
    public final static String MSO_CREATE_TYPE_PPT = "CREATE_PG";
    /**
     * @Type String
     * 创建类型：演示文稿
     */
    public final static String MSO_CREATE_TYPE_PPT_X = "CREATE_PG_X";
    /**
     * 审阅用户
     */
    private String reviseUser;
    /**
     * @Type Boolean
     * <p/>
     * 审阅模式
     * 打开文档后控制是否进入审阅模式
     */
    public static boolean MSO_REVISE_MODE = false;
    /**
     * @Type String
     * <p/>
     * 默认审阅用户
     * 不设定将将直接取这个值
     */
    public static final String MSO_REVISE_USER = "WB";

    /**
     * Office审阅工具可见性
     * 0 --- 不显示
     * 非0 --- 显示
     */
    private int msoReviseToolsVisibility;
    /**
     * PDF审阅工具可见性
     * 0 --- 不显示
     * 非0 --- 显示
     */
    private int pdfReviseToolsVisibility = 100;
    /**
     * @Type Integer
     * 在PDF视图中是否显示审阅工具
     * 0 --- 不显示
     * 非0 --- 显示
     */
    public static final String PDF_REVISE_TOOLS = "REVISE_TOOLS";
    /**
     * @Type String
     * 参数: ['ReadMode', 'Normal', 'ReadOnly', 'Signature']
     * ReadMode: 打开直接进入阅读器模式仅Word文档支持
     * Normal: 正常模式
     * READONLY: 只读模式，不可编辑
     * SIGNATURE: 签批模式
     */
    public static final String MSO_DOC_MODE_READ = "ReadMode";
    public static final String MSO_DOC_MODE_NORMAL = "Normal";
    public static final String MSO_DOC_MODE_READONLY = "ReadOnly";
    public static final String MSO_DOC_MODE_SIGNATURE = "Signature";

    /**
     * 文档模式
     */
    private String docMode = MSO_DOC_MODE_NORMAL;
    /**
     * 文档的远程路径
     */
    private String documentRemoteAddress;
    /**
     * 文档的本地路径
     */
    private String documentLocalAddress;
    /**
     * 文档上传的地址
     */
    private String documentUploadAddress;
    /**
     * 文档上传失败后是否重试
     */
    private boolean retryOnUploadFailed = true;
    /**
     * 文档上传失败重试次数
     */
    private int documentUploadFailedRetryTimes = 2;

    /**
     * @Type Integer
     * 下载服务器的文档，并且设置了上传路径，在文档关闭时，文档将被上传到传入的服务器地址。
     * 此参数控制上传失败重试的次数，默认值为3次
     */
    public final static String UPLOAD_FAILED_RETRY = "UPLOAD_FAILED_RETRY";

    /**
     * @Type Integer
     * 上传到服务器失败后控制是否广播该失败事件
     */
    public final static String UPLOAD_MSO_SESSION_BROADCAST = "Event.RH.Document.UPLOAD.MSO";
    public final static String UPLOAD_PDF_SESSION_BROADCAST = "Event.RH.Document.UPLOAD.PDF";

    /**
     * 广播上传失败事件，非0为开启广播
     */
    private int sendBroadcastOnUploadFailed;
    /**
     * @Type Integer
     * 上传到服务器失败后控制是否广播该失败事件
     */
    public final static String UPLOAD_FAILED_BROADCAST = "UPLOAD_FAILED_BROADCAST";
    public final static String UPLOAD_FAILED_BROADCAST_FILTER = "Event.RH.Document.UPLOAD_FAILED";

    /**
     * 广播上传完成事件，非0为开启广播
     */
    private int sendBroadcastOnUploadComplete;
    /**
     * @Type Integer
     * 上传到服务器完成后控制是否广播该事件
     */
    public final static String UPLOAD_COMPLETE_BROADCAST = "UPLOAD_COMPLETE_BROADCAST";
    public final static String UPLOAD_COMPLETE_BROADCAST_FILTER = "Event.RH.Document.UPLOAD_COMPLETE";

    /**
     * @Type Boolean
     * 如果为真，则Back按下时需将文件上传到服务器，如果你设置了上传服务器地址的话。
     * 通常如果该值为假，在关闭文档时（BACK键被按下）文档不会上传到预先设定的服务器上,因为BACK键按下时，
     * 如果文件有改动，且同时选择保存改动，当文件保存完成后，文档将上传到服务器。所有，如果设置该值为真，
     * 那么将可能出现上传两次的情况。
     * fixed-bug： upload twice
     * @see com.ntko.app.office.documents.UploadOptions
     */
    public static boolean DO_UPLOAD_ON_BACK_PRESSED = false;

    /**
     * 使用ONLY_UPLOAD_ON_SAVE代替
     */
    @Deprecated
    public static boolean DO_NOT_UPLOAD_IF_NOT_MODIFIED = true;

    /**
     * @Type Boolean
     * 如果文件以只读方式打开，则不必上传到服务器，如果你设置了上传服务器地址的话。
     */
    public static boolean DO_NOT_UPLOAD_IF_READONLY = true;

    /**
     * 控制文档的上传时机以及上传失败是否重传
     */

    private UploadOptions uploadOptions;

    /**
     * @Type Integer
     * 从服务器下载文档失败后控制是否广播该事件，非0为开启广播
     */
    private int sendBroadcastOnDownloadFailed;
    /**
     * @Type Integer
     * 从服务器下载文档失败后控制是否广播该失败事件
     */
    public final static String DOWNLOAD_FAILED_BROADCAST = "DOWNLOAD_FAILED_BROADCAST";
    public final static String DOWNLOAD_FAILED_BROADCAST_FILTER = "Event.RH.Document.DOWNLOAD_FAILED";
    /**
     * @Type Integer
     * 从服务器下载文档完成后控制是否广播该事件，非0为开启广播
     */
    private int sendBroadcastOnDownloadComplete;
    /**
     * @Type Integer
     * 从服务器下载文档完成后控制是否广播该事件
     */
    public final static String DOWNLOAD_COMPLETE_BROADCAST = "DOWNLOAD_COMPLETE_BROADCAST";
    public final static String DOWNLOAD_COMPLETE_BROADCAST_FILTER = "Event.RH.Document.DOWNLOAD_COMPLETE";

    public final static int DOC_TYPE_PDF = 1;
    public final static int DOC_TYPE_MSO = 2;

    private int docType;

    private SourceType sourceType;

    private CustomFields customFormFields;

    /**
     * 请求来源
     */
    public enum SourceType {
        /**
         * 请求来自本地文件
         */
        LOCAL(0),

        /**
         * 请求来自Web文件
         */
        REMOTE(1),

        /**
         * 创建新文档
         */
        NEW(2);

        int mID;

        SourceType(int id) {
            mID = id;
        }

        public static SourceType createFromId(int id) {
            switch (id) {
                case 0:
                    return LOCAL;
                case 1:
                    return REMOTE;
                default:
                    throw new IllegalArgumentException("invalid Source Type id:" + id);
            }
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    public final static String BUNDLE_NAME = "Bundle";
    public final static String InT_FILTER_PDF_EDIT_LOCAL = "InT_FILTER_PDF_EDIT_LOCAL";
    public final static String InT_FILTER_PDF_EDIT_REMOTE = "InT_FILTER_PDF_EDIT_REMOTE";
    public final static String Per_PDF_SVR_BROADCAST_PERMISSION = "PDF_SVR_BROADCAST_PERMISSION";

    public Params(SourceType type) {
        if (type == null) {
            throw new RuntimeException("invalid Source Type: null");
        }
        sourceType = type;
    }

    public Params sendBroadcastOnDownloadComplete(int flag) {
        sendBroadcastOnDownloadComplete = flag;
        return this;
    }


    public Params sendBroadcastOnDownloadFailed(int flag) {
        this.sendBroadcastOnDownloadFailed = flag;
        return this;
    }

    public Params sendBroadcastOnUploadComplete(int flag) {
        sendBroadcastOnUploadComplete = flag;
        return this;
    }


    public Params sendBroadcastOnUploadFailed(int flag) {
        sendBroadcastOnUploadFailed = flag;
        return this;
    }

    public String getDocumentRemoteAddress() {
        return documentRemoteAddress;
    }

    public void setDocumentRemoteAddress(String documentRemoteAddress) {
        this.documentRemoteAddress = documentRemoteAddress;
    }

    public String getDocumentUploadAddress() {
        return documentUploadAddress;
    }

    public void setDocumentUploadAddress(String documentUploadAddress) {
        this.documentUploadAddress = documentUploadAddress;
    }

    public String getDocumentLocalAddress() {
        return documentLocalAddress;
    }

    public void setDocumentLocalAddress(String documentLocalAddress) {
        this.documentLocalAddress = documentLocalAddress;
    }

    public boolean isRetryOnUploadFailed() {
        return retryOnUploadFailed;
    }

    public void setRetryOnUploadFailed(boolean retryOnUploadFailed) {
        this.retryOnUploadFailed = retryOnUploadFailed;
    }

    public void setDocumentUploadFailedRetryTimes(int times) {
        documentUploadFailedRetryTimes = times;
    }

    public int getDocumentUploadFailedRetryTimes() {
        return documentUploadFailedRetryTimes;
    }

    public UploadOptions getUploadOptions() {
        return uploadOptions;
    }

    public void setUploadOptions(UploadOptions uploadOptions) {
        this.uploadOptions = uploadOptions;
    }

    public String getDocMode() {
        return docMode;
    }

    public void setDocMode(String docMode) {
        this.docMode = docMode;
    }

    /**
     * 仅适用于标准版
     *
     * @return 标准版本文档的工作模式
     */
    public String getStdDocModeStr() {
        // read 阅读模式       --->   Params.MSO_DOC_MODE_READ
        // edit 编辑模式       --->   Params.MSO_DOC_MODE_NORMAL
        // handwrite 手写模式  --->   Params.MSO_DOC_MODE_SIGNATURE

        switch (docMode) {
            case MSO_DOC_MODE_READ:
                return "read";
            case MSO_DOC_MODE_SIGNATURE:
                return "handwrite";
            default:
                return "edit";
        }
    }

    /**
     * 仅适用于高级版
     *
     * @return 高级版本文档的工作模式
     */
    public String getProDocModeStr() {
        return docMode.equals(MSO_DOC_MODE_SIGNATURE) ? MSO_DOC_MODE_NORMAL : docMode;
    }

    public int getPdfReviseToolsVisibility() {
        return pdfReviseToolsVisibility;
    }

    public void setPdfReviseToolsVisibility(int pdfReviseToolsVisibility) {
        this.pdfReviseToolsVisibility = pdfReviseToolsVisibility;
    }

    public int getMsoReviseToolsVisibility() {
        return msoReviseToolsVisibility;
    }

    public void setMsoReviseToolsVisibility(int msoReviseToolsVisibility) {
        this.msoReviseToolsVisibility = msoReviseToolsVisibility;
    }

    public String getReviseUser() {
        return reviseUser;
    }

    public void setReviseUser(String reviseUser) {
        this.reviseUser = reviseUser;
        MSO_REVISE_MODE = true;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public int getDocType() {
        return docType;
    }

    public String getDocTypeName() {
        switch (docType) {
            case DOC_TYPE_MSO:
                return "Microsoft Office Document";
            case DOC_TYPE_PDF:
                return "Acrobat PDF Document";
            default:
                return "unknown";
        }
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public CustomFields getCustomFormFields() {
        return customFormFields;
    }

    public void setCustomFormFields(CustomFields customFormFields) {
        this.customFormFields = customFormFields;
    }

    @Override
    public String toString() {
        return "Params{" +
                "Title='" + documentTitle + '\'' +
                ", FileType=" + fileType +
                ", Type=" + getDocTypeName() +
                ", Mode=" + docMode +
                ", Source=" + sourceType +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.documentTitle);
        dest.writeString(this.fileType);
        dest.writeString(this.createType);
        dest.writeString(this.reviseUser);
        dest.writeInt(this.msoReviseToolsVisibility);
        dest.writeInt(this.pdfReviseToolsVisibility);
        dest.writeString(this.docMode);
        dest.writeString(this.documentRemoteAddress);
        dest.writeString(this.documentLocalAddress);
        dest.writeString(this.documentUploadAddress);
        dest.writeByte(retryOnUploadFailed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.documentUploadFailedRetryTimes);
        dest.writeInt(this.sendBroadcastOnUploadFailed);
        dest.writeInt(this.sendBroadcastOnUploadComplete);
        dest.writeParcelable(this.uploadOptions, 0);
        dest.writeInt(this.sendBroadcastOnDownloadFailed);
        dest.writeInt(this.sendBroadcastOnDownloadComplete);
        dest.writeInt(this.docType);
        dest.writeInt(this.sourceType == null ? -1 : this.sourceType.ordinal());
        dest.writeParcelable(this.customFormFields, 0);
        //static params
        dest.writeString(FILE_TYPE);
        dest.writeInt(MSO_REVISE_MODE ? 1 : 0);
        dest.writeInt(DO_UPLOAD_ON_BACK_PRESSED ? 1 : 0);
    }

    private Params(Parcel in) {
        this.documentTitle = in.readString();
        this.fileType = in.readString();
        this.createType = in.readString();
        this.reviseUser = in.readString();
        this.msoReviseToolsVisibility = in.readInt();
        this.pdfReviseToolsVisibility = in.readInt();
        this.docMode = in.readString();
        this.documentRemoteAddress = in.readString();
        this.documentLocalAddress = in.readString();
        this.documentUploadAddress = in.readString();
        this.retryOnUploadFailed = in.readByte() != 0;
        this.documentUploadFailedRetryTimes = in.readInt();
        this.sendBroadcastOnUploadFailed = in.readInt();
        this.sendBroadcastOnUploadComplete = in.readInt();
        this.uploadOptions = in.readParcelable(UploadOptions.class.getClassLoader());
        this.sendBroadcastOnDownloadFailed = in.readInt();
        this.sendBroadcastOnDownloadComplete = in.readInt();
        this.docType = in.readInt();
        int tmpSourceType = in.readInt();
        this.sourceType = tmpSourceType == -1 ? null : SourceType.values()[tmpSourceType];
        this.customFormFields = in.readParcelable(CustomFields.class.getClassLoader());
        //static params
        Params.FILE_TYPE = in.readString();
        Params.MSO_REVISE_MODE = in.readInt() == 1;
        Params.DO_UPLOAD_ON_BACK_PRESSED = in.readInt() == 1;
    }

    public static final Creator<Params> CREATOR = new Creator<Params>() {
        public Params createFromParcel(Parcel source) {
            return new Params(source);
        }

        public Params[] newArray(int size) {
            return new Params[size];
        }
    };
}
