package com.tecace.retail.videomanager.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tecace.retail.videomanager.R;

/**
 * Created by smheo on 9/29/2015.
 */
public class CoreSuperTextView extends SuperTextView {

    public CoreSuperTextView(Context context) {
        super(context);
    }

    public CoreSuperTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CoreSuperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Super Text backgrounds can be specified here
    @Override
    public int getSuperBackground(int lineCount) {
        TextView superView = (TextView) findViewById(R.id.superText);

        if (superView.getBackground() == null) {
            //return R.color.subtitle_background;
            return R.drawable.super_bg;
        } else {
            //really don't like this, look into returning the id of the background drawable
            return R.color.feature_background;
        }
    }
}
