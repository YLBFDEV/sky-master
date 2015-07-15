package com.skytech.android;

public class Logging {
    public static final String LOG_TAG = "com.skytech";

    /**
     * Set this to 'true' to enable as much MOA logging as possible.
     */
    public static final boolean LOGD;

    /**
     * If this is enabled then logging that normally hides sensitive information
     * like passwords will show that information.
     */
    public static final boolean DEBUG_SENSITIVE;

    /**
     * If true, logging regarding UI (such as activity/fragment lifecycle) will be enabled.
     * <p/>
     * TODO rename it to DEBUG_UI.
     */
    public static final boolean DEBUG_LIFECYCLE;
    public static final boolean DEBUG;

    static {
        // Declare values here to avoid dead code warnings; it means we have some extra
        // "if" statements in the byte code that always evaluate to "if (false)"
        DEBUG = LOGD = true; // DO NOT CHECK IN WITH TRUE
        DEBUG_SENSITIVE = true; // DO NOT CHECK IN WITH TRUE
        DEBUG_LIFECYCLE = true; // DO NOT CHECK IN WITH TRUE
    }
}
