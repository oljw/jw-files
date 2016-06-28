package com.tecace.app.manager.ext.ui.view.compareview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tecace.app.manager.ext.R;

/**
 * Created by JW on 2016-06-23.
 *
 * This View is made to be used exclusively on Camera Demo in Retail Grace.
 * Will be updated promptly when necessary.
 */
public class CompareOverlayView extends RelativeLayout {
    private static final String TAG = CompareOverlayView.class.getSimpleName();

    /**
     * Minimum value of X to limit prevent scale button to go out of scope.
     */
    private static final int LEFT_SPACE_MIN = 300;

    /**
     * Maximum value of X to limit prevent scale button to go out of scope.
     */
    private static final int LEFT_SPACE_MAX = 1140;

    /**
     * Maximum screen width (portrait mode).
     */
    private static final int MAX_SCREEN_WIDTH = 1440;

    /**
     * Current Context of the app.
     */
    private Context mContext;

    /**
     * An instance of a LayoutInflater.
     */
    private LayoutInflater mInflater;

    /**
     * An instance of the Overlay Image that will cover half of the screen
     * as a comparison.
     */
    private ImageView mOverlayImgView;

    /**
     * A button to control scaling of Overlay ImageView.
     */
    private Button mScaleBtn;

    /**
     * Initial X position where the user tapped.
     */
    private float initialTouchX;

    /**
     * Initial transparent space on the left.
     */
    private float initialSpaceOnLeft;



    public CompareOverlayView(Context context) {
        super(context);
        init(context, null);
    }

    public CompareOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CompareOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CompareOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_compare, this, true);

        mOverlayImgView = (ImageView) findViewById(R.id.img);

        mScaleBtn = (Button) findViewById(R.id.btn);
        mScaleBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        && mOverlayImgView  != null
                        && mScaleBtn        != null) {

                    // Set the initial X position so button stays where it's supposed to be.
                    ViewGroup.LayoutParams imgLayoutParams = mOverlayImgView.getLayoutParams();
                    initialSpaceOnLeft = MAX_SCREEN_WIDTH - imgLayoutParams.width;
                    initialTouchX = event.getRawX();

                } else if (event.getAction() == MotionEvent.ACTION_MOVE
                        && mOverlayImgView  != null
                        && mScaleBtn        != null) {

                    // Get the Raw-X position of where the user touches the screen.
                    // And calculate so button stays where it's supposed to be.
                    int spaceOnLeft = (int) (event.getRawX() - initialTouchX + initialSpaceOnLeft);

                    // Set Minimum and Maximum X position of mScaleBtn & mOverlayImgView.
                    spaceOnLeft = Math.max(LEFT_SPACE_MIN, spaceOnLeft);
                    spaceOnLeft = Math.min(LEFT_SPACE_MAX, spaceOnLeft);

                    // Move around the overlay image.
                    ViewGroup.LayoutParams imgLayoutParams = mOverlayImgView.getLayoutParams();
                    imgLayoutParams.width = MAX_SCREEN_WIDTH - spaceOnLeft;
                    mOverlayImgView.setLayoutParams(imgLayoutParams);

                    // Move around the scaling button.
                    int left = spaceOnLeft - (mScaleBtn.getWidth()/2);
                    LayoutParams btnLayoutParams = (LayoutParams) mScaleBtn.getLayoutParams();
                    btnLayoutParams.leftMargin = left;
                    mScaleBtn.setLayoutParams(btnLayoutParams);
                }
                return true;
            }
        });
    }
}
