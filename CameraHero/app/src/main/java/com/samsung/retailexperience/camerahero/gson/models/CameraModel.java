package com.samsung.retailexperience.camerahero.gson.models;

import android.util.Log;

/**
 * Created by icanmobile on 1/14/16.
 */
public class CameraModel {
    private String stillFolder;
    private String videoFolder;

    public CameraModel() {
        this.stillFolder = null;
        this.videoFolder = null;
    }

    public String getStillFolder() {
        return this.stillFolder;
    }
    public void setStillFolder(String stillFolder) {
        this.stillFolder = stillFolder;
    }

    public String getVideoFolder() {
        return this.videoFolder;
    }
    public void setVideoFolder(String videoFolder) {
        this.videoFolder = videoFolder;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.stillFolder != null)
            appendString(builder, "stillFolder = " + this.stillFolder);
        if (this.videoFolder != null) {
            appendString(builder, "videoFolder = " + this.videoFolder);
        }
        return builder.toString();
    }

    private void appendString(StringBuilder builder, String value) {
        builder.append(value + System.getProperty("line.separator"));
    }

    public void print() {
        Log.d(FragmentModel.class.getName(), toString());
    }
}
