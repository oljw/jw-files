package com.dev.jw.restapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/***************************
 * Created by jw on 3/1/19.
 ***************************/
public class VideoList {

    @SerializedName("videos")
    private ArrayList<Video> videoList;

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }
}
