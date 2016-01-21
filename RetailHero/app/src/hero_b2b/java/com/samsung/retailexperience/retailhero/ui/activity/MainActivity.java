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
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_AmoledDisplayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.business.B2B_CameraFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives.B2B_KnoxFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives.B2B_SamsungPayFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.exclusives.B2B_SamsungPlusFragment;
import com.samsung.retailexperience.retailhero.ui.fragment.demos.productivity.B2B_ExpandSDFragment;
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
                    fragmentModel.setActionBackKey("UI_STATE_ATTRACT_LOOP");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/decision_title",           //title
                            "models/decision_menu.json"));      //menu for ListView

                    fragmentModel.setLayout("@layout/fragment_menu_center_align");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;


            /**
             * End demo page
             */
            case UI_STATE_DEMO_END:
                break;


            /**
             *  MAIN - what's new : 3 demos
             */
            case UI_STATE_WHATS_NEW_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_whats_new");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/whats_new_demo",                //title
                            "models/whats_new_menu.json"));     //menu for ListView

                    fragmentModel.setLayout("@layout/fragment_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_PRODUCTIVITY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New : Productivity",  //title
                            "video/productivity_base.mp4", "frame/productivity_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_EXCLUSIVES:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Whats New : SS Exclusives",  //title
                            "video/SsExclusives_base.mp4", "frame/SsExclusives_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_WHATS_DEMO_DESIGNED:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_WHATS_NEW_MAIN");
                    fragmentModel.setFragment(new VideoModel("Whats New : Designed for business",  //title
                            "video/designed_base.mp4", "frame/designed_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Productivity : 3 demos
             */
            case UI_STATE_PRODUCTIVITY_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();

                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_productivity");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/productivity_demo",                //title
                            "models/productivity_menu.json"));     //menu for ListView

                    fragmentModel.setLayout("@layout/fragment_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_PRODUCTIVITY_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : Video",     //title
                            "video/productivity_base.mp4", "frame/productivity_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_PRODUCTIVITY_DEMO_BATTERY:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : All day battery",   //title
                            "video/productivity_battery.mp4", "frame/productivity_battery_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_PRODUCTIVITY_DEMO_SD:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_PRODUCTIVITY_MAIN");
                    fragmentModel.setFragment(new VideoModel("Productivity : Expandable SD",   //title
                            "video/productivity_sd.mp4", "frame/productivity_sd_frame.jpg",
                            "chapter/productivity_sd_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_productivity_sd");
                    mFragment = B2B_ExpandSDFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Exclusives : 4 demos
             */
            case UI_STATE_EXCLUSIVES_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_ss_exclusives");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/ss_exclusives_demo",                //title
                            "models/samsung_exclusives.json"));     //menu for ListView

                    fragmentModel.setLayout("@layout/fragment_menu");
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
            case UI_STATE_EXCLUSIVES_DEMO_SS_KNOX:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_EXCLUSIVES_MAIN");
                    fragmentModel.setFragment(new VideoModel("SS Exclusives : Samsung KNOX",             //title
                            "video/SsExclusives_knox.mp4", "frame/SsExclusives_knox_frame.jpg",
                            "chapter/SsExclusives_knox_chap.json", null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/frag_samsung_knox");
                    mFragment = B2B_KnoxFragment.newInstance(fragmentModel);
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
                    mFragment = B2B_SamsungPayFragment.newInstance(fragmentModel);
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
                    mFragment = B2B_SamsungPlusFragment.newInstance(fragmentModel);
                }
                break;

            /**
             * MAIN - Designed for Business : 3 demos
             */
            case UI_STATE_DESIGNED_MAIN:
                {
                    FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DECISION");
                    fragmentModel.setBackground("@color/white");
                    fragmentModel.setDrawerId("@id/drawer_design_for_business");
                    fragmentModel.setFragment(ModelUtil.getMenuModel(
                            "@string/designed_demo",                //title
                            "models/designed_menu.json"));     //menu for ListView

                    fragmentModel.setLayout("@layout/fragment_menu");
                    mFragment = MenuFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGNED_DEMO_VIDEO:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGNED_MAIN");
                    fragmentModel.setFragment(new VideoModel( "Designed : Video",             //title
                            "video/designed_base.mp4", "frame/designed_base_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            case UI_STATE_DESIGNED_DEMO_AMOLED:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGNED_MAIN");
                    fragmentModel.setFragment(new VideoModel("Designed : Super AMOLED Display",             //title
                            "video/designed_amoled.mp4", "frame/designed_amoled_frame.jpg",
                            "chapter/designed_amoled_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_designed_amoled");
                    mFragment = B2B_AmoledDisplayFragment.newInstance(fragmentModel);
                }
                break;
            /* This demo is in Hero2 B2B.
            case UI_STATE_DESIGNED_DEMO_EDGE_FUNC:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGNED_MAIN");
                    fragmentModel.setFragment(new VideoModel("Designed : Edge Functionality",             //title
                            "video/designed_edge.mp4", "frame/designed_edge_frame.jpg", null, null, null));

                    // DefaultVideoFragment : only video play(no interaction)
                    fragmentModel.setLayout("@layout/fragment_default_video");
                    mFragment = DefaultVideoFragment.newInstance(fragmentModel);
                }
                break;
            */
            case UI_STATE_DESIGNED_DEMO_CAMERA:
                {
                    FragmentModel<VideoModel> fragmentModel = new FragmentModel<VideoModel>();
                    fragmentModel.setActionBackKey("UI_STATE_DESIGNED_MAIN");
                    fragmentModel.setFragment(new VideoModel("Designed : Camera",             //title
                            "video/designed_camera.mp4", "frame/designed_camera_frame.jpg",
                            "chapter/designed_camera_chap.json", null, null));

                    fragmentModel.setLayout("@layout/frag_designed_camera");
                    mFragment = B2B_CameraFragment.newInstance(fragmentModel);
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

        FragmentModel<MenuModel> fragmentModel = new FragmentModel<MenuModel>();
        fragmentModel.setActionBackKey(backFragment.name());
        fragmentModel.setBackground("@color/white");
        fragmentModel.setFragment(menuModel);     //menu for ListView

        fragmentModel.setLayout("@layout/fragment_menu");
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
                else if (menuItem.getItemId() == R.id.drawer_productivity)
                    newState = AppConst.UIState.UI_STATE_PRODUCTIVITY_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_ss_exclusives)
                    newState = AppConst.UIState.UI_STATE_EXCLUSIVES_MAIN;
                else if (menuItem.getItemId() == R.id.drawer_design_for_business)
                    newState = AppConst.UIState.UI_STATE_DESIGNED_MAIN;

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