package com.dev.jw.restapp.network;

import com.dev.jw.restapp.model.Video;
import com.dev.jw.restapp.model.VideoList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/***************************
 * Created by jw on 2/28/19.
 ***************************/
public interface APIService {

    @GET("/videos/")
    Call<VideoList> getVideoList();

    @GET("/videos/{id}")
    Call<Video> getVideo(@Path("id") int id);

    @FormUrlEncoded
    @PUT("/videos/{id}")
    Call<Video> putBoolean(@Path("id") int id, @Field("play") boolean play);
}