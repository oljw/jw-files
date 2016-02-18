package com.samsung.retailexperience.retailhero.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.DeviceModel;
import com.samsung.retailexperience.retailhero.gson.models.DeviceSpecModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.async.MainMenuAsyncTask;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.AttractorFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.DeviceSpecsFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.LandscapeVideoFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.SelectDeviceFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.SmartSwitchFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_CameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_DisplayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_HeadTurningDesignFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_IP68Fragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives.B2B_KnoxFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives.B2B_SamsungPayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity.B2B_AllDayBatteryFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity.MoreStorageFragment;
import com.samsung.retailexperience.retailhero.util.AnimationUtil;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ModelUtil;
import com.samsung.retailexperience.retailhero.view.HelpLayout;
import com.samsung.retailexperience.retailhero.view.PaginationView;
import com.samsung.retailexperience.retailhero.view.PaginationViewItem;
import com.samsung.retailexperience.retailhero.view.SlideFrameLayout;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private AppConst.UIState mUIState = AppConst.UIState.UI_STATE_NONE;
    private FrameLayout mFragmentView = null;

    private PaginationView<MenuModel> mPaginationView = null;
    private LinearLayout mPaginationDotsLayout = null;

    private SlideFrameLayout mDeviceSpecContainer = null;

    private HelpLayout mHelpLayout = null;
    private boolean mShowMainMenuHelp = false;

    private AppConst.UIState mPrevUIState = AppConst.UIState.UI_STATE_NONE;
    private AppConst.UIState mMainMenuState = AppConst.UIState.UI_STATE_WHATS_NEW_MAIN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "##### onCreate)+ ");

        mContext = this;
        setContentView(R.layout.activity_main);
        setupDrawerLayout(getDrawerItems());

        mPaginationView = (PaginationView) findViewById(R.id.paginationView);
        mPaginationDotsLayout = (LinearLayout) findViewById(R.id.dots_layout);

        mDeviceSpecContainer = (SlideFrameLayout) findViewById(R.id.deviceSpecContainer);

        mHelpLayout = (HelpLayout) findViewById(R.id.help_layout);

        mFragmentView = (FrameLayout) findViewById(R.id.fragmentContainer);

        mDevice = AppConsts.DeviceModel.GALAXY_S7;

        changeFragment(getUIState(), AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        super.onDestroy();
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        Log.d(TAG, "##### onNewIntent)+");

        super.onNewIntent(intent);
        setIntent(intent);

        AppConsts.TransactionDir dir = AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD;
        AppConst.UIState newState = getUIState();
        if (newState == AppConst.UIState.UI_STATE_ATTRACT_LOOP || newState == AppConst.UIState.UI_STATE_WHATS_NEW_MAIN)
            dir = AppConsts.TransactionDir.TRANSACTION_DIR_NONE;

        changeFragment(newState, dir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onBackKeyPressed() {
        return closeDrawer();
    }

    @Override
    public void serviceConnected() {
        //we know that service is connected
    }

    public AppConst.UIState getUIState() {
        //default UI state
        AppConst.UIState newState = AppConst.UIState.UI_STATE_ATTRACT_LOOP;

        //check bundle args
        if (getIntent().getExtras() != null &&
                getIntent().getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT) != null &&
                getIntent().getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT).length() > 0) {
            newState = AppConst.UIState.valueOf(getIntent().getExtras().getString(AppConsts.ARG_NEXT_FRAGMENT));
            Log.d(TAG, "##### getUIState : ARG_NEXT_FRAGMENT : " + newState.name());
        }
        return newState;
    }


    /*
     * Control Commands one by one
     */
    private boolean mRunningCommand = false;
    private final int FINISH_COMMAND_MSG = 1;
    private int mLoopCount = 0;
    private int MAX_LOOP_COUNT = 5;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_COMMAND_MSG:
                {
                    if (AnimationUtil.isAnimationsRunning()) {
                        if (mLoopCount >= MAX_LOOP_COUNT) {
                            mRunningCommand = false;
                            mLoopCount = 0;
                            if (mPaginationView != null)
                                mPaginationView.setFreeze(false);
                            return;
                        }

                        mLoopCount++;
                        mHandler.sendEmptyMessageDelayed(FINISH_COMMAND_MSG, 100);
                        return;
                    }

                    mRunningCommand = false;
                    mLoopCount = 0;
                    if (mPaginationView != null)
                        mPaginationView.setFreeze(false);
                }
                break;
            }
        }
    };
    public void changeFragment(AppConst.UIState newState, AppConsts.TransactionDir dir) {
        if (mRunningCommand) return;
        mRunningCommand = true;

        if (mPaginationView != null)
            mPaginationView.setFreeze(true);

        changeFragment(newState, dir, false);
        mHandler.sendEmptyMessageDelayed(FINISH_COMMAND_MSG, (getResources().getInteger(R.integer.animTime) + 100));
    }

    public void changeFragment(AppConst.UIState newState, AppConsts.TransactionDir dir, boolean runningCommand) {
        if (mUIState == newState) {
            if (newState == AppConst.UIState.UI_STATE_UNDER_CONSTRUCTION)
                showMessage("This menu is not implemented", Toast.LENGTH_LONG);
            return;
        }

        //[[ START : Notification to the upper layer class : BaseActivity
        OnChangeFragment(newState, mUIState, dir);
        //]] END

        switch (newState) {
            case UI_STATE_NONE:
                selfFinish();
                break;

            /**
             * Attract loop page
             */
            case UI_STATE_ATTRACT_LOOP:
                {
                    mShowMainMenuHelp = true;

                    if (mPaginationView.getPages() == null || mPaginationView.getPages().size() == 0)
                        createMainMenu(newState.name());

                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NONE");
                    fragmentModel.setFragment(new VideoModel("Attractor",   //title
                            "video/0_B2B_Hero_Attractor.mp4", "frame/0_B2B_Hero_Attractor_frame.jpg",
                            null, null, null));
                    fragmentModel.setLayout("@layout/fragment_attractor");
                    mFragment = AttractorFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * Decision page
             */
            case UI_STATE_DECISION:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_ATTRACT_LOOP");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@drawable/hero_logo_black",           //title
                            "@string/decision_subtitle",        //subtitle
                            "models/decision_menu.json",    //menu for ListView
                            null, null, null));             // video
                    fragmentModel.setPivot(getResources().getInteger(R.integer.animStartOffset), getResources().getInteger(R.integer.animYOffset));
                    fragmentModel.setLayout("@layout/fragment_decision_page");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * End demo page
             */
            case UI_STATE_DEMO_END:
            {
                MenuModel menuModel = ModelUtil.getMenuModel("@drawable/hero_demo_end_logo_black",
                        "@string/demo_end_subtitle", "models/demo_end_menu.json", null, null, null);
                menuModel.getMenuItems().get(1).setAction(mMainMenuState.name());    //change action for first menu item

                //modify menu item title : Back to <What's New>
                String mainMenuTitle = new StringBuilder()
                        .append(getString(R.string.demo_end_back_to))
                        .append(" ")
                        .append(getMainMenuTitle(mMainMenuState))
                        .toString();
                menuModel.getMenuItems().get(1).setTitle(mainMenuTitle);

                FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                fragmentModel.setActionBackKey(mMainMenuState.name());
                fragmentModel.setBackground("@color/white");
                fragmentModel.setFragment(menuModel);     //menu for ListView

                fragmentModel.setLayout("@layout/fragment_end_page");
                fragmentModel.setReservedData("@string/business_legal_end_card");   //legal string

                mFragment = MenuFragment.newInstance(fragmentModel);
            }
            break;

            /*
             * Main Menu - What's new has 4 demos.
             */
            //Power Up - 1.1_B2B_Hero_Battery
            case UI_STATE_WHATS_DEMO_1:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : All day battery",   //title
                            "video/1.1_B2B_Hero_Battery.mp4", "frame/1.1_B2B_Hero_Battery_frame.jpg",
                            "chapter/1.1_B2B_Hero_Battery_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_battery");
                    mFragment = B2B_AllDayBatteryFragment.newInstance(fragmentModel);
                }
                break;

            //Samsung Security - 2.1_B2B_Hero_Knox
            case UI_STATE_WHATS_DEMO_2:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Samsung KNOX",             //title
                            "video/2.1_B2B_Hero_Knox.mp4", "frame/2.1_B2B_Hero_Knox_frame.jpg",
                            "chapter/2.1_B2B_Hero_Knox_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_knox");
                    mFragment = B2B_KnoxFragment.newInstance(fragmentModel);
                }
                break;

            //Water & Dust Resistant - 3.3_B2B_Hero_Design
            case UI_STATE_WHATS_DEMO_3:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                            "video/0.1_B2B_Hero_IP68.mp4", "frame/0.1_B2B_Hero_IP68_frame.jpg",
                            "chapter/0.1_B2B_Hero_IP68_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_ip68");
                    mFragment = B2B_IP68Fragment.newInstance(fragmentModel);
                }
                break;

            //Expandable Memory - 1.2_B2B_Hero_Expandable_SD
            case UI_STATE_WHATS_DEMO_4:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                fragmentModel.setFragment(new VideoModel("Productivity : Expandable SD",   //title
                        "video/1.2_B2B_Hero_Expandable_SD.mp4", "frame/1.2_B2B_Hero_Expandable_SD_frame.jpg",
                        "chapter/1.2_B2B_Hero_Expandable_SD_chap.json", null, null));

                fragmentModel.setLayout("@layout/frag_expandable_sd");
                mFragment = MoreStorageFragment.newInstance(fragmentModel);
            }
            break;


            /*
             * Main Menu - Productivity has 1 life style and 2 demos.
             */
            //Life Style - MATT_B2B_HERO_012416_1300_2398_MP4
            case UI_STATE_PRODUCTIVITY_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : Video",     //title
                            "video/b2b_hero_matt.mp4", "frame/b2b_hero_matt_frame.jpg", null, null, null));

                    fragmentModel.setLayout("@layout/frag_landscape_video");
                    mFragment = LandscapeVideoFragment.newInstance(fragmentModel);
                }
                break;

            //Power Up - 1.1_B2B_Hero_Battery
            case UI_STATE_PRODUCTIVITY_DEMO_BATTERY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : All day battery",   //title
                            "video/1.1_B2B_Hero_Battery.mp4", "frame/1.1_B2B_Hero_Battery_frame.jpg",
                            "chapter/1.1_B2B_Hero_Battery_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_battery");
                    mFragment = B2B_AllDayBatteryFragment.newInstance(fragmentModel);
                }
                break;

            //Expandable Memory - 1.2_B2B_Hero_Expandable_SD
            case UI_STATE_PRODUCTIVITY_DEMO_SD:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : Expandable SD",   //title
                            "video/1.2_B2B_Hero_Expandable_SD.mp4", "frame/1.2_B2B_Hero_Expandable_SD_frame.jpg",
                            "chapter/1.2_B2B_Hero_Expandable_SD_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_expandable_sd");
                    mFragment = MoreStorageFragment.newInstance(fragmentModel);
                }
                break;


            /*
             * Main menu - Design has 1 life style and 3 demos.
             */
            //Life Style -
            case UI_STATE_DESIGN_DEMO_VIDEO:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                fragmentModel.setFragment(new VideoModel( "Designed : Video",             //title
                        "video/b2b_hero_matt.mp4", "frame/b2b_hero_matt_frame.jpg", null, null, null));

                fragmentModel.setLayout("@layout/frag_landscape_video");
                mFragment = LandscapeVideoFragment.newInstance(fragmentModel);
            }
            break;

            //Breaking Colors - 3.1_B2B_Hero_Display
            case UI_STATE_DESIGN_DEMO_AMOLED:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                fragmentModel.setFragment(new VideoModel("Designed : Super AMOLED Display",             //title
                        "video/3.1_B2B_Hero_Display.mp4", "frame/3.1_B2B_Hero_Display_frame.jpg",
                        "chapter/3.1_B2B_Hero_Display_chap.json", null, null));

                fragmentModel.setLayout("@layout/frag_display");
                mFragment = B2B_DisplayFragment.newInstance(fragmentModel);
            }
            break;

            //Water & Dust Resistant - 3.3_B2B_Hero_Design (This video and capter are from B2C Hero)
            case UI_STATE_DESIGN_DEMO_HEAD_TURNING_DESIGN:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                        "video/0.1_B2B_Hero_IP68.mp4", "frame/0.1_B2B_Hero_IP68_frame.jpg",
                        null, null, null));

                fragmentModel.setLayout("@layout/frag_head_turning_design");
                mFragment = B2B_HeadTurningDesignFragment.newInstance(fragmentModel);
            }
            break;

            //Dual-pixel Camera - 3.2_B2B_Hero_Camera
            case UI_STATE_DESIGN_DEMO_CAMERA:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                fragmentModel.setFragment(new VideoModel("Designed : Camera",             //title
                        "video/3.2_B2B_Hero_Camera.mp4", "frame/3.2_B2B_Hero_Camera_frame.jpg",
                        "chapter/3.2_B2B_Hero_Camera_chap.json", null, null));

                fragmentModel.setLayout("@layout/frag_camera");
                mFragment = B2B_CameraFragment.newInstance(fragmentModel);
            }
            break;


            /*
             * Main menu - Galaxy Technology has 1 life style and 2 demos.
             */
            //Life Style -
            case UI_STATE_EXCLUSIVES_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : Video",             //title
                            "video/b2b_hero_matt.mp4", "frame/b2b_hero_matt_frame.jpg", null, null, null));

                    fragmentModel.setLayout("@layout/frag_landscape_video");
                    mFragment = LandscapeVideoFragment.newInstance(fragmentModel);
                }
                break;

            //Samsung Security - 2.1_B2B_Hero_Knox
            case UI_STATE_EXCLUSIVES_DEMO_SS_KNOX:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Samsung KNOX",             //title
                            "video/2.1_B2B_Hero_Knox.mp4", "frame/2.1_B2B_Hero_Knox_frame.jpg",
                            "chapter/2.1_B2B_Hero_Knox_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_knox");
                    mFragment = B2B_KnoxFragment.newInstance(fragmentModel);
                }
                break;

            //Samsung Pay - 2.2_B2B_Hero_Pay
            case UI_STATE_EXCLUSIVES_DEMO_SS_PAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung pay",             //title
                            "video/2.2_B2B_Hero_Pay.mp4", "frame/2.2_B2B_Hero_Pay_frame.jpg",
                            "chapter/2.2_B2B_Hero_Pay_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_pay");
                    mFragment = B2B_SamsungPayFragment.newInstance(fragmentModel);
                }
                break;




            /*
             * Main menu - Switching to Android has 2 demos.
             */
            //Smart Switch - 4.1_B2B_Hero_Smart_Switch
            case UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel("New to android Demo : Smart switch",             //title
                            "video/4.1_B2B_Hero_Smart_Switch.mp4", "frame/4.1_B2B_Hero_Smart_Switch_frame.jpg",
                            "chapter/4.1_B2B_Hero_Smart_Switch_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_smart_switch");
                    mFragment = SmartSwitchFragment.newInstance(fragmentModel);
                }
                break;

            //Expandable Memory -
            case UI_STATE_NEW_TO_ANDROID_DEMO_EXPANDABLE_MEMORY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : Expandable SD",   //title
                            "video/1.2_B2B_Hero_Expandable_SD.mp4", "frame/1.2_B2B_Hero_Expandable_SD_frame.jpg",
                            "chapter/1.2_B2B_Hero_Expandable_SD_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_expandable_sd");
                    mFragment = MoreStorageFragment.newInstance(fragmentModel);
                }
                break;


            /*
             * Device Specs
             */
            case UI_STATE_DEVICE_SPECS:
                {
                    mSelectedDevice = null;
                    if (mUIState != AppConst.UIState.UI_STATE_DEVICE_SPECS &&
                        mUIState != AppConst.UIState.UI_STATE_CHOOSE_DEVICE &&
                        mUIState != AppConst.UIState.UI_STATE_DEVICE_COMPARE)
                        mPrevUIState = mUIState;

                    FragmentModel<DeviceSpecModel> fragmentModel = new FragmentModel<DeviceSpecModel>();
                    fragmentModel.setActionBackKey(mPrevUIState.name());

                    DeviceSpecModel  deviceSpecModel = ModelUtil.getDeviceSpecModel(AppConst.UIState.UI_STATE_CHOOSE_DEVICE.name(),
                            getDeviceSpecJson(mDevice));
                    deviceSpecModel.setCloseAction(mPrevUIState.name());
                    fragmentModel.setFragment(deviceSpecModel);
                    fragmentModel.setLayout("@layout/fragment_device_specs");
                    mFragment = DeviceSpecsFragment.newInstance(fragmentModel);
                }
                break;


            case UI_STATE_CHOOSE_DEVICE:
                {
                    FragmentModel<DeviceModel> fragmentModel = new FragmentModel<DeviceModel>();
                    fragmentModel.setActionBackKey(AppConst.UIState.UI_STATE_DEVICE_SPECS.name());
                    DeviceModel deviceModel = ModelUtil.getDeviceModel(AppConst.UIState.UI_STATE_DEVICE_COMPARE.name(), "models/hero_device_compare_list.json");
                    deviceModel.setCloseAction(mPrevUIState.name());

                    if (mSelectedDevice != null) {
                        for(int i=0; i<deviceModel.getDeviceItems().size(); i++) {
                            int tagResId = deviceModel.getDeviceItems().get(i).getTagResId();
                            if (getString(tagResId).equals(mSelectedDevice.name())) {
                                deviceModel.getDeviceItems().get(i).setSelected(true);
                                break;
                            }
                        }
                    }

                    fragmentModel.setFragment(deviceModel);
                    fragmentModel.setLayout("@layout/fragment_select_device");
                    mFragment = SelectDeviceFragment.newInstance(fragmentModel);
                }
                break;

            case UI_STATE_DEVICE_COMPARE:
                {
                    if (mSelectedDevice == null) return;

                    FragmentModel<DeviceSpecModel> fragmentModel = new FragmentModel<DeviceSpecModel>();
                    fragmentModel.setActionBackKey(AppConst.UIState.UI_STATE_DEVICE_SPECS.name());
                    DeviceSpecModel deviceSpecModel = ModelUtil.getDeviceSpecModel(AppConst.UIState.UI_STATE_CHOOSE_DEVICE.name(),
                            getDeviceSpecJson(mDevice), getDeviceSpecJson(mSelectedDevice));
                    deviceSpecModel.setCloseAction(mPrevUIState.name());

                    //change layout for compare device
                    deviceSpecModel.getDeviceSpecItems().get(0).setLayout("@layout/device_spec_item_categories");
                    fragmentModel.setFragment(deviceSpecModel);
                    fragmentModel.setLayout("@layout/fragment_device_compare");
                    mFragment = DeviceSpecsFragment.newInstance(fragmentModel);
                }
                break;

            case UI_STATE_UNDER_CONSTRUCTION:
                showMessage("This menu is not implemented", Toast.LENGTH_LONG);
                return;



            /*
             * Main Menu UIs
             */
            case UI_STATE_WHATS_NEW_MAIN:
            case UI_STATE_PRODUCTIVITY_MAIN:
            case UI_STATE_EXCLUSIVES_MAIN:
            case UI_STATE_DESIGN_MAIN:
            case UI_STATE_NEW_TO_ANDROID_MAIN:
                mMainMenuState = newState;
                if (mFragment != null)
                    mFragment.onPause();    //pause fragment on fragmentContainer

                if (mPaginationView.getPages() == null || mPaginationView.getPages().size() == 0) {
                    createMainMenu(newState.name());
                    return;
                }
                else {
                    if (mPaginationDotsLayout.getChildCount() == 0)
                        addDots(mPaginationDotsLayout, mPaginationView.getPages().size());

                    PaginationViewItem<MenuModel> page = mPaginationView.getPage(newState.name());
                    mPaginationView.setPage(newState.name());
                    mFragment = page.getFragment();
                    setDrawer(page.getModel().getDrawerId());
                    setDots(mPaginationView, mPaginationDotsLayout, mPaginationView.getPageIndex(newState.name()));
                }
                break;
        }


        //do not insert
        if (newState != AppConst.UIState.UI_STATE_DEVICE_SPECS              &&
            newState != AppConst.UIState.UI_STATE_CHOOSE_DEVICE             &&
            newState != AppConst.UIState.UI_STATE_DEVICE_COMPARE            &&
            newState != AppConst.UIState.UI_STATE_WHATS_NEW_MAIN            &&
            newState != AppConst.UIState.UI_STATE_PRODUCTIVITY_MAIN         &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN           &&
            newState != AppConst.UIState.UI_STATE_DESIGN_MAIN               &&
            newState != AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN)
        {
            //remove previous fragments in mFragmentView
            removeFragments(false);
            insertFragment(mFragment);
        }

        //remove and add fragments for mDeviceSpecContainer
        if (newState == AppConst.UIState.UI_STATE_DEVICE_SPECS      ||
                newState == AppConst.UIState.UI_STATE_CHOOSE_DEVICE     ||
                newState == AppConst.UIState.UI_STATE_DEVICE_COMPARE    )
        {
            removeDeviceSpecFragments(false);
            insertDeviceSpecFragment(mFragment);
        }

        if (mFragment != null) {
            switch (dir) {
                case TRANSACTION_DIR_NONE:
                    switch (newState) {
                        case UI_STATE_ATTRACT_LOOP:
                            mDeviceSpecContainer.setYFraction(1.f);
                            mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                            AnimationUtil.setMarginX(mPaginationView, 0);
                            mPaginationView.setVisibility(View.INVISIBLE);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, mFragment)
                                    .commitAllowingStateLoss();
                            AnimationUtil.setScaleXY(mFragmentView, 1f);
                            AnimationUtil.setMarginX(mFragmentView, 0);
                            mFragmentView.setVisibility(View.VISIBLE);
                            mFragmentView.bringToFront();
                            mFragmentView.requestLayout();
                            mNavigationView.bringToFront();
                            removeFragments(false);
                            break;
                        case UI_STATE_WHATS_NEW_MAIN:
                        case UI_STATE_PRODUCTIVITY_MAIN:
                        case UI_STATE_EXCLUSIVES_MAIN:
                        case UI_STATE_DESIGN_MAIN:
                        case UI_STATE_NEW_TO_ANDROID_MAIN:
                            mDeviceSpecContainer.setYFraction(1.f);
                            mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                            AnimationUtil.setMarginX(mFragmentView, 0);
                            mFragmentView.setVisibility(View.INVISIBLE);

                            AnimationUtil.setMarginX(mPaginationView, AnimationUtil.getMarginX(mPaginationView));
                            mPaginationView.setVisibility(View.VISIBLE);
                            mPaginationView.bringToFront();
                            mPaginationView.requestLayout();
                            mNavigationView.bringToFront();
                            removeFragments(true);
                            break;
                        default:
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, mFragment)
                                    .commitAllowingStateLoss();
                            removeFragments(false);
                            break;
                    }
                    break;
                case TRANSACTION_DIR_FORWARD:
                    switch (newState) {
                        case UI_STATE_WHATS_NEW_MAIN:
                        case UI_STATE_PRODUCTIVITY_MAIN:
                        case UI_STATE_EXCLUSIVES_MAIN:
                        case UI_STATE_DESIGN_MAIN:
                        case UI_STATE_NEW_TO_ANDROID_MAIN:
                            if (mPaginationView.isShown()) break;
                            mPaginationView.setVisibility(View.VISIBLE);
                            if (mUIState == AppConst.UIState.UI_STATE_DECISION) {
                                AnimationUtil.scaleAnimation(mFragmentView, true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mPaginationView.bringToFront();
                                        mPaginationView.requestLayout();
                                        mFragmentView.setVisibility(View.INVISIBLE);

                                        if (mShowMainMenuHelp) {
                                            mHelpLayout.setVisibility(View.VISIBLE);
                                            mHelpLayout.bringToFront();
                                            mHelpLayout.requestLayout();
                                            mHelpLayout.getMainMenuHelp().start();
                                            mShowMainMenuHelp = false;
                                        }

                                        mNavigationView.bringToFront();
                                    }
                                });
                            }
                            else {
                                if (AnimationUtil.getMarginX(mFragmentView) == 0) {
                                    AnimationUtil.marginXAnimation(mFragmentView, true, new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mPaginationView.bringToFront();
                                            mPaginationView.requestLayout();
                                            AnimationUtil.setMarginX(mFragmentView, 0);
                                            mFragmentView.setVisibility(View.INVISIBLE);
                                            removeFragments(true);

                                            if (mShowMainMenuHelp) {
                                                mHelpLayout.setVisibility(View.VISIBLE);
                                                mHelpLayout.bringToFront();
                                                mHelpLayout.requestLayout();
                                                mHelpLayout.getMainMenuHelp().start();
                                                mShowMainMenuHelp = false;
                                            }

                                            mNavigationView.bringToFront();
                                        }
                                    });
                                }
                            }
                            break;

                        case UI_STATE_DEVICE_SPECS:
                            if (!mDeviceSpecContainer.isShown()) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.deviceSpecContainer, mFragment)
                                        .commitAllowingStateLoss();
                                mDeviceSpecContainer.setVisibility(View.VISIBLE);
                                mDeviceSpecContainer.setYFraction(1.f);
                                mDeviceSpecContainer.bringToFront();
                                mDeviceSpecContainer.requestLayout();
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mNavigationView.bringToFront();
                                    }
                                });
                            }
                            else {
                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.left_out)
                                        .replace(R.id.deviceSpecContainer, mFragment)
                                        .commitAllowingStateLoss();
                            }
                            break;

                        case UI_STATE_CHOOSE_DEVICE:
                        case UI_STATE_DEVICE_COMPARE:
                            {
                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.left_out)
                                        .replace(R.id.deviceSpecContainer, mFragment)
                                        .commitAllowingStateLoss();
                            }
                            break;
                        default:
                            switch(mUIState) {
                                case UI_STATE_ATTRACT_LOOP:
                                case UI_STATE_DECISION:
                                    if (!mFragmentView.isShown()) {
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commitAllowingStateLoss();
                                        AnimationUtil.setScaleXY(mFragmentView, 1f);
                                        AnimationUtil.setMarginX(mFragmentView, 0);
                                        mFragmentView.setVisibility(View.VISIBLE);

                                        if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                            AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    mFragmentView.bringToFront();
                                                    mFragmentView.requestLayout();
                                                    AnimationUtil.setMarginX(mPaginationView, 0);
                                                    mPaginationView.setVisibility(View.INVISIBLE);
                                                    mNavigationView.bringToFront();
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        getFragmentManager().beginTransaction()
                                                .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.scale_down)
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commitAllowingStateLoss();
                                    }
                                    break;
                                default:
                                    if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                        AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                                mDeviceSpecContainer.setYFraction(1.f);
                                                mNavigationView.bringToFront();

                                                getFragmentManager().beginTransaction()
                                                        .replace(R.id.fragmentContainer, mFragment)
                                                        .commitAllowingStateLoss();
                                                AnimationUtil.setScaleXY(mFragmentView, 1f);
                                                AnimationUtil.setMarginX(mFragmentView, 0);
                                                mFragmentView.setVisibility(View.VISIBLE);

                                                if (mPaginationView.isShown()) {
                                                    if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                                        AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                super.onAnimationEnd(animation);
                                                                mFragmentView.bringToFront();
                                                                mFragmentView.requestLayout();
                                                                AnimationUtil.setMarginX(mPaginationView, 0);
                                                                mPaginationView.setVisibility(View.INVISIBLE);
                                                                mNavigationView.bringToFront();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    else if (!mFragmentView.isShown()) {
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commitAllowingStateLoss();
                                        AnimationUtil.setScaleXY(mFragmentView, 1f);
                                        AnimationUtil.setMarginX(mFragmentView, 0);
                                        mFragmentView.setVisibility(View.VISIBLE);

                                        if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                            AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    mFragmentView.bringToFront();
                                                    mFragmentView.requestLayout();

                                                    AnimationUtil.setMarginX(mPaginationView, 0);

                                                    mPaginationView.setVisibility(View.INVISIBLE);
                                                    mNavigationView.bringToFront();
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        getFragmentManager().beginTransaction()
                                                .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.left_out)
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commitAllowingStateLoss();
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    switch (newState) {
                        case UI_STATE_WHATS_NEW_MAIN:
                        case UI_STATE_PRODUCTIVITY_MAIN:
                        case UI_STATE_EXCLUSIVES_MAIN:
                        case UI_STATE_DESIGN_MAIN:
                        case UI_STATE_NEW_TO_ANDROID_MAIN:
                            if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                        mDeviceSpecContainer.setYFraction(1.f);
                                        mNavigationView.bringToFront();
                                    }
                                });
                            }
                            else {
//                              final boolean bGearS2DemoShow = mGearS2DemoView.isShown();

                                AnimationUtil.setMarginX(mPaginationView, AnimationUtil.getMarginX(mPaginationView));
                                mPaginationView.setVisibility(View.VISIBLE);
                                mPaginationView.bringToFront();
                                mPaginationView.requestLayout();
                                mNavigationView.bringToFront();

                                AnimationUtil.marginXAnimation(mPaginationView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mFragmentView, 0);
                                        mFragmentView.setVisibility(View.INVISIBLE);
                                        removeFragments(true);
                                    }
                                });
                            }
                            break;

                        case UI_STATE_ATTRACT_LOOP:
                            //close device spec page
                            if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                        mDeviceSpecContainer.setYFraction(1.f);
                                        mNavigationView.bringToFront();
                                    }
                                });
                            }

                            if (!mFragmentView.isShown()) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commitAllowingStateLoss();
                                AnimationUtil.setScaleXY(mFragmentView, 1f);
                                AnimationUtil.setMarginX(mFragmentView, 0);
                                mFragmentView.setVisibility(View.VISIBLE);
                                mFragmentView.bringToFront();
                                mFragmentView.requestLayout();
                                mNavigationView.bringToFront();

                                AnimationUtil.scaleAnimation(mFragmentView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                        removeFragments(false);
                                    }
                                });
                            }
                            else {
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.scale_up, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commitAllowingStateLoss();
                                removeFragments(false);
                            }
                            break;
                        case UI_STATE_DECISION:
                            if (mHelpLayout.isShown())
                                mHelpLayout.stop();

                            if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                        mDeviceSpecContainer.setYFraction(1.f);

                                        getFragmentManager().beginTransaction()
                                                .add(R.id.fragmentContainer, mFragment)
                                                .commitAllowingStateLoss();
                                        mNavigationView.bringToFront();
                                    }
                                });
                            }
                            else if (!mFragmentView.isShown()) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commitAllowingStateLoss();
                                AnimationUtil.setScaleXY(mFragmentView, 1f);
                                AnimationUtil.setMarginX(mFragmentView, 0);
                                mFragmentView.setVisibility(View.VISIBLE);
                                mFragmentView.bringToFront();
                                mFragmentView.requestLayout();
                                mNavigationView.bringToFront();

                                AnimationUtil.scaleAnimation(mFragmentView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                        removeFragments(false);
                                    }
                                });
                            }
                            else {
                                removeFragments(false);
                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.animator.scale_up, FragmentTransaction.TRANSIT_NONE)
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commitAllowingStateLoss();
                            }
                            break;

                        case UI_STATE_DEVICE_SPECS:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.left_in, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.deviceSpecContainer, mFragment)
                                    .commitAllowingStateLoss();
                            break;

                        default:
                            if (!mFragmentView.isShown()) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commitAllowingStateLoss();
                                AnimationUtil.setMarginX(mFragmentView, getResources().getInteger(R.integer.animXOffsetMinus));
                                mFragmentView.setVisibility(View.VISIBLE);
                                mFragmentView.bringToFront();
                                mFragmentView.requestLayout();
                                mNavigationView.bringToFront();


                                AnimationUtil.marginXAnimation(mFragmentView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } else {
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.left_in, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commitAllowingStateLoss();
                            }
                            break;

                    }
                    break;
            }
        }

        mUIState = newState;
    }

