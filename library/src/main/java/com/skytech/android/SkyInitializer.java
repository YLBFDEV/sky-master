package com.skytech.android;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.skytech.R;
import com.skytech.android.androidpn.ServiceManager;
import com.skytech.android.common.StringUtils;
import com.skytech.android.database.DBHelper;
import com.skytech.android.exception.AppException;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.android.model.User;
import com.skytech.android.util.Constant;
import com.skytech.android.util.NetworkUtils;
import com.skytech.android.util.PathUtil;
import com.skytech.android.util.netstate.NetChangeObserver;
import com.skytech.android.util.netstate.NetType;
import com.skytech.android.util.property.PropertyManagement;
import com.skytech.android.util.property.SharedPreferencePropertyManagement;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;

public class SkyInitializer {
    private static SkyInitializer initializer;
    protected Context context;
    private AppManager mAppManager;
    protected PropertyManagement propertyManage;
    private User user;
    /*文件缓存*/
    private static final String SYSTEMCACHE = "moa";
    private ArkActivity currentActivity;
    /*网络状态观察者*/
    private NetChangeObserver netChangeObserver;
    /*网络状态标志位*/
    private Boolean networkAvailable = false;
    private ServiceManager msgPush;
    private String deviceId;
    private ExecutorService executorService;
    private boolean signAuthed;
    /**
     * App异常崩溃处理器
     */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static SkyInitializer getInstance() {
        return initializer;
    }

    public Context getContext() {
        return context;
    }

    public String getPackageName() {
        return context.getPackageName();
    }

    public SkyInitializer(Context context) {
        this.context = context;
        SharedPreferencePropertyManagement.initialize(context);
        propertyManage = SharedPreferencePropertyManagement.getInstance();
        // Global SharedPreferences for Ark Application
        doOnCreate();
        DBHelper.initialize(context, getAppKey() + ".db");
        PathUtil.initialize(context.getPackageName());
        initAppUrl();
        initImageLoader(context);
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public static void initialize(Context context) {
        initializer = new SkyInitializer(context);
    }

    public PropertyManagement getPropertyManagement() {
        return propertyManage;
    }

    private void initMsgPushService() {
        msgPush = new ServiceManager(context);
        msgPush.setNotificationIcon(R.drawable.ic_launcher);
        msgPush.startService();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.discCacheFileNameGenerator(new Md5FileNameGenerator());
        config.discCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getAppUrl() {
        return "";
    }

    public void setAppUrl(String url) {
        propertyManage.setString(SkyHttpClient.BASE_URL, url);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLocalDb() {
        return context.getPackageName() + ".db";
    }

    private void doOnCreate() {
        // 注册App异常崩溃处理器
        //Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
        /*网络状态观察者*/
        netChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetType type) {
                super.onConnect(type);
                SkyInitializer.this.onConnect(type);
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                SkyInitializer.this.onDisConnect();
            }
        };
        user = new User("2", "1232");
    }

    public String getDeviceId() {
        if (StringUtils.isEmpty(deviceId)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();            // MEID 具有电话功能的设备才有
            if (StringUtils.isEmpty(deviceId)) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                //恢复出厂设置后，该值可能会改变
                //对于Android 2.2（“Froyo”）之前的设备不是100％的可靠
            }
            if (StringUtils.isEmpty(deviceId)) {
                if (propertyManage.contains(Constant.DEVICE_ID)) {
                    deviceId = propertyManage.getString(Constant.DEVICE_ID);
                } else {
                    deviceId = (new StringBuilder("EMU")).append((new Random(System.currentTimeMillis())).nextLong()).toString();
                    propertyManage.setString(Constant.DEVICE_ID, deviceId);
                }
            }
            Log.d(Logging.LOG_TAG, "deviceId=" + deviceId);
        }
        return deviceId;
    }

    public String getAppKey() {
        String s[] = context.getPackageName().split("\\.");
        return s[s.length - 1];
    }

    /**
     * 当前没有网络连接
     */

    public void onDisConnect() {
        networkAvailable = false;
        if (currentActivity != null) {
            currentActivity.onDisConnect();
        }
    }

    /**
     * 网络连接连接时调用
     */
    protected void onConnect(NetType type) {
        networkAvailable = true;
        if (currentActivity != null) {
            currentActivity.onConnect(type);
        }
    }

    public String getNetWorkState() {
        return NetworkUtils.getConnTypeName(context);
    }

    public AppManager getAppManager() {
        if (mAppManager == null) {
            mAppManager = AppManager.getAppManager();
        }
        return mAppManager;
    }

    /**
     * 设置 App异常崩溃处理器
     *
     * @param uncaughtExceptionHandler
     */
    public void setUncaughtExceptionHandler(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    private Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = AppException.getInstance(context);
        }
        return uncaughtExceptionHandler;
    }

    public boolean isSignAuthed() {
        return signAuthed;
    }

    public void setSignAuthed(boolean signAuthed) {
        this.signAuthed = signAuthed;
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行,如果为true则为后台运行
     */
    public void exitApp(Boolean isBackground) {
        getAppManager().AppExit(context, isBackground);
    }

    public void onActivityCreating(ArkActivity activity) {

    }

    public void onActivityCreated(ArkActivity activity) {
        currentActivity = activity;
    }

    private void initAppUrl() {
        String config = getServerURL();
        if (!TextUtils.isEmpty(config)) {
            if (config.indexOf("http://") == -1) {
                setAppUrl("http://" + config);
            } else {
                setAppUrl("http://" + config.substring(config.indexOf("http://") + 7));
            }
        }
    }

    private String getServerURL() {
        String fileName = PathUtil.getInstance().getRootDir() + "/url.txt";//文件路径
        String res = "";
        try {
            File f = new File(fileName);
            if (!f.exists()) return "";
            FileInputStream fin = new FileInputStream(fileName);
            // FileInputStream fin = openFileInput(fileName);
            // 用这个就不行了，必须用FileInputStream
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8").trim();////依Y.txt的编码类型选择合适的编码，如果不调整会乱码
            fin.close();//关闭资源
            System.out.println("res--->" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
