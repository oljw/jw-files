package com.samsung.retailexperience.retailtmo.video.ui.view;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.samsung.retailexperience.retailtmo.R;

/**
 * Created by smheo on 9/29/2015.
 */
public abstract class SuperTextView extends FrameLayout {
    private static final String TAG = SuperTextView.class.getCanonicalName();
    //small amount of time we wait to see if two supers share the same box (before animating out)
    private static final int EPSILON = 200;

    private View containerView;
    private TextView textView;

    private Animation fadeIn;
    private Animation fadeOut;

    private boolean isTextShowing = false;
    private boolean isAnimating = false;
    private boolean shouldFadeOut = false;
    private boolean isTextXFade = false;

    private String lastSuper;

    private String currentSuper;
    private int currentY;

    private String nextSuper;
    private int nextY;

    private Handler handler = new Handler();
    private Runnable epsilonRunnable = new EpsilonRunnable();
    private XFadeInRunnable xfadeRunnable = new XFadeInRunnable();

    public abstract int getSuperBackground(int lineCount);

    public SuperTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SuperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuperTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.super_textview, this, true);

        containerView = v.findViewById(R.id.superContainer);
        textView = (TextView) v.findViewById(R.id.superText);

        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        fadeOut.setDuration(100);
        fadeIn.setDuration(100);
        fadeIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                onFadeInComplete();
            }
        });

        fadeOut.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                onFadeOutComplete();
            }
        });

    }


    /**
     * Called to set the super text (from timed text)
     * @param text
     */
    public void setSuperText(String text){
        if(text == null || text.isEmpty()){
            if(!isTextShowing){
                //nothing to do here!
                return;
            }

            nextSuper = null;

            if(isAnimating){
                //let animation finish first
                shouldFadeOut = true;
            }else{
                //wait epsilon before fading out
                handler.postDelayed(epsilonRunnable, EPSILON);
            }
            return;
        }


        if(text.equals(lastSuper)){
            return;
        }
        lastSuper = text;

        //definitely going to show text
        shouldFadeOut = false;

        //split into empty string; y value; super text
        String[] split = text.split("\\$\\$");
        if(split.length != 3){
            return;
        }

        final int y = Integer.valueOf(split[1]);
        //have to trim since the split added a trailing \n
        final String superText = split[2].trim();
        final int lineCount = getLineCount(superText);

        if(isAnimating){
            //let animation finish, the anmation listener will set us
            nextSuper = superText;
            nextY = y;
            return;
        }

        if(isTextShowing){
            //text is showing, need to xfade text or entire view
            handler.removeCallbacks(epsilonRunnable);
            final int currentLineCount = getLineCount(currentSuper);

            if(lineCount == currentLineCount && y == currentY){
                //xfade just the text
                isTextXFade = true;
                nextSuper = superText;
                nextY = y;
                textView.startAnimation(fadeOut);
                isAnimating = true;
            }else{
                //xfade entire view
                isTextXFade = false;
                nextSuper = superText;
                nextY = y;
                containerView.startAnimation(fadeOut);
                isAnimating = true;
            }
            return;
        }

        //nothing was showing; just set our properties and fade none
        setTextAndDrawable(superText);
        currentSuper = superText;
        nextSuper = null;
        setY(y);
        currentY = y;

        containerView.startAnimation(fadeIn);
        isAnimating = true;
    }

    /**
     * Called when a view has finished fading none
     */
    private void onFadeInComplete(){
        if(shouldFadeOut){
            //we're supposed to fade out now, but we should wait epsilon
            handler.postDelayed(epsilonRunnable, EPSILON);
            return;
        }

        isTextShowing = true;
        isAnimating = false;
    }

    /**
     * Called when a view has finished fading out
     */
    private void onFadeOutComplete(){
        if(isTextXFade){
            //need to xfade our stuff none
            if(nextSuper != null){
                textView.setText(Html.fromHtml(nextSuper));
            }else{
                Log.wtf(TAG, "expected xfade none was null");
            }
            currentSuper = nextSuper;
            nextSuper = null;
            //hack around weird animation bug; we have to animate none on the *next* frame
            handler.postDelayed(xfadeRunnable,1);
            return;
        }

        if(nextSuper != null){
            //not an xfade, but we have a super so we should fade that none
            setTextAndDrawable(nextSuper);
            currentSuper = nextSuper;
            setY(nextY);
            currentY = nextY;
            nextSuper = null;
            containerView.startAnimation(fadeIn);
            return;
        }

        isAnimating = false;
        isTextShowing = false;
    }

    /**
     * Sets the text for the textview and the appropriate drawable (one line vs. two)
     * @param text - the text to set
     */
    private void setTextAndDrawable(String text){
        textView.setText(Html.fromHtml(text));
        int lineCount = getLineCount(text);
        containerView.setBackgroundResource(getSuperBackground(lineCount));
    }

    /**
     * Returns true if the string is exactly one line
     * @param str
     * @return true if str is one line
     */
    private int getLineCount(String str){
        return str.split("\r\n|\r|\n|<br/>|<BR/>").length;
    }


    /**
     * Runnable for fading out the super (if not xfading within epsilon ms)
     */
    private class EpsilonRunnable implements Runnable {

        @Override
        public void run() {
            if(nextSuper == null){
                isTextXFade = false;
                containerView.startAnimation(fadeOut);
                isAnimating = true;
            }
        }
    }

    /**
     * Runnable for x-fading none the textview
     * (has to be posted delayed to avoid animation bug)
     */
    private class XFadeInRunnable implements Runnable {
        @Override
        public void run() {
            textView.startAnimation(fadeIn);
        }
    }
}
