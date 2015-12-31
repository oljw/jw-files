package com.jaewoolee.photogridviewctl.photogridview;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class fingerTracker implements View.OnTouchListener {
	private OnScrollListener myOnScrollListener;
	
	public fingerTracker(OnScrollListener onScrollListener){
		myOnScrollListener = onScrollListener;
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		final int action = event.getAction();
		boolean mFingerUp = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL;
		if (mFingerUp) {
			myOnScrollListener.onScrollStateChanged((AbsListView) view, OnScrollListener.SCROLL_STATE_FLING);
			myOnScrollListener.onScrollStateChanged((AbsListView) view, OnScrollListener.SCROLL_STATE_IDLE);
		}
		return false;
	}
}
