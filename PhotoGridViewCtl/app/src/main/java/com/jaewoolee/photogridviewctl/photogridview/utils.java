package com.jaewoolee.photogridviewctl.photogridview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

public class utils {
    private utils() {};

    @TargetApi(11)
    public static void enableStrictMode() {
        if (utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

//            if (Utils.hasHoneycomb()) {
//                threadPolicyBuilder.penaltyFlashScreen();
//                vmPolicyBuilder
//                        .setClassInstanceLimit(PCollageImageSelectActivity.class, 1)
//                        .setClassInstanceLimit(PGalleryActivity.class, 1)
//                		.setClassInstanceLimit(POrganizeActivity.class, 1);
//            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    //ICANMOBILE (-)
    /*
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
	*/
    
//    public static boolean hasJellyBean() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
//    }
}

