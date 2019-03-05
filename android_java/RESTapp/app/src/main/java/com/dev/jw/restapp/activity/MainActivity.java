package com.dev.jw.restapp.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.dev.jw.restapp.R;
import com.dev.jw.restapp.model.Video;
import com.dev.jw.restapp.network.RetrofitInstance;
import com.dev.jw.restapp.network.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final VideoView vidView = findViewById(R.id.myVideo);
        final MediaController vidControl = new MediaController(this);

        /*Create handle for the RetrofitInstance interface*/
        APIService service = RetrofitInstance.getRetrofitInstance().create(APIService.class);

        // getting video url
        Call<Video> call = service.getVideo(1);
        Log.d("------> JW: request().url(): ", call.request().url() + "");
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                Log.d("------> JW: body().getPath(): ", response.body().getPath());
                String vidAddress = response.body().getPath();
                Uri vidUri = Uri.parse(vidAddress);
                vidView.setVideoURI(vidUri);


            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
            }
        });

        // putting boolean value
        Call<Video> call2 = service.putBoolean(1, true);
        call2.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                Log.d("----------> JW: ", "yeah baby");
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {

            }
        });

        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();
    }
}
