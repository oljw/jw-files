package com.samsung.retailexperience.retailhero.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samsung.retailexperience.retailhero.R;
import com.samsung.retailexperience.retailhero.gson.models.BaseModel;
import com.samsung.retailexperience.retailhero.gson.models.FragmentModel;
import com.samsung.retailexperience.retailhero.gson.models.MenuModel;
import com.samsung.retailexperience.retailhero.gson.models.VideoModel;
import com.samsung.retailexperience.retailhero.ui.async.GearS2DemoAsyncTask;
import com.samsung.retailexperience.retailhero.ui.async.MainMenuAsyncTask;
import com.samsung.retailexperience.retailhero.ui.fragment.BaseFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.MenuFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.AttractorFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.DefaultVideoFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.DeviceSpecsFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.GearS2Fragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.android.SmartSwitchFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.AutoFocusFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.PhotoQualityFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.camera.SelfiesFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.design.AmazingDisplayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.design.HeadTurningDesignFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.AllDayBatteryFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.GearVRFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.MoreStorageFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.SamsungPayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusive.SamsungPlusFragment;
import com.samsung.retailexperience.retailhero.util.AnimationUtil;
import com.samsung.retailexperience.retailhero.util.AppConst;
import com.samsung.retailexperience.retailhero.util.AppConsts;
import com.samsung.retailexperience.retailhero.util.ModelUtil;
import com.samsung.retailexperience.retailhero.view.PaginationView;
import com.samsung.retailexperience.retailhero.view.PaginationViewItem;
import com.samsung.retailexperience.retailhero.view.SlideFrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private AppConst.UIState mUIState = AppConst.UIState.UI_STATE_NONE;
    private FrameLayout mFragmentView = null;

    private PaginationView mPaginationView = null;
    private LinearLayout mPaginationDotsLayout = null;

    private PaginationView<BaseModel> mGearS2DemoView = null;
    private LinearLayout mGearS2DemoDotsLayout = null;

    private SlideFrameLayout mDeviceSpecContainer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "##### onCreate)+ ");

        mContext = this;
        setContentView(R.layout.activity_main);
        setupDrawerLayout(getDrawerItems());

        mDeviceSpecContainer = (SlideFrameLayout) findViewById(R.id.deviceSpecContainer);

        mFragmentView = (FrameLayout) findViewById(R.id.fragmentContainer);
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

    public void changeFragment(AppConst.UIState newState, AppConsts.TransactionDir dir) {
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
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NONE");
                    fragmentModel.setFragment(new VideoModel("Attractor",   //title
                            "video/0_Hero_Attractor.mp4", "frame/0_Hero_Attractor_frame.jpg",
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
                    fragmentModel.setBackground("@drawable/decision_bg");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@drawable/hero_logo",           //title
                            "@string/decision_subtitle",        //subtitle
                            "models/decision_menu.json",    //menu for ListView
                            null, null, null));             // video

                    fragmentModel.setLayout("@layout/fragment_decision_page");
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
//            case UI_STATE_WHATS_NEW_MAIN:
//                {
//                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
//                    fragmentModel.setBackground("@drawable/whats_new_bg");
//                    fragmentModel.setDrawerId("@id/drawer_whats_new");
//                    fragmentModel.setFragment(ModelUtil.getMenuModel(
//                            "@string/whats_new_demo_title",                //title
//                            "@string/whats_new_demo_subtitle",
//                            "models/whats_new_menu.json", //menu for ListView
//                            null,
//                            null,
//                            null));
//
//                    fragmentModel.setLayout("@layout/fragment_no_video_menu");
//                    mFragment = MenuFragment.newInstance(fragmentModel);
//                }
//                break;
            case UI_STATE_WHATS_DEMO_1:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                            "video/1.2_Hero_HeadTurning_Design.mp4", "frame/1.2_Hero_HeadTurning_Design_frame.jpg",
                            "chapter/1.2_Hero_HeadTurning_Design_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_head_turning_design");
                    mFragment = HeadTurningDesignFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_2:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("New to android Demo : more storage",             //title
                            "video/3.2_Hero_More_Storage.mp4", "frame/3.2_Hero_More_Storage_frame.jpg",
                            "chapter/3.2_Hero_More_Storage_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_more_storage");
                    mFragment = MoreStorageFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_3:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Auto-Focus",             //title
                            "video/2.1_Hero_Auto_Focus.mp4", "frame/2.1_Hero_Auto_Focus_frame.jpg",
                            "chapter/2.1_Hero_Auto_Focus_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_auto_focus");
                    mFragment = AutoFocusFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_4:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : always on touch display",   //title
                            "video/1.3_Hero_Always_On_Touch_Display.mp4", "frame/1.3_Hero_Always_On_Touch_Display_frame.jpg",
                            null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - 1. Design & Display : 4 demos
             */
