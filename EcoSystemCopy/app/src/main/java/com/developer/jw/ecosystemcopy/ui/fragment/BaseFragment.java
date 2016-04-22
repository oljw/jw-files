package com.developer.jw.ecosystemcopy.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.developer.jw.ecosystemcopy.ui.activity.MainActivity;
import com.developer.jw.ecosystemcopy.util.AppConst;
import com.developer.jw.ecosystemcopy.video.config.ConfigProvider;
import com.developer.jw.ecosystemcopy.video.config.environment.Environments;
import com.developer.jw.ecosystemcopy.video.config.environment.IEnvironments;
import com.developer.jw.ecosystemcopy.video.config.setting.ISettings;
import com.developer.jw.ecosystemcopy.video.util.ResourceUtil;

/**
 * Created by JW on 2016-04-21.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected View mView = null;

    protected IEnvironments mEnvMgr;
    protected ISettings mSetMgr;
    protected ResourceUtil mResMgr;

    protected Context mAppContext;

    //Volume Setting
    private static final int[] AUDIO_TYPES = {
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_DTMF,
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL
    };

    private static final int VIBRATE_DURATION = 150;

    abstract public void onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
        getGlobalVariables();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*  cause param is used for Analytics
        public void changeFragment(String json, AppConst.TransactionDir dir, FragmentChangeCause cause) {
            try {
                ((MainActivity) getActivity()).changeFragment(json, dir, cause);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    */

    public void changeFragment(String json, AppConst.TransactionDir dir) {
        try {
            ((MainActivity) getActivity()).changeFragment(json, dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View getView() {
        return mView;
    }

    public Context getAppContext() {
        return this.mAppContext;
    }

    private void getGlobalVariables() {
        mEnvMgr = ((ConfigProvider)getActivity().getApplicationContext()).getEnvironmentConfig();
        mSetMgr = ((ConfigProvider)getActivity().getApplicationContext()).getSettingsManager();
        mResMgr = ((ConfigProvider)getActivity().getApplicationContext()).getResourceUtil();
    }

    protected void setMaxVolume() {
        if (mEnvMgr != null) {
            int getVolume = Byte.parseByte(mEnvMgr.getStringValue(Environments.MAX_VOLUME_PERCENT));
            if (getVolume < 0) {
                getVolume = 0;
            }
            getVolume %= 100;
            float putVolPercentage = ((float) getVolume) / 100f;

            if (mEnvMgr.getBooleanValue(Environments.ENABLE_MAX_VOLUME)) {
                AudioManager audioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    for (int streamType : AUDIO_TYPES) {
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            audioManager.adjustStreamVolume(streamType, AudioManager.ADJUST_UNMUTE, 0);
                        } else {
                            audioManager.setStreamMute(streamType, false);
                        }

                        int maxVol = audioManager.getStreamMaxVolume(streamType);
                        maxVol *= putVolPercentage;
                        audioManager.setStreamVolume(streamType, maxVol, 0);
                    }
                } else {
                    Log.e(TAG, "Audiomanager is null");
                }
            }
        }
    }

    protected void vibrate() {
        Vibrator vib = (Vibrator) mAppContext.getSystemService(mAppContext.VIBRATOR_SERVICE);
        if (vib != null) {
            vib.vibrate(VIBRATE_DURATION);
        }
    }
}
