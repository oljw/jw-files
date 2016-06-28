package com.samsung.retailexperience.retailhero.ui.fragment.demos.business;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smheo on 1/17/2016.
 */
public class B2B_DisplayFragment extends BaseVideoFragment implements View.OnClickListener,
        View.OnTouchListener, TimerHandler.OnTimeoutListener, B2B_DisplayFragmentAsyncResponse {

    private static final String TAG = B2B_DisplayFragment.class.getSimpleName();

    private static final int DURATION_UNDETERMINED = -1;

    // chapter 0 at 21 seconds
    // chapter 1 at 26 seconds
    enum DisplayChapter {
        chapterWelcomeVideo(-1, DURATION_UNDETERMINED),
        chapter0TapOnThumbnailInteraction(0, 6000), // 6 seconds to select a photo
        chapter0ZoomInInteraction(0, 10000),        // 10 seconds to zoom in
        chapter1TapAndCompareImagesVideo(1, DURATION_UNDETERMINED),
        chapter2CurvedSuper(2, 4800),
        chapter3WaterResistant(3, 5800);

        final int chapterIndex;
        final int duration;
        DisplayChapter(int chapterIndex, int duration) {
            this.chapterIndex = chapterIndex;
            this.duration = duration;
        }
    }

    private DisplayChapter mChapter = DisplayChapter.chapterWelcomeVideo;

    private static int TAP_IMAGE_NUM_COLUMN = 4;
    private static int TAP_IMAGE_NUM_ROW = 6;
    private static final List<String> TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH =
            new ArrayList<>(TAP_IMAGE_NUM_COLUMN * TAP_IMAGE_NUM_ROW);
    static {
        // first row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo1.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo2.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo3.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo4.jpg");
        // second row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo5.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo6.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo7.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo8.jpg");
        // third row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo9.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo10.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo11.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo12.jpg");
        // fourth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo13.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo14.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo15.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo16.jpg");
        // fifth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo17.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo18.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo19.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo20.jpg");
        // sixth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo21.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo22.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo23.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2B_Hero_backpack_photo24.jpg");
    }

    private static final int NO_PHOTO_RESOURCE_INDEX = -1;

    private static final int DEFAULT_IMAGE_INDEX_WHEN_UNSELECTED = 0;


    // TODO update it
    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_TOP = 374f;
    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT = 2f;
    // 359-2=357 (width) 731-374=357 (height)
    private static final float TAP_IMAGE_SIZE = 357f;
    private static final float TAP_IMAGE_CLICKABLE_AREA = TAP_IMAGE_SIZE + 2f;  // 2f is margin right

    private View mTapAPhoto;
    private ImageView mZoomingInAnimation;
    private GalleryZoomView mZoomInableImageView;
    private View mCurvedSuper;
    private View mWaterResistant;

    private View mZoomInOverlay;

    private TimerHandler mTimerHandler;

    private float mLastTouchEventX;
    private float mLastTouchEventY;

    private AsyncTaskRunner mRetrieveBitmapForZoomIn;
    private boolean mIsPaused;
    private boolean mIsPreparingZoomInBitmap;
    private boolean mIsPreparedZoomInBitmap;
    private boolean mIsAnimationEnded;

    public static B2B_DisplayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        B2B_DisplayFragment fragment = new B2B_DisplayFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        if (view != null) {
            mTapAPhoto = view.findViewById(R.id.amoled_tap_a_photo);
            mZoomingInAnimation = (ImageView) view.findViewById(R.id.amoled_zooming_in_animation);
            mZoomInableImageView = (GalleryZoomView) view.findViewById(R.id.amoled_zoom_in_image);
            mZoomInOverlay = view.findViewById(R.id.almoed_zoom_in_overlay);
            mCurvedSuper = view.findViewById(R.id.amoled_curved_super);
            mWaterResistant = view.findViewById(R.id.amoled_water_resistant);

            if (mTapAPhoto != null) {
                mTapAPhoto.setOnClickListener(this);
                mTapAPhoto.setOnTouchListener(this);
            }
            if (mZoomInOverlay != null) {
                mZoomInOverlay.setOnTouchListener(this);
            }
        }

        mTimerHandler = new TimerHandler();
        if (mTimerHandler != null) {
            mTimerHandler.setOnTimeoutListener(this);
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

        if (mTapAPhoto != null) {
            mTapAPhoto.setClickable(true);
        }

        mIsPaused = false;
        mIsPreparingZoomInBitmap = false;
        mIsPreparedZoomInBitmap = false;
        mIsAnimationEnded = false;
        mRetrieveBitmapForZoomIn = new AsyncTaskRunner(this);

        mChapter = DisplayChapter.chapterWelcomeVideo;

        hideView(mTapAPhoto);
        hideView(mZoomingInAnimation);
        hideView(mZoomInableImageView);
        hideView(mZoomInOverlay);
        hideView(mCurvedSuper);
        hideView(mWaterResistant);

        if (mZoomInableImageView != null) {
            mZoomInableImageView.resetZoom();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }

        mIsPaused = true;
        if (mRetrieveBitmapForZoomIn != null) {
            mRetrieveBitmapForZoomIn.cancel(true);
        }

        recycleBitmap(mZoomingInAnimation);
        recycleBitmap(mZoomInableImageView);
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

    private void recycleBitmap(ImageView iv) {
        if (iv == null) {
            return;
        }
        Drawable drawable = iv.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        iv.setImageBitmap(null);
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
        // setForceSeekToChapter is called before, don't need to call again.
        //setForcedSeekToChapter(chapter);
        playVideo();
    }

    private int getTappedImageIndex() {
        int tappedImageIndex = NO_PHOTO_RESOURCE_INDEX;
        if (mLastTouchEventX > TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT && mLastTouchEventY > TAP_IMAGE_IMAGE_LIST_MARGIN_TOP) {
            int columnIndex = (int) ((mLastTouchEventX - TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT) / TAP_IMAGE_CLICKABLE_AREA);
            int rowIndex = (int) ((mLastTouchEventY - TAP_IMAGE_IMAGE_LIST_MARGIN_TOP) / TAP_IMAGE_CLICKABLE_AREA);
            if (columnIndex < TAP_IMAGE_NUM_COLUMN && rowIndex < TAP_IMAGE_NUM_ROW) {
                tappedImageIndex = rowIndex * TAP_IMAGE_NUM_COLUMN + columnIndex;
            }
        }
        return tappedImageIndex;
    }

    private void setZoomInMode() {
        if (!mIsPreparedZoomInBitmap || !mIsAnimationEnded) {
            return;
        }

        mChapter = DisplayChapter.chapter0ZoomInInteraction;

        hideView(mZoomingInAnimation);
        showView(mZoomInableImageView);
        showView(mZoomInOverlay);

        if (mTimerHandler != null) {
            mTimerHandler.start(mChapter.duration);
        }

        // move to video first.   Otherwise, it shows thumbnail for ~0.1 seconds
        setForcedSeekToChapter(1);
    }

    private void animateScaling(int imgIndex) {
        if (mZoomingInAnimation == null) {
            return;
        }

        mZoomingInAnimation.setVisibility(View.VISIBLE);

        int screenWidth = getResources().getDimensionPixelSize(R.dimen.display_width_in_pixel);
        int screenHeight = getResources().getDimensionPixelSize(R.dimen.display_height_in_pixel);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mZoomingInAnimation.getLayoutParams();
        lp.width = (int) TAP_IMAGE_SIZE;
        lp.height = (int) TAP_IMAGE_SIZE;
        int col = imgIndex % TAP_IMAGE_NUM_COLUMN;
        int row = imgIndex / TAP_IMAGE_NUM_COLUMN;

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
        animSet.setDuration(650);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.play(animX).with(animY).with(animScaleX).with(animScaleY);


        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimationEnded = true;

                setZoomInMode();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsAnimationEnded = true;

                setZoomInMode();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSet.start();
    }

    private void showAPhotoAtIndex(int imgIndex){
        synchronized (B2B_DisplayFragment.class) {
            if (mIsPreparingZoomInBitmap) {
                return;
            }
            mIsPreparingZoomInBitmap = true;
        }

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }

        if (mVideoView == null || mZoomingInAnimation == null) {
            return;
        }

        ResourceUtil resourceUtil = mVideoView.getmResourceUtil();

        String photoPath = TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.get(imgIndex);

        if (photoPath == null || resourceUtil == null || resourceUtil.isMissingContentFile(photoPath)) {
            return;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap smallBitmap = BitmapFactory.decodeFile(resourceUtil.getContentFilePath(photoPath), options);
        if (smallBitmap == null) {
            // Hack!!!! notify the image is not valid!
            resourceUtil.isMissingContentFile(photoPath + " is malformed!");
            return;
        }
        mZoomingInAnimation.setImageBitmap(smallBitmap);
        animateScaling(imgIndex);

        prepareZoomInableBitmap(resourceUtil.getContentFilePath(photoPath));
    }

    private void prepareZoomInableBitmap(String filePath) {
        if (mZoomInableImageView == null || mRetrieveBitmapForZoomIn == null) {
            // if imageview is not ready, don't try to populate the bitmap.
            return;
        }

        String[] s = new String[1];
        s[0] = filePath;
        mRetrieveBitmapForZoomIn.execute(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amoled_tap_a_photo:
                if (mTapAPhoto != null) {
                    mTapAPhoto.setClickable(false);
                }

                if (mChapter == DisplayChapter.chapter0TapOnThumbnailInteraction) {
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

        if (mZoomInOverlay != null && mZoomInOverlay.getVisibility() == View.VISIBLE) {
            mZoomInOverlay.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentModel() != null && getFragmentModel().getActionBackKey() != null) {
            changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                    AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
        }
    }

    @Override
    public void onTimeout() {
        switch (mChapter) {
            case chapter0TapOnThumbnailInteraction:
                // User didn't swipe to show a card.  start animation manually
                showAPhotoAtIndex(DEFAULT_IMAGE_INDEX_WHEN_UNSELECTED);
                break;
            case chapter0ZoomInInteraction:
                jumpToChapter(DisplayChapter.chapter1TapAndCompareImagesVideo.chapterIndex);
                break;
            case chapter2CurvedSuper:
                hideView(mCurvedSuper);
                break;
            case chapter3WaterResistant:
                hideView(mWaterResistant);
                break;

        }
    }

    @Override
    public void processFinish(Bitmap bitmap) {
        if (mIsPaused) {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            return;
        }

        if (bitmap == null) {
            // Hack!!!! notify the image is not valid!
            mVideoView.getmResourceUtil().isMissingContentFile("Image is malformed!");
            return;
        }

        Log.d(TAG, "bitmap: " + bitmap.getWidth() + ", height: " + bitmap.getHeight());

        if (mZoomInOverlay != null) {
            mZoomInableImageView.setImageBitmap(bitmap);
            mIsPreparedZoomInBitmap = true;
            setZoomInMode();
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, Bitmap> {
        private B2B_DisplayFragmentAsyncResponse mListener;
        AsyncTaskRunner(B2B_DisplayFragmentAsyncResponse listener) {
            mListener = listener;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String filePath = params[0];
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //super.onPostExecute(bitmap);
            mListener.processFinish(bitmap);
        }
    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        pauseVideo();

        showView(mTapAPhoto);
        mChapter = DisplayChapter.chapter0TapOnThumbnailInteraction;
        if (mTimerHandler != null) {
            mTimerHandler.start(mChapter.duration);
        }
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");

        hideView(mTapAPhoto);
        hideView(mZoomingInAnimation);
        hideView(mZoomInOverlay);
        hideView(mZoomInableImageView);

        mChapter = DisplayChapter.chapter1TapAndCompareImagesVideo;
    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_2");

        showView(mCurvedSuper);

        mChapter = DisplayChapter.chapter2CurvedSuper;
        if (mTimerHandler != null) {
            mTimerHandler.start(mChapter.duration);
        }
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_3");

        showView(mWaterResistant);
        mChapter = DisplayChapter.chapter3WaterResistant;
        if (mTimerHandler != null) {
            mTimerHandler.start(mChapter.duration);
        }
    }
}