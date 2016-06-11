package com.tecace.retail.appmanager.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.tecace.retail.appmanager.iRetailAppService;
import com.tecace.retail.appmanager.iRetailAppServiceCallback;
import com.tecace.retail.appmanager.receiver.ScreenOffReceiver;
import com.tecace.retail.appmanager.util.RetailAppManagerConst;
import com.tecace.retail.appmanager.util.FuncUtil;
import com.tecace.retail.appmanager.util.PreferenceUtil;
import com.tecace.retail.appmanager.util.TopExceptionHandler;

/**
 * Created by icanmobile on 5/31/16.
 */
public class RetailAppService extends Service {
    private static final String TAG = RetailAppService.class.getSimpleName();

    private static RetailAppService sInstance = null;
    public static RetailAppService getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "##### onCreate)+ ");
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(null, null));

        sInstance = this;

        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");
        sInstance = null;
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

    final RemoteCallbackList<iRetailAppServiceCallback> mCallbacks =  new RemoteCallbackList<iRetailAppServiceCallback>();
    private final iRetailAppService.Stub mBinder = new iRetailAppService.Stub() {

        @Override
        public void registerCallback(iRetailAppServiceCallback cb) throws RemoteException {
            if (cb != null)
                mCallbacks.register(cb);
        }

        @Override
        public void unregisterCallback(iRetailAppServiceCallback cb) throws RemoteException {
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


    private void registerScreenOffReceiver() {
        Log.d(TAG, "##### registerScreenOffReceiver)+ ");
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver, filter);


        // checking screen on/off state
        if (!FuncUtil.getInstance().isScreenOn(getApplicationContext())) {
            FuncUtil.getInstance().wakeUpDevice(getApplicationContext());
            FuncUtil.getInstance().startApp(getApplicationContext(),
                    PreferenceUtil.getInstance().getString(getApplicationContext(), RetailAppManagerConst.PREFERENCE_APP_PACKAGE),
                    PreferenceUtil.getInstance().getString(getApplicationContext(), RetailAppManagerConst.PREFERENCE_APP_CLASS));
        }
    }
}
