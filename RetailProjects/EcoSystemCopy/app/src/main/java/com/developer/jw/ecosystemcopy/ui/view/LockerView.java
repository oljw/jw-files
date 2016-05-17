package com.developer.jw.ecosystemcopy.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.developer.jw.ecosystemcopy.R;

/**
 * Created by icanmobile on 4/6/16.
 */
public class LockerView extends RelativeLayout {
    private static final String TAG = LockerView.class.getSimpleName();

    private Context mContext = null;
    private RelativeLayout mParent = null;
    private EditText mPassword = null;
    private Button mOk = null;

    private static String PASSWORD = "Nextisnow";
    public LockerViewListener mListener = null;

    public LockerView(Context context) {
        super(context);
        init(context);
    }

    public LockerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LockerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mParent = this;
        RelativeLayout view = (RelativeLayout) inflate(getContext(), R.layout.view_locker, this);

        mPassword = (EditText) view.findViewById(R.id.password);
        mPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) hideKeyboard(mParent);
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resetInputTimeOut();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mOk = (Button) view.findViewById(R.id.ok);
        mOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(mParent);
                removeMessages();
                if (mPassword.getText().toString().equals(PASSWORD))
                    getListener().onLockerMatch(true);
                else
                    getListener().onLockerMatch(false);
            }
        });
    }

    public void show() {
        if (mPassword != null) mPassword.setText("");
        resetCount();
        resetInputTimeOut();
    }

    public void hide() {
        if (mPassword != null) mPassword.setText("");
        resetCount();
        removeMessages();
    }

    public interface LockerViewListener {
        void onLockerShow();
        void onLockerMatch(boolean bMatched);
        void onLockerHide();
    }
    public LockerViewListener getListener() {
        return mListener;
    }
    public void setListener(LockerViewListener listener) {
        mListener = listener;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private boolean mCounting = false;
    private int MAX_COUNT = 10;
    private int mCount = MAX_COUNT;
    private Toast toast = null;
    public void setKeyDown(int keyCode) {
        if (isShown()) return;

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            initCount();
        }
        if (mCounting && keyCode == KeyEvent.KEYCODE_BACK) {
            decreaseCount();
        }
    }
    public void setKeyUp(int keyCode) {
        if (isShown()) return;
        if (mCounting && keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            resetCount();
        }
    }

    public void initCount() {
        mCounting = true;
        mCount = MAX_COUNT;
        resetKeyDownTimeOut();
    }
    public void decreaseCount() {
        resetKeyDownTimeOut();
        if (--mCount <= 0) {
            if (toast != null) toast.cancel();
            getListener().onLockerShow();
        }

        if (mCount == 0) {
            if (toast != null) toast.cancel();
            String message = new StringBuilder()
                    .append("You are now in exit mode!")
                    .toString();
            toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (mCount <= 3) {
            if (toast != null) toast.cancel();
            String message = new StringBuilder()
                    .append("You are now ")
                    .append(Integer.toString(mCount))
                    .append(" steps away from exit mode!")
                    .toString();
            toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void resetCount() {
        removeMessages();
        mCounting = false;
        mCount = MAX_COUNT;
    }

    private static int INPUT_TIME_OUT = 20000;
    private static int KEY_DOWN_TIME_OUT = 5000;
    private static final int MSG_INPUT_TIME_OUT = 1;
    private static final int MSG_KEY_DOWN_TIME_OUT = 2;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INPUT_TIME_OUT:
                {
                    getListener().onLockerHide();
                }
                break;

                case MSG_KEY_DOWN_TIME_OUT:
                {
                    getListener().onLockerHide();
                }
                break;
            }
        }
    };

    private void resetInputTimeOut() {
        removeMessages();
        handler.sendEmptyMessageDelayed(MSG_INPUT_TIME_OUT, INPUT_TIME_OUT);
    }
    private void resetKeyDownTimeOut() {
        removeMessages();
        handler.sendEmptyMessageDelayed(MSG_KEY_DOWN_TIME_OUT, KEY_DOWN_TIME_OUT);
    }
    private void removeMessages() {
        handler.removeCallbacksAndMessages(null);
    }
}
