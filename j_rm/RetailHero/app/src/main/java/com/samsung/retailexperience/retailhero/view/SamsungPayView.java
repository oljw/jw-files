package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import com.samsung.retailexperience.retailhero.R;

/**
 * Created by MONSTER on 1/20/2016.
 */
public class SamsungPayView extends FrameLayout implements
        CustomizedGallery.GalleryTapListener, CustomizedGallery.GalleryTouchListener {
    private static final String TAG = SamsungPayView.class.getSimpleName();

    // if a user moved this threshold, it is up or down motion (or swiping)
    private static final float GESTURE_UP_DOWN_THRESHOLD = 20f;
    // if the last gesture happened in last 300 milli second, it is effective gesture
    private static final long GESTURE_EFFECTIVE_TIME_THRESHOLD = 300l;
    private static final float INIT_TRANSPARENT_VISIBLE_HEIGHT = 300f;

    private static final int DEFAULT_CARD_SELECTION = 1;    // 0 base, so 2nd card for index 1.

    private static final List<Integer> CARDS = new ArrayList<>(2);
    static {
        CARDS.add(R.drawable.pay_card_2);
        CARDS.add(R.drawable.pay_card_1);
        CARDS.add(R.drawable.pay_card_3);
        CARDS.add(R.drawable.pay_card_4);
    }
    private static final int TRANSPARENT_CARD_RESOURCE_ID = R.drawable.pay_card_1;

    private static final float SWIPING_SQUARED_DISTANCE_MIN = 1000f;
    private float mTouchStartPosX, mTouchStartPosY;


    private View mKnob;
    private View mTransparentCard;
    private View mCardListRoot;
    private CustomizedGallery mCardList;
    private View mCardSelectionIndicator;
    private View mKnobContainer;
    //private TextView mSuperView;
    private View mOverlayBackground;
    private View mSuperSwipeLeft;
    private View mSuperTapCard;

    private boolean mIsDraggingATransparentCard;
    // touch X position is not necessarily since card is moving only up and downwards.
    private float mTouchStartY;
    private float mLastTouchY;
    private long mLastUpwardGestureTime;
    private float mTransparentCardInitY;

    private CardImageAdapter imageAdapter;

    private AnimatorSet mTransparentCardAnim;

    private SamsungPayEventsListener mSamsungPayEventsListener;

    private FlingAnimation mFlingHorizontal, mFlingVertical;

    public SamsungPayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SamsungPayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SamsungPayView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init(getContext());

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        imageAdapter.makeSureCardCache();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        imageAdapter.releaseImageCache();
    }

    public void relaseTimers() {
        mFlingVertical.destroyAnimation();
        mFlingHorizontal.destroyAnimation();
    }

    public void initFlingAnimation() {
        mFlingVertical.initializeAnimation();
        mFlingHorizontal.initializeAnimation();

        mFlingVertical.setVisibility(View.INVISIBLE);
        mFlingHorizontal.setVisibility(View.INVISIBLE);
    }

    public void initSamsungPay() {
        mTransparentCard.setY(this.getBottom());
        mTransparentCard.setVisibility(View.INVISIBLE);

        initFlingAnimation();

        mCardListRoot.setVisibility(View.INVISIBLE);
        mKnobContainer.setVisibility(View.INVISIBLE);
        mFlingVertical.setVisibility(View.INVISIBLE);
        hideSuper();

        updateSelectedCard(DEFAULT_CARD_SELECTION, false);

        mIsDraggingATransparentCard = false;

        resetTransparentCardAnim();
        enableScrolling();
    }

    private void resetTransparentCardAnim() {
        if (mTransparentCardAnim != null) {
            if (mTransparentCardAnim.isRunning()) {
                mTransparentCardAnim.cancel();
            }
            mTransparentCardAnim = null;
        }
    }

    public void setSamsungPayEventsListener(SamsungPayEventsListener samsungPayEventsListener) {
        this.mSamsungPayEventsListener = samsungPayEventsListener;
    }

    public void showSwipeLeftSuper() {
        mOverlayBackground.setVisibility(View.VISIBLE);
        mSuperSwipeLeft.setVisibility(View.VISIBLE);
        mFlingHorizontal.setVisibility(View.VISIBLE);
        mSuperTapCard.setVisibility(View.GONE);
    }

    public void showTapACardSuper() {
        mOverlayBackground.setVisibility(View.VISIBLE);
        mSuperSwipeLeft.setVisibility(View.GONE);
        mFlingHorizontal.setVisibility(View.GONE);
        mSuperTapCard.setVisibility(View.VISIBLE);
    }

    public void hideSuper() {
        mOverlayBackground.setVisibility(View.GONE);
        mSuperSwipeLeft.setVisibility(View.GONE);
        mFlingHorizontal.setVisibility(View.GONE);
        mSuperTapCard.setVisibility(View.GONE);
    }

    public void disableScrolling() {
        mCardList.setScrollable(false);
    }
    public void enableScrolling() {
        mCardList.setScrollable(true);
    }

    private void init(Context context) {
        mKnobContainer = findViewById(R.id.pay_lock_root);
        mKnob = findViewById(R.id.pay_knob);
        mTransparentCard = findViewById(R.id.pay_transparent_card);
        ((ImageView) mTransparentCard).setImageResource(TRANSPARENT_CARD_RESOURCE_ID);

        mCardListRoot = findViewById(R.id.pay_gallery_root);
        mCardList = (CustomizedGallery)findViewById(R.id.pay_cardlist);
        mCardSelectionIndicator = findViewById(R.id.pay_selectoin_indicator);

        mOverlayBackground = findViewById(R.id.pay_overlay_background);
        mSuperSwipeLeft = findViewById(R.id.pay_swipe_left);
        mSuperTapCard = findViewById(R.id.pay_tap_card);

        initGallery(context, mCardList);
        initCardSelectionIndicator(context, (RadioGroup) mCardSelectionIndicator);

        mFlingVertical = (FlingAnimation)findViewById(R.id.vertical_fling_anim_container);
        mFlingHorizontal = (FlingAnimation)findViewById(R.id.horizontal_fling_anim_container);
        initFlingAnimation();

        initSamsungPay();
    }

    private void initGallery(Context context, Gallery gallery) {
        imageAdapter = new CardImageAdapter(context);
        gallery.setAdapter(imageAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCardSelectionIndicator(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // it shouldn't happen.  Log it now.
                Log.e(TAG, "ERROR: No item is selected in Gallery View.  Investigate it.");
            }
        });

        gallery.setClickable(false);
        gallery.setLongClickable(false);
        ((CustomizedGallery)gallery).setGalleryTapListener(this);
        ((CustomizedGallery)gallery).setGalleryTouchListener(this);
    }
    private void updateCardSelectionIndicator(int index) {
        RadioGroup r = ((RadioGroup) mCardSelectionIndicator);
        int checkedIndex = r.getCheckedRadioButtonId();
        if (checkedIndex != index) {
            r.check(index);
        }
    }

    private void initCardSelectionIndicator(Context context, RadioGroup cardSelectionIndicator) {
        Resources res = getResources();

        int radioButtonWidth = res.getDimensionPixelSize(R.dimen.pay_selection_dot_width);
        int radioButtonHeight = res.getDimensionPixelSize(R.dimen.pay_selection_dot_height);
        int radioButtonHorizontalMargin = res.getDimensionPixelSize(R.dimen.pay_selection_dot_horizontal_margin);

        // add indicator
        int size = CARDS.size();
        for (int i = 0; i < size; i++) {
            RadioButton rb = new RadioButton(context);
            rb.setBackgroundResource(R.drawable.pay_selection_dots);
            rb.setButtonDrawable(0);

            RadioGroup.LayoutParams params
                    = new RadioGroup.LayoutParams(radioButtonWidth, radioButtonHeight);
            params.setMargins(radioButtonHorizontalMargin, 0, radioButtonHorizontalMargin, 0);
            rb.setLayoutParams(params);

            rb.setId(i);
            cardSelectionIndicator.addView(rb);
        }

        // cardSelectionIndicator.check(DEFAULT_CARD_SELECTION);
        cardSelectionIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateSelectedCard(checkedId, true);
            }
        });
    }

    public void updateSelectedCard(int position, boolean animate) {
        if (mCardList.getSelectedItemPosition() != position) {
            mCardList.setSelection(position, animate);
        }
    }

    private void animateTransparentCard(float targetPosY) {
        animateTransparentCard(targetPosY, null);
    }

    private void animateTransparentCard(float targetPosY, Animator.AnimatorListener animatorListener) {
        if (mTransparentCardAnim != null) {
            if (mTransparentCardAnim.isRunning()) {
                // only happens for time out.
                mTransparentCardAnim.cancel();
                mTransparentCard.setY(mTransparentCardInitY);
            }
            mTransparentCardAnim = null;
        }


        ObjectAnimator animY = ObjectAnimator.ofFloat(mTransparentCard, "y", targetPosY);
        mTransparentCardAnim = new AnimatorSet();
        long milliSeconds = Math.abs((long) ((mTransparentCard.getY() - targetPosY) / 5f));

        mTransparentCardAnim.setDuration(milliSeconds);
        if (animatorListener != null) {
            mTransparentCardAnim.addListener(animatorListener);
        }
        mTransparentCardAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mTransparentCardAnim.play(animY);

        mTransparentCardAnim.start();
    }

    private boolean isTouchingKnob(float touchX, float touchY) {
        if (mKnobContainer.getVisibility() != VISIBLE) {
            return false;
        }

        float knobX = mKnob.getX();
        float knobY = mKnob.getY();
        float knobTailX = knobX + mKnob.getWidth();
        float knobTailY = knobY + mKnob.getHeight();
        if (touchX < knobX || touchX > knobTailX || touchY < knobY || touchY > knobTailY) {
            return false;
        }

        mTouchStartY = touchY;
        mLastTouchY = touchY;
        return true;
    }

    private void showTransparentCard(float touchX, float touchY) {
        // 1. hide the knob
        mKnobContainer.setVisibility(GONE);
        mFlingVertical.setVisibility(GONE);

        resetTransparentCardAnim();

        // 2. show transparent card (transparent card is hidden below the screen.  scroll the card upward, and show partially)
        mTransparentCardInitY = this.getBottom() - INIT_TRANSPARENT_VISIBLE_HEIGHT;
        mTransparentCard.setY(this.getBottom());
        mTransparentCard.setVisibility(VISIBLE);
        animateTransparentCard(mTransparentCardInitY);

        mIsDraggingATransparentCard = true;
    }

    private float getTransparentCardMinY() {
        return mCardList.getY();
    }

    private void dragTransparentCard(float touchY) {
        if (mTransparentCardAnim != null) {
            if (mTransparentCardAnim.isRunning()) {
                float movedOffset = Math.abs(mLastTouchY - touchY);
                if (movedOffset > 10f) {
                    mTransparentCardAnim.cancel();
                    mTransparentCardAnim = null;
                }
            }
        }

        float movedOffsetFromLastPosY = mLastTouchY - touchY;
        if (movedOffsetFromLastPosY > GESTURE_UP_DOWN_THRESHOLD) {
            mLastUpwardGestureTime = System.currentTimeMillis();
        } else if (movedOffsetFromLastPosY < -GESTURE_UP_DOWN_THRESHOLD) {
            // swiping downward.  reset upward gesture time
            mLastUpwardGestureTime = 0l;
        }
        mLastTouchY = touchY;

        if (touchY > mTouchStartY) {
            // don't do anything.
            return;
        }

        float touchMoveOffsetY = mTouchStartY - touchY;
        float newY = mTransparentCardInitY - touchMoveOffsetY;

        // min Y should be the list of cards.
        newY = Math.max(newY, getTransparentCardMinY());

        mTransparentCard.setY(newY);
    }

    // user ended touching.  perform animation based on user's last gesture
    private void wrapUpDragging() {
        if (mTransparentCardAnim != null) {
            if (mTransparentCardAnim.isRunning()) {
                mTransparentCardAnim.cancel();
                mTransparentCardAnim = null;
                showKnob();
                return;
            }
            mTransparentCardAnim = null;
        }

        // 1. if a card is already dragged to the top Y position, show the list of cards
        if (Math.abs(mTransparentCard.getY() - getTransparentCardMinY()) < 2f) { // update 2f and make it a constant
            //showCardList();
            notifyTransparentCardAtTop();
            return;
        }

        boolean upward = false;
        // 2. if a card is reasonably dragged to the upward, animate dragging a card to upward
        upward |= mTransparentCard.getY() < this.getHeight() * 0.66f;    // update 0.66f and make it a constant

        // 3. if a customer's last gesture is swiping upward, animate dragging a card to upward
        upward |= (mLastUpwardGestureTime > 0 && mLastUpwardGestureTime < System.currentTimeMillis() + GESTURE_EFFECTIVE_TIME_THRESHOLD);

        // if it didn't meet the above conditions, it is most likely we have to animate dragging a card to downward.

        if (!upward) {
            showKnob();
            return;
        }

        if (mSamsungPayEventsListener != null) {
            mSamsungPayEventsListener.swipeUpAnimationStarted();
        }

        performDraggingTransparentCardAnimation(upward);
    }

    //    private void showCardList() {
