package com.samsung.retailexperience.retailtmo.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.samsung.retailexperience.retailtmo.R;
import com.samsung.retailexperience.retailtmo.ui.activity.BaseActivity;
import com.samsung.retailexperience.retailtmo.ui.activity.MainActivity;
import com.samsung.retailexperience.retailtmo.util.AppConst;
import com.samsung.retailexperience.retailtmo.video.config.environment.Environments;
import com.samsung.retailexperience.retailtmo.video.util.TimerHandler;


/**
 * Created by smheo on 9/29/2015.
 */
public class MissingContentActivity extends BaseActivity {

    private static final String TAG = "MissingContent";

    //private String mContentKey;
    private String mMissingFile;
    private TimerHandler mTimer;
    private static final int DEFAULT_TIME_TO_CHECK_CONTENTS = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_content);

        // get activity name
        String action = getIntent().getAction();
        if (action != null) {
            if (getIntent().getAction().equals(AppConst.ACTION_MISSING_FILE)) {
                mMissingFile = getIntent().getStringExtra(AppConst.WHAT_MISSING_FILE);
            }

            TextView missingTextView = (TextView) findViewById(R.id.filenameTv);
            if(missingTextView != null && mMissingFile != null) {
                missingTextView.setText(mMissingFile);
            }

            //Log.d(TAG, "Missing Activity Name : " + mContentKey);

            //[[ set timer
            mTimer = new TimerHandler();
            if (mTimer != null) {
                mTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                    @Override
                    public void onTimeout() {
                        if (mMissingFile == null) {
                            Log.e(TAG, "resource is null");
                            return;
                        }

                        if (gResMgr != null) {
                            if (gResMgr.isMissingFile(gResMgr.getContentFilePath(mMissingFile))) {
                                if (mTimer != null) {
                                    startSearchTimer();
                                    Log.d(TAG, mMissingFile + " : checking again the presence of the missing contents");
                                }
                            } else {
                                Log.d(TAG, mMissingFile + " are confirmed presence");

                                // go attractor activity
                                goToMainActivity(0, R.animator.identity_animation, true);
                            }
                        }
                    }
                });
            }
            //]] END set timer
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTimer != null) {
            startSearchTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.setOnTimeoutListener(null);
            mTimer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // go to the attractor activity
            goToMainActivity(0, R.animator.identity_animation, true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void startSearchTimer() {
        if (mTimer != null) {
            int setTime;
            if (gEnvMgr != null) {
                setTime = Integer.parseInt(gEnvMgr.getStringValue(Environments.SEARCH_PERIOD));
            } else {
                setTime = DEFAULT_TIME_TO_CHECK_CONTENTS;
            }
            mTimer.start(setTime);
        }
    }

    private void goToMainActivity(int enterAnim, int exitAnim, boolean finish) {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
        if (finish) {
            finish();
        }
    }
}
