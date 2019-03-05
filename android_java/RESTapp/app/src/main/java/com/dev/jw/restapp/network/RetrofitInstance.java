package com.dev.jw.restapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***************************
 * Created by jw on 3/1/19.
 ***************************/
public class RetrofitInstance {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://192.168.86.88:3000";

    private RetrofitInstance() {}

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
