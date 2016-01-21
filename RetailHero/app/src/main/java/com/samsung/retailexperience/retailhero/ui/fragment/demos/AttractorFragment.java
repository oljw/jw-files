package com.samsung.retailexperience.retailhero.ui.fragment.demos;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.VideoTextureView;


/**
 * Created by smheo on 1/12/16.
 */
public class AttractorFragment extends BaseFragment {

    private static final String TAG = AttractorFragment.class.getSimpleName();

    private VideoTextureView mAttractorVideo;

    public AttractorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attractor, container, false);
        if (view != null) {
            mAttractorVideo = (VideoTextureView) view.findViewById(R.id.attractor_video);
            if (mAttractorVideo != null) {
                mAttractorVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeFragment(AppConst.UIState.UI_STATE_DECISION,
                                AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                    }
                });
            }
        }

        //for scale up and down transition
        view.setPivotX(getResources().getInteger(R.integer.animStartOffset));
        view.setPivotY(getResources().getInteger(R.integer.animYOffset));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        if (mAttractorVideo != null) {
            mAttractorVideo.release();
        }
    }

    @Override
    public void onSetDrawer() {
        setDrawer(0);
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.UI_STATE_NONE,
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

}
