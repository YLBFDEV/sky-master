package com.skytech.android.util.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description ArkLogger 是框架的日志打印类
 * <p/>
 * Created by yikai on 2014/11/19.
 */
public class ArkLogger {
    /**
     * Priority constant for the println method; use TALogger.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use TALogger.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use TALogger.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use TALogger.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use TALogger.e.
     */
    public static final int ERROR = 6;
    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    private static HashMap<String, ILogger> loggerHashMap = new HashMap<String, ILogger>();
    private static final ILogger defaultLogger = new PrintToLogCatLogger();

    public static void addLogger(ILogger logger) {
        String loggerName = logger.getClass().getName();
        String defaultLoggerName = defaultLogger.getClass().getName();
        if (!loggerHashMap.containsKey(loggerName)
                && !defaultLoggerName.equalsIgnoreCase(loggerName)) {
            logger.open();
            loggerHashMap.put(loggerName, logger);
        }

    }

    public static void removeLogger(ILogger logger) {
        String loggerName = logger.getClass().getName();
        if (loggerHashMap.containsKey(loggerName)) {
            logger.close();
            loggerHashMap.remove(loggerName);
        }

    }

    public static void d(Object object, String message) {

        printLogger(DEBUG, object, message);

    }

    public static void e(Object object, String message) {

        printLogger(ERROR, object, message);

    }

    public static void i(Object object, String message) {

        printLogger(INFO, object, message);

    }

    public static void v(Object object, String message) {

        printLogger(VERBOSE, object, message);

    }

    public static void w(Object object, String message) {

        printLogger(WARN, object, message);

    }

    public static void d(String tag, String message) {

        printLogger(DEBUG, tag, message);

    }

    public static void e(String tag, String message, Throwable tr) {

        printLogger(ERROR, tag, message, tr);

    }

    public static void i(String tag, String message) {

        printLogger(INFO, tag, message);

    }

    public static void v(String tag, String message) {

        printLogger(VERBOSE, tag, message);

    }

    public static void w(String tag, String message) {

        printLogger(WARN, tag, message);

    }

    public static void println(int priority, String tag, String message) {
        printLogger(priority, tag, message);
    }

    private static void printLogger(int priority, Object object, String message) {
        Class<?> cls = object.getClass();
        String tag = cls.getName();
        String arrays[] = tag.split("\\.");
        tag = arrays[arrays.length - 1];
        printLogger(priority, tag, message, null);
    }

    private static void printLogger(int priority, String tag, String message, Throwable tr) {
        if (LoggerConfig.DEBUG) {
            printLogger(defaultLogger, priority, tag, message, tr);
            Iterator<Map.Entry<String, ILogger>> iter = loggerHashMap.entrySet()
                    .iterator();
            while (iter.hasNext()) {
                Map.Entry<String, ILogger> entry = iter.next();
                ILogger logger = entry.getValue();
                if (logger != null) {
                    printLogger(logger, priority, tag, message, tr);
                }
            }
        }
    }

    private static void printLogger(ILogger logger, int priority, String tag, String message, Throwable tr) {
        switch (priority) {
            case VERBOSE:
                logger.v(tag, message);
                break;
            case DEBUG:
                logger.d(tag, message);
                break;
            case INFO:
                logger.i(tag, message);
                break;
            case WARN:
                logger.w(tag, message);
                break;
            case ERROR:
                logger.e(tag, message, tr);
                break;
            default:
                break;
        }
    }
}
