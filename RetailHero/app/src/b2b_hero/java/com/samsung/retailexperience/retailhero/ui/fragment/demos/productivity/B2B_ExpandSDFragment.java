package com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_ExpandSDFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = B2B_ExpandSDFragment.class.getSimpleName();

    // TODO confirm
    private static final int CHAPTER_0_TAP_TO_DOWNLOAD_FILE_INTERACTION = 0;   // 21.5 sec
    private static final int CHAPTER_1_TAP_TO_DOWNLOAD_FILE_VIDEO = 1;   // 47.5 sec

    private View mDownloadFile;
    private View mDownloadFileClickableArea;
    private int mChapterIndex;


    public static B2B_ExpandSDFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_ExpandSDFragment fragment = new B2B_ExpandSDFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mDownloadFile = view.findViewById(R.id.sd_tap_to_download_container);
        mDownloadFileClickableArea = view.findViewById(R.id.sd_tap_to_download_clickable);

        mDownloadFileClickableArea.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        mDownloadFile.setVisibility(View.GONE);
        mChapterIndex = -1; // -1 is default
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sd_tap_to_download_clickable:
                if (mChapterIndex == CHAPTER_0_TAP_TO_DOWNLOAD_FILE_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_1_TAP_TO_DOWNLOAD_FILE_VIDEO);
                }
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        mDownloadFile.setVisibility(View.VISIBLE);
        mChapterIndex = CHAPTER_0_TAP_TO_DOWNLOAD_FILE_INTERACTION;

    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mDownloadFile.setVisibility(View.GONE);
        mChapterIndex = CHAPTER_1_TAP_TO_DOWNLOAD_FILE_VIDEO;
    }

}
