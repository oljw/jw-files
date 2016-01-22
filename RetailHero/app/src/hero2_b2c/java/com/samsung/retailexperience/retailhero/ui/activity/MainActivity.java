package com.samsung.retailexperience.retailhero.ui.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.AttractorFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.CompareDeviceFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.DefaultVideoFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.SmartSwitchFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.AutoFocusFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.PhotoQualityFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.SelfiesFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.design.AmazingDisplayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.design.EdgeShortcutFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.GearVRFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.MoreStorageFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.SamsungPayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.SamsungPlusFragment;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ModelUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private AppConst.UIState mUIState = AppConst.UIState.UI_STATE_NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "##### onCreate)+ ");

        mContext = this;
        setContentView(R.layout.activity_main);
        setupDrawerLayout();
        changeFragment(getUIState(), AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "##### onDestroy)+ ");

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    public void changeFragment(AppConst.UIState newState, AppConsts.TransactionDir dir) {
        if (mUIState == newState) {
            if (newState == AppConst.UIState.UI_STATE_UNDER_CONSTRUCTION)
                showMessage("This menu is not implemented", Toast.LENGTH_LONG);
            return;
        }

        switch (newState) {
            case UI_STATE_NONE:
                selfFinish();
                break;

            /**
             * Attract loop page
             */
            case UI_STATE_ATTRACT_LOOP:
                mFragment = new AttractorFragment();
                break;

            /**
             * Decision page
             */
            case UI_STATE_DECISION:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_ATTRACT_LOOP");
                    fragmentModel.setBackground("@drawable/decision_bg");
                    fragmentModel.setDrawerId("@id/drawer_whats_new");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@drawable/hero2_logo",           //title
                            "@string/decision_subtitle",        //subtitle
                            "models/decision_menu.json",    //menu for ListView
                            null, null, null));             // video

                    fragmentModel.setLayout("@layout/fragment_start_end");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * End demo page
             */
            case UI_STATE_DEMO_END:
                break;

            /**
             *  MAIN - what's new : 4 demos
             */
            case UI_STATE_WHATS_NEW_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@drawable/whats_new_bg");
                    fragmentModel.setDrawerId("@id/drawer_whats_new");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/whats_new_demo_title",                //title
                            "@string/whats_new_demo_subtitle",
                            "models/whats_new_menu.json", //menu for ListView
                            null,
                            null,
                            null));

                    fragmentModel.setLayout("@layout/fragment_no_video_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_DESIGN:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : Design",  //title
                            "video/design_base.mp4", "frame/design_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_CAMERA:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : Camera",             //title
                            "video/camera_base.mp4", "frame/camera_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_EXCLUSIVES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : SS Exclusives",             //title
                            "video/SsExclusives_base.mp4", "frame/SsExclusives_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_NEW_TO_ANDROID:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : new to android",             //title
                            "video/android_base.mp4", "frame/android_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Design : 5 demos
             */
            case UI_STATE_DESIGN_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@drawable/design_bg");
                    fragmentModel.setDrawerId("@id/drawer_design");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/design_demo_title",                //title
                            "@string/design_demo_subtitle",
                            "models/design_menu.json",                  //menu for ListView
                            "@string/design_demo_video_title",
                            "@string/design_demo_video_subtitle",
                            "UI_STATE_DESIGN_DEMO_VIDEO"));

                    fragmentModel.setLayout("@layout/fragment_video_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Video",     //title
                            "video/design_base.mp4", "frame/design_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_AMAZING_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Amazing display",   //title
                            "video/design_display.mp4", "frame/design_display_frame.jpg",
                            "chapter/design_display_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_amazing_display");
                    mFragment = AmazingDisplayFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_HEAD_TURNING_DESIGN:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                            "video/design_AmazingDesign.mp4", "frame/design_AmazingDesign_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_EDGE_SHORTCUT:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Edge shortcut",   //title
                            "video/design_shortcut.mp4", "frame/design_shortcut_frame.jpg",
                            "chapter/design_shortcut_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_edge_shortcut");
                    mFragment = EdgeShortcutFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_TOUCH_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : always on touch display",   //title
                            "video/design_AlwaysTouch.mp4", "frame/design_AlwaysTouch_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Camera : 4 demos
             */
            case UI_STATE_CAMERA_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@drawable/camera_bg");
                    fragmentModel.setDrawerId("@id/drawer_camera");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/camera_demo_title",                //title
                            "@string/camera_demo_subtitle",
                            "models/camera_menu.json",                  //menu for ListView
                            "@string/camera_demo_video_title",
                            "@string/camera_demo_video_subtitle",
                            "UI_STATE_CAMERA_DEMO_VIDEO"));

                    fragmentModel.setLayout("@layout/fragment_video_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Video",             //title
                            "video/camera_base.mp4", "frame/camera_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_AUTO_FOCUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Auto-Focus",             //title
                            "video/camera_AutoFocus.mp4", "frame/camera_AutoFocus_frame.jpg",
                            "chapter/camera_AutoFocus_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_auto_focus");
                    mFragment = AutoFocusFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_PHOTO_QUALITY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Photo Quality",             //title
                            "video/camera_PhotoQuality.mp4", "frame/camera_PhotoQuality_frame.jpg",
                            "chapter/camera_PhotoQuality_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_photo_quality");
                    mFragment = PhotoQualityFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_SELFIES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Photo Selfies",             //title
                            "video/camera_selfies.mp4", "frame/camera_selfies_frame.jpg",
                            "chapter/camera_selfies_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_selfies");
                    mFragment = SelfiesFragment.newInstance(fragmentModel);
                }
                break;


            /**
             * MAIN - Exclusives : 6 demos
             */
            case UI_STATE_EXCLUSIVES_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@drawable/ss_exclusives_bg");
                    fragmentModel.setDrawerId("@id/drawer_ss_exclusives");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/ss_exclusives_demo_title",                //title
                            "@string/ss_exclusives_demo_subtitle",
                            "models/samsung_exclusives.json",                  //menu for ListView
                            "@string/ss_exclusives_demo_video_title",
                            "@string/ss_exclusives_demo_video_subtitle",
                            "UI_STATE_EXCLUSIVES_DEMO_VIDEO"));

                    fragmentModel.setLayout("@layout/fragment_video_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : Video",             //title
                            "video/SsExclusives_base.mp4", "frame/SsExclusives_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_BATTERY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : All-day battery",             //title
                            "video/SsExclusives_battery.mp4", "frame/SsExclusives_battery_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_MORE_STORAGE:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : more storage",             //title
                            "video/SsExclusives_storage.mp4", "frame/SsExclusives_storage_frame.jpg",
                            "chapter/SsExclusives_storage_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_more_storage");
                    mFragment = MoreStorageFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_SS_PAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung pay",             //title
                            "video/SsExclusives_pay.mp4", "frame/SsExclusives_pay_frame.jpg",
                            "chapter/SsExclusives_pay_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_pay");
                    mFragment = SamsungPayFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_SS_PLUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung+",             //title
                            "video/SsExclusives_plus.mp4", "frame/SsExclusives_plus_frame.jpg",
                            "chapter/SsExclusives_plus_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_plus");
                    mFragment = SamsungPlusFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_GEAR_VR:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Gear VR",             //title
                            "video/SsExclusives_GearVR.mp4", "frame/SsExclusives_GearVR_frame.jpg",
                            "chapter/SsExclusives_GearVR_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_gear_vr");
                    mFragment = GearVRFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - new to android : 2 demo
             */
            case UI_STATE_NEW_TO_ANDROID_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@drawable/new_to_android_bg");
                    fragmentModel.setDrawerId("@id/drawer_new_to_android");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/new_to_android_demo_title",                //title
                            "@string/new_to_android_demo_subtitle",
                            "models/new_to_android_menu.json", //menu for ListView
                            null,
                            null,
                            null));

                    fragmentModel.setLayout("@layout/fragment_no_video_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_NEW_TO_ANDROID_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel( "New to android : Video",             //title
                            "video/android_base.mp4", "frame/android_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel("New to android Demo : Smart switch",             //title
                            "video/android_smartSwitch.mp4", "frame/android_smartSwitch_frame.jpg",
                            "chapter/android_smartSwitch_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_smart_switch");
                    mFragment = SmartSwitchFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * Compare device page
             */
            case UI_STATE_DEVICE_SPECS:
                {
                    mFragment = new CompareDeviceFragment();
                }
                break;

            case UI_STATE_UNDER_CONSTRUCTION:
                showMessage("This menu is not implemented", Toast.LENGTH_LONG);
                break;
        }

        if (mFragment != null) {
            switch (dir) {
                case TRANSACTION_DIR_NONE:
                    switch (mUIState) {
                        case UI_STATE_ATTRACT_LOOP:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.scale_down)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                        default:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.left_out)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                    }
                    break;
                case TRANSACTION_DIR_FORWARD:
                    switch (mUIState) {
                        case UI_STATE_ATTRACT_LOOP:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.scale_down)
                                    .replace(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                        default:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.animator.left_out)
                                    .replace(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                    }
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    switch (newState) {
                        case UI_STATE_ATTRACT_LOOP:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.scale_up, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                        default:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.left_in, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                    }
                    break;
            }

            mFragmentsHandler.postDelayed(mFragmentsRunnable, getResources().getInteger(R.integer.animTime));
        }

        mUIState = newState;
    }

    /**
     *  goto the end demo page
     *
     */
    public void changeEndDemoFragment(AppConst.UIState backFragment, AppConsts.TransactionDir dir) {
        MenuModel menuModel = ModelUtil.getMenuModel("@drawable/hero2_logo",
                "@string/demo_end_subtitle", "models/demo_end_menu.json", null, null, null);
        menuModel.getMenuItems().get(0).setAction(backFragment.name());    //change action for first menu item

        FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
        fragmentModel.setActionBackKey(backFragment.name());
        fragmentModel.setBackground("@drawable/end_page_bg");
        fragmentModel.setFragment(menuModel);     //menu for ListView

        fragmentModel.setLayout("@layout/fragment_start_end");
        mFragment = MenuFragment.newInstance(fragmentModel);
        changeFragment(AppConst.UIState.UI_STATE_DEMO_END, dir);
    }

    public void showMessage(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    /*
     * Navigation Drawer
     */
    private DrawerLayout mDrawerLayout = null;
    private NavigationView mNavigationView = null;
//    private RelativeLayout mFragmentContainer = null;

    private void setupDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mDrawerLayout == null || mNavigationView == null) {
            mDrawerLayout = null;
            mNavigationView = null;
            return;
        }

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.setDrawerListener(mDrawerListener);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                AppConst.UIState newState = AppConst.UIState.UI_STATE_NONE;
                if (menuItem.getItemId() == R.id.drawer_whats_new)
                    newState = AppConst.UIState.UI_STATE_WHATS_NEW_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_design)
                    newState = AppConst.UIState.UI_STATE_DESIGN_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_camera)
                    newState = AppConst.UIState.UI_STATE_CAMERA_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_ss_exclusives)
                    newState = AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_new_to_android)
                    newState = AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_device_specs)
                    newState = AppConst.UIState.UI_STATE_DEVICE_SPECS;

                changeFragment(newState, AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
                return true;
            }
        });
    }

    DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            setMargin(findViewById(R.id.fragmentContainer), Math.round(mNavigationView.getWidth() * slideOffset));
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

    public void setDrawer(int drawerId) {
        if (drawerId > 0) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mNavigationView);
            mNavigationView.setCheckedItem(drawerId);
        }
        else {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) mDrawerLayout.closeDrawer(mNavigationView);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mNavigationView);
        }
    }

    public void clickDrawerBtn() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView))
            mDrawerLayout.closeDrawer(mNavigationView);
        else
            mDrawerLayout.openDrawer(mNavigationView);
    }

    private void setMargin(View view, int l) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        p.setMargins(l, 0, -l, 0);
        view.requestLayout();
    }
}