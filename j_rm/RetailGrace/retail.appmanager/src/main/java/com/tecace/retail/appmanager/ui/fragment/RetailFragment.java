package com.tecace.retail.appmanager.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.tecace.retail.appmanager.config.environment.EnvironmentManager;
import com.tecace.retail.appmanager.config.environment.Environments;
import com.tecace.retail.appmanager.util.ResourceUtil;

/**
 * Created by icanmobile on 5/31/16.
 */
public class RetailFragment extends Fragment {
    private static final String TAG = RetailFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    protected boolean isCustomAnimator(int nextAnim) {
        if (nextAnim == ResourceUtil.getInstance().getResId("@animator/coded_circle_reveal") ||
            nextAnim == ResourceUtil.getInstance().getResId("@animator/coded_circle_unreveal") )
            return true;
        return false;
    }

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


    protected void setMaxVolume() {
        int getVolume = Byte.parseByte(EnvironmentManager.getInstance().getStringValue(getAppContext(), Environments.MAX_VOLUME_PERCENT));
        if (getVolume < 0) {
            getVolume = 0;
        }
        getVolume %= 100;
        float putVolPercentage = ((float) getVolume) / 100f;

        if (EnvironmentManager.getInstance().getBooleanValue(getAppContext(), Environments.ENABLE_MAX_VOLUME)) {
            AudioManager audioManager = (AudioManager) getAppContext().getSystemService(Context.AUDIO_SERVICE);
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

    protected void vibrate() {
        Vibrator vib = (Vibrator) getAppContext().getSystemService(getAppContext().VIBRATOR_SERVICE);
        if (vib != null)
            vib.vibrate(VIBRATE_DURATION);
    }
}
