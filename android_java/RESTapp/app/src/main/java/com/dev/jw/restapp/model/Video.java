package com.dev.jw.restapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/***************************
 * Created by jw on 2/28/19.
 ***************************/
public class Video {

    @SerializedName("path")
    private String path;

    @SerializedName("play")
    private boolean play;

    public Video(String path, boolean play) {
        this.path = path;
        this.play = play;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
}
