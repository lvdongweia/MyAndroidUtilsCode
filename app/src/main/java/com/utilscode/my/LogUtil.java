package com.utilscode.my;

import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
     * Log存储至文件
     */
    private static final boolean LOG_TO_FILE = false;

    private static final String LOG_FILE = "xxxxxx";

    /**
     * Log输出级别
     */
    private static final int DEBUG_LEVEL = Log.VERBOSE;

    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};

    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();

    private static SimpleDateFormat mSimpleDateFormat;

    private static String mPackageName;
    private static String mProcessName;

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

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
        if (isLogToFile()) {
            logToFile(priority, relTag, relMsg);
        }
    }

    private static void logToFile(final int priority, final String tag, final String msg) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                print2File(priority, tag, msg);
            }
        });
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

    private static void print2File(int priority, String tag, String msg) {
        String format = getSdf().format(new Date());
        String date = format.substring(0, 10);
        //String time = format.substring(11);
        final String fullPath = EXTERNAL_STORAGE + "/"
            + getPackageName() + "/"
            + date + "/"
            + LOG_FILE + "_"
            + getProcessName();
        if (!createOrExistsFile(fullPath)) {
            Log.e(TAG, "Create " + fullPath + " failed!");
            return;
        }

        String content = format +
            getProcessName() + "/" +
            getPackageName() + " " +
            T[priority - Log.VERBOSE] + "/" +
            tag + ": " +
            msg +
            LINE_SEP;
        input2File(content, fullPath);
    }

    private static void input2File(final String input, final String filePath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.write(input);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Log to " + filePath + " failed!");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        }

        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }

        try {
            boolean isCreate = file.createNewFile();
            if (isCreate) {

            }
            return isCreate;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean createOrExistsDir(File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static SimpleDateFormat getSdf() {
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.getDefault());
        }
        return mSimpleDateFormat;
    }

    private static String getPackageName() {
        if (mPackageName == null) {
            //mPackageName = MainApplication.getInstance().getPackageName();
        }
        return mPackageName;
    }

    private static String getProcessName() {
        if (mProcessName == null) {
            int pid = Process.myPid();
            mProcessName = String.valueOf(pid);
        }
        return mProcessName;
    }

    private static boolean isDebug(int level) {
        return (level >= DEBUG_LEVEL) && DEBUG;
    }

    private static boolean isLogToFile() {
        return LOG_TO_FILE;
    }
}