//        mCardListRoot.setVisibility(VISIBLE);
//        mTransparentCard.setVisibility(GONE);
//    }
    private void notifyTransparentCardAtTop() {
        mTransparentCard.setVisibility(GONE);

        if (mSamsungPayEventsListener != null) {
            mSamsungPayEventsListener.swipUpAnimationEnded();
        }
    }

    public void showCardList() {
        mCardListRoot.setVisibility(VISIBLE);
        mKnobContainer.setVisibility(GONE);
        mFlingVertical.setVisibility(GONE);
    }

    public boolean isKnobVisible() {
        return mKnobContainer.getVisibility() == View.VISIBLE;
    }

    public void forceSwipingUpAnimation() {
        mKnobContainer.setVisibility(GONE);
        mFlingVertical.setVisibility(GONE);

        if (mIsDraggingATransparentCard) {
            mIsDraggingATransparentCard = false;
        } else {
            mTransparentCard.setY(this.getBottom());
        }

        mTransparentCard.setVisibility(VISIBLE);
        performDraggingTransparentCardAnimation(true);
    }

    public void showKnob() {
        mTransparentCard.setVisibility(INVISIBLE);
        mKnobContainer.setVisibility(VISIBLE);
        mFlingVertical.setVisibility(VISIBLE);
    }

    private void performDraggingTransparentCardAnimation(final boolean upward) {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // no additional work required.  ignore it.
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (upward) {
                    notifyTransparentCardAtTop();
                } else {
                    showKnob();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (upward) {
                    notifyTransparentCardAtTop();
                } else {
                    showKnob();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // it shouldn't happen
            }
        };
        float targetY = upward ? getTransparentCardMinY() : this.getBottom();
        animateTransparentCard(targetY, animatorListener);
    }

    private boolean isSwiping(float x, float y) {
        float offsetX = mTouchStartPosX - x;
        float offsetY = mTouchStartPosY - y;

//        if (offsetX * offsetX + offsetY * offsetY > SWIPING_SQUARED_DISTANCE_MIN) {
//            return true;
//        }
//        return false;

        return offsetX * offsetX + offsetY * offsetY > SWIPING_SQUARED_DISTANCE_MIN;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Bad code (is there a better way to do it??)
        // Issue: list/gallery should be scrollable when a user scroll anywhere on screen (not just card list area, but a whole screen)
        if (mCardListRoot.getVisibility() == VISIBLE) {
            // Temporary solution: pass touch event to gallery
            mCardList.onTouchEvent(event);
            return true;
        }

//        float touchX = event.getX();
//        float touchY = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (isTouchingKnob(touchX, touchY)) {
//                    if (mSamsungPayEventsListener != null) {
//                        mSamsungPayEventsListener.swipeUpTouchStarted();
//                    }
//                    showTransparentCard(touchX, touchY);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mIsDraggingATransparentCard) {
//                    dragTransparentCard(touchY);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_OUTSIDE:
//                if (mIsDraggingATransparentCard) {
//                    wrapUpDragging();
//
//                    mIsDraggingATransparentCard = false;
//                    return true;
//                }
//                break;
//        }
//        return super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchStartPosX = event.getX();
            mTouchStartPosY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mSamsungPayEventsListener != null  && isSwiping(event.getX(), event.getY())) {
                mSamsungPayEventsListener.swipeMotionDetected();
            }
        }


        if (mKnobContainer.getVisibility() == View.VISIBLE) {
            mKnobContainer.setVisibility(View.GONE);
            mFlingVertical.setVisibility(View.GONE);
            // TODO
            if (mSamsungPayEventsListener != null) {
                mSamsungPayEventsListener.swipeUpTouchStarted();
            }
        }

        return true;
    }

    @Override
    public void onGalleyTap() {
        if (mSamsungPayEventsListener != null) {
            mSamsungPayEventsListener.paymentSelected();
        }
    }

    @Override
    public void onTouchStarted() {
        hideSuper();
    }

    @Override
    public void onTouchMoved() {

    }

    @Override
    public void onTouchEnd() {

    }

    public static class CardImageAdapter extends CustomizedGalleryAdapter {
        final Context mContext;
        final int cardWidth;
        final int cardHeight;
        final int cardSpace;

        //ArrayList<View> mCachedViews;
        ArrayList<Bitmap> mCachedImages;

        public CardImageAdapter(Context context) {
            mContext = context;
            cardWidth = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_width);
            cardHeight = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_height);
            cardSpace = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_space_between_cards);

            initCardCache();

        }

        private synchronized void initCardCache() {
            mCachedImages = new ArrayList<>(CARDS.size());
            for (int card : CARDS) {
                Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), card);
                mCachedImages.add(b);
            }
        }

        public synchronized void releaseImageCache() {
            if (mCachedImages != null) {
                for (int cardIndex = mCachedImages.size()-1; cardIndex >= 0; cardIndex--) {
                    Bitmap b = mCachedImages.get(cardIndex);
                    mCachedImages.set(cardIndex, null);
                    if (b != null && !b.isRecycled()) {
                        b.recycle();
                    }
                    b = null;
                }
            }
            mCachedImages = null;
        }

        private synchronized void makeSureCardCache() {
            if (mCachedImages == null) {
                initCardCache();
            }
        }

        @Override
        public int getCount() {
            return CARDS.size();
        }

        @Override
        public Object getItem(int position) {
            return CARDS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getCoverFlowItem(int position, View reusableView, ViewGroup parent) {
            if (reusableView == null) {
                reusableView = new ImageView(mContext);
                reusableView.setLayoutParams(new Gallery.LayoutParams(cardWidth + cardSpace, cardHeight));
            }

            ((ImageView)reusableView).setImageBitmap(mCachedImages.get(position));
            return reusableView;
            //return mCachedViews.get(position);
        }
    }

    public interface SamsungPayEventsListener {
        void swipeUpTouchStarted();
        void swipeUpAnimationStarted();
        void swipUpAnimationEnded();
        void paymentSelected();
        void swipeMotionDetected();
    }
}
