package com.samsung.retailexperience.retailhero.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
public class SamsungPayView extends FrameLayout implements CustomizedGallery.GalleryTapListener {
    private static final String TAG = SamsungPayView.class.getSimpleName();

    private static final long TRANSPARENT_CARD_ANIM_DURATION = 250;

    // if a user moved this threshold, it is up or down motion (or swiping)
    private static final float GESTURE_UP_DOWN_THRESHOLD = 20f;
    // if the last gesture happened in last 300 milli second, it is effective gesture
    private static final long GESTURE_EFFECTIVE_TIME_THRESHOLD = 300l;
    private static final float INIT_TRANSPARENT_VISIBLE_HEIGHT = 300f;

    private static final List<Integer> CARDS = new ArrayList<>(2);
    static {
        CARDS.add(R.drawable.pay_card_2);
        CARDS.add(R.drawable.pay_card_1);
        CARDS.add(R.drawable.pay_card_3);
        CARDS.add(R.drawable.pay_card_4);
    }
    private static final int TRANSPARENT_CARD_RESOURCE_ID = R.drawable.pay_card_1;

    private View mKnob;
    private View mTransparentCard;
    private View mCardListRoot;
    private View mCardList;
    private View mCardSelectionIndicator;
    private View mLockScreen;

    private boolean mIsDraggingATransparentCard;
    // touch X position is not necessarily since card is moving only up and downwards.
    private float mTouchStartY;
    private float mLastTouchY;
    private long mLastUpwardGestureTime;
    private float mTransparentCardInitY;

    private SamsungPayEventsListener mSamsungPayEventsListener;

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

    public void setSamsungPayEventsListener(SamsungPayEventsListener samsungPayEventsListener) {
        this.mSamsungPayEventsListener = samsungPayEventsListener;
    }

    private void init(Context context) {
        mKnob = findViewById(R.id.pay_knob);
        mTransparentCard = findViewById(R.id.pay_transparent_card);
        // TODO hide transparent card below the screen
        mTransparentCard.setX(this.getBottom());
        ((ImageView) mTransparentCard).setImageResource(TRANSPARENT_CARD_RESOURCE_ID);

        mCardListRoot = findViewById(R.id.pay_gallery_root);
        mCardList = findViewById(R.id.pay_cardlist);
        mCardSelectionIndicator = findViewById(R.id.pay_selectoin_indicator);
        mLockScreen = findViewById(R.id.pay_lock_root);

        initGallery(context, (Gallery) mCardList);
        initCardSelectionIndicator(context, (RadioGroup) mCardSelectionIndicator);
    }

    private void initGallery(Context context, Gallery gallery) {
        CardImageAdapter adapter = new CardImageAdapter(context);
        gallery.setAdapter(adapter);
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

        cardSelectionIndicator.check(0);
        cardSelectionIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateSelectedCard(checkedId, true);
            }
        });
    }

    public void updateSelectedCard(int position, boolean animate) {
        Gallery g = ((Gallery) mCardList);
        if (g.getSelectedItemPosition() != position) {
            g.setSelection(position, animate);
        }
    }

    private void animateTransparentCard(float targetPosY) {
        animateTransparentCard(targetPosY, null);
    }

    private void animateTransparentCard(float targetPosY, Animator.AnimatorListener animatorListener) {
        ObjectAnimator animY = ObjectAnimator.ofFloat(mTransparentCard, "y", targetPosY);
        AnimatorSet animSet = new AnimatorSet();
        long milliSeconds = Math.abs((long) ((mTransparentCard.getY() - targetPosY) / 6f));
        animSet.setDuration(milliSeconds);
        if (animatorListener != null) {
            animSet.addListener(animatorListener);
        }
        animSet.play(animY);

        animSet.start();
    }

    private boolean isTouchingKnob(float touchX, float touchY) {
        if (mKnob.getVisibility() != VISIBLE) {
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
        mKnob.setVisibility(GONE);

        // 2. show transparent card (transparent card is hidden below the screen.  scroll the card upward, and show partially)
        mTransparentCardInitY = this.getBottom() - INIT_TRANSPARENT_VISIBLE_HEIGHT;
        mTransparentCard.setY(this.getBottom());
        animateTransparentCard(mTransparentCardInitY);
        mTransparentCard.setVisibility(VISIBLE);

        mIsDraggingATransparentCard = true;
    }

    private float getTransparentCardMinY() {
        return mCardList.getY();
    }

    private void dragTransparentCard(float touchY) {
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

        if (upward && mSamsungPayEventsListener != null) {
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
        mLockScreen.setVisibility(GONE);
    }

    public void forceSwipingUpAnimation() {
        mKnob.setVisibility(GONE);

        mTransparentCard.setY(this.getBottom());
        mTransparentCard.setVisibility(VISIBLE);
        performDraggingTransparentCardAnimation(true);
    }

    private void showKnob() {
        mTransparentCard.setVisibility(INVISIBLE);
        mKnob.setVisibility(VISIBLE);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Bad code (is there a better way to do it??)
        // Issue: list/gallery should be scrollable when a user scroll anywhere on screen (not just card list area, but a whole screen)
        if (mCardListRoot.getVisibility() == VISIBLE) {
            // Temporary solution: pass touch event to gallery
            mCardList.onTouchEvent(event);
            return true;
        }

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchingKnob(touchX, touchY)) {
                    if (mSamsungPayEventsListener != null) {
                        mSamsungPayEventsListener.swipeUpTouchStarted();
                    }
                    showTransparentCard(touchX, touchY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDraggingATransparentCard) {
                    dragTransparentCard(touchY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (mIsDraggingATransparentCard) {
                    wrapUpDragging();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onGalleyTap() {
        if (mSamsungPayEventsListener != null) {
            mSamsungPayEventsListener.paymentSelected();
        }
    }

    public static class CardImageAdapter extends CustomizedGalleryAdapter {

        final Context mContext;
        final int cardWidth;
        final int cardHeight;
        final int cardSpace;
        public CardImageAdapter(Context context) {
            mContext = context;
            cardWidth = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_width);
            cardHeight = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_height);
            cardSpace = mContext.getResources().getDimensionPixelSize(R.dimen.pay_card_space_between_cards);
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

            ((ImageView)reusableView).setImageResource(CARDS.get(position));
            return reusableView;
        }
    }

    public interface SamsungPayEventsListener {
        void swipeUpTouchStarted();
        void swipeUpAnimationStarted();
        void swipUpAnimationEnded();
        void paymentSelected();
    }
}