//    public void changeEndDemoFragment(AppConst.UIState backFragment, AppConsts.TransactionDir dir) {
//        MenuModel menuModel = ModelUtil.getMenuModel("@drawable/hero_demo_end_logo_black",
//                "@string/demo_end_subtitle", "models/demo_end_menu.json", null, null, null);
//        menuModel.getMenuItems().get(1).setAction(backFragment.name());    //change action for first menu item
//
//        //modify menu item title : Back to <What's New>
//        String modifyMenuTitle = getString(R.string.demo_end_back_to);
//        for (int i=0; i<menuModel.getMenuItems().size(); i++) {
//            if (getString(menuModel.getMenuItems().get(i).getTitleResId()).equals(modifyMenuTitle)) {
//                String modifiedMenuTitle = new StringBuilder()
//                        .append(getString(R.string.demo_end_back_to))
//                        .append(" ")
//                        .append(getMainMenuTitle(backFragment))
//                        .toString();
//                menuModel.getMenuItems().get(i).setTitle(modifiedMenuTitle);
//                break;
//            }
//        }
//
//        FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//        fragmentModel.setActionBackKey(backFragment.name());
//        fragmentModel.setBackground("@color/white");
//        fragmentModel.setFragment(menuModel);     //menu for ListView
//
//        fragmentModel.setLayout("@layout/fragment_end_page");
//        mFragment = MenuFragment.newInstance(fragmentModel);
//        changeFragment(AppConst.UIState.UI_STATE_DEMO_END, dir);
//    }

    private String getMainMenuTitle(AppConst.UIState uiState) {
        String menuTitle = getString(R.string.whats_new_title);

        switch(uiState) {
            case UI_STATE_WHATS_NEW_MAIN:
                menuTitle = getString(R.string.whats_new_title);
                break;
            case UI_STATE_DESIGN_MAIN:
                menuTitle = getString(R.string.design_demo_title);
                break;
            case UI_STATE_PRODUCTIVITY_MAIN:
                menuTitle = getString(R.string.productivity_demo_title);
                break;
            case UI_STATE_EXCLUSIVES_MAIN:
                menuTitle = getString(R.string.ss_exclusives_demo_title);
                break;
            case UI_STATE_NEW_TO_ANDROID_MAIN:
                menuTitle = getString(R.string.new_to_android_title);
                break;
        }
        return menuTitle;
    }

    public void showMessage(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }



    /*
     * Navigation Drawer
     */
    private ArrayList<DrawerItem> mDrawerItems = new ArrayList<DrawerItem>();
    private ArrayList<DrawerItem> getDrawerItems() {
        mDrawerItems.clear();

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.chevron_blue);
        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.whats_new_title),
                        AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.productivity_demo_title),
                        AppConst.UIState.UI_STATE_PRODUCTIVITY_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.design_demo_title),
                        AppConst.UIState.UI_STATE_DESIGN_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.ss_exclusives_demo_title),
                        AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.new_to_android_title),
                        AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.device_specs),
                        AppConst.UIState.UI_STATE_DEVICE_SPECS.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.explore_the_phone),
                        AppConst.UIState.UI_STATE_NONE.name()));

        return mDrawerItems;
    }

    public void onSlideDrawer(View drawerView, float slideOffset) {
        if (mFragmentView.isShown())
            AnimationUtil.setMarginX(mFragmentView, (int)(Math.floor(mNavigationView.getWidth() * slideOffset)));
        else if (mPaginationView.isShown()) {
            AnimationUtil.setMarginX(mPaginationView, (int)(Math.floor(mNavigationView.getWidth() * slideOffset)));
        }
    }

    public void onSelectDrawerItem(final int position) {
        if (mDrawerItems.get(position).getAction().equals(AppConst.UIState.UI_STATE_DEVICE_SPECS.name())) {
            if (mRunningCommand)
                return;
            mRunningCommand = true;

            closeDrawer(new DrawerStateListener() {
                @Override
                public void onDrawerOpened() {
                }

                @Override
                public void onDrawerClosed() {
                    mRunningCommand = false;
                    changeFragment(AppConst.UIState.valueOf(mDrawerItems.get(position).getAction()), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                    closeDrawer(null);
                }
            });
        }
        else {
            changeFragment(AppConst.UIState.valueOf(mDrawerItems.get(position).getAction()), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
            closeDrawer();
        }
    }

    public void setDrawer(String drawerId) {
        super.setDrawer(drawerId);
    }

    public void clickDrawerBtn() {
        if (mRunningCommand) return;
        mRunningCommand = true;

        super.clickDrawerBtn();
        mHandler.sendEmptyMessageDelayed(FINISH_COMMAND_MSG, (getResources().getInteger(R.integer.animTime) * 2));
    }

    /*
     * Main Menu Pagination View
     */
    private void createMainMenu(String uiState) {
        if (mPaginationView.getPages() != null && mPaginationView.getPages().size() > 0) return;
        MainMenuAsyncTask mainMenuAsyncTask = new MainMenuAsyncTask(mPaginationView,
                getFragmentManager(), mPaginationViewListener);
        mainMenuAsyncTask.execute(uiState);
    }

//    private void deleteMainMenu() {
//        if (mPaginationView.getPages() == null || mPaginationView.getPages().size() == 0) return;
//        for(int i=mPaginationView.getPages().size()-1; i>=0; i--) {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.remove(mPaginationView.getPages().get(i).getFragment());
//            ft.commitAllowingStateLoss()
//            mPaginationView.getPages().remove(i);
//        }
//    }

    private PaginationView.PaginationViewListener mPaginationViewListener = new PaginationView.PaginationViewListener() {
        @Override
        public void onPaginationReady(String uiState) {
            mRunningCommand = false;
            if (!uiState.equals(AppConst.UIState.UI_STATE_ATTRACT_LOOP))
                changeFragment(AppConst.UIState.valueOf(uiState), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
        }

        @Override
        public
        void onChangedPage(BaseFragment fragment, String uiState, String drawerId, int pageIndex) {
            mFragment = (BaseFragment)fragment;
            mMainMenuState = mUIState = AppConst.UIState.valueOf(uiState);
            setDrawer(drawerId);
            setDots(mPaginationView, mPaginationDotsLayout, pageIndex);
        }
    };

    /*
     * Pagination Dots
     */
    private void addDots(LinearLayout dotLayout, int count) {
        for (int i = 0; i < dotLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) dotLayout.getChildAt(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.recycle();
            }
        }
        dotLayout.removeAllViews();

        for (int i=0; i<count; i++) {
            ImageView imageView = new ImageView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.carousel_dot);
            imageView.setTag(i);
            dotLayout.addView(imageView);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            params.setMargins(20, 0, 20, 0);
        }
    }

    private void setDots(PaginationView paginationView, LinearLayout dotLayout, int pageIndex) {
        pageIndex = (paginationView.getPages().size() - 1) - pageIndex;
        for (int i=0; i<paginationView.getPages().size(); i++) {
            ImageView imageView = (ImageView) dotLayout.getChildAt(i);
            if (i == pageIndex)
                imageView.setImageResource(R.drawable.carousel_dot_active);
            else
                imageView.setImageResource(R.drawable.carousel_dot);
        }
    }
}