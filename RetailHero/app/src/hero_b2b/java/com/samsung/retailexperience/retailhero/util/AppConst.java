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

        // what's new : 3 demos
        UI_STATE_WHATS_NEW_MAIN,
        UI_STATE_WHATS_DEMO_PRODUCTIVITY,
        UI_STATE_WHATS_DEMO_EXCLUSIVES,
        UI_STATE_WHATS_DEMO_DESIGNED,

        // Productivity : 3 demos
        UI_STATE_PRODUCTIVITY_MAIN,
        UI_STATE_PRODUCTIVITY_DEMO_VIDEO,
        UI_STATE_PRODUCTIVITY_DEMO_BATTERY,
        UI_STATE_PRODUCTIVITY_DEMO_SD,

        // samsung exclusives : 4 demos
        UI_STATE_EXCLUSIVES_MAIN,
        UI_STATE_EXCLUSIVES_DEMO_VIDEO,
        UI_STATE_EXCLUSIVES_DEMO_SS_KNOX,
        UI_STATE_EXCLUSIVES_DEMO_SS_PAY,
        UI_STATE_EXCLUSIVES_DEMO_SS_PLUS,

        // designed for business : 3 demos
        UI_STATE_DESIGN_MAIN,
        UI_STATE_DESIGN_DEMO_VIDEO,
        UI_STATE_DESIGN_DEMO_AMOLED,
        UI_STATE_DESIGN_DEMO_CAMERA
//        UI_STATE_DESIGN_DEMO_EDGE_FUNC,
        ,

        // compare device - ref decision
        UI_STATE_DEVICE_SPECS,

        UI_STATE_UNDER_CONSTRUCTION
    }
}