package jw.developer.com.camera2project;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import jw.developer.com.camera2project.environment.EnvironmentManager;
import jw.developer.com.camera2project.environment.Environments;
import jw.developer.com.camera2project.util.AppConst;

//import com.tecace.retail.appmanager.config.environment.IEnvironments;
//import com.tecace.retail.appmanager.config.setting.ISettings;

/**
 * Created by icanmobile on 3/1/16.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected View mView = null;

    protected AppConst.TransactionDir mTransactionDir = null;
//    protected ArrayList<Integer> mNoAnimChildViewIDs = new ArrayList<Integer>(Arrays.asList(R.id.back_button, R.id.image_bg));

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

//    public void changeFragment(String json, AppConst.TransactionDir dir) {
//        try {
//            ((MainActivity) getActivity()).changeFragment(json, dir);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public View getView() {
        return mView;
    }

    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    protected AppConst.TransactionDir getTransactionDir() {
        return this.mTransactionDir;
    }
    protected boolean getTransactionDirBoolean() {
        switch (this.mTransactionDir) {
            case TRANSACTION_DIR_FORWARD:
                return true;
            case TRANSACTION_DIR_BACKWARD:
                return false;
        }
        return false;
    }

//    protected boolean isCustomAnimator(int nextAnim) {
//        switch (nextAnim) {
//            case R.animator.coded_circle_reveal:
//            case R.animator.coded_circle_unreveal:
//                return true;
//        }
//        return false;
//    }


    private void getGlobalVariables() {
    }

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
