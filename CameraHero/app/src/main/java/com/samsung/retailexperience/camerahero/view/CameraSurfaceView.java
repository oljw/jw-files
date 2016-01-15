package com.samsung.retailexperience.camerahero.view;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);
    }

    public interface CameraSurfaceListener {
        void onDummy();
    };

    private CameraSurfaceListener mListener = null;
    public void setListener(CameraSurfaceListener listener) {
        mListener = listener;
    }
    public CameraSurfaceListener getListener() {
        return mListener;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
