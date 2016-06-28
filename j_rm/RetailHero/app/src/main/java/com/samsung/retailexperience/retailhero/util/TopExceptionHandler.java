package com.samsung.retailexperience.retailhero.util;

import android.app.Activity;
import android.util.Log;

/**
 * Created by icanmobile on 1/18/16.
 */
public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = TopExceptionHandler.class.getName();

    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;
    private boolean raizeException = false;

    public TopExceptionHandler(Activity app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
        raizeException = false;
    }

    Thread mThread;
    Throwable mThrowable;
    public void uncaughtException(Thread t, Throwable e)
    {
        if (this.raizeException){
            defaultUEH.uncaughtException(t, e);
            return;
        }
        this.raizeException = true;
        this.mThread = t;
        this.mThrowable = e;

        Log.e(TAG, getExceptionDetail(e));
    }

    public static String getExceptionDetail(Throwable e) {
        String lineSep = System.getProperty("line.separator");

        StackTraceElement[] arr = e.getStackTrace();
        StringBuilder report = new StringBuilder();
        report.append(e.toString()).append(lineSep);
        report.append("********** STACK TRACE **********").append(lineSep);
        for (int i=0; i<arr.length; i++)
        {
            report.append("    ").append(arr[i].toString()).append(lineSep);
        }
        report.append("************* CAUSE ************").append(lineSep);
        Throwable cause = e.getCause();
        if(cause != null) {
            report.append(cause.toString()).append(lineSep);
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++)
            {
                report.append("    ").append(arr[i].toString()).append(lineSep);
            }
        }
        report.append("*********************************").append(lineSep);

        return report.toString();
    }
}
