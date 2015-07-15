package com.ntko.app.office.documents;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ntko.app.office.documents.support.OfficeSupportCompatV1;
import org.apache.http.Header;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xjiaoyang on 2015/1/20.
 * 不再建议使用该类
 */
@SuppressWarnings({"deprecation", "UnusedDeclaration"})
public class MobileOfficeConnector {
    private static final String TAG = MobileOfficeConnector.class.getName() + " pid::" + android.os.Process.myPid();
    protected static Activity context;

    public final static String OFFICE_DEFAULT_REVISE_USERNAME = Params.MSO_REVISE_USER;//默认痕迹用户名
    public final static String CUSTOM_FORM_FIELDS = Params.EDIT_FILE_FORM_FIELDS;//自定义表单
    public final static String OPS_PARAMS = Params.EDIT_FILE_PARAMS;
    public final static String ACTION_NAME = Params.ACTION_NAME;
    public static final String CONFIG_DOWNLOAD = Params.ACTION_DOWNLOAD;
    public static final String CONFIG_CREATE = Params.ACTION_CREATE;
    public static final String CONFIG_OPEN = Params.ACTION_OPEN;
    public static final String DOWNLOAD_URL = Params.EDIT_FILE_DOWNLOAD_PATH;
    public static final String UPLOAD_URL = Params.EDIT_FILE_UPLOAD_PATH;
    public static final String LOCAL_PATH = Params.EDIT_FILE_PATH;
    public static final String FILE_NAME = Params.EDIT_FILE_NAME;

    @Deprecated
    public static final int MSG_OFFICE_OPEN_LOCAL = 1731;
    @Deprecated
    public static final int MSG_OFFICE_OPEN_REMOTE = 1732;
    @Deprecated
    public static final int MSG_OFFICE_CREATE_NEW = 1733;
    @Deprecated
    public static final String ACT_OFFICE_QUERY = "com.ntko.app.office.action.QUERY";
    @Deprecated
    public static final String ACT_OFFICE_CONFIRMED = "com.ntko.app.office.action.CONFIRMED";
    @Deprecated
    public static final String ACT_OFFICE_CREATE_NEW = "com.ntko.app.office.action.CREATE_NEW";
    @Deprecated
    public static final String ACT_OFFICE_OPEN_LOCAL = "com.ntko.app.office.action.OPEN_LOCAL";
    @Deprecated
    public static final String ACT_OFFICE_OPEN_REMOTE = "com.ntko.app.office.action.OPEN_REMOTE";
    @Deprecated
    public static final int MSG_UNREGISTER_OFFICE_CLIENT = 1734;
    @Deprecated
    public static final int MSG_REGISTER_OFFICE_CLIENT = 1375;
    @Deprecated
    protected final static String InT_FILTER_CREATE = "OFFICE_OPS_CREATE";
    @Deprecated
    protected final static String InT_FILTER_OPEN_DOC_RMT = "OFFICE_OPS_OPEN_DOC_1";
    @Deprecated
    protected final static String InT_FILTER_OPEN_DOC_LCL = "OFFICE_OPS_OPEN_DOC_2";
    @Deprecated
    protected final static String InT_FILTER_OPEN_PDF_RMT = "OFFICE_OPS_OPEN_PDF_1";
    @Deprecated
    protected final static String InT_FILTER_OPEN_PDF_LCL = "OFFICE_OPS_OPEN_PDF_2";
    @Deprecated
    public final static String InT_FILTER_FIELDS_SETTER = "GLOBAL_FIELDS_SETTER";
    @Deprecated
    public final static String InT_FILTER_FIELDS_CLEAR = "GLOBAL_FIELDS_CLEAR";
    @Deprecated
    public final static String InT_FILTER_D_CTX_UPLOAD = "MSO_UPLOAD_EXT_CTX";
    @Deprecated
    public final static String InT_FILTER_MSO_DOC_OPENED = "MSO_DOC_OPENED";
    @Deprecated
    public final static String InT_FILTER_MSO_DOC_CLOSED = "MSO_DOC_CLOSED";

    protected final static OfficeOpsBroadcastReceiver _OfficeOpsBroadReceiver = new OfficeOpsBroadcastReceiver();
    protected final static IntentFilter _OfficeOpsIntentFilter = new IntentFilter();

    protected final static IntentFilter _PDFOpsIntentFilter = new IntentFilter();
    protected final static PDFOpsBroadcastReceiver _PdfOpsBroadReceiver = new PDFOpsBroadcastReceiver();

    protected final static IntentFilter _FieldsSetterIntentFilter = new IntentFilter();
    protected final static CustomFieldsSetterBroadcastReceiver _FieldsSetterBroadReceiver = new CustomFieldsSetterBroadcastReceiver();

    protected final static UseCtxDeterminedOnUploadingReceiver _UseCtxDeterminedOnUploadingReceiver = new UseCtxDeterminedOnUploadingReceiver();
    protected final static IntentFilter _UseCtxDeterminedOnUploadingFilter = new IntentFilter();

    protected final static _OfficeOps _officeOps = new _OfficeOps();
    protected final static _OfficeOps.V2ServiceConnector _officeConnV2 = new _OfficeOps.V2ServiceConnector();
    protected final static _PdfConnector _pdfConn = new _PdfConnector();
    protected final static _PdfConnector.V2ServiceConnector _pdfConnV2 = new _PdfConnector.V2ServiceConnector();

