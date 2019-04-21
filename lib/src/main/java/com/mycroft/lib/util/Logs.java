package com.mycroft.lib.util;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by Mycroft_Wong on 2015/12/30.
 */
public final class Logs {
    private Logs() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    private static boolean sIsLogEnabled = true;

    private static String sApplicationTag = "mycroft";

    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     */
    public static void v(String msg) {
        if (sIsLogEnabled) {
            LogUtils.v(msg);
        }
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg
     */
    public static void d(String msg) {
        if (sIsLogEnabled) {
            LogUtils.d(msg);
        }
    }

    /**
     * Send an INFO log message.
     *
     * @param msg
     */
    public static void i(String msg) {
        if (sIsLogEnabled) {
            LogUtils.i(msg);
        }
    }

    /**
     * Send a WARN log message.
     *
     * @param msg
     */
    public static void w(String msg) {
        if (sIsLogEnabled) {
            LogUtils.w(msg);
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg
     */
    public static void e(String msg) {
        if (sIsLogEnabled) {
            LogUtils.e(msg);
        }
    }

    /**
     * Send an ERROR log message.
     *
     * @param exception
     */
    public static void e(Exception exception) {
        if (sIsLogEnabled) {
            LogUtils.e(exception);
        }
    }

    public static boolean issIsLogEnabled() {
        return sIsLogEnabled;
    }

    /**
     * Set if the Logs print log or not
     *
     * @param sIsLogEnabled
     */
    public static void setsIsLogEnabled(boolean sIsLogEnabled) {
        Logs.sIsLogEnabled = sIsLogEnabled;
    }

    public static String getsApplicationTag() {
        return sApplicationTag;
    }

    public static void setsApplicationTag(String sApplicationTag) {
        Logs.sApplicationTag = sApplicationTag;
    }

}
