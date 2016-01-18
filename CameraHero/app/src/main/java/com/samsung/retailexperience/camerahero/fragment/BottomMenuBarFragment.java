package com.samsung.retailexperience.camerahero.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.samsung.retailexperience.camerahero.R;

/**
 * Created by icanmobile on 1/14/16.
 */
public class BottomMenuBarFragment extends Fragment {
    private static final String TAG = TopMenuBarFragment.class.getSimpleName();

    protected View mView = null;
    private ImageButton mStillBtn = null;
    private ImageButton mSwitchBtn = null;
    private ImageButton mVideoBtn = null;
    private BottomMenuBarListener mListener = null;

    public interface BottomMenuBarListener {
        void onStillClicked();
        void onSwitchClicked();
        void onVideoClicked();
    }

    public void setListener(BottomMenuBarListener listener) {
        mListener = listener;
    }
    public BottomMenuBarListener getListener() {
        return mListener;
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState)
    {
        mView = inflater.inflate( R.layout.camera_bottom_menubar, container, false );

        mStillBtn = (ImageButton) mView.findViewById(R.id.still_button);
        mStillBtn.setOnClickListener(mStillBtnClickListener);

        mSwitchBtn = (ImageButton) mView.findViewById(R.id.switch_button);
        mSwitchBtn.setOnClickListener(mSwitchBtnClickListener);

        mVideoBtn = (ImageButton) mView.findViewById(R.id.video_button);
        mVideoBtn.setOnClickListener(mVideoBtnClickListener);

        return mView;
    }

    View.OnClickListener mStillBtnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            getListener().onStillClicked();
        }
    };

    View.OnClickListener mSwitchBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getListener().onSwitchClicked();
        }
    };

    View.OnClickListener mVideoBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getListener().onVideoClicked();
        }
    };
}
