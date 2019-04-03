package com.utilscode.my;

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
     * Log最大长度
     */
    private static final int LOG_MAX_LEN = 3000;

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
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.VERBOSE, tag, msg);
            } else {
                printMsg(Log.VERBOSE, tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug(Log.DEBUG)) {
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.DEBUG, tag, msg);
            } else {
                printMsg(Log.DEBUG, tag, msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug(Log.INFO)) {
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.INFO, tag, msg);
            } else {
                printMsg(Log.INFO, tag, msg);
            }
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug(Log.WARN)) {
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.WARN, tag, msg);
            } else {
                printMsg(Log.WARN, tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug(Log.ERROR)) {
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.ERROR, tag, msg);
            } else {
                printMsg(Log.ERROR, tag, msg);
            }
        }
    }

    public static void printThrowable(String tag, Throwable tr) {
        if (isDebug(Log.DEBUG)) {
            String msg = Log.getStackTraceString(tr);
            if (msg.length() > LOG_MAX_LEN) {
                printLongMsg(Log.ERROR, tag, msg);
            } else {
                printMsg(Log.ERROR, tag, msg);
            }
        }
    }

    private static void printMsg(int priority, String tag, String msg) {
        String relTag = CLASS_TAG ? tag : TAG;
        String relMsg = CLASS_TAG ? msg : formatMsg(tag, msg);

        Log.println(priority, relTag, relMsg);
    }

    private static void printLongMsg(int priority, String tag, String msg) {
        final int len = msg.length();
        int start = 0;
        int end = LOG_MAX_LEN;

        while (end < len) {
            printMsg(priority, tag, msg.substring(start, end));
            start = end;
            end += LOG_MAX_LEN;
        }

        printMsg(priority, tag, msg.substring(start));
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
