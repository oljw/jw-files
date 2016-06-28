package com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

    private static final int CHAPTER_0_SHOW_200GB_SUPER = 0;
    private static final int CHAPTER_1_HIDE_200GB_SUPER = 1;
    private static final int CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION = 2;
    private static final int CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE_AND_SHOW_YOUR_FILES = 3;
    private static final int CHAPTER_4_HIDE_YOUR_FILES = 4;
    private static final int CHAPTER_5_SHOW_SOLD_SUPER = 5;
    private static final int CHAPTER_6_HIDE_SOLD_SUPER = 6;

    private int mChapterIndex;

    private View mDownloadFile;
    private View mDownloadFileClickableArea;
    private View mSuper200GB;
    private View mSuperYourFiles;
    private View mSuperSoldSeparately;

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
            mDownloadFile = view.findViewById(R.id.sd_tap_to_download_container);
            mDownloadFileClickableArea = view.findViewById(R.id.sd_tap_to_download_clickable);
            mSuper200GB = view.findViewById(R.id.sd_super_200gb);
            mSuperYourFiles = view.findViewById(R.id.sd_super_your_files);
            mSuperSoldSeparately = view.findViewById(R.id.sd_super_sold_separately);

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

        hideView(mDownloadFile);
        hideView(mSuper200GB);
        hideView(mSuperYourFiles);
        hideView(mSuperSoldSeparately);

        mChapterIndex = -1; // -1 is default
    }

    @Override
    public void onPause() {
        super.onPause();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sd_tap_to_download_clickable:
                if (mChapterIndex == CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION) {
                    setForcedSeekToChapter(CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE_AND_SHOW_YOUR_FILES);
                }
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_SHOW_200GB_SUPER)
    public void onChaper_0() {
        Log.i(TAG, "Chaper_0 : show 200GB_SUPER");

        showView(mSuper200GB);
        mChapterIndex = CHAPTER_0_SHOW_200GB_SUPER;
    }

    @OnChapter(chapterIndex = CHAPTER_1_HIDE_200GB_SUPER)
    public void onChaper_1() {
        Log.i(TAG, "Chapter_1 : hide 200GB_SUPER");

        hideView(mSuper200GB);
        mChapterIndex = CHAPTER_1_HIDE_200GB_SUPER;
    }

    @OnChapter(chapterIndex = CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION)
    public void onChaper_2() {
        Log.i(TAG, "Chapter_2 : TAP_TO_DOWNLOAD_FILE_INTERACTION");

        showView(mDownloadFile);
        mChapterIndex = CHAPTER_2_TAP_TO_DOWNLOAD_FILE_INTERACTION;
    }

    @OnChapter(chapterIndex = CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE_AND_SHOW_YOUR_FILES)
    public void onChaper_3() {
        Log.i(TAG, "Chapter_3 : END_TAP_TO_DOWNLOAD_FILE_AND_SHOW_YOUR_FILES");

        hideView(mDownloadFile);
        showView(mSuperYourFiles);
        mChapterIndex = CHAPTER_3_END_TAP_TO_DOWNLOAD_FILE_AND_SHOW_YOUR_FILES;
    }

    @OnChapter(chapterIndex = CHAPTER_4_HIDE_YOUR_FILES)
    public void onChaper_4() {
        Log.i(TAG, "Chapter_4 : HIDE_YOUR_FILES");

        hideView(mSuperYourFiles);
        mChapterIndex = CHAPTER_4_HIDE_YOUR_FILES;
    }

    @OnChapter(chapterIndex = CHAPTER_5_SHOW_SOLD_SUPER)
    public void onChaper_5() {
        Log.i(TAG, "Chapter_5 : show SOLD_SUPER");

        showView(mSuperSoldSeparately);
        mChapterIndex = CHAPTER_5_SHOW_SOLD_SUPER;
    }

    @OnChapter(chapterIndex = CHAPTER_6_HIDE_SOLD_SUPER)
    public void onChaper_6() {
        Log.i(TAG, "Chapter_6 : hide SOLD_SUPER");

        hideView(mSuperSoldSeparately);
        mChapterIndex = CHAPTER_6_HIDE_SOLD_SUPER;
    }
}