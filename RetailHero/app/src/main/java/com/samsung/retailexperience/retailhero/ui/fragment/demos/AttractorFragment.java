package com.samsung.retailexperience.retailhero.ui.fragment.demos;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.VideoTextureView;


/**
 * Created by smheo on 1/12/16.
 */
public class AttractorFragment extends BaseFragment {

    private static final String TAG = AttractorFragment.class.getSimpleName();

    private static final int ATTLOOP_BRIGHTNESS_LEVEL_FIRST_PHASE_TIMER = 420000;   //7 Minutes
    private static final int ATTLOOP_BRIGHTNESS_LEVEL_SECOND_PHASE_TIMER = 120000;  //2 Minutes

    private FragmentModel<VideoModel> mFragmentModel = null;
    private VideoTextureView mAttractorVideo = null;

    private TimerHandler mBrightnessTimer;
    private boolean mFirstTimeout = true;

    public static AttractorFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AttractorFragment fragment = new AttractorFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    public AttractorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentModel = (FragmentModel<VideoModel>) getArguments().getSerializable(AppConsts.ARG_FRAGMENT_MODEL);
        }

        mBrightnessTimer = new TimerHandler();
        if(mBrightnessTimer != null){
            mBrightnessTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    if (mFirstTimeout) {
                        // Set Brightness Level to 70%, after 2 minutes, Brightness level will be 60%.
                        setBrightness(0.7f);
                        mFirstTimeout = false;
                        mBrightnessTimer.setTimeout(ATTLOOP_BRIGHTNESS_LEVEL_SECOND_PHASE_TIMER);
                        mBrightnessTimer.start();
                    } else {
                        // After 9 minutes from the start, set Brightness Level to 60%
                        setBrightness(0.6f);
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set drawer lock/unlock
        setDrawer(mFragmentModel.getDrawerId());

        if (view != null) {
            mAttractorVideo = (VideoTextureView) view.findViewById(R.id.attractor_video);
            if (mAttractorVideo != null) {

                // sets contents files
                if (mFragmentModel.getFragment().getVideoFile() != null) {
                    mAttractorVideo.setVideoFile(mFragmentModel.getFragment().getVideoFile());
                }
                if (mFragmentModel.getFragment().getFrameFile() != null) {
                    mAttractorVideo.setVideoFrameFile(mFragmentModel.getFragment().getFrameFile());
                }

                mAttractorVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeFragment(AppConst.UIState.UI_STATE_DECISION,
                                AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                    }
                });
            }

            //for scale up and down transition
            view.setPivotX(getResources().getInteger(R.integer.animStartOffset));
            view.setPivotY(getResources().getInteger(R.integer.animYOffset));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMaxVolume();

        if(mBrightnessTimer != null) {
            // Set Brightness Level to 80%, after 7 minutes, Brightness level will be 70%.
            setBrightness(0.8f);
            mFirstTimeout = true;
            mBrightnessTimer.setTimeout(ATTLOOP_BRIGHTNESS_LEVEL_FIRST_PHASE_TIMER);
            mBrightnessTimer.start();
        }

        if (mAttractorVideo != null) {
            try {
                mAttractorVideo.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set Brightness Level to 100%
        setBrightness(1.0f);

        if(mBrightnessTimer != null) {
            mBrightnessTimer.stop();
        }

        if (mAttractorVideo != null) {
            mAttractorVideo.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBrightnessTimer != null) {
            mBrightnessTimer.setOnTimeoutListener(null);
            mBrightnessTimer = null;
        }

        if (mAttractorVideo != null) {
            mAttractorVideo.setOnClickListener(null);
            mAttractorVideo = null;
        }

    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.UI_STATE_NONE,
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

}
