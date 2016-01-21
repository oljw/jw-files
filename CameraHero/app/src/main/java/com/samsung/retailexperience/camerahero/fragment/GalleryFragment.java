package com.samsung.retailexperience.camerahero.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.samsung.retailexperience.camerahero.R;
import com.samsung.retailexperience.camerahero.util.AppConsts;
import com.samsung.retailexperience.camerahero.view.GalleryZoomView;

/**
 * Created by Jaewoo on 2016-01-18.
 */
public class GalleryFragment extends BaseGalleryFragment{
    private static final String TAG = GalleryFragment.class.getSimpleName();

    private TopGalleryBarFragment mTopGalleryBar = null;
    private BottomGalleryBarFragment mBottomGalleryBar = null;
//    private ImageView mGView = null;
    private GalleryZoomView mGView = null;

    public static GalleryFragment newInstance(String fragmentModel) {
        Log.d(TAG, "##### GalleryFragment newInstance Called");

        GalleryFragment fragment = new GalleryFragment();

        Bundle args = new Bundle();
        args.putString(AppConsts.ARG_JSON_MODEL, fragmentModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view) {
        Log.d(TAG, "######### GalleryFragment onViewCreated");
        mTopGalleryBar = (TopGalleryBarFragment)
                getChildFragmentManager().findFragmentById(R.id.top_gallery_fragment);

        mBottomGalleryBar = (BottomGalleryBarFragment)
                getChildFragmentManager().findFragmentById(R.id.bottom_gallery_fragment);

//        mGView = (ImageView) view.findViewById(R.id.IMAGEID);
        mGView = (GalleryZoomView) view.findViewById(R.id.IMAGEID);

//        mGView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.test_picture1));

        mGView.setOnClickListener(mGalleryPreviewListener);
    }

    View.OnClickListener mGalleryPreviewListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.d(TAG, "##### mGalleryPreviewListener onClick");
            Toast.makeText(getActivity(),"터치", Toast.LENGTH_SHORT).show();
            setMenuBar();
        }
    };

        @Override
    public void onBackPressed() {
        Log.d(TAG, "##### GalleryFragment onBackPressed called");
//            changeFragment(AppConsts.UIState.UI_STATE_CAMERA, AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    private void setMenuBar() {
        Log.d(TAG, "##### setMenuBar Called");
        if (mTopGalleryBar.isHidden()) {
            getFragmentManager().beginTransaction().setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out
            ).show( mTopGalleryBar ).setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out
            ).show( mBottomGalleryBar ).commit();
        }
        else {
            getFragmentManager().beginTransaction().setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out
            ).hide( mTopGalleryBar ).setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out
            ).hide( mBottomGalleryBar ).commit();
        }
    }
}
