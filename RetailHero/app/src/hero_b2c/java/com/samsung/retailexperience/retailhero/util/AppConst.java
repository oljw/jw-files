package com.samsung.retailexperience.retailhero.util;

/**
 * Created by icanmobile on 1/12/16.
 */
public class AppConst {
    public enum UIState {
        UI_STATE_NONE,

        UI_STATE_ATTRACT_LOOP,

        UI_STATE_DECISION,

        UI_STATE_DEMO_END,

        // what's new : 4 demos
        UI_STATE_WHATS_NEW_MAIN,
        UI_STATE_WHATS_DEMO_DESIGN,
        UI_STATE_WHATS_DEMO_CAMERA,
        UI_STATE_WHATS_DEMO_EXCLUSIVES,
        UI_STATE_WHATS_DEMO_NEW_TO_ANDROID,

        // design : 4 demos
        UI_STATE_DESIGN_MAIN,
        UI_STATE_DESIGN_DEMO_VIDEO,
        UI_STATE_DESIGN_DEMO_AMAZING_DISPLAY,
        UI_STATE_DESIGN_DEMO_HEAD_TURNING_DESIGN,
//        UI_STATE_DESIGN_DEMO_EDGE_SHORTCUT,
        UI_STATE_DESIGN_DEMO_TOUCH_DISPLAY,

        // camera : 4 demos
        UI_STATE_CAMERA_MAIN,
        UI_STATE_CAMERA_DEMO_VIDEO,
        UI_STATE_CAMERA_DEMO_AUTO_FOCUS,
        UI_STATE_CAMERA_DEMO_PHOTO_QUALITY,
        UI_STATE_CAMERA_DEMO_SELFIES,

        // exclusives : 6 demos
        UI_STATE_EXCLUSIVES_MAIN,
        UI_STATE_EXCLUSIVES_DEMO_VIDEO,
        UI_STATE_EXCLUSIVES_DEMO_BATTERY,
        UI_STATE_EXCLUSIVES_DEMO_MORE_STORAGE,
        UI_STATE_EXCLUSIVES_DEMO_SS_PAY,
        UI_STATE_EXCLUSIVES_DEMO_SS_PLUS,
        UI_STATE_EXCLUSIVES_DEMO_GEAR_VR,

        // new to android : 2 demo
        UI_STATE_NEW_TO_ANDROID_MAIN,
        UI_STATE_NEW_TO_ANDROID_DEMO_VIDEO,
        UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH,

        // compare device - ref decision
        UI_STATE_COMPARE_DEVICE,

        UI_STATE_UNDER_CONSTRUCTION
    }
}