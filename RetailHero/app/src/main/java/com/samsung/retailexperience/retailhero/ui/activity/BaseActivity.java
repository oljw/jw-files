package com.samsung.retailexperience.retailhero.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.RetailHeroApplication;
import com.samsung.retailexperience.retailhero.config.ConfigProvider;
import com.samsung.retailexperience.retailhero.config.environment.Environments;
import com.samsung.retailexperience.retailhero.config.environment.IEnvironments;
import com.samsung.retailexperience.retailhero.config.setting.ISettings;
import com.samsung.retailexperience.retailhero.iHeroService;
import com.samsung.retailexperience.retailhero.iHeroServiceCallback;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.DeviceSpecsFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ResourceUtil;
import com.samsung.retailexperience.retailhero.util.TimerHandler;
import com.samsung.retailexperience.retailhero.util.TopExceptionHandler;
import com.samsung.retailexperience.retailhero.view.CustomFontTextView;

import java.util.ArrayList;

/**
 * Created by icanmobile on 1/12/16.
 */
public abstract class BaseActivity extends Activity implements ResourceUtil.MissingResourceListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected IEnvironments gEnvMgr;
    protected ISettings gSetMgr;
    protected ResourceUtil gResMgr;

    protected BaseFragment mFragment = null;
    protected ArrayList<BaseFragment> mFragments = new ArrayList<BaseFragment>();
    protected Handler mFragmentsHandler = null;
    protected Runnable mFragmentsRunnable = null;

    protected DrawerLayout mDrawerLayout = null;
    protected ListView mNavigationView = null;
    protected DrawerAdapter mDrawerAdapter = null;

    public int mDisplayW;
    public int mDisplayH;

    /**
     *  when user don't have interaction, we are calling the attractor loop
     */
    private TimerHandler mUserInterActionTimer = null;
    private boolean mIsTimerout = false;
    private static final int NO_INTERACTION_TIMEOUT = 30000;
    private static final Class[] NO_INTERACTION_DETECT_CLASS = {
            MenuFragment.class,
            DeviceSpecsFragment.class
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(null));

        getGlobalVariables();
        setWindowFeatures();
        getDisplaySize();
        screenStayOn(true);
        setMissingContentListener();

        // for no interaction
        if (mUserInterActionTimer == null) {
            mUserInterActionTimer = new TimerHandler();
            mUserInterActionTimer.setTimeout(NO_INTERACTION_TIMEOUT);
            mUserInterActionTimer.setOnTimeoutListener(new TimerHandler.OnTimeoutListener() {
                @Override
                public void onTimeout() {
                    if (mFragment != null) {
                        //Log.d(TAG, "@@@ Timeout :" + mFragment.getClass().getSimpleName());

                        // searching the no interaction class
                        for (Class objClass : NO_INTERACTION_DETECT_CLASS) {
                            if (objClass.equals(mFragment.getClass())) {
                                Log.d(TAG, "@@@ user interaction didn't occur for " + NO_INTERACTION_TIMEOUT + "ms");
                                closeDrawer();
                                mFragment.changeFragment(AppConst.UIState.UI_STATE_ATTRACT_LOOP,
                                        AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                            }
                        }
                        mIsTimerout = true;
                    }
                }
            });
        }

        mFragmentsHandler = new Handler();
        mFragmentsRunnable = new Runnable() {
            @Override
            public void run() {
                removeFragments();
            }
        };

        if (isOverlayGranted()) {
            startHeroService();
            bindHeroService();
        }
        else {
            requestOverlayPermission();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "##### onStart)+ ");

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "##### onStop)+ ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        screenStayOn(false);
        unbindHeroService();

        // for no interaction
        // clear timer for the no interaction
        if (mUserInterActionTimer != null) {
            mUserInterActionTimer.stop();
            mUserInterActionTimer.setOnTimeoutListener(null);
            mUserInterActionTimer = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "##### onResume)+ ");

        // for no interaction
        if (mUserInterActionTimer != null) {
            Log.d(TAG, "@@@ : restart timer");
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "##### onPause)+ ");

        // for no interaction : stop a timer
        if (mUserInterActionTimer != null) {
            Log.d(TAG, "@@@ : stop timer");
            mUserInterActionTimer.stop();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "##### onBackPressed)+ ");

        super.onBackPressed();
    }

    @Override
    public void onUserInteraction() {
        // for no interaction
        if (mFragment != null) {
            if (mIsTimerout == false) {
                Log.d(TAG, "@@@ User Interaction");
                mUserInterActionTimer.start();
            }
        }
        super.onUserInteraction();
    }

    // for no interaction
    protected void OnChangeFragment(AppConst.UIState newState, AppConst.UIState oldState, AppConsts.TransactionDir dir) {
        if (mUserInterActionTimer != null) {
            Log.d(TAG, "@@@ Changed Fragment");
            mIsTimerout = false;
            mUserInterActionTimer.start();
        }
    }

    public void insertFragment(BaseFragment fragment) {
        mFragments.add(fragment);
    }
    public void removeFragments() {
        for (int f=mFragments.size()-2; f>=0; f--) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mFragments.get(f));
            ft.commit();
            mFragments.remove(f);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onBackKeyPressed())
                return true;

            if (mFragment != null)
                mFragment.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public abstract boolean onBackKeyPressed();

    @Override
    public void onMissingResource(String missingfile) {
        Log.d(TAG, "onMissingResource : " + missingfile);

        /**
         *  go to a missing page (activity? or fragment?)
         */
        Intent intent = new Intent(this, MissingContentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(AppConsts.ACTION_MISSING_FILE);
        intent.putExtra(AppConsts.WHAT_MISSING_FILE, missingfile);
        startActivity(intent);
        overridePendingTransition(0, R.anim.identity_animation);
        finish();
    }

    public void selfFinish() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /*
     * Activity Environment
     */
    private void getGlobalVariables() {
        gEnvMgr = ((ConfigProvider)getApplication()).getEnvironmentConfig();
        gSetMgr = ((ConfigProvider)getApplication()).getSettingsManager();
        gResMgr = ((ConfigProvider)getApplication()).getResourceUtil();

        Log.i(TAG, "Activity Target : " + gEnvMgr.getStringValue(Environments.FLAVOR));
    }

    public void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void screenStayOn(boolean on) {
        if (on) {
            //WindowManager.LayoutParams lp = getWindow().getAttributes();
            //lp.screenBrightness = 1.0f;
            //getWindow().setAttributes(lp);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void getDisplaySize() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mDisplayW = metrics.widthPixels;
        mDisplayH = metrics.heightPixels;

//        Log.d(TAG, "##### mDisplayW = " + mDisplayW);
//        Log.d(TAG, "##### mDisplayH = " + mDisplayH);
    }

    private void setMissingContentListener() {
        if (gResMgr != null) {
            gResMgr.setMissingResourceListener(this);
        }
    }

    //http://developer.android.com/intl/ko/reference/android/provider/Settings.html#ACTION_MANAGE_OVERLAY_PERMISSION
    //Unable to add window android.view.ViewRoot$W@44da9bc0 -- permission denied for this window type
    private int REQUEST_OVERLAY_PERMISSION = 100;
    public boolean isOverlayGranted() {
        if (Settings.canDrawOverlays(this))
            return true;
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                startHeroService();
                bindHeroService();
            }
        }
    }




    /*
     * Navigation Drawer
     */
    protected void setupDrawerLayout(ArrayList<DrawerItem> drawerItems) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.setDrawerListener(drawerListener);

        mNavigationView = (ListView) findViewById(R.id.navigation_view);
        mNavigationView.setOnItemClickListener(new DrawerItemClickListener());

        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.drawer_header, null, false);
        mNavigationView.addHeaderView(header, null, false);

        mDrawerAdapter = new DrawerAdapter(this, drawerItems);
        mNavigationView.setAdapter(mDrawerAdapter);
    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            onSlideDrawer(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
    public abstract void onSlideDrawer(View drawerView, float slideOffset);

    public void setDrawer(String drawerId) {
        if (drawerId != null) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mNavigationView);
        }
        else {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);
            }
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mNavigationView);
        }
    }

    protected boolean closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
            return true;
        }
        return false;
    }

    public void clickDrawerBtn() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView))
            mDrawerLayout.closeDrawer(mNavigationView);
        else
            mDrawerLayout.openDrawer(mNavigationView);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position -= mNavigationView.getHeaderViewsCount();
            onSelectDrawerItem(position);
            mNavigationView.setItemChecked(position, true);
            closeDrawer();
        }
    }
    public abstract void onSelectDrawerItem(int position);

    class DrawerItem {
        Bitmap icon = null;
        String name = null;
        String action = null;

        public DrawerItem(Bitmap icon, String name, String action){
            this.icon = icon;
            this.name = name;
            this.action = action;
        }

        public Bitmap getIcon() {
            return this.icon;
        }
        public void setIcon(Bitmap icon) {
            this.icon = icon;
        }

        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getAction() {
            return this.action;
        }
        public void setAction(String action) {
            this.action = action;
        }
    }

    class DrawerAdapter extends ArrayAdapter<DrawerItem> {
        private Context context = null;
        private ArrayList<DrawerItem> items = null;

        public DrawerAdapter(Context context, ArrayList<DrawerItem> items) {
            super( context, -1, items );
            this.context = context;
            this.items = items;
        }
        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public DrawerItem getItem(int position) {
            return this.items.get( position );
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void setItems(ArrayList<DrawerItem> menuItems) {
            this.items = menuItems;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rootView = inflater.inflate(R.layout.drawer_item, parent, false);
            ((ImageView) rootView.findViewById(R.id.drawer_item_icon)).setImageBitmap(items.get(position).getIcon());
            ((CustomFontTextView) rootView.findViewById(R.id.drawer_item_name)).setText(items.get(position).getName());

            //design debugging
//            switch (position) {
//                case 0:
//                    rootView.setBackgroundColor(0x77FF0000);
//                    break;
//
//                case 1:
//                    rootView.setBackgroundColor(0x7700FF00);
//                    break;
//
//                case 2:
//                    rootView.setBackgroundColor(0x770000FF);
//                    break;
//
//                case 3:
//                    rootView.setBackgroundColor(0x77777777);
//                    break;
//            }
            return rootView;
        }
    }




    /*
     * service bind function
     */
    private static final String HERO_SERVICE = "com.samsung.retailexperience.retailhero.HERO_SERVICE";
    private iHeroService mService = null;
    private boolean mIsBound = false;

    public iHeroService getService() {
        return mService;
    }

    private void startHeroService() {
        Intent serviceIntent = new Intent(HERO_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);

        Bundle bundle = new Bundle();
        bundle.putString(AppConsts.ARG_START_SERVICE, AppConsts.ArgStartService.SYSTEM_REBOOT.name());
        serviceIntent.putExtras(bundle);

        RetailHeroApplication.getContext().startService(serviceIntent);
    }
    private void stopHeroService() {
        Intent serviceIntent = new Intent(HERO_SERVICE);
        ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
        serviceIntent.setPackage(ri.serviceInfo.packageName);
        RetailHeroApplication.getContext().stopService(serviceIntent);
    }
    private void bindHeroService() {
        if (mIsBound == false) {
            Intent serviceIntent = new Intent(HERO_SERVICE);
            ResolveInfo ri = getPackageManager().resolveService(serviceIntent, 0);
            serviceIntent.setPackage(ri.serviceInfo.packageName);
            RetailHeroApplication.getContext().bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }
    private void unbindHeroService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    mService.unregisterCallback(mServiceCallback);
                    mService = null;
                    RetailHeroApplication.getContext().unbindService(mServiceConnection);
                } catch (RemoteException e) {
                    e.printStackTrace();;
                }
            }
            mIsBound = false;
        }
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = iHeroService.Stub.asInterface(service);
            try {
                mService.registerCallback(mServiceCallback);
                mIsBound = true;
                serviceConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            mIsBound = false;
        }
    };

    private iHeroServiceCallback mServiceCallback = new iHeroServiceCallback() {
        //sample function
        @Override
        public void responseServiceFunc() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    public abstract void serviceConnected();
}
