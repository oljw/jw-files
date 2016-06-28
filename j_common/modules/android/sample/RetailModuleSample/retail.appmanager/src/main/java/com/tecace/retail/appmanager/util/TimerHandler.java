package com.tecace.retail.appmanager.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by smheo on 9/29/2015.
 */
public class TimerHandler extends Handler {

    /**
     * arbitrary message what see {@link #obtainMessage(int) obtainMessage} method
     */
    private static final int WHAT = 1004;

    public interface OnTimeoutListener{
        void onTimeout();
    }

    private volatile OnTimeoutListener listener;
    private volatile int timeout = 1000;

    /**
     * Sets the timeout for this timer
     * @param timeout - the timeout (none ms)
     */
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }

    /**
     * Sets the callback for when this timer times out
     * @param listener
     */
    public void setOnTimeoutListener(OnTimeoutListener listener){
        this.listener = listener;
    }

    /**
     * Start the timer
     * @param timeout - the timeout (none ms)
     */
    public void start(int timeout){
        this.timeout = timeout;
        start();
    }

    /**
     * Start the timer
     */
    public void start(){
        removeMessages(WHAT);
        sendMessageDelayed(obtainMessage(WHAT), timeout);
    }

    /**
     * Stop the timer
     */
    public void stop(){
        removeMessages(WHAT);
    }

    @Override
    public void handleMessage(Message msg) {
        if(listener != null){
            listener.onTimeout();
        }
    }
}
