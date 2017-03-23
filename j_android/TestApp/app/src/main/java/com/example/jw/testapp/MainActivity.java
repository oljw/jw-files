package com.example.jw.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.samsung.retailexperience.retaildream.dream");
                    intent.putExtra("STORE_ID", "Bellevue");
                    intent.putExtra("DEVICE_ID", "123-456");
                    intent.putExtra("CHANNEL_NAME", "Best Buy");
                    getApplicationContext().startActivity(intent);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
