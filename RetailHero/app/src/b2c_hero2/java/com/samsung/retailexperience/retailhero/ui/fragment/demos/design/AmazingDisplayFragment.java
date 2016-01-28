package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmazingDisplayFragment extends BaseVideoFragment implements View.OnClickListener,
        View.OnTouchListener, TimerHandler.OnTimeoutListener {

    private static final String TAG = AmazingDisplayFragment.class.getSimpleName();

    private static final int DURATION_UNDETERMINED = -1;
    // TODO confirm designed_amoled_chap.json
    // chapter 0 at 21 seconds
    // chapter 1 at 26 seconds
    enum DisplayChapter {
        chapterWelcomeVideo(-1, DURATION_UNDETERMINED),
        chapter0TapOnThumbnailInteraction(0, 6000), // 6 seconds to select a photo
        chapter0ZoomInInteraction(0, 10000),        // 10 seconds to zoom in
        chapter1TapAndCompareImagesVideo(1, DURATION_UNDETERMINED);

        final int chapterIndex;
        final int duration;
        DisplayChapter(int chapterIndex, int duration) {
            this.chapterIndex = chapterIndex;
            this.duration = duration;
        }
    }

    private DisplayChapter chapter = DisplayChapter.chapterWelcomeVideo;

    // TODO update images
    private static int TAP_IMAGE_NUM_COLUMN = 4;
    private static int TAP_IMAGE_NUM_ROW = 6;
    private static final List<Integer> TAP_THUMBNAILS_RESOURCE_IDS =
            new ArrayList<>(TAP_IMAGE_NUM_COLUMN * TAP_IMAGE_NUM_ROW);
    static {
        // first row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
        // second row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
        // third row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
        // fourth row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
        // fifth row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
        // sixth row
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_1);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_2);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_3);
        TAP_THUMBNAILS_RESOURCE_IDS.add(R.drawable.pay_card_4);
    }

    private static final int NO_PHOTO_RESOURCE_INDEX = -1;

    private static final int DEFAULT_VIDEO_WHEN_UNSELECTED = 0;


    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_TOP = 374f;
    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT = 3f;


    private static final float TAP_IMAGE_SIZE = 357f;
    private static final float TAP_IMAGE_CLICKABLE_AREA = TAP_IMAGE_SIZE + 2f;  // 2f is margin right

    private View mTapAPhoto;
    private ImageView mZoomingInAnimation;
    private GalleryZoomView mZoomInableImageView;

    private View mZoomInOverlay;

    private TimerHandler mTimerHandler;

    private float mLastTouchEventX;
    private float mLastTouchEventY;

    public static AmazingDisplayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AmazingDisplayFragment fragment = new AmazingDisplayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mTapAPhoto = view.findViewById(R.id.amoled_tap_a_photo);
        mZoomingInAnimation = (ImageView) view.findViewById(R.id.amoled_zooming_in_animation);
        mZoomInableImageView = (GalleryZoomView) view.findViewById(R.id.amoled_zoom_in_image);
        mZoomInOverlay = view.findViewById(R.id.almoed_zoom_in_overlay);


        mTimerHandler = new TimerHandler();
        mTimerHandler.setOnTimeoutListener(this);

        mTapAPhoto.setOnClickListener(this);
        mTapAPhoto.setOnTouchListener(this);
        mZoomInOverlay.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        chapter = DisplayChapter.chapterWelcomeVideo;
        mTapAPhoto.setVisibility(View.GONE);
        mZoomingInAnimation.setVisibility(View.GONE);
        mZoomInableImageView.setVisibility(View.GONE);
        mZoomInableImageView.resetZoom();
        mZoomInOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimerHandler.stop();
    }


    void pauseVideo() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    void playVideo() {
        if (mVideoView != null) {
            try {
                mVideoView.play();
            } catch (Exception e) {
                Log.e(TAG, e + " : video play");
            }
        }
    }

    private void jumpToChapter(int chapter) {
        setForcedSeekToChapter(chapter);
        playVideo();
    }

    private int getTappedImageIndex() {
        int tappedImageIndex = NO_PHOTO_RESOURCE_INDEX;
        if (mLastTouchEventX > TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT && mLastTouchEventY > TAP_IMAGE_IMAGE_LIST_MARGIN_TOP) {
            int columnIndex = (int) ((mLastTouchEventX - TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT) / TAP_IMAGE_CLICKABLE_AREA);
            int rowIndex = (int) ((mLastTouchEventY - TAP_IMAGE_IMAGE_LIST_MARGIN_TOP) / TAP_IMAGE_CLICKABLE_AREA);
            if (columnIndex < TAP_IMAGE_NUM_COLUMN && rowIndex < TAP_IMAGE_NUM_ROW) {
                tappedImageIndex = rowIndex * TAP_IMAGE_NUM_COLUMN + columnIndex;
                Log.i(TAG, "row: " + (rowIndex) + ", col: " + (columnIndex) + "is selected index: " + tappedImageIndex);
            }
        }
        return tappedImageIndex;
    }

    private void setZoomInMode() {
        chapter = DisplayChapter.chapter0ZoomInInteraction;
        mZoomingInAnimation.setVisibility(View.GONE);
        mZoomInableImageView.setVisibility(View.VISIBLE);
        mZoomInOverlay.setVisibility(View.VISIBLE);
        mTimerHandler.start(chapter.duration);

    }

    private void showAPhotoAtIndex(int imgResourceIndex){
        mTimerHandler.stop();

        int imgResourceId = TAP_THUMBNAILS_RESOURCE_IDS.get(imgResourceIndex);
        mTapAPhoto.setVisibility(View.GONE);
        mZoomingInAnimation.setImageResource(imgResourceId);
        mZoomInableImageView.setImageResource(imgResourceId);
        mZoomingInAnimation.setVisibility(View.VISIBLE);

        int screenWidth = getResources().getDimensionPixelSize(R.dimen.display_width_in_pixel);
        int screenHeight = getResources().getDimensionPixelSize(R.dimen.display_height_in_pixel);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mZoomingInAnimation.getLayoutParams();
        lp.width = (int) TAP_IMAGE_SIZE;
        lp.height = (int) TAP_IMAGE_SIZE;
        int col = imgResourceIndex % TAP_IMAGE_NUM_COLUMN;
        int row = imgResourceIndex / TAP_IMAGE_NUM_COLUMN;

        // use clickable area instead of size since clickable area includes margin values
        float originalX = TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT + col * TAP_IMAGE_CLICKABLE_AREA;
        float originalY = TAP_IMAGE_IMAGE_LIST_MARGIN_TOP + row * TAP_IMAGE_CLICKABLE_AREA;

        mZoomingInAnimation.setX(originalX);
        mZoomingInAnimation.setY(originalY);
        mZoomingInAnimation.setScaleX(1f);
        mZoomingInAnimation.setScaleY(1f);

        // TODO animation to xml + update scale animation
        float scaleX = screenWidth / TAP_IMAGE_SIZE;
        float scaleY = screenHeight / TAP_IMAGE_SIZE;
        // animation
        ObjectAnimator animX = ObjectAnimator.ofFloat(mZoomingInAnimation, "x", 0f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(mZoomingInAnimation, "y", 0f);
        ObjectAnimator animScaleX = ObjectAnimator.ofFloat(mZoomingInAnimation, "scaleX", scaleX);
        ObjectAnimator animScaleY = ObjectAnimator.ofFloat(mZoomingInAnimation, "scaleY", scaleY);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.play(animX).with(animY).with(animScaleX).with(animScaleY);


        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setZoomInMode();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setZoomInMode();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSet.start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amoled_tap_a_photo:
                if (chapter == DisplayChapter.chapter0TapOnThumbnailInteraction) {
                    int selectedImageIndex = getTappedImageIndex();
                    if (selectedImageIndex != NO_PHOTO_RESOURCE_INDEX) {
                        showAPhotoAtIndex(selectedImageIndex);
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mLastTouchEventX = event.getX();
        mLastTouchEventY = event.getY();

        if (mZoomInOverlay.getVisibility()==View.VISIBLE) {
            mZoomInOverlay.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }

    @Override
    public void onTimeout() {
        switch (chapter) {
            case chapter0TapOnThumbnailInteraction:
                // User didn't swipe to show a card.  start animation manually
                showAPhotoAtIndex(DEFAULT_VIDEO_WHEN_UNSELECTED);
                break;
            case chapter0ZoomInInteraction:
                jumpToChapter(DisplayChapter.chapter1TapAndCompareImagesVideo.chapterIndex);
                break;
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        pauseVideo();

        mTapAPhoto.setVisibility(View.VISIBLE);
        chapter = DisplayChapter.chapter0TapOnThumbnailInteraction;
        mTimerHandler.start(chapter.duration);
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        mTapAPhoto.setVisibility(View.GONE);
        mZoomingInAnimation.setVisibility(View.GONE);
        mZoomInableImageView.setVisibility(View.GONE);
        mZoomInOverlay.setVisibility(View.GONE);

        chapter = DisplayChapter.chapter1TapAndCompareImagesVideo;
    }
}
