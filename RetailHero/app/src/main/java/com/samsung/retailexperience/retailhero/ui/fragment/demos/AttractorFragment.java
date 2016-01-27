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
import com.samsung.retailexperience.retailhero.view.VideoTextureView;


/**
 * Created by smheo on 1/12/16.
 */
public class AttractorFragment extends BaseFragment {

    private static final String TAG = AttractorFragment.class.getSimpleName();

    protected View mView = null;
    private FragmentModel<VideoModel> mFragmentModel = null;
    private VideoTextureView mAttractorVideo = null;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(mFragmentModel.getLayoutResId(), container, false);

        //set drawer lock/unlock
        setDrawer(mFragmentModel.getDrawerId());

        if (mView != null) {
            mAttractorVideo = (VideoTextureView) mView.findViewById(R.id.attractor_video);
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
            mView.setPivotX(getResources().getInteger(R.integer.animStartOffset));
            mView.setPivotY(getResources().getInteger(R.integer.animYOffset));
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        setMaxVolume();

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
    public void onBackPressed() {
        changeFragment(AppConst.UIState.UI_STATE_NONE,
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

}
