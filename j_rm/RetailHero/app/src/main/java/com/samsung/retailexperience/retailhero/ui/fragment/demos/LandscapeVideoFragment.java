package com.samsung.retailexperience.retailhero.ui.fragment.demos;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

/**
 * Created by smheo on 1/16/2016.
 */
public class LandscapeVideoFragment extends BaseVideoFragment {

    private static final String TAG = DefaultVideoFragment.class.getSimpleName();

    private FrameLayout mRotationContainer = null;
    private ImageView mRotationDeviceIv = null;
    private TimerHandler mTimerHandler = null;

    // for the screen orientation
    private OrientationEventListener mOrientationEventListener = null;

    public static LandscapeVideoFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        LandscapeVideoFragment fragment = new LandscapeVideoFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        // image view for the screen rotation
        mRotationContainer = (FrameLayout) view.findViewById(R.id.rotate_container);
        mRotationDeviceIv = (ImageView) view.findViewById(R.id.rotate_device);

        mTimerHandler = new TimerHandler();
        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    // disable the listener for orientation
                    if (mOrientationEventListener != null) {
                        mOrientationEventListener.disable();
                    }

                    mainVideoPlay();
                }
            });
        }

        mOrientationEventListener = new OrientationEventListener(getActivity().getApplicationContext(),
                SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged (int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return;

                int degrees = 270;
                if (orientation < 45 || orientation > 315) degrees=0;
                else if (orientation < 135) degrees = 90;
                else if (orientation < 225) degrees = 180;

                if (degrees == 90) {
                    Log.i(TAG, "@@@ LandVideo : Phone orientation changed to " + degrees);

                    // disable the listener for orientation
                    if (mOrientationEventListener != null) {
                        mOrientationEventListener.disable();
                    }

                    // start timer - for starting video play
                    if (mTimerHandler != null) {
                        mTimerHandler.start(250);
                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVideoView != null) {
            mVideoView.setVisibility(View.GONE);
        }

        if (mRotationContainer != null) {
            mRotationContainer.setVisibility(View.VISIBLE);
        }

        setRotateAnimation(true);

        if (mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }

        if (mTimerHandler != null) {
            mTimerHandler.start(AppConsts.TIMEOUT_SIX_SECOND);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mVideoView != null) {
            mVideoView.setVisibility(View.INVISIBLE);
        }

        setRotateAnimation(false);

        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }

    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        if (mRotationDeviceIv != null) {
            mRotationDeviceIv = null;
        }

        if (mRotationContainer != null) {
            mRotationContainer = null;
        }

        if (mOrientationEventListener != null) {
            mOrientationEventListener = null;
        }

        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(null);
            mTimerHandler = null;
        }
    }

    private void mainVideoPlay() {

        if (mVideoView != null) {
            mVideoView.setVisibility(View.VISIBLE);
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

        if (mRotationContainer != null) {
            mRotationContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void setRotateAnimation(boolean onOff) {
        if (onOff == true) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotator);
            if (rotateAnimation != null) {
                if (mRotationDeviceIv != null) {
                    mRotationDeviceIv.startAnimation(rotateAnimation);
                }
            }
        } else {
            if (mRotationDeviceIv != null) {
                mRotationDeviceIv.clearAnimation();
            }
            if (mRotationContainer != null) {
                mRotationContainer.setVisibility(View.GONE);
            }
        }
    }
}
