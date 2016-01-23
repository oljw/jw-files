package com.samsung.retailexperience.retailhero.ui.fragment.demos.camera;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.annotation.OnChapter;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.activity.MainActivity;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseCameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseVideoFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomGalleryBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopGalleryBarFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopMenuBarFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.view.CameraSurfaceView;
import com.samsung.retailexperience.retailhero.view.GalleryZoomView;

/**
 * Created by smheo on 1/15/2016.
 */
public class AutoFocusFragment extends BaseCameraFragment
        implements BottomMenuBarFragment.BottomMenuBarListener,
        CameraSurfaceView.CameraSurfaceListener {

    private static final String TAG = AutoFocusFragment.class.getSimpleName();

    private TopGalleryBarFragment mTopGalleryBar = null;
    private BottomGalleryBarFragment mBottomGalleryBar = null;
//    private GalleryZoomView mGView = null;
    private GalleryZoomView mGalleryPreview;

    public static AutoFocusFragment newInstance(FragmentModel<VideoModel> fragmentModel) {
        AutoFocusFragment fragment = new AutoFocusFragment();

        Bundle args = new Bundle();
        args.putSerializable(AppConsts.ARG_FRAGMENT_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        mTopMenuBar = (TopMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.top_fragment_test);
        mBottomMenuBar = (BottomMenuBarFragment) getChildFragmentManager().findFragmentById(R.id.bottom_fragment_test);

        mTopGalleryBar = (TopGalleryBarFragment) getChildFragmentManager().
                findFragmentById(R.id.top_gallery_fragment_test);
        mBottomGalleryBar = (BottomGalleryBarFragment) getChildFragmentManager().
                findFragmentById(R.id.bottom_gallery_fragment_test);

        getFragmentManager().beginTransaction().hide(mTopMenuBar).hide(mBottomMenuBar).
                hide(mTopGalleryBar).hide(mBottomGalleryBar).commit();

        mBottomMenuBar.setListener(this);

        mCamera = getCameraInstance(-1);
        mCameraSurface = new CameraSurfaceView((MainActivity)getActivity(), mCamera);
        mCameraSurface.setListener(this);

        mFocusIcon = (ImageView) view.findViewById(R.id.focus_icon_test);
        mFocusIcon.bringToFront();

        mGalleryPreview = (GalleryZoomView) view.findViewById(R.id.gallery_view_test);
        mGalleryPreview.setVisibility(View.GONE);

        mPreview = (RelativeLayout) view.findViewById(R.id.camera_view_test);
        mPreview.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        changeFragment(AppConst.UIState.valueOf(getFragmentModel().getActionBackKey()),
                AppConsts.TransactionDir.TRANSACTION_DIR_BACKWARD);
    }



//    @Override
//    public void drawFocusIcon(final float x, final float y) {
//        Log.d(TAG, "########## BaseCameraFragment drawFocusIcon");
//
//        if (rotateIconAnimator != null && rotateIconAnimator.isRunning())
//            rotateIconAnimator.cancel();
//
//        rotateIconAnimator = ObjectAnimator.ofFloat(mFocusIcon , "rotation", 0f, 180f);
//        rotateIconAnimator.setDuration(200);
//        rotateIconAnimator.addListener(new Animator.AnimatorListener() {
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                mFocusIcon.setVisibility(View.VISIBLE);
//
//                ViewGroup.MarginLayoutParams marginLayoutParams =
//                        (ViewGroup.MarginLayoutParams)mFocusIcon.getLayoutParams();
//                marginLayoutParams.setMargins
//                        ((int)(x - 300/2),
//                                (int)(y - 300/2),
//                                (int)(-x + 300/2),
//                                (int)(-y + 300 / 2));
//                mFocusIcon.setLayoutParams(marginLayoutParams);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mFocusIcon.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                mFocusIcon.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//        rotateIconAnimator.start();
//    }

    /**
     * Chapter callback methods
     */
    @OnChapter(chapterIndex = 0)
    public void onChaper_0() {
        Log.i(TAG, "onChaper_0");

        getFragmentManager().beginTransaction().show(mTopMenuBar).show(mBottomMenuBar).commit();

        mPreview.addView(mCameraSurface);
        mPreview.setVisibility(View.VISIBLE);
        mFocusIcon.bringToFront();
    }

    @OnChapter(chapterIndex = 1)
    public void onChaper_1() {
        Log.i(TAG, "onChaper_1");


    }

    @OnChapter(chapterIndex = 2)
    public void onChaper_2() {
        Log.i(TAG, "onChaper_1");

        getFragmentManager().beginTransaction().hide(mTopMenuBar).hide(mBottomMenuBar).commit();
        getFragmentManager().beginTransaction().show(mTopGalleryBar).show(mBottomGalleryBar).commit();

        mPreview.setVisibility(View.GONE);
        mGalleryPreview.setVisibility(View.VISIBLE);
    }

    @OnChapter(chapterIndex = 3)
    public void onChaper_3() {
        Log.i(TAG, "onChaper_1");

    }
}