//            case UI_STATE_DESIGN_MAIN:
//                {
//                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
//                    fragmentModel.setBackground("@drawable/design_bg");
//                    fragmentModel.setDrawerId("@id/drawer_design");
//                    fragmentModel.setFragment(ModelUtil.getMenuModel(
//                            "@string/design_demo_title",                //title
//                            "@string/design_demo_subtitle",
//                            "models/design_menu.json",                  //menu for ListView
//                            "@string/design_demo_video_title",
//                            "@string/design_demo_video_subtitle",
//                            "UI_STATE_DESIGN_DEMO_VIDEO"));
//
//                    fragmentModel.setLayout("@layout/fragment_video_menu");
//                    mFragment = MenuFragment.newInstance(fragmentModel);
//                }
//                break;

            // 1.0 Video play
            case UI_STATE_DESIGN_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Video",     //title
                            "video/1.0_Hero_DisplayBase.mp4", "frame/1.0_Hero_DisplayBase_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            // 1.1 Display
            case UI_STATE_DESIGN_DEMO_AMAZING_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Amazing display",   //title
                            "video/1.1_Hero_Amazing_Display.mp4", "frame/1.1_Hero_Amazing_Display_frame.jpg",
                            "chapter/1.1_Hero_Amazing_Display_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_amazing_display");
                    mFragment = AmazingDisplayFragment.newInstance(fragmentModel);
                }
                break;

            // 1.2 Design
            case UI_STATE_DESIGN_DEMO_HEAD_TURNING_DESIGN:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : Head turning design",   //title
                            "video/1.2_Hero_HeadTurning_Design.mp4", "frame/1.2_Hero_HeadTurning_Design_frame.jpg",
                            "chapter/1.2_Hero_HeadTurning_Design_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_head_turning_design");
                    mFragment = HeadTurningDesignFragment.newInstance(fragmentModel);
                }
                break;

            // 1.3 Edge Shorcut
