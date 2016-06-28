package com.samsung.retailexperience.retailecosystem.ui.view.circlemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samsung.retailexperience.retailecosystem.R;

/**
 * Created by icanmobile on 3/8/16.
 */
public class CircleMenuItemView extends RelativeLayout {
    private static final String TAG = CircleMenuItemView.class.getSimpleName();

    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private TextView mUpperTitle = null;
    private TextView mLowerTitle = null;
    private ImageView mIcon = null;

    private Drawable mIconDrawable = null;
    private Drawable mIconPressedDrawable = null;

    public CircleMenuItemView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) return;

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.view_circle_menu_item, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleMenuItemView);
        mIconDrawable = a.getDrawable(R.styleable.CircleMenuItemView_itemIcon);
        mIconPressedDrawable = a.getDrawable(R.styleable.CircleMenuItemView_itemIconPressed);
        Boolean useUpperTitle = a.getBoolean(R.styleable.CircleMenuItemView_useUpperTitle, true);
        String title = a.getString(R.styleable.CircleMenuItemView_itemTitle);
        a.recycle();

        mUpperTitle = (TextView) findViewById(R.id.upper_title);
        mLowerTitle = (TextView) findViewById(R.id.lower_title);
        mIcon = (ImageView) findViewById(R.id.icon);

        if (useUpperTitle) {
            mUpperTitle.setText(title);
            mLowerTitle.setVisibility(View.GONE);
        }
        else {
            mLowerTitle.setText(title);
            mUpperTitle.setVisibility(View.GONE);
        }
        mIcon.setImageDrawable(mIconDrawable);
    }

    public void onIconPressed(boolean bPressed) {
        if (bPressed)
            mIcon.setImageDrawable(mIconPressedDrawable);
        else
            mIcon.setImageDrawable(mIconDrawable);
    }
}
