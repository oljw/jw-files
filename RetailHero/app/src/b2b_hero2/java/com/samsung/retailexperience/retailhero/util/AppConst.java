package com.samsung.retailexperience.retailhero.util;

/**
 * Created by icanmobile on 1/12/16.
 */
public class AppConst {
    public enum UIState {
        UI_STATE_NONE,

        UI_STATE_ATTRACT_LOOP,

        UI_STATE_DECISION,

        // what's new : 3 demos
        UI_STATE_WHATS_NEW_MAIN,
        UI_STATE_WHATS_DEMO_1,
        UI_STATE_WHATS_DEMO_2,
        UI_STATE_WHATS_DEMO_3,

        // 1.0 Productivity : 3 demos
        UI_STATE_PRODUCTIVITY_MAIN,
        UI_STATE_PRODUCTIVITY_DEMO_VIDEO,
        UI_STATE_PRODUCTIVITY_DEMO_BATTERY,
        UI_STATE_PRODUCTIVITY_DEMO_SD,

        // 2.0 samsung exclusives : 4 demos
        UI_STATE_EXCLUSIVES_MAIN,
        UI_STATE_EXCLUSIVES_DEMO_VIDEO,
        UI_STATE_EXCLUSIVES_DEMO_SS_KNOX,
        UI_STATE_EXCLUSIVES_DEMO_SS_PAY,
        UI_STATE_EXCLUSIVES_DEMO_SS_PLUS,
        UI_STATE_EXCLUSIVE_DEMO_GEAR_S2,
        UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_2,
        UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_3,
        UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_4,
        UI_STATE_EXCLUSIVE_DEMO_GEAR_S2_5,

        // 3.0 designed for business : 4 demos
        UI_STATE_DESIGN_MAIN,
        UI_STATE_DESIGN_DEMO_VIDEO,
        UI_STATE_DESIGN_DEMO_AMOLED,
        UI_STATE_DESIGN_DEMO_EDGE_FUNC,
        UI_STATE_DESIGN_DEMO_CAMERA,

        // 4.0 new to android : 2 demo
        UI_STATE_NEW_TO_ANDROID_MAIN,
//        UI_STATE_NEW_TO_ANDROID_DEMO_VIDEO,
        UI_STATE_NEW_TO_ANDROID_DEMO_SMART_SWITCH,
        UI_STATE_NEW_TO_ANDROID_DEMO_EXPANDABLE_MEMORY,

        // compare device - ref decision
        UI_STATE_DEVICE_SPECS,

        UI_STATE_DEMO_END,

        UI_STATE_UNDER_CONSTRUCTION
    }
}