//            case UI_STATE_DESIGN_DEMO_EDGE_SHORTCUT:
//                {
//                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
//                    fragmentModel.setFragment(new VideoModel("Design : Edge shortcut",   //title
//                            "video/1.3_Hero_Edge_Shortcuts.mp4", "frame/1.3_Hero_Edge_Shortcuts_frame.jpg",
//                            "chapter/1.3_Hero_Edge_Shortcuts_chap.json", null, null));
//
//                    fragmentModel.setLayout("@layout/frag_edge_shortcut");
//                    mFragment = EdgeShortcutFragment.newInstance(fragmentModel);
//                }
//                break;

            // 1.3 Always on touch display
            case UI_STATE_DESIGN_DEMO_TOUCH_DISPLAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGN_MAIN");
                    fragmentModel.setFragment(new VideoModel("Design : always on touch display",   //title
                            "video/1.3_Hero_Always_On_Touch_Display.mp4", "frame/1.3_Hero_Always_On_Touch_Display_frame.jpg",
                            null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - 2. Camera : 4 demos
             */
//            case UI_STATE_CAMERA_MAIN:
//                {
//                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
//                    fragmentModel.setBackground("@drawable/camera_bg");
//                    fragmentModel.setDrawerId("@id/drawer_camera");
//                    fragmentModel.setFragment(ModelUtil.getMenuModel(
//                            "@string/camera_demo_title",                //title
//                            "@string/camera_demo_subtitle",
//                            "models/camera_menu.json",                  //menu for ListView
//                            "@string/camera_demo_video_title",
//                            "@string/camera_demo_video_subtitle",
//                            "UI_STATE_CAMERA_DEMO_VIDEO"));
//
//                    fragmentModel.setLayout("@layout/fragment_video_menu");
//                    mFragment = MenuFragment.newInstance(fragmentModel);
//                }
//                break;

            // 2.0 Video play
            case UI_STATE_CAMERA_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Camera : Video",             //title
                            "video/2.0_Hero_CameraBase.mp4", "frame/2.0_Hero_CameraBase_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            // 2.1 Auto Focus
            case UI_STATE_CAMERA_DEMO_AUTO_FOCUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Auto-Focus",             //title
                            "video/2.1_Hero_Auto_Focus.mp4", "frame/2.1_Hero_Auto_Focus_frame.jpg",
                            "chapter/2.1_Hero_Auto_Focus_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_auto_focus");
                    mFragment = AutoFocusFragment.newInstance(fragmentModel);
                }
                break;

            // 2.2 Photo Quality
            case UI_STATE_CAMERA_DEMO_PHOTO_QUALITY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Photo Quality",             //title
                            "video/2.2_Hero_Image_Quality.mp4", "frame/2.2_Hero_Image_Quality_frame.jpg",
                            "chapter/2.2_Hero_Image_Quality_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_photo_quality");
                    mFragment = PhotoQualityFragment.newInstance(fragmentModel);
                }
                break;

            // 2.3 Selfies
            case UI_STATE_CAMERA_DEMO_SELFIES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_CAMERA_MAIN");
                    fragmentModel.setFragment(new VideoModel("Camera : Photo Selfies",             //title
                            "video/2.3_Hero_Selfies.mp4", "frame/2.3_Hero_Selfies_frame.jpg",
                            "chapter/2.3_Hero_Selfies_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_selfies");
                    mFragment = SelfiesFragment.newInstance(fragmentModel);
                }
                break;


            /**
             * MAIN - 3. Samsung Exclusives : 6 demos
             */
//            case UI_STATE_EXCLUSIVES_MAIN:
//                {
//                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
//                    fragmentModel.setBackground("@drawable/ss_exclusives_bg");
//                    fragmentModel.setDrawerId("@id/drawer_ss_exclusives");
//                    fragmentModel.setFragment(ModelUtil.getMenuModel(
//                            "@string/ss_exclusives_demo_title",                //title
//                            "@string/ss_exclusives_demo_subtitle",
//                            "models/samsung_exclusives.json",                  //menu for ListView
//                            "@string/ss_exclusives_demo_video_title",
//                            "@string/ss_exclusives_demo_video_subtitle",
//                            "UI_STATE_EXCLUSIVES_DEMO_VIDEO"));
//
//                    fragmentModel.setLayout("@layout/fragment_video_menu");
//                    mFragment = MenuFragment.newInstance(fragmentModel);
//                }
//                break;

            // 3.0 Video play
            case UI_STATE_EXCLUSIVES_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Video",             //title
                            "video/3.0_Hero_ExclusiveBase.mp4", "frame/3.0_Hero_ExclusiveBase_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            // 3.1 All day battery (More power to you)
            case UI_STATE_EXCLUSIVES_DEMO_BATTERY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel( "SS Exclusives : All-day battery",             //title
                            "video/3.1_Hero_All_Day_Battery.mp4", "frame/3.1_Hero_All_Day_Battery_frame.jpg",
                            "chapter/3.1_Hero_All_Day_Battery_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_battery");
                    mFragment = AllDayBatteryFragment.newInstance(fragmentModel);
                }
                break;

            // 3.2 Samsung Pay
            case UI_STATE_EXCLUSIVES_DEMO_SS_PAY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung pay",             //title
                            "video/3.3_Hero_Samsung_Pay.mp4", "frame/3.3_Hero_Samsung_Pay_frame.jpg",
                            "chapter/3.3_Hero_Samsung_Pay_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_pay");
                    mFragment = SamsungPayFragment.newInstance(fragmentModel);
                }
                break;

            // 3.3 Samsung+
            case UI_STATE_EXCLUSIVES_DEMO_SS_PLUS:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : samsung+",             //title
                            "video/3.4_Hero_Samsung_Plus.mp4", "frame/3.4_Hero_Samsung_Plus_frame.jpg",
                            "chapter/3.4_Hero_Samsung_Plus_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_samsung_plus");
                    mFragment = SamsungPlusFragment.newInstance(fragmentModel);
                }
                break;

            // 3.4 Gear VR
            case UI_STATE_EXCLUSIVES_DEMO_GEAR_VR:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Gear VR",             //title
                            "video/3.5_Hero_Gear_VR.mp4", "frame/3.5_Hero_Gear_VR_frame.jpg",
                            "chapter/3.5_Hero_Gear_VR_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_gear_vr");
                    mFragment = GearVRFragment.newInstance(fragmentModel);
                }
                break;

            // 3.5 Gear S2
            case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2:
            case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_2:
            case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_3:
            case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_4:
            case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_5:
            {
                if (mFragment != null)
                    mFragment.onPause();    //pause fragment on fragmentContainer

                if (mGearS2DemoView == null) {
                    createGearS2Demo(newState.name());
                    return;
                }
                else {
                    if (mGearS2DemoDotsLayout.getChildCount() == 0)
                        addDots(mGearS2DemoDotsLayout, mGearS2DemoView.getPages().size());

                    PaginationViewItem<BaseModel> page = mGearS2DemoView.getPage(newState.name());
                    mGearS2DemoView.setPage(newState.name());
                    mFragment = page.getFragment();
                    setDrawer(page.getModel().getDrawerId());
                    setDots(mGearS2DemoView, mGearS2DemoDotsLayout, mGearS2DemoView.getPageIndex(newState.name()));
                }
            }
            break;

            /**
             * MAIN - 4. new to android (switching to samsung) : 2 demo
             */