    final static CustomFields fields = new CustomFields();

    protected static OfficeSupportCompatV1.OfficeCompatCode _OfficeCompatCode = OfficeSupportCompatV1.OfficeCompatCode.PDF_STANDALONE;

    public static void connect(Activity activity) {
        if (activity != null) {
            context = activity;
            _officeConnV2.doBindService();
            _pdfConn.doBindService();
            _pdfConnV2.doBindService();

            _OfficeOpsIntentFilter.addAction(InT_FILTER_CREATE);
            _OfficeOpsIntentFilter.addAction(InT_FILTER_OPEN_DOC_RMT);
            _OfficeOpsIntentFilter.addAction(InT_FILTER_OPEN_DOC_LCL);
            activity.registerReceiver(_OfficeOpsBroadReceiver, _OfficeOpsIntentFilter);

            _PDFOpsIntentFilter.addAction(InT_FILTER_OPEN_PDF_RMT);
            _PDFOpsIntentFilter.addAction(InT_FILTER_OPEN_PDF_LCL);
            activity.registerReceiver(_PdfOpsBroadReceiver, _PDFOpsIntentFilter);

            _FieldsSetterIntentFilter.addAction(InT_FILTER_FIELDS_SETTER);
            _FieldsSetterIntentFilter.addAction(InT_FILTER_FIELDS_CLEAR);
            activity.registerReceiver(_FieldsSetterBroadReceiver, _FieldsSetterIntentFilter);

            _UseCtxDeterminedOnUploadingFilter.addAction(Params.UPLOAD_MSO_SESSION_BROADCAST);
            _UseCtxDeterminedOnUploadingFilter.addAction(Params.UPLOAD_PDF_SESSION_BROADCAST);
            activity.registerReceiver(_UseCtxDeterminedOnUploadingReceiver, _UseCtxDeterminedOnUploadingFilter);
        } else {
            throw new RuntimeException("Context is Null!");
        }
    }

    public static void destroy(Activity activity) {
        if (activity != null) {
            _officeConnV2.doUnbindService();
            _pdfConn.doUnbindService();
            _pdfConnV2.doUnbindService();
            activity.unregisterReceiver(_OfficeOpsBroadReceiver);
            activity.unregisterReceiver(_PdfOpsBroadReceiver);
            activity.unregisterReceiver(_FieldsSetterBroadReceiver);
            activity.unregisterReceiver(_UseCtxDeterminedOnUploadingReceiver);
        } else {
            throw new RuntimeException("Context is Null!");
        }
    }

    /**
     *
     */
    protected static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return
     */
    public static boolean useExplicitIntent() {
        return context.getApplicationInfo().targetSdkVersion >= 22;
    }

    protected static class OfficeOpsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case InT_FILTER_CREATE:
                    _officeOps.createNew(
                            // String createType,
                            intent.getStringExtra(_OfficeOps.KEY_CREATE_NAME),
                            // String uploadURL,
                            intent.getStringExtra(UPLOAD_URL),
                            // Parcelable params,
                            intent.getParcelableExtra(OPS_PARAMS)
                    );
                    break;
                case InT_FILTER_OPEN_DOC_RMT:
                    _officeOps.openFromURL(
                            // String fileName,
                            intent.getStringExtra(FILE_NAME),
                            //String downloadURL
                            intent.getStringExtra(DOWNLOAD_URL),
                            // String uploadURL,
                            intent.getStringExtra(UPLOAD_URL),
                            // Parcelable params,
                            intent.getParcelableExtra(OPS_PARAMS)
                    );
                    break;
                case InT_FILTER_OPEN_DOC_LCL:
                    _officeOps.openLocalFile(
                            //String downloadURL
                            intent.getStringExtra(LOCAL_PATH),
                            // String uploadURL,
                            intent.getStringExtra(UPLOAD_URL),
                            // Parcelable params,
                            intent.getParcelableExtra(OPS_PARAMS)
                    );
                    break;
                default:
                    Toast.makeText(context, "无效的参数!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    protected static class PDFOpsBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case InT_FILTER_OPEN_PDF_RMT:
                    _pdfConn.openFromURL(
                            // String fileName,
                            intent.getStringExtra(FILE_NAME),
                            //String downloadURL
                            intent.getStringExtra(DOWNLOAD_URL),
                            // String uploadURL,
                            intent.getStringExtra(UPLOAD_URL),
                            // Parcelable params,
                            intent.getParcelableExtra(OPS_PARAMS)
                    );
                    break;
                case InT_FILTER_OPEN_PDF_LCL:
                    _pdfConn.openLocalFile(
                            // String localPath,
                            intent.getStringExtra(LOCAL_PATH),
                            //String downloadURL
                            intent.getStringExtra(DOWNLOAD_URL),
                            // Parcelable params,
                            intent.getParcelableExtra(_OfficeOps.KEY_OPS_PARAMS));
                    break;
                default:
                    Toast.makeText(context, "无效的参数!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * 如果指定了上传文档时使用客户调用程序的Context
     */
    protected static class UseCtxDeterminedOnUploadingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle props = intent.getExtras();
            props.setClassLoader(Params.class.getClassLoader());
            String action = intent.getAction();
            if (Params.UPLOAD_MSO_SESSION_BROADCAST.equals(action)) {
                Log.d(TAG, "上传文档到远程路径，类别：常规文档");
                sendUpload(props);
            } else if (Params.UPLOAD_PDF_SESSION_BROADCAST.equals(action)) {
                Log.d(TAG, "上传文档到远程路径，类别：PDF文档");
                sendUpload(props);
            }
        }

        void sendUpload(final Bundle props) {
            getUploadSession().doUpload(props.<Params>getParcelable(Params.EDIT_FILE_PARAMS));
        }
    }

