package jw.developer.com.camera2project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    static MainActivity myActivity;
    private Button openCamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);

        openCamBtn = (Button) findViewById(R.id.camera_opener);
        openCamBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  openCamBtn.setVisibility(View.GONE);
                  getFragmentManager().beginTransaction()
                      .replace(R.id.container, BaseCameraFragment.newInstance())
                      .commit();
                }
            }
        );
    }
}
