package com.samsung.retailexperience.retailhero.ui.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
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
        mContext = this;
        setContentView(R.layout.activity_main);

        setupDrawerLayout();

        changeFragment(AppConst.UIState.UI_STATE_ATTRACT_LOOP,
                AppConsts.TransactionDir.TRANSACTION_DIR_NONE);
    }

    @Override
    protected void onDestroy() {
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
                    fragmentModel.setLayout("@layout/fragment_menu_center_align");
                    fragmentModel.setActionBackKey("UI_STATE_ATTRACT_LOOP");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/decision_title",           //title
                            "models/decision_menu.json"));      //menu for ListView
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
                    fragmentModel.setLayout("@layout/fragment_menu");
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_whats_new");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/whats_new_demo",                //title
                            "models/whats_new_menu.json"));     //menu for ListView
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_DESIGN:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : Design",  //title
                            "video/design_base.mp4", "frame/design_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_CAMERA:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : Camera",             //title
                            "video/camera_base.mp4", "frame/camera_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_EXCLUSIVES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : SS Exclusives",             //title
                            "video/SsExclusives_base.mp4", "frame/SsExclusives_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_NEW_TO_ANDROID:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New Demo : new to android",             //title
                            "video/android_base.mp4", "frame/android_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Design : 4 demos
             */
            case UI_STATE_DESIGN_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setLayout("@layout/fragment_menu");
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_design");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/design_demo",                //title
                            "models/design_menu.json"));     //menu for ListView
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Video",     //title
                            "video/design_base.mp4", "frame/design_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_AMAZING_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_amazing_display");
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Amazing display",   //title
                            "video/design_display.mp4", "frame/design_display_frame.jpg",
                            "chapter/design_display_chap.json", null, null));
                    mFragment = AmazingDisplayFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGN_DEMO_HEAD_TURNING_DESIGN:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                            "video/design_AmazingDesign.mp4", "frame/design_AmazingDesign_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /* This demo is in Hero2_B2C. This are Hero_B2C
            case UI_STATE_DESIGN_DEMO_EDGE_SHORTCUT:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_edge_shortcut");
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Edge shortcut",   //title
                            "video/design_shortcut.mp4", "frame/design_shortcut_frame.jpg",
                            "chapter/design_shortcut_chap.json", null, null));
                    mFragment = EdgeShortcutFragment.newInstance(fragmentModel);
                }
                break;
            */

            case UI_STATE_DESIGN_DEMO_TOUCH_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : always on touch display",   //title
                            "video/design_AlwaysTouch.mp4", "frame/design_AlwaysTouch_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Camera : 4 demos
             */
            case UI_STATE_CAMERA_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setLayout("@layout/fragment_menu");
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_camera");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/camera_demo",                //title
                            "models/camera_menu.json"));     //menu for ListView
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Video",             //title
                            "video/camera_base.mp4", "frame/camera_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_AUTO_FOCUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_auto_focus");
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Auto-Focus",             //title
                            "video/camera_AutoFocus.mp4", "frame/camera_AutoFocus_frame.jpg",
                            "chapter/camera_AutoFocus_chap.json", null, null));
                    mFragment = AutoFocusFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_PHOTO_QUALITY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_photo_quality");
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Photo Quaity",             //title
                            "video/camera_PhotoQuality.mp4", "frame/camera_PhotoQuality_frame.jpg",
                            "chapter/camera_PhotoQuality_chap.json", null, null));
                    mFragment = PhotoQualityFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_CAMERA_DEMO_SELFIES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_selfies");
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Photo Quaity",             //title
                            "video/camera_selfies.mp4", "frame/camera_selfies_frame.jpg",
                            "chapter/camera_selfies_chap.json", null, null));
                    mFragment = SelfiesFragment.newInstance(fragmentModel);
                }
                break;


            /**
             * MAIN - Exclusives : 6 demos
             */
            case UI_STATE_EXCLUSIVES_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setLayout("@layout/fragment_menu");
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_ss_exclusives");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/ss_exclusives_demo",                //title
                            "models/samsung_exclusives.json"));     //menu for ListView
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : Video",             //title
                            "video/SsExclusives_base.mp4", "frame/SsExclusives_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_BATTERY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : All-day battery",             //title
                            "video/SsExclusives_battery.mp4", "frame/SsExclusives_battery_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_MORE_STORAGE:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_more_storage");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : more storage",             //title
                            "video/SsExclusives_storage.mp4", "frame/SsExclusives_storage_frame.jpg",
                            "chapter/SsExclusives_storage_chap.json", null, null));
                    mFragment = MoreStorageFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_SS_PAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_samsung_pay");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung pay",             //title
                            "video/SsExclusives_pay.mp4", "frame/SsExclusives_pay_frame.jpg",
                            "chapter/SsExclusives_pay_chap.json", null, null));
                    mFragment = SamsungPayFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_SS_PLUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_samsung_plus");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung+",             //title
                            "video/SsExclusives_plus.mp4", "frame/SsExclusives_plus_frame.jpg",
                            "chapter/SsExclusives_plus_chap.json", null, null));
                    mFragment = SamsungPlusFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_EXCLUSIVES_DEMO_GEAR_VR:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_gear_vr");
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Gear VR",             //title
                            "video/SsExclusives_GearVR.mp4", "frame/SsExclusives_GearVR_frame.jpg",
                            "chapter/SsExclusives_GearVR_chap.json", null, null));
                    mFragment = GearVRFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - new to android : 2 demo
             */
            case UI_STATE_NEW_TO_ANDROID_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setLayout("@layout/fragment_menu");
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_new_to_android");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/new_to_android_demo",                //title
                            "models/new_to_android_menu.json"));     //menu for ListView
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_NEW_TO_ANDROID_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel( "New to android : Video",             //title
                            "video/android_base.mp4", "frame/android_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setLayout("@layout/frag_smart_switch");
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel("New to android Demo : Video",             //title
                            "video/android_smartSwitch.mp4", "frame/android_smartSwitch_frame.jpg",
                            "chapter/android_smartSwitch_chap.json", null, null));
                    mFragment = SmartSwitchFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * Compare device page
             */
            case UI_STATE_COMPARE_DEVICE:
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

    public void changeEndDemoFragment(AppConst.UIState backFragment, AppConsts.TransactionDir dir) {
        MenuModel menuModel = ModelUtil.getMenuModel("@string/demo_end_title", "models/demo_end_menu.json");
        menuModel.getMenuItems().get(0).setAction(backFragment.name());    //change action for first menu item
        FragmentModel<MenuModel> fragmentModel
                = new FragmentModel<MenuModel>(
                "@layout/fragment_menu",                                //fragment layout xml
                backFragment.name(),                            //actionBackKey
                menuModel,                                              //menu for ListView
                "@color/white");                                         //background);
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