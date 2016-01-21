package com.samsung.retailexperience.retailhero.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.samsung.retailexperience.retailhero.iHeroService;
import com.samsung.retailexperience.retailhero.iHeroServiceCallback;
import com.samsung.retailexperience.retailhero.receiver.UIControlReceiver;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TopExceptionHandler;
import com.samsung.retailexperience.retailhero.view.TriggerMessageView;

/**
 * Created by icanmobile on 1/18/16.
 */
public class HeroService extends Service {
    private static final String TAG = HeroService.class.getSimpleName();

    private static HeroService sInstance = null;
    private TriggerMessageView mTriggerMessageView = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "##### onCreate)+ ");
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(null));

        sInstance = this;

        UIControlReceiver.setService(sInstance);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        setTriggerMessage(false, null, null);

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        try {
            if (intent != null && intent.getExtras() != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {

                }
            }
        } catch (Exception e) {
            Log.e(TAG, TopExceptionHandler.getExceptionDetail(e));
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    final RemoteCallbackList<iHeroServiceCallback> mCallbacks =  new RemoteCallbackList<iHeroServiceCallback>();
    private final iHeroService.Stub mBinder = new iHeroService.Stub() {

        @Override
        public void registerCallback(iHeroServiceCallback cb) throws RemoteException {
            if (cb != null)
                mCallbacks.register(cb);
        }

        @Override
        public void unregisterCallback(iHeroServiceCallback cb) throws RemoteException {
            if (cb != null)
                mCallbacks.unregister(cb);
        }

        //sample function
        @Override
        public void requestServiceFunc() throws RemoteException {
            sendRequestFunc(mHandler, 0);
        }
    };

    /*
     * Receiver's callback functions
     */
    public void setForegroundApp(String appName) {
        Log.d(TAG, "##### setForegroundApp)+ appName = " + appName);

        if (appName.equals("com.sec.android.app.camera/.Camera")) {
            String message = new StringBuilder()
                    .append("Trigger Message: ")
                    .append(appName)
                    .toString();
            setTriggerMessage(true, message, AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name());
        }
        else {
            setTriggerMessage(false, null, null);
        }
    }
    private void setTriggerMessage(boolean bVisible, String message, String triggerUIState) {
        if (bVisible) {
            if (mTriggerMessageView == null)
                mTriggerMessageView = new TriggerMessageView(sInstance, message, triggerUIState);
            else {
                mTriggerMessageView.setMessage(message);
                mTriggerMessageView.setTriggerUIState(triggerUIState);
            }
        }
        else {
            if (mTriggerMessageView != null) {
                mTriggerMessageView.destory();
                mTriggerMessageView = null;
            }
        }
    }

    public void changeFragment(AppConst.UIState newState) {
        startActivity(newState);
    }

    private void startActivity(AppConst.UIState newState) {
        Intent intent = new Intent(sInstance, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppConsts.ARG_NEXT_FRAGMENT, newState.name());
        startActivity(intent);
    }


    /*
     * Handler for Service
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //sample function
                case MSG_REQUEST_FUNC:
                    {
                        final int N = mCallbacks.beginBroadcast();
                        for (int i=0; i<N; i++) {
                            try {
                                mCallbacks.getBroadcastItem(i)
                                        .responseServiceFunc();
                            } catch (RemoteException e) {

                            }
                        }
                        mCallbacks.finishBroadcast();
                    }
                    break;
            }
        }
    };

    //sample function
    private static final int MSG_REQUEST_FUNC = 1;
    public void sendRequestFunc(Handler handler, int delayTime) {
        if (handler == null) return;
        handler.sendMessageDelayed(handler.obtainMessage(MSG_REQUEST_FUNC), delayTime);
    }
}
