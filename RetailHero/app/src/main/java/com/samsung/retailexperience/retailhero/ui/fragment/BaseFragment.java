package com.samsung.retailexperience.retailhero.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.samsung.retailexperience.retailhero.config.ConfigProvider;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.config.environment.IEnvironments;
import com.samsung.retailexperience.retailhero.config.setting.ISettings;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;

/**
 * Created by icanmobile on 1/12/16.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

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
        onSetDrawer();
    }

    @Override
    public void onResume() {
        super.onResume();
        setMaxVolume();
    }

    private void getGlobalVariables() {
        mEnvMgr = ((ConfigProvider)getActivity().getApplicationContext()).getEnvironmentConfig();
        mSetMgr = ((ConfigProvider) getActivity().getApplicationContext()).getSettingsManager();
        mResMgr = ((ConfigProvider)getActivity().getApplicationContext()).getResourceUtil();

        Log.i(TAG, "Fragment Target : " + mEnvMgr.getStringValue(Environments.FLAVOR));
    }


    public void changeFragment(AppConst.UIState newState, AppConsts.TransactionDir dir) {
        ((MainActivity)getActivity()).changeFragment(newState, dir);
    }

    public void changeEndDemoFragment(AppConst.UIState backFragment, AppConsts.TransactionDir dir) {
        ((MainActivity)getActivity()).changeEndDemoFragment(backFragment, dir);
    }

    public abstract void onSetDrawer();
    public void setDrawer(int drawerId) {
        ((MainActivity)getActivity()).setDrawer(drawerId);
    }

    public void clickDrawerBtn() {
        ((MainActivity)getActivity()).clickDrawerBtn();
    }

    protected void setMaxVolume() {
        if (mEnvMgr != null) {
            int getVolume = Byte.parseByte(mEnvMgr.getStringValue(Environments.MAX_VOLUME_PERCENT));
            if (getVolume < 0) {
                getVolume = 0;
            }
            getVolume %= 100;
            float putVolPercentage = ((float) getVolume) / 100f;
            Log.d(TAG, "Volume : " + getVolume + "% (" + putVolPercentage + ")");

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