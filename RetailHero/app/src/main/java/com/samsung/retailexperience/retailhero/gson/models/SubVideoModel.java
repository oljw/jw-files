package com.samsung.retailexperience.retailhero.gson.models;

/**
 * Created by smheo on 1/31/2016.
 */
public class SubVideoModel {
    private String videoFile;
    private String frameFile;

    public SubVideoModel(String videoFile, String frameFile) {
        this.videoFile = videoFile;
        this.frameFile = frameFile;
    }

    public String getVideoFile() {
        return this.videoFile;
    }
    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    public String getFrameFile() {
        return this.frameFile;
    }
    public void setFrameFile(String frameFile) {
        this.frameFile = frameFile;
    }
}
