package com.dev.jw.jproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/***************************
 * Created by jw on 2019-05-20.
 ***************************/
public class AnotherActivity extends AppCompatActivity {

    RelativeLayout container;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_activity);

        TextView textView = findViewById(R.id.another_text_view);

        container = findViewById(R.id.another_container);



//        if (getIntent().hasExtra("image")){
            //convert to bitmap
//            byte[] byteArray = getIntent().getByteArrayExtra("image");
//            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        }

//        if (bitmap != null) {
            //Convert bitmap to BitmapDrawable
            bitmap = getIntent().getParcelableExtra("image");
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            container.setBackground(bitmapDrawable);
//        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AnotherActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
