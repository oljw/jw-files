package com.dev.jw.restapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
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

        final TextView textViewButton = findViewById(R.id.text_view_btn);
        final TextView textViewButton2 = findViewById(R.id.text_view_btn2);

        final APIService service = RetrofitInstance.getRetrofitInstance().create(APIService.class);

        textViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // putting boolean value
                Call<Video> call = service.putBoolean(1, true);
                call.enqueue(new Callback<Video>() {
                    @Override
                    public void onResponse(Call<Video> call, Response<Video> response) {
                        Log.d("-----> JW: 1", "PUT: to RESTful API successful.");
                    }

                    @Override
                    public void onFailure(Call<Video> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        textViewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // putting boolean value
                Call<Video> call = service.putBoolean(2, true);
                call.enqueue(new Callback<Video>() {
                    @Override
                    public void onResponse(Call<Video> call, Response<Video> response) {
                        Log.d("-----> JW: 2", "PUT: to RESTful API successful.");
                    }

                    @Override
                    public void onFailure(Call<Video> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        EditText editText = findViewById(R.id.edit_text);
//        editText.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                Log.d("JW", "-----------> keyCode: " + keyCode);
////                Log.d("JW", "-----------> event: " + event);
//
//                return false;
//            }
//        });

        final boolean[] isPressed = {false};
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (isPressed[0]) {
                        Log.d("JW", "-----------> shift enter ");
                        // Shift + Enter pressed

                        return false;
                    } else {
                        Log.d("JW", "-----------> enter ");
                        // Enter pressed

                        return true;
                    }
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT)) {
                    Log.d("JW", "-----------> shift down ");
                    isPressed[0] = true;
                    return false;
                }

                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT)) {
                    Log.d("JW", "-----------> shift up ");
                    isPressed[0] = false;
                    return false;
                }


                return false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.d("JW", "-----------> dispatchKeyEvent: " + event);
        return super.dispatchKeyEvent(event);
    }
}
