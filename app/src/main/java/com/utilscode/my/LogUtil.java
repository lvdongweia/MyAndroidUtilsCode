package com.utilscode.my;

import android.content.Context;
import android.util.Log;


/**
 * 格式化Log输出
 *
 * @author Lvdw
 * @date 2018/11/28
 */
public class LogUtil {
    private static final String TAG = "MyDemoApp";

    /**
     * Log开关
     */
    private static final boolean DEBUG = true;

    /**
     * 输出TAG为传过来的tag，或是总的tag
     */
    private static final boolean CLASS_TAG = false;

    /**
     * Log输出级别
     */
    private static final int DEBUG_LEVEL = Log.VERBOSE;

    public static void v(String tag, String msg) {
        if (isDebug(Log.VERBOSE)) {
            if (CLASS_TAG) {
                Log.v(tag, msg);
            } else {
                Log.v(TAG, formatMsg(tag, msg));
            }
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug(Log.VERBOSE)) {
            if (CLASS_TAG) {
                Log.d(tag, msg);
            } else {
                Log.d(TAG, formatMsg(tag, msg));
            }
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug(Log.VERBOSE)) {
            if (CLASS_TAG) {
                Log.i(tag, msg);
            } else {
                Log.i(TAG, formatMsg(tag, msg));
            }
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug(Log.VERBOSE)) {
            if (CLASS_TAG) {
                Log.w(tag, msg);
            } else {
                Log.w(TAG, formatMsg(tag, msg));
            }
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug(Log.VERBOSE)) {
            if (CLASS_TAG) {
                Log.e(tag, msg);
            } else {
                Log.e(TAG, formatMsg(tag, msg));
            }
        }
    }

    public static void printThrowable(String tag, Throwable tr) {
        if (isDebug(Log.ERROR)) {
            if (CLASS_TAG) {
                Log.e(tag, Log.getStackTraceString(tr));
            } else {
                Log.e(TAG, formatMsg(tag, Log.getStackTraceString(tr)));
            }
        }
    }

    private static String formatMsg(String tag, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(tag).append("]: ").append(msg);
        return sb.toString();
    }

    private static boolean isDebug(int level) {
        return (level >= DEBUG_LEVEL) && DEBUG;
    }

}
