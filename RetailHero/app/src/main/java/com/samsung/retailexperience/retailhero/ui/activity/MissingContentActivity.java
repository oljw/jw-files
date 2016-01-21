package com.samsung.retailexperience.retailhero.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.TimerHandler;


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
            if (getIntent().getAction().equals(AppConsts.ACTION_MISSING_FILE)) {
                mMissingFile = getIntent().getStringExtra(AppConsts.WHAT_MISSING_FILE);
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
                                goToMainActivity(0, R.anim.identity_animation, true);
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
    public void onBackPressed() {
        super.onBackPressed();

        // go to the attractor activity
        goToMainActivity(0, R.anim.identity_animation, true);
    }

    @Override
    public void serviceConnected() {
        //we know that service is connected
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

    private void goToMainActivity(int enterAnim, int exitAnim, boolean finsh) {
        Intent intent = new Intent(this,  MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
        if (finsh) {
            finish();
        }
    }
}
