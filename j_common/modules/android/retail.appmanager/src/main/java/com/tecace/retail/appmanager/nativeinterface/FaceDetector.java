package com.tecace.retail.appmanager.nativeinterface;

/**
 * Created by icanmobile on 6/20/16.
 */
public class FaceDetector {
    private static final String TAG = FaceDetector.class.getSimpleName();

    private static FaceDetector sInstance = null;
    public static FaceDetector getInstance() {
        if (sInstance == null)
            sInstance = new FaceDetector();
        return sInstance;
    }

    public void Init(String appDir, String storageRootDir, String cascadeFileName) {
        init(appDir, storageRootDir, cascadeFileName);
    }

    public void Deinit() {
        deinit();
    }

    public void SetFrame(byte[] rgba, int width, int height, int channels) {
        setFrame(rgba, width, height, channels);
    }

    public void DoProcess() {
        doProcess();
    }

    public int[] GetRegions() {
        return getRegions();
    }


    public int[] Process(String photoPath, byte[] rgba, int width, int height, int channels)
    {
        return process(photoPath, rgba, width, height, channels);
    }




    static {
        System.loadLibrary("NativeFaceDetector");
    }

    public native void init(String appDir, String storageRootDir, String cascadeFileName);
    public native void deinit();
    public native void setFrame(byte[] rgba, int width, int height, int channels);
    public native void doProcess();
    public native int[] getRegions();

    public native int[] process(String photoPath, byte[] rgba, int width, int height, int channels);
}