    protected static class CustomFieldsSetterBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case InT_FILTER_FIELDS_SETTER:
                    intent.setExtrasClassLoader(CustomFields.class.getClassLoader());
                    Serializable serializable = intent.getSerializableExtra(CUSTOM_FORM_FIELDS);
                    if (serializable != null) {
                        CustomFields customFields = (CustomFields) serializable;
                        if (!customFields.fieldsList.isEmpty()) {
                            fields.fieldsList.addAll(customFields.fieldsList);
                        }
                    }
                    break;
                case InT_FILTER_FIELDS_CLEAR:
                    fields.fieldsList.clear();
                    break;
                default:
                    Toast.makeText(context, "无效的参数!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public final static class _OfficeOps {

        private static final String TAG = _OfficeOps.class.getName() + " pid::" + android.os.Process.myPid();
        /**
         * 绑定文档服务，intent-filter建议使用‘RHDocumentService’，
         * ‘RH_OFFICE_PROXY’在2.0版本中不再生效，将在后面的版本中移除
         */
        @Deprecated
        public static final String COMPONENT_MSO_INTENT_ACTION = "RH_OFFICE_PROXY";
        /**
         * 将在后期版本中移除，请勿使用
         */
        @Deprecated
        public static final String COMPONENT_MSO_PROXY_NAME = "com.ntko.app.main.OfficeProxyActivity";
        public static final String COMPONENT_MSO_PACKAGE_NAME = "com.ntko.app.office";
        public static final String KEY_CONFIG_NAME = "OFFICE_CONFIGS";
        public static final String KEY_ACTION_NAME = ACTION_NAME;
        public static final String KEY_CREATE_NAME = "CREATE_TYPE";
        public static final String TYPE_CONFIG_CREATE = CONFIG_CREATE;
        public static final String TYPE_CONFIG_DOWNLOAD = CONFIG_DOWNLOAD;
        public static final String TYPE_CONFIG_OPEN = CONFIG_OPEN;
        public static final String KEY_DOWNLOAD_URL = DOWNLOAD_URL;
        public static final String KEY_UPLOAD_URL = UPLOAD_URL;
        public static final String KEY_LOCAL_PATH = LOCAL_PATH;
        public static final String KEY_FILE_NAME = FILE_NAME;
        public static final String KEY_OPS_PARAMS = OPS_PARAMS;
        public static final String KEY_CUSTOM_FORM_FIELDS = CUSTOM_FORM_FIELDS;

        /**
         * 创建新文档
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void createNew(String createType, Params params) {
            createNew(createType, null, params);
        }

        /**
         * 创建新文档，并保存到服务器
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void createNew(final String createType, final String uploadURL, final Parcelable params) {
            Log.v(TAG, "本地新建:" + createType + "," + uploadURL);

            if (createType == null || createType.trim().equals("")) {
                Toast.makeText(context, "类型无效!", Toast.LENGTH_SHORT).show();
                return;
            }
            //
            if (params instanceof Params) {
                ((Params) params).setDocType(Params.DOC_TYPE_MSO);
                ((Params) params).setSourceType(Params.SourceType.NEW);
                ((Params) params).setCreateType(createType);
                ((Params) params).setCustomFormFields(fields);
                ((Params) params).setDocumentUploadAddress(uploadURL);
            }
            //发送到服务端
            _officeConnV2.sendMessageToService(Params.OBTAIN_CREATE, mkNBundle(createType, uploadURL, params));
        }

        private static Bundle mkNBundle(String createType, String uploadURL, Parcelable params) {
            final String type = createType.toUpperCase();
            if (//2003兼容格式
                    type.equals(Params.MSO_CREATE_TYPE_SHEET)
                            || type.equals(Params.MSO_CREATE_TYPE_WORD)
                            || type.equals(Params.MSO_CREATE_TYPE_PPT)
                            //2007以上版本格式
                            || type.equals(Params.MSO_CREATE_TYPE_SHEET_X)
                            || type.equals(Params.MSO_CREATE_TYPE_WORD_X)
                            || type.equals(Params.MSO_CREATE_TYPE_PPT_X)) {
                Bundle configs = new Bundle();
                //设置此次操作为创建文档
                configs.putString(KEY_ACTION_NAME, TYPE_CONFIG_CREATE);
                //创建文档的类型
                configs.putString(KEY_CREATE_NAME, type);
                //上传文档的服务器地址
                configs.putString(KEY_UPLOAD_URL, uploadURL);
                //相关参数
                configs.putParcelable(KEY_OPS_PARAMS, params);
                //如果保存到服务器时要设置自定义表单字段
                configs.putParcelable(CUSTOM_FORM_FIELDS, fields);
                return configs;
            }

            throw new RuntimeException("无效的文档类型:" + createType);
        }

        /**
         * 打开本地文档, 不上传
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void openLocalFile(String localPath, Parcelable params) {
            if (params instanceof Params) {
                openLocalFile(localPath, ((Params) params).getDocumentUploadAddress(), params);
            } else {
                Log.e(TAG, "invalid params!");
            }
        }

        /**
         * 打开本地文档, 保存后上传
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void openLocalFile(final String localPath, final String uploadURL, final Parcelable params) {
            Log.v(TAG, "打开本地文档:" + localPath + "," + "," + uploadURL);

            if (params instanceof Params) {
                ((Params) params).setDocType(Params.DOC_TYPE_MSO);
                ((Params) params).setSourceType(Params.SourceType.LOCAL);
                ((Params) params).setCustomFormFields(fields);
                ((Params) params).setDocumentLocalAddress(localPath);
                ((Params) params).setDocumentUploadAddress(uploadURL);
            }

            //发送到服务端
            _officeConnV2.sendMessageToService(Params.OBTAIN_EDIT_LOCAL_DOC, mkLBundle(localPath, uploadURL, params));
        }

        private static Bundle mkLBundle(String localPath, String uploadURL, Parcelable params) {

            Bundle configs = new Bundle();
            //设置此次操作为打开本地文档
            configs.putString(KEY_ACTION_NAME, TYPE_CONFIG_OPEN);
            //本地文档地址
            configs.putString(KEY_LOCAL_PATH, localPath);
            //上传文档的服务器地址
            configs.putString(KEY_UPLOAD_URL, uploadURL);
            //相关参数
            configs.putParcelable(KEY_OPS_PARAMS, params);
            //如果保存到服务器时要设置自定义表单字段
            configs.putParcelable(CUSTOM_FORM_FIELDS, fields);
            return configs;
        }

        /**
         * 从服务器打开文档, 不上传
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void openFromURL(String fileName, String downloadURL, Parcelable params) {
            if (params instanceof Params) {
                openFromURL(fileName, downloadURL, ((Params) params).getDocumentUploadAddress(), params);
            } else {
                Log.e(TAG, "invalid params!");
            }
        }

        /**
         * 从服务器打开文档，并将文档保存回去
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        @Deprecated
        public void openFromURL(final String fileName, final String downloadURL, final String uploadURL, final Parcelable params) {
            Log.v(TAG, "从服务器打开:" + fileName + "," + downloadURL + "," + uploadURL);
            if (params instanceof Params) {
                ((Params) params).setDocType(Params.DOC_TYPE_MSO);
                ((Params) params).setSourceType(Params.SourceType.REMOTE);
                ((Params) params).setDocumentTitle(fileName);
                ((Params) params).setCustomFormFields(fields);
                ((Params) params).setDocumentRemoteAddress(downloadURL);
                ((Params) params).setDocumentUploadAddress(uploadURL);
            }
            //发送到服务端
            _officeConnV2.sendMessageToService(Params.OBTAIN_EDIT_REMOTE_DOC, mkRBundle(fileName, downloadURL, uploadURL, params));
        }

        private static Bundle mkRBundle(String fileName, String downloadURL, String uploadURL, Parcelable params) {
            Bundle configs = new Bundle();
            //设置此次操作为下载服务器文档
            configs.putString(KEY_ACTION_NAME, TYPE_CONFIG_DOWNLOAD);
            //下载文档的服务器地址
            configs.putString(KEY_FILE_NAME, fileName);
            //下载文档的服务器地址
            configs.putString(KEY_DOWNLOAD_URL, downloadURL);
            //上传文档的服务器地址
            configs.putString(KEY_UPLOAD_URL, uploadURL);
            //相关参数
            configs.putParcelable(KEY_OPS_PARAMS, params);
            //如果保存到服务器时要设置自定义表单字段
            configs.putParcelable(CUSTOM_FORM_FIELDS, fields);
            return configs;
        }

        /* ***************************************************************************** */
        // V2 Methods
        // 编辑文档
        /* ********* */
        public void edit(Params params) {
            _officeConnV2.edit(params);
        }

        static class V2ServiceConnector implements ServiceConnection {
            public static String INTENT_BIND_OFFICE_QUERY_V2 = null;
            private boolean mIsBoundV2;
            private Messenger mServiceMessengerV2 = null;
            private ServiceConnection mConnectionV2 = this;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServiceMessengerV2 = new Messenger(service);
                Log.i(TAG, "Office文档服务(V2)已经连接！");
                mIsBoundV2 = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
                mServiceMessengerV2 = null;
                mIsBoundV2 = false;
                Log.i(TAG, "文档服务已经断开连接！");
                // re-bind for un-installation problem
                doBindService();
            }

            /**
             * Bind this Activity to Service
             */
            private void doBindService() {
                if (mIsBoundV2) {
                    return;
                }
                boolean explicit = useExplicitIntent();
                String className;
                switch (_OfficeCompatCode) {
                    case PDF_STANDALONE:
                        Log.e(TAG, "尚未授权移动编辑.");
                        return;
                    case OFFICE_PRO:
                        className = "WPSProService";
                        INTENT_BIND_OFFICE_QUERY_V2 = explicit ?
                                "com.ntko.app.office.service.WPSProService" : Params.SVR_OFFICE_PRO;
                        break;
                    case OFFICE_STD:
                        className = "YzStdService";
                        INTENT_BIND_OFFICE_QUERY_V2 = explicit ?
                                "com.ntko.app.office.service.YzStdService" : Params.SVR_OFFICE_STD;
                        break;
                    default:
                        throw new RuntimeException("当绑定到文档服务时取得了错误的软件代码:" + _OfficeCompatCode);
                }

                Intent intent = new Intent(INTENT_BIND_OFFICE_QUERY_V2);
                if (explicit) {
                    intent.setClassName(
                            "com.ntko.app.office.service",
                            "com.ntko.app.office.service." + className
                    );
                }

                context.bindService(
                        intent,
                        mConnectionV2,
                        Context.BIND_AUTO_CREATE
                );
            }

            /**
             * Un-bind this Activity to Service
             */
            private void doUnbindService() {
                if (mIsBoundV2) {
                    try {
                        // Detach our existing connection.
                        context.unbindService(mConnectionV2);
                        mIsBoundV2 = false;
                    } catch (Exception ignore) {
                        //or not registered?
                    }
                }
            }

            /**
             * Send data to the service
             *
             * @param data The data to send
             */
            private void sendMessageToService(int messageType, Bundle data) {
                Log.i(TAG, "处理Office文档消息(" + _OfficeCompatCode + ")：" + data);
                //发送消息到文档服务
                if (mIsBoundV2) {
                    if (mServiceMessengerV2 != null) {
                        try {
                            Message msg = Message.obtain(null, messageType, 0, 0);
                            msg.setData(data);
                            mServiceMessengerV2.send(msg);
                            Log.i(TAG, "已发送消息到文档服务(V2)");
                        } catch (RemoteException e) {
                            Toast.makeText(context, "发送消息到文档服务(V2)出现异常！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }

            /* ***************************************************************************** */
            // V2 Methods
            // 编辑文档
            /* ********* */
            public void edit(Params params) {
                Log.i(TAG, "编辑Office文档：" + params);
                if (params.getDocType() == Params.DOC_TYPE_MSO) {

                    //自定义表单域
                    if (fields.size() > 0) {
                        if (params.getCustomFormFields() != null) {
                            params.getCustomFormFields()
                                    .getFieldsList()
                                    .addAll(fields.getFieldsList());
                        } else {
                            params.setCustomFormFields(fields);
                        }
                    }

                    switch (params.getSourceType()) {
                        case REMOTE:
                            _officeConnV2.sendMessageToService(Params.OBTAIN_EDIT_REMOTE_DOC, mkRBundle(params.getDocumentTitle(), params.getDocumentRemoteAddress(), params.getDocumentUploadAddress(), params));
                            break;
                        case LOCAL:
                            _officeConnV2.sendMessageToService(Params.OBTAIN_EDIT_LOCAL_DOC, mkLBundle(params.getDocumentLocalAddress(), params.getDocumentUploadAddress(), params));
                            break;
                        case NEW:
                            _officeConnV2.sendMessageToService(Params.OBTAIN_CREATE, mkNBundle(params.getCreateType(), params.getDocumentUploadAddress(), params));
                            break;
                    }
                } else {
                    Log.e(TAG, "无效的文件类型！");
                }
            }
        }
    }

    public final static class _PdfConnector implements ServiceConnection {

        private static final String TAG = _PdfConnector.class.getName() + " pid::" + android.os.Process.myPid();

        public static final String INTENT_BIND_PDF_QUERY = "RH.PDF_SVC_V1";//"com.ntko.app.office.service.PDFDelegateService";//"RH.PDF_SVC_V1";
        public static final String INTENT_BIND_PDF_QUERY_V2 = "RH.PDF_SVC_V2";//"com.ntko.app.office.service.PDFDocService";//"RH.PDF_SVC_V2";
        public static final String COMPONENT_PDF_SERVICE_NAME = "com.ntko.pdf.service.PDFDelegateService";
        public static final int MSG_REGISTER_CLIENT = 1220;
        public static final int MSG_UNREGISTER_CLIENT = 1221;
        public static final int MSG_REF_OPEN_LOCAL = 1230;
        public static final int MSG_REF_OPEN_REMOTE = 1231;
        public static final int SERVER_MSG_RUNTIME_EXCEPTION = 1240;
        public static final int SERVER_MSG_INVALID_FILE_PATH = 1241;

        public static final String KEY_DOWNLOAD_URL = DOWNLOAD_URL;
        public static final String KEY_UPLOAD_URL = UPLOAD_URL;
        public static final String KEY_ACTION_NAME = ACTION_NAME;
        public static final String KEY_LOCAL_PATH = LOCAL_PATH;
        public static final String KEY_FILE_NAME = FILE_NAME;
        public static final String KEY_OPS_PARAMS = OPS_PARAMS;
        public static final String CUSTOM_FORM_FIELDS = MobileOfficeConnector.CUSTOM_FORM_FIELDS;

        private boolean mIsBound;
        private Messenger mServiceMessenger = null;
        private ServiceConnection mConnection = this;

        /**
         * 编辑本地pdf文档
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        public void openLocalFile(String localPath, String uploadURL, Parcelable params) {
            Log.v(TAG, "打开本地文档：" + localPath);
            Log.v(TAG, "应用设定参数：" + params);
            Log.v(TAG, "指定上传地址：" + uploadURL);

            if (params == null) {
                throw new RuntimeException("invalid params:null");
            }

            //V2 适用
            if (params instanceof Params) {
                ((Params) params).setCustomFormFields(fields);
            }

            //发送到文档服务
            sendMessageToService(MSG_REF_OPEN_LOCAL, mkLBundle((Params) params));
        }

        static Bundle mkLBundle(Params params) {
            Bundle bundle = new Bundle();
            //设置此次操作为打开本地文档
            bundle.putString(KEY_ACTION_NAME, CONFIG_OPEN);
            //必须设定文件名称，否则内部自动将取消下载进程
            bundle.putString(KEY_FILE_NAME, params.getDocumentTitle());
            //本地文档地址
            bundle.putString(KEY_LOCAL_PATH, params.getDocumentLocalAddress());
            //文档上传地址, 如果设置将保存到服务器
            bundle.putString(KEY_UPLOAD_URL, params.getDocumentUploadAddress());
            //相关参数
            bundle.setClassLoader(Params.class.getClassLoader());
            bundle.putParcelable(OPS_PARAMS, params);
            //如果保存到服务器时要设置自定义表单字段
            bundle.putParcelable(CUSTOM_FORM_FIELDS, fields);
            return bundle;
        }

        /**
         * 打开服务器文档并保存回服务器
         * 不建议直接使用该方法,请使用 {@link #edit}edit(Params params)
         */
        public void openFromURL(String fileName, String downloadURL, String uploadURL, Parcelable params) {
            Log.v(TAG, "文档名称：" + fileName);
            Log.v(TAG, "应用设定参数：" + params);
            Log.v(TAG, "指定下载地址：" + downloadURL);
            Log.v(TAG, "指定上传地址：" + uploadURL);

            if (params == null) {
                throw new RuntimeException("invalid params:null");
            }

            //V2 适用
            if (params instanceof Params) {
                ((Params) params).setCustomFormFields(fields);
            }
            //发送到文档服务
            sendMessageToService(MSG_REF_OPEN_REMOTE, mkRBundle((Params) params));
        }

        static Bundle mkRBundle(Params params) {
            Bundle bundle = new Bundle();
            //设置此次操作为打开服务器文档
            bundle.putString(KEY_ACTION_NAME, CONFIG_DOWNLOAD);
            //必须设定文件名称，否则内部自动将取消下载进程
            bundle.putString(KEY_FILE_NAME, params.getDocumentTitle());
            //文档下载地址
            bundle.putString(KEY_DOWNLOAD_URL, params.getDocumentRemoteAddress());
            //文档上传地址, 如果设置将保存到服务器
            bundle.putString(KEY_UPLOAD_URL, params.getDocumentUploadAddress());
            //如果保存到服务器时要设置自定义表单字段
            bundle.putParcelable(CUSTOM_FORM_FIELDS, fields);
            //相关参数
            bundle.setClassLoader(Params.class.getClassLoader());
            bundle.putParcelable(OPS_PARAMS, params);
            return bundle;
        }

        public void edit(Params params) {
            _pdfConnV2.edit(params);
        }

        /**
         * Bind this Activity to MyService
         */
        private void doBindService() {
            if (mIsBound) {
                return;
            }

            boolean explicit = useExplicitIntent();
            Intent intent = new Intent(explicit ?
                    "com.ntko.app.office.service.PDFDelegateService" :
                    INTENT_BIND_PDF_QUERY
            );

            if (explicit) {
                intent.setClassName(
                        "com.ntko.app.office.service",
                        "com.ntko.app.office.service.PDFDelegateService"
                );
            }

            context.bindService(
                    intent,
                    mConnection,
                    Context.BIND_AUTO_CREATE
            );
            mIsBound = true;
        }

        /**
         * Un-bind this Activity to MyService
         */
        private void doUnbindService() {
            if (mIsBound) {
                try {
                    // Detach our existing connection.
                    context.unbindService(mConnection);
                    mIsBound = false;
                } catch (Exception ignore) {
                    //or not registered?
                } finally {
                    //important to let the service know that client is leaving!
                    sendMessageToService(MSG_UNREGISTER_CLIENT, null);
                }
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            try {
                Message msg = Message.obtain(null, MSG_REGISTER_CLIENT);
                mServiceMessenger.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
                Log.i(TAG, "文档服务连接异常:" + e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mServiceMessenger = null;
            Log.i(TAG, "文档服务已经断开连接！");
            // re-bind for un-installation problem
            doBindService();
        }

        /**
         * Send data to the service
         *
         * @param data The data to send
         */
        private void sendMessageToService(int messageType, Bundle data) {
            if (mIsBound) {
                if (mServiceMessenger != null) {
                    try {
                        Message msg = Message.obtain(null, messageType, 0, 0);
                        msg.setData(data);
                        mServiceMessenger.send(msg);
                    } catch (RemoteException e) {
                        Toast.makeText(context, "发送消息到文档服务出现异常！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }

        static class V2ServiceConnector implements ServiceConnection {
            private boolean mIsBoundV2;
            private Messenger mServiceMessengerV2 = null;
            private ServiceConnection mConnectionV2 = this;

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mServiceMessengerV2 = new Messenger(service);
                mIsBoundV2 = true;
                Log.i(TAG, "PDF文档服务(V2)已经连接！");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
                mServiceMessengerV2 = null;
                mIsBoundV2 = false;
                Log.i(TAG, "文档服务已经断开连接！");
                // re-bind for un-installation problem
                doBindService();
            }

            /**
             * Bind this Activity to Service
             */
            private void doBindService() {
                if (mIsBoundV2) {
                    return;
                }

                boolean explicit = useExplicitIntent();
                Intent intent = new Intent(explicit ?
                        "com.ntko.app.office.service.PDFDocService" :
                        INTENT_BIND_PDF_QUERY_V2
                );

                if (explicit) {
                    intent.setClassName(
                            "com.ntko.app.office.service",
                            "com.ntko.app.office.service.PDFDocService"
                    );
                }

                context.bindService(
                        intent,
                        mConnectionV2,
                        Context.BIND_AUTO_CREATE
                );
                mIsBoundV2 = true;
            }

            /**
             * Un-bind this Activity to Service
             */
            private void doUnbindService() {
                if (mIsBoundV2) {
                    try {
                        // Detach our existing connection.
                        context.unbindService(mConnectionV2);
                        mIsBoundV2 = false;
                    } catch (Exception ignore) {
                        //or not registered?
                    } finally {
                        //important to let the service know that client is leaving!
                        sendMessageToService(MSG_UNREGISTER_CLIENT, null);
                    }
                }
            }

            /**
             * Send data to the service
             *
             * @param data The data to send
             */
            private void sendMessageToService(int messageType, Bundle data) {
                if (mIsBoundV2) {
                    if (mServiceMessengerV2 != null) {
                        try {
                            Message msg = Message.obtain(null, messageType, 0, 0);
                            msg.setData(data);
                            mServiceMessengerV2.send(msg);
                        } catch (RemoteException e) {
                            Toast.makeText(context, "发送消息到文档服务(V2)出现异常！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }

            /* ***************************************************************************** */
            // V2 Methods
            // 编辑PDF文档
            /* ********* */
            public void edit(Params params) {
                Log.i(TAG, "编辑PDF文档：" + params);
                if (params.getDocType() == Params.DOC_TYPE_PDF) {

                    //自定义表单域
                    if (fields.size() > 0) {
                        if (params.getCustomFormFields() != null) {
                            params.getCustomFormFields()
                                    .getFieldsList()
                                    .addAll(fields.getFieldsList());
                        } else {
                            params.setCustomFormFields(fields);
                        }
                    }

                    Bundle config;
                    switch (params.getSourceType()) {
                        case REMOTE:
                            config = mkRBundle(params);
                            sendMessageToService(Params.OBTAIN_EDIT_REMOTE_PDF, config);
                            break;
                        case LOCAL:
                            config = mkLBundle(params);
                            sendMessageToService(Params.OBTAIN_EDIT_PDF, config);
                            break;
                    }
                } else {
                    Log.e(TAG, "无效的文件类型！");
                }
            }
        }
    }

    /**
     * ***************************************************************
     */
    static UploadSession getUploadSession() {
        return new UploadSession();
    }

    protected final static class UploadSession {
        private AsyncHttpClient client = new AsyncHttpClient();
        protected Params lastSendToUpload;
        private AtomicInteger deletionFailedRetry = new AtomicInteger(0);
        private AtomicInteger lastSendToUploadRetry = new AtomicInteger(0);

        private void doUpload(final Params props) {

            lastSendToUpload = props;

            final String fileName = props.getDocumentTitle();
            final String filePath = props.getDocumentLocalAddress();
            final String uploadUrl = props.getDocumentUploadAddress();
            final String downloadUrl = props.getDocumentRemoteAddress();
            final CustomFields fields = props.getCustomFormFields();
            final boolean retryIfFailed = props.isRetryOnUploadFailed();
            final int countFailed = lastSendToUploadRetry.get();

            Log.v(TAG, "启动文件上传:\n 文件名称 ---> " + fileName +
                            "\n文件路径 --> " + filePath +
                            "\n上传地址 --> " + uploadUrl +
                            "\n文件来源 --> " + downloadUrl +
                            "\n重传设定 --> " + (retryIfFailed ? "打开" : "关闭") +
                            "\n失败统计 --> " + countFailed
            );

            File source = new File(filePath);
            if ("".equals(filePath.trim()) || !source.exists()) {
                Log.e(TAG, "上传已终止 ---> 文件不存在");
                Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }

            if (uploadUrl == null
                    || "".equals(uploadUrl.trim())
                    || "null".equals(uploadUrl.trim())) {
                Log.e(TAG, "上传已终止 ---> 上传路径没有指定");
                return;
            }


            RequestParams params = new RequestParams();

            params.put("fileName", fileName);
            params.put("documentName", fileName);
            params.put("documentFrom", downloadUrl);

            File myFile = new File(filePath);
            try {
                params.put("fileUpload", myFile);
            } catch (FileNotFoundException e) {
                Log.w(TAG, "无法找到要上传的文件 ---> " + e.getMessage());
                Toast.makeText(context, "要上传的文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }

            //add custom fields
            if (fields != null) {
                for (CustomFieldKeyPair keyPair : fields.fieldsList) {
                    Log.v(TAG, "加入自定义表单字段 ---> " + keyPair);
                    params.put(keyPair.getKey(), keyPair.getValue());
                }
            }

            Log.v(TAG, "开始上传...");
            Toast.makeText(context, "正在保存到远端(第" + (countFailed + 1) + "尝试)...", Toast.LENGTH_SHORT).show();
            client.post(context, uploadUrl, params, new TextHttpResponseHandler() {

                /**
                 * Called when request fails
                 *
                 * @param statusCode     http response status line
                 * @param headers        response headers if any
                 * @param responseString string response of given charset
                 * @param throwable      throwable returned when processing request
                 */
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG, "文件上传失败：(status: " + statusCode + ", retry: " + retryIfFailed + ", echo: " + responseString + ")");
                    if (retryIfFailed) {
                        if (countFailed < lastSendToUpload.getDocumentUploadFailedRetryTimes()) {
                            Log.i(TAG, "重新启动上传...");
                            lastSendToUploadRetry.getAndIncrement();
                            doUpload(lastSendToUpload);
                        } else {
                            onUploadFailed();
                            Toast.makeText(context, "文件[" + fileName + "]上传失败", Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                        }
                    }
                }

                /**
                 * Called when request succeeds
                 *
                 * @param statusCode     http response status line
                 * @param headers        response headers if any
                 * @param responseString string response of given charset
                 */
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.i(TAG, "文件上传完成：(状态码: " + statusCode + ", 消息: " + responseString + ")");

                    if (200 != statusCode) {
                        gotError(statusCode);
                    } else {
                        message("文件上传完成");
                    }
                }
            });
        }

        protected void message(final String msg) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }

        protected void onUploadFailed() {
            new AlertDialog.Builder(context)
                    .setMessage("文件上传失败,是否重新上传?")
                    .setCancelable(false)
                    .setNeutralButton("保存到本地", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveCopy();
                        }
                    }).setPositiveButton("重传", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lastSendToUploadRetry.set(0);
                    doUpload(lastSendToUpload);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    askIfCleanDirty();
                }
            }).show();
        }

        private void saveCopy() {
            File backupFolder = Environment.getExternalStorageDirectory();
            if (backupFolder == null || !backupFolder.exists()) {
                message("备份文件夹无法读取，请检查权限是否被第三方应用禁止!");
            } else {
                try {
                    File file = new File(lastSendToUpload.getDocumentLocalAddress());
                    File backup = new File(backupFolder, file.getName());
                    copyTo(file, backup);
                    message("文件已经备份至:" + backup.getAbsolutePath());

                } catch (IOException e) {
                    message("备份文件失败!");
                }
            }
        }

        private void askIfCleanDirty() {
            new AlertDialog.Builder(context)
                    .setMessage("是否删除本地副本?")
                    .setCancelable(false)
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            message("本地副本已删除");
                            try {
                                delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("保留副本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveCopy();
                        }
                    })
                    .show();
        }

        private void gotError(int code) {
            new AlertDialog.Builder(context)
                    .setMessage("上传文件已经完成，但是服务器返回了无效的状态码: " + code)
                    .setCancelable(false)
                    .setPositiveButton("取消并删除副本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            message("本地副本已删除");
                            try {
                                delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNeutralButton("重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doUpload(lastSendToUpload);
                        }
                    })
                    .setNegativeButton("保留副本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveCopy();
                        }
                    })
                    .show();
        }

        void delete() throws IOException {
            if (lastSendToUpload != null
                    && lastSendToUpload.getDocumentLocalAddress() != null
                    && lastSendToUpload.getDocumentLocalAddress().trim().length() > 0) {
                final File source = new File(lastSendToUpload.getDocumentLocalAddress());
                if (source.exists() && !source.isDirectory()) {
                    if (!source.delete()) {
                        postDelayedDeletion(source.getAbsolutePath());
                    }
                }
            }
        }

        private void postDelayedDeletion(final String absoluteFile) {
            final Deletion deletion = new Deletion(absoluteFile);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        int deletionCount = deletionFailedRetry.getAndIncrement();
                        if (!deletion.call() && deletionCount < 10) {
                            postDelayedDeletion(absoluteFile);
                        } else {
                            deletionFailedRetry.set(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        deletionFailedRetry.set(0);
                    }
                }
            }, 200);
        }

        void copyTo(File s, File t) throws IOException {

            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            try {
                fi = new FileInputStream(s);
                fo = new FileOutputStream(t);
                in = fi.getChannel();
                out = fo.getChannel();
                in.transferTo(0, in.size(), out);
            } finally {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            }

        }

        class Deletion implements Callable<Boolean> {
            private String toDelete;

            Deletion(String toDelete) {
                this.toDelete = toDelete;
            }

            @Override
            public Boolean call() throws IOException {
                return new File(toDelete).delete();
            }
        }
    }

    /**
     * ***************************************************************
     */
    public static class NewThread {
        public static void apply(final Runnable v) {
            new Thread(v).start();
        }
    }
}
