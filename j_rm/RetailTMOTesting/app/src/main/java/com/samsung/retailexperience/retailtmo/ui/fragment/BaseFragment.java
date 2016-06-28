package com.samsung.retailexperience.retailtmo.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailtmo.analytics.FragmentChangeCause;
import com.samsung.retailexperience.retailtmo.ui.activity.MainActivity;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.video.config.ConfigProvider;
import com.samsung.retailexperience.retailtmo.video.config.environment.Environments;
import com.samsung.retailexperience.retailtmo.video.config.environment.IEnvironments;
import com.samsung.retailexperience.retailtmo.video.config.setting.ISettings;
import com.samsung.retailexperience.retailtmo.video.util.ResourceUtil;

/**
 * Created by icanmobile on 3/1/16.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected View mView = null;

    protected IEnvironments mEnvMgr;
    protected ISettings mSetMgr;
    protected ResourceUtil mResMgr;

    protected Context mAppContext;

    // volume setting
    private static final int[] AUDIO_TYPES = {
            AudioManager.STREAM_ALARM,
            AudioManager.STREAM_DTMF,
            AudioManager.STREAM_MUSIC,
            AudioManager.STREAM_NOTIFICATION,
            AudioManager.STREAM_RING,
            AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL};

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

    public void changeFragment(String json, AppConst.TransactionDir dir, FragmentChangeCause cause) {
        try {
            ((MainActivity) getActivity()).changeFragment(json, dir, cause);
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
        mSetMgr = ((ConfigProvider) getActivity().getApplicationContext()).getSettingsManager();
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
                    Log.e(TAG, "AudioManager is null");
                }
            }
        }
    }

    protected void vibrate() {
        Vibrator vib = (Vibrator) mAppContext.getSystemService(mAppContext.VIBRATOR_SERVICE);
        if (vib != null)
            vib.vibrate(VIBRATE_DURATION);
    }
}
