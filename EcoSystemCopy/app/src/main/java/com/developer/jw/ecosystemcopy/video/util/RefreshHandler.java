package com.developer.jw.ecosystemcopy.video.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by smheo on 9/29/2015.
 */
public class RefreshHandler extends Handler {
    private static final int WHAT = 0; //arbitrary message data

    public interface OnRefreshListener{
        /**
         * Called on an interval when it's time to refresh
         */
        void onRefresh();
    }

    private volatile OnRefreshListener mListener;
    private volatile int mDelay = 100;


    public void setDelay(int delay){
        mDelay = delay;
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }

    public void start(){
        this.removeMessages(WHAT);
        sendMessageDelayed(obtainMessage(WHAT), mDelay);
    }

    public void stop(){
        this.removeMessages(WHAT);
    }

    public boolean isStarted(){
        return hasMessages(WHAT);
    }

    @Override
    public void handleMessage(Message msg) {
        sendMessageDelayed(obtainMessage(WHAT), mDelay);

        if(mListener != null){
            mListener.onRefresh();
        }
    }
}
