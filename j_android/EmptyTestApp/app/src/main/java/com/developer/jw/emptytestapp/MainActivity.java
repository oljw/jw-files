package com.developer.jw.emptytestapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    FrameLayout f1;
    FrameLayout f2;
    FrameLayout f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        f1 = (FrameLayout) findViewById(R.id.first);
        f2 = (FrameLayout) findViewById(R.id.second);
        f3 = (FrameLayout) findViewById(R.id.third);

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SUP", " frame 1 clicked ");
            }
        });

        f3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("SUP", " frame 3 clicked ");
                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("SUP", " Screen clicked ");
        return super.dispatchTouchEvent(ev);
    }
}
