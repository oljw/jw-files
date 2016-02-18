package com.samsung.retailexperience.retailhero.ui.fragment.demos.design;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class AmazingDisplayFragment extends BaseVideoFragment implements View.OnClickListener,
        View.OnTouchListener, TimerHandler.OnTimeoutListener, AmazingDisplayAsyncResponse {

    private static final String TAG = AmazingDisplayFragment.class.getSimpleName();

    private static final int CHAPTER_0_TAP_A_PHOTO_INTERACTION = 0;   // 21260 TAP INTERACTION + ANIMATION
    private static final int CHAPTER_1_ZOOM_INABLE_VIDEO_SOUND = 1;       // 28000 YOU CAN ZOOM IN VIDEO (NO VIDEO SCREEN)
//    private static final int CHAPTER_2_ZOOM_IN_INTERACTION = 2;  //    28000 SUPER ZOOM IN
//    private static final int CHAPTER_3_REMAINING_VIDEO = 3;  // 38000

    private int mChapter = -1;  // default

    // TODO update images
    private static int TAP_IMAGE_NUM_COLUMN = 4;
    private static int TAP_IMAGE_NUM_ROW = 6;
    private static final List<String> TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH =
        new ArrayList<>(TAP_IMAGE_NUM_COLUMN * TAP_IMAGE_NUM_ROW);
        static {
        // first row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo1.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo2.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo3.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo4.jpg");
        // second row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo5.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo6.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo7.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo8.jpg");
        // third row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo9.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo10.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo11.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo12.jpg");
        // fourth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo13.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo14.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo15.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo16.jpg");
        // fifth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo17.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo18.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo19.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo20.jpg");
        // sixth row
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo21.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo22.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo23.jpg");
        TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.add("image/B2C_Hero2_Fabric_photo24.jpg");
    }

    private static final int NO_PHOTO_RESOURCE_INDEX = -1;
    private static final int DEFAULT_VIDEO_WHEN_UNSELECTED = 0;
    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_TOP = 328f;
    private static final float TAP_IMAGE_IMAGE_LIST_MARGIN_LEFT = 1f;


    // 359 x 686
    // 359 - 1 = 358
    // 686 - 328 = 358
    private static final float TAP_IMAGE_SIZE = 358f;
    private static final float TAP_IMAGE_CLICKABLE_AREA = TAP_IMAGE_SIZE + 1f;  // 1f is margin right

    private View mTapAPhoto;
    private ImageView mZoomingInAnimation;
    private GalleryZoomView mZoomInableImageView;
    private View mZoomInOverlay;

    private TimerHandler mTimerHandler;

    private float mLastTouchEventX;
    private float mLastTouchEventY;

    private AsyncTaskRunner mRetrieveBitmapForZoomIn;
    private boolean mIsPaused;
    private boolean mIsPreparingZoomInBitmap;
    private boolean mIsPreparedZoomInBitmap;
    private boolean mIsAnimationEnded;

    public static AmazingDisplayFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AmazingDisplayFragment fragment = new AmazingDisplayFragment();

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

        mChapter = -1;

        hideView(mTapAPhoto);
        hideView(mZoomingInAnimation);
        hideView(mZoomInableImageView);
        hideView(mZoomInOverlay);

        if (mZoomInableImageView != null) {
            mZoomInableImageView.resetZoom();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mTimerHandler.stop();

        mIsPaused = true;
        if (mRetrieveBitmapForZoomIn != null) {
            mRetrieveBitmapForZoomIn.cancel(true);
        }

        // TODO recycle a bitmap
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
        if (!mIsPreparedZoomInBitmap || !mIsAnimationEnded) {
            return;
        }
        jumpToChapter(CHAPTER_1_ZOOM_INABLE_VIDEO_SOUND);
        hideView(mZoomingInAnimation);
        showView(mZoomInableImageView);
        showView(mZoomInOverlay);
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
        synchronized (AmazingDisplayFragment.class) {
            if (mIsPreparingZoomInBitmap) {
                return;
            }
            mIsPreparingZoomInBitmap = true;
        }

        if (mTimerHandler != null) {
            mTimerHandler.stop();
        }


//        int imgResourceId = TAP_THUMBNAILS_RESOURCE_IDS.get(imgResourceIndex);
//        mTapAPhoto.setVisibility(View.GONE);
//        mZoomingInAnimation.setImageResource(imgResourceId);
//        mZoomInableImageView.setImageResource(imgResourceId);

//        mTapAPhoto.setVisibility(View.GONE);
//        String photoPath = TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.get(imgIndex);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
//        mZoomingInAnimation.setImageBitmap(bitmap);
//        mZoomInableImageView.setImageBitmap(bitmap);

//        mTapAPhoto.setVisibility(View.GONE);
//        mZoomingInAnimation.setImageResource(R.drawable.pay_bg);
//        mZoomInableImageView.setImageResource(R.drawable.pay_bg);

        if (mVideoView == null || mZoomingInAnimation == null) {
            return;
        }

        ResourceUtil resourceUtil = mVideoView.getmResourceUtil();

        String photoPath = TAP_THUMBNAILS_ORIGINAL_IMAGE_FILE_PATH.get(imgIndex);

        if (photoPath == null || resourceUtil.isMissingContentFile(photoPath)) {
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
                if (mChapter == CHAPTER_0_TAP_A_PHOTO_INTERACTION) {
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

        if (mZoomInOverlay != null && mZoomInOverlay.getVisibility()==View.VISIBLE) {
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
            case CHAPTER_0_TAP_A_PHOTO_INTERACTION:
                // User didn't swipe to show a card.  start animation manually
                showAPhotoAtIndex(DEFAULT_VIDEO_WHEN_UNSELECTED);
                break;
//            case CHAPTER_2_ZOOM_IN_INTERACTION:
//                jumpToChapter(CHAPTER_3_REMAINING_VIDEO);
//                break;
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
            if (mVideoView != null) {
                // Hack!!!! notify the image is not valid!
                mVideoView.getmResourceUtil().isMissingContentFile("Image is malformed!");
            }
            return;
        }

        mZoomInableImageView.setImageBitmap(bitmap);
        mIsPreparedZoomInBitmap = true;
        setZoomInMode();
    }


    private class AsyncTaskRunner extends AsyncTask<String, Void, Bitmap> {
        private AmazingDisplayAsyncResponse mListener;
        AsyncTaskRunner(AmazingDisplayAsyncResponse listener) {
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
            if (mListener != null) {
                mListener.processFinish(bitmap);
            }
        }
    }


    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = CHAPTER_0_TAP_A_PHOTO_INTERACTION)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0 CHAPTER_0_TAP_A_PHOTO_INTERACTION");

        pauseVideo();
        mChapter = CHAPTER_0_TAP_A_PHOTO_INTERACTION;

        showView(mTapAPhoto);
        if (mTimerHandler != null) {
            mTimerHandler.start(6000);
        }
    }

    @OnChapter(chapterIndex = CHAPTER_1_ZOOM_INABLE_VIDEO_SOUND)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1 CHAPTER_1_ZOOM_INABLE_VIDEO_SOUND");

        //mTapAPhoto.setVisibility(View.GONE);

        hideView(mZoomingInAnimation);

        mChapter = CHAPTER_1_ZOOM_INABLE_VIDEO_SOUND;
    }
}