//            case UI_STATE_NEW_TO_ANDROID_MAIN:
//                {
//                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
//                    fragmentModel.setBackground("@drawable/new_to_android_bg");
//                    fragmentModel.setDrawerId("@id/drawer_new_to_android");
//                    fragmentModel.setFragment(ModelUtil.getMenuModel(
//                            "@string/new_to_android_demo_title",                //title
//                            "@string/new_to_android_demo_subtitle",
//                            "models/new_to_android_menu.json", //menu for ListView
//                            null,
//                            null,
//                            null));
//
//                    fragmentModel.setLayout("@layout/fragment_no_video_menu");
//                    mFragment = MenuFragment.newInstance(fragmentModel);
//                }
//                break;

            // 4.0 Video play
//            case UI_STATE_NEW_TO_ANDROID_DEMO_VIDEO:
//                {
//                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
//                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
//                    fragmentModel.setFragment(new VideoModel( "New to android : Video",             //title
//                            "video/android_base.mp4", "frame/android_base_frame.jpg", null, null, null));
//
//                    // DefaultVideoFragment : only video play(no interaction)
//                    fragmentModel.setLayout("@layout/fragment_default_video");
//                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
//                }
//                break;

            // 4.1 Smart switch
            case UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                    fragmentModel.setFragment(new VideoModel("New to android Demo : Smart switch",             //title
                            "video/4.1_Hero_Smart_Switch.mp4", "frame/4.1_Hero_Smart_Switch_frame.jpg",
                            "chapter/4.1_Hero_Smart_Switch_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_smart_switch");
                    mFragment = SmartSwitchFragment.newInstance(fragmentModel);
                }
                break;

            // 4.2 More Storage
            case UI_STATE_NEW_TO_ANDROID_DEMO_MORE_STORAGE:
            {
                FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                fragmentModel.setActionBackKey("UI_STATE_NEW_TO_ANDROID_MAIN");
                fragmentModel.setFragment(new VideoModel("New to android Demo : more storage",             //title
                        "video/3.2_Hero_More_Storage.mp4", "frame/3.2_Hero_More_Storage_frame.jpg",
                        "chapter/3.2_Hero_More_Storage_chap.json", null, null));

                fragmentModel.setLayout("@layout/frag_more_storage");
                mFragment = MoreStorageFragment.newInstance(fragmentModel);
            }
            break;

            /**
             * Compare device page
             */
            case UI_STATE_DEVICE_SPECS:
                {
                    FragmentModel<BaseModel> fragmentModel = new FragmentModel<BaseModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setFragment(new BaseModel());
                    fragmentModel.setLayout("@layout/fragment_device_specs");
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
            case UI_STATE_DESIGN_MAIN:
            case UI_STATE_CAMERA_MAIN:
            case UI_STATE_EXCLUSIVES_MAIN:
            case UI_STATE_NEW_TO_ANDROID_MAIN:
                if (mFragment != null)
                    mFragment.onPause();    //pause fragment on fragmentContainer

                if (mPaginationView == null) {
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
            newState != AppConst.UIState.UI_STATE_WHATS_NEW_MAIN            &&
            newState != AppConst.UIState.UI_STATE_CAMERA_MAIN               &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN           &&
            newState != AppConst.UIState.UI_STATE_DESIGN_MAIN               &&
            newState != AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN       &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2    &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_2  &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_3  &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_4  &&
            newState != AppConst.UIState.UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_5)
            insertFragment(mFragment);

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
                    switch (newState) {
                        case UI_STATE_WHATS_NEW_MAIN:
                        case UI_STATE_DESIGN_MAIN:
                        case UI_STATE_CAMERA_MAIN:
                        case UI_STATE_EXCLUSIVES_MAIN:
                        case UI_STATE_NEW_TO_ANDROID_MAIN:
                            if (mPaginationView.isShown()) break;
                            mPaginationView.setVisibility(View.VISIBLE);
                            if (mUIState == AppConst.UIState.UI_STATE_DECISION) {
                                AnimationUtil.scaleXYAnimation(mFragmentView, true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mPaginationView.bringToFront();
                                        mPaginationView.invalidate();
                                        mFragmentView.setVisibility(View.INVISIBLE);
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
                                            mPaginationView.invalidate();
                                            AnimationUtil.setMarginX(mFragmentView, 0);
                                            mFragmentView.setVisibility(View.INVISIBLE);
                                            mNavigationView.bringToFront();
                                        }
                                    });
                                }
                            }
                            break;

                        case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2:
                        case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_2:
                        case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_3:
                        case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_4:
                        case UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_5:
                            if (mGearS2DemoView.isShown()) break;
                            mGearS2DemoView.setVisibility(View.VISIBLE);
                            if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mGearS2DemoView.bringToFront();
                                        mGearS2DemoView.invalidate();
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                        mNavigationView.bringToFront();
                                    }
                                });
                            }
                            break;

                        case UI_STATE_DEVICE_SPECS:
                            if (!mDeviceSpecContainer.isShown()) {
                                getFragmentManager().beginTransaction()
                                        .add(R.id.deviceSpecContainer, mFragment)
                                        .commit();

                                mDeviceSpecContainer.setYFraction(1.f);
                                mDeviceSpecContainer.bringToFront();
                                mDeviceSpecContainer.setVisibility(View.VISIBLE);
                                mDeviceSpecContainer.invalidate();
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, true, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }
                                });
                            }
                            break;

                        default:
                            switch(mUIState) {
                                case UI_STATE_ATTRACT_LOOP:
                                case UI_STATE_DECISION:
                                    if (!mFragmentView.isShown()) {
                                        mFragmentView.setVisibility(View.VISIBLE);

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commit();

                                        if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                            AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    mFragmentView.bringToFront();
                                                    mFragmentView.invalidate();
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
                                                .commit();
                                    }
                                    break;
                                default:
                                    if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commit();

                                        AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                                mDeviceSpecContainer.setYFraction(1.f);
                                            }
                                        });
                                    }
                                    else if (!mFragmentView.isShown()) {
                                        AnimationUtil.setScaleXY(mFragmentView, 1f);
                                        AnimationUtil.setMarginX(mFragmentView, 0);
                                        mFragmentView.setVisibility(View.VISIBLE);

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.fragmentContainer, mFragment)
                                                .commit();

                                        if (AnimationUtil.getMarginX(mPaginationView) == 0) {
                                            AnimationUtil.marginXAnimation(mPaginationView, true, new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);
                                                    mFragmentView.bringToFront();
                                                    mFragmentView.invalidate();

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
                                                .commit();
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
                case TRANSACTION_DIR_BACKWARD:
                    switch (newState) {
                        case UI_STATE_WHATS_NEW_MAIN:
                        case UI_STATE_CAMERA_MAIN:
                        case UI_STATE_EXCLUSIVES_MAIN:
                        case UI_STATE_DESIGN_MAIN:
                        case UI_STATE_NEW_TO_ANDROID_MAIN:
                            final boolean bGearS2DemoShow = mGearS2DemoView.isShown();
                            AnimationUtil.setMarginX(mPaginationView, getResources().getInteger(R.integer.animXOffsetMinus));
                            mPaginationView.bringToFront();
                            mPaginationView.setVisibility(View.VISIBLE);
                            mNavigationView.bringToFront();
                            AnimationUtil.marginXAnimation(mPaginationView, false, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                    if (bGearS2DemoShow) {
                                        AnimationUtil.setMarginX(mGearS2DemoView, 0);
                                        mGearS2DemoView.setVisibility(View.INVISIBLE);
//                                        deleteGearS2Demo();
                                    }
                                    else {
                                        AnimationUtil.setMarginX(mFragmentView, 0);
                                        mFragmentView.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                            break;
                        case UI_STATE_ATTRACT_LOOP:
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.animator.scale_up, FragmentTransaction.TRANSIT_NONE)
                                    .add(R.id.fragmentContainer, mFragment)
                                    .commit();
                            break;
                        case UI_STATE_DECISION:
                            if (mDeviceSpecContainer.isShown()) {   //device spec fragment is front
                                AnimationUtil.slidingAnimation(mDeviceSpecContainer, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mDeviceSpecContainer.setVisibility(View.INVISIBLE);
                                        mDeviceSpecContainer.setYFraction(1.f);

                                        getFragmentManager().beginTransaction()
                                                .add(R.id.fragmentContainer, mFragment)
                                                .commit();
                                    }
                                });
                            }
                            else if (!mFragmentView.isShown()) {
                                mFragmentView.bringToFront();
                                mFragmentView.setVisibility(View.VISIBLE);
                                mNavigationView.bringToFront();

                                getFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commit();

                                AnimationUtil.scaleAnimation(mFragmentView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else {
                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.animator.scale_up, FragmentTransaction.TRANSIT_NONE)
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commit();
                            }
                            break;
                        default:
                            if (!mFragmentView.isShown()) {
                                AnimationUtil.setMarginX(mFragmentView, getResources().getInteger(R.integer.animXOffsetMinus));
                                mFragmentView.bringToFront();
                                mFragmentView.setVisibility(View.VISIBLE);
                                mNavigationView.bringToFront();

                                getFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commit();

                                AnimationUtil.marginXAnimation(mFragmentView, false, new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        AnimationUtil.setMarginX(mPaginationView, 0);
                                        mPaginationView.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else {
                                getFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.animator.left_in, FragmentTransaction.TRANSIT_NONE)
                                        .add(R.id.fragmentContainer, mFragment)
                                        .commit();
                            }
                            break;
                    }

                    mFragmentsHandler.postDelayed(mFragmentsRunnable, getResources().getInteger(R.integer.animTime) + 100);
                    break;
            }
        }


        mUIState = newState;
    }

    /**
     *  goto the end demo page
     *
     */
    public void changeEndDemoFragment(AppConst.UIState backFragment, AppConsts.TransactionDir dir) {
        MenuModel menuModel = ModelUtil.getMenuModel("@drawable/hero_logo_black",
                "@string/demo_end_subtitle", "models/demo_end_menu.json", null, null, null);
        menuModel.getMenuItems().get(1).setAction(backFragment.name());    //change action for first menu item

        FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
        fragmentModel.setActionBackKey(backFragment.name());
        fragmentModel.setBackground("@drawable/end_page_bg");
        fragmentModel.setFragment(menuModel);     //menu for ListView

        fragmentModel.setLayout("@layout/fragment_end_page");
        mFragment = MenuFragment.newInstance(fragmentModel);
        changeFragment(AppConst.UIState.UI_STATE_DEMO_END, dir);
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

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.chevron_black);
        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.discover_new_features),
                        AppConst.UIState.UI_STATE_WHATS_NEW_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.dazzling_display),
                        AppConst.UIState.UI_STATE_DESIGN_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.advanced_camera),
                        AppConst.UIState.UI_STATE_CAMERA_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.samsung_exclusives),
                        AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.switching_to_samsung),
                        AppConst.UIState.UI_STATE_NEW_TO_ANDROID_MAIN.name()));

        mDrawerItems.add(
                new DrawerItem(
                        icon,
                        getString(R.string.device_specs),
                        AppConst.UIState.UI_STATE_UNDER_CONSTRUCTION.name()));

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

    public void onSelectDrawerItem(int position) {
        changeFragment(AppConst.UIState.valueOf(mDrawerItems.get(position).getAction()), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
    }

    public void setDrawer(String drawerId) {
        super.setDrawer(drawerId);
    }

    public void clickDrawerBtn() {
        super.clickDrawerBtn();
    }


    /*
     * Main Menu Pagination View
     */
    private void createMainMenu(String uiState) {
        if (mPaginationView != null || mPaginationDotsLayout != null) return;
        mPaginationView = (PaginationView) findViewById(R.id.paginationView);
        mPaginationDotsLayout = (LinearLayout) findViewById(R.id.dots_layout);
        MainMenuAsyncTask mainMenuAsyncTask = new MainMenuAsyncTask(mPaginationView,
                getFragmentManager(), mPaginationViewListener);
        mainMenuAsyncTask.execute(uiState)if (mPaginationView.getPages() == null) return;;
    }

    private void deleteMainMenu() {
        if (mPaginationView == null || mPaginationDotsLayout == null) return;
        if (mPaginationView.getPages() == null) return;

        for(int i=mPaginationView.getPages().size()-1; i>=0; i--) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mPaginationView.getPages().get(i).getFragment());
            ft.commit();
            mPaginationView.getPages().remove(i);
        }
        mPaginationView = null;
        mPaginationDotsLayout = null;
    }

    private PaginationView.PaginationViewListener mPaginationViewListener = new PaginationView.PaginationViewListener() {
        @Override
        public void onPaginationReady(String uiState) {
            changeFragment(AppConst.UIState.valueOf(uiState), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
        }

        @Override
        public
        void onChangedPage(BaseFragment fragment, String uiState, String drawerId, int pageIndex) {
            mFragment = (BaseFragment)fragment;
            mUIState = AppConst.UIState.valueOf(uiState);
            setDrawer(drawerId);
            setDots(mPaginationView, mPaginationDotsLayout, pageIndex);
        }
    };


    /*
     * Gear S2 Pagination View
     */
    private void createGearS2Demo(String uiState) {
        if (mGearS2DemoView != null || mGearS2DemoDotsLayout!= null) return;
        mGearS2DemoView = (PaginationView) findViewById(R.id.gearS2DemoPaginationView);
        mGearS2DemoDotsLayout = (LinearLayout) findViewById(R.id.gearS2Demo_dots_layout);
        GearS2DemoAsyncTask gearS2DemoAsyncTask = new GearS2DemoAsyncTask(mGearS2DemoView,
                getFragmentManager(), mGearS2DemoViewListener);
        gearS2DemoAsyncTask.execute(uiState);
    }

    private void deleteGearS2Demo() {
        if (mGearS2DemoView == null || mGearS2DemoDotsLayout == null) return;
        if (mGearS2DemoView.getPages() == null) return;

        for(int i=mGearS2DemoView.getPages().size()-1; i>=0; i--) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(mGearS2DemoView.getPages().get(i).getFragment());
            ft.commit();
            mGearS2DemoView.getPages().remove(i);
        }
        mGearS2DemoView = null;
        mGearS2DemoDotsLayout = null;
    }

    private PaginationView.PaginationViewListener mGearS2DemoViewListener = new PaginationView.PaginationViewListener() {
        @Override
        public void onPaginationReady(String uiState) {
            changeFragment(AppConst.UIState.valueOf(uiState), AppConsts.TransactionDir.TRANSACTION_DIR_FORWARD);
        }

        @Override
        public
        void onChangedPage(BaseFragment fragment, String uiState, String drawerId, int pageIndex) {
            mFragment = (BaseFragment)fragment;
            mUIState = AppConst.UIState.valueOf(uiState);
            setDrawer(drawerId);
            setDots(mGearS2DemoView, mGearS2DemoDotsLayout, pageIndex);
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