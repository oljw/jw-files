package com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;

/**
 * Created by smheo on 1/15/2016.
 */
public class MoreStorageFragment extends BaseVideoFragment implements View.OnClickListener {

    private static final String TAG = MoreStorageFragment.class.getSimpleName();

    private static final int CHAPTER_0_SHOW_SUPER_200GB_AND_DISCLAIMER_SOLD = 0;
    private static final int CHAPTER_1_HIDE_SUPER_200GB_AND_DISCLAIMER_SOLD = 1;
    private static final int CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION = 2;
    private static final int CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE = 3;
    private static final int CHAPTER_4_SHOW_SUPER_YOUR_FILES = 4;
    private static final int CHAPTER_5_HIDE_SUPER_YOUR_FILES = 5;
    private static final int CHAPTER_6_SHOW_DISCLAIMER_SOLD = 6;    // sd sold separately
    private static final int CHAPTER_7_HIDE_DISCLAIMER_SOLD = 7;

    private View mSuper200GB = null;
    private View mSuperYourFiles = null;
    private View mDisclaimerSold = null;
    private View mDownloadFile;
    private View mDownloadFileClickableArea;
    private int mChapterIndex;
    private TimerHandler mTimeoutTimer = null;

    public static MoreStorageFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        MoreStorageFragment fragment = new MoreStorageFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mSuper200GB = view.findViewById(R.id.sd_super_200gb);
            mSuperYourFiles = view.findViewById(R.id.sd_super_your_files);
            mDisclaimerSold = view.findViewById(R.id.sd_disclaimer_sold_separately);
            mDownloadFile = view.findViewById(R.id.sd_tap_to_download_container);
            mDownloadFileClickableArea = view.findViewById(R.id.sd_tap_to_download_clickable);
            if (mDownloadFileClickableArea != null) {
                mDownloadFileClickableArea.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }

        hideView(mSuper200GB);
        hideView(mSuperYourFiles);
        hideView(mDisclaimerSold);
        hideView(mDownloadFile);

        mChapterIndex = -1; // -1 is default

        if (mTimeoutTimer == null) {
            mTimeoutTimer = new TimerHandler();
            if (mTimeoutTimer != null) {
                mTimeoutTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                    @Override
                    public void onTimeout() {
                        if (mChapterIndex == CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION) {
                            // when timer have 6second timeout, video are going to jump next chapter frame
                            setForcedSeekToChapter(CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE);
                            mTimeoutTimer.stop();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimeoutTimer != null) {
            mTimeoutTimer.stop();
            mTimeoutTimer = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sd_tap_to_download_clickable:
                if (mChapterIndex == CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE);
                }
                break;
        }
    }

    private void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SHOW_SUPER_200GB_AND_DISCLAIMER_SOLD)
    public void onChaper_0() {
        Log.i(TAG, "Chaper_0 : show super 200gb");

        showView(mSuper200GB);
        showView(mDisclaimerSold);
        mChapterIndex = CHAPTER_0_SHOW_SUPER_200GB_AND_DISCLAIMER_SOLD;
    }

    @OnChapter(chapterIndex = CHAPTER_1_HIDE_SUPER_200GB_AND_DISCLAIMER_SOLD)
    public void onChaper_1() {
        Log.i(TAG, "Chaper_1 : hide super 200gb");

        hideView(mSuper200GB);
        hideView(mDisclaimerSold);
        mChapterIndex = CHAPTER_1_HIDE_SUPER_200GB_AND_DISCLAIMER_SOLD;
    }

    @OnChapter(chapterIndex = CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION)
    public void onChaper_2() {
        Log.i(TAG, "Chaper_2 : show TAP_TO_DOWNLOAD_FILE_INTERACTION");

        showView(mDownloadFile);
        mChapterIndex = CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION;
        if (mTimeoutTimer != null) {
            mTimeoutTimer.start(AppConsts.TIMEOUT_SIX_SECOND);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE)
    public void onChaper_3() {
        Log.i(TAG, "Chaper_3 : hide TAP_TO_DOWNLOAD_FILE_INTERACTION");

        hideView(mDownloadFile);
        mChapterIndex = CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE;
    }

    @OnChapter(chapterIndex = CHAPTER_4_SHOW_SUPER_YOUR_FILES)
    public void onChaper_4() {
        Log.i(TAG, "Chaper_4 : show super your files");

        showView(mSuperYourFiles);
        mChapterIndex = CHAPTER_4_SHOW_SUPER_YOUR_FILES;
    }

    @OnChapter(chapterIndex = CHAPTER_5_HIDE_SUPER_YOUR_FILES)
    public void onChaper_5() {
        Log.i(TAG, "Chaper_5 : hide super your files");

        hideView(mSuperYourFiles);
        mChapterIndex = CHAPTER_5_HIDE_SUPER_YOUR_FILES;
    }

    @OnChapter(chapterIndex = CHAPTER_6_SHOW_DISCLAIMER_SOLD)
    public void onChaper_6() {
        Log.i(TAG, "Chaper_6 : show disclaimer sd sold separately");

        showView(mDisclaimerSold);
        mChapterIndex = CHAPTER_6_SHOW_DISCLAIMER_SOLD;
    }

    @OnChapter(chapterIndex = CHAPTER_7_HIDE_DISCLAIMER_SOLD)
    public void onChaper_7() {
        Log.i(TAG, "Chaper_7 : hide disclaimer sd sold separately");

        hideView(mDisclaimerSold);
        mChapterIndex = CHAPTER_7_HIDE_DISCLAIMER_SOLD;
    }
}
