package developer.jw.dryproject;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        doSomething();
        createTextFile("jwTextFile.txt", "this is a test");
    }

    private void doSomething() {
//        final String extDir = new StringBuilder()
////                .append(Environment.getExternalStorageDirectory().getAbsolutePath())
//                .append("/storage/emulated/0/")
//                .append(File.separator)
//                .append("Android")
//                .append(File.separator)
//                .append("data")
//                .append(File.separator)
////                .append(this.getPackageName())
//                .append("com.samsung.retailexperience.retailcrown")
//                .append(File.separator)
//                .append("files")
//                .toString();
        final String extDir = new StringBuilder()
                .append(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator)
                .append("contents")
                .toString();

        File dir  = new File(extDir);
        if (!dir.exists()) {
            Log.d(TAG, "##### ICANMOBILE : dir.mkdir : extDir = " + extDir);
            dir.mkdir();
        }
        else {
            Log.d(TAG, "##### ICANMOBILE : EXIST : extDir = " + extDir);
        }
    }

    public void createTextFile(String fileName, String sBody) {
        try {
//            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            final String extDir = new StringBuilder()
                    .append(Environment.getExternalStorageDirectory().getAbsolutePath())
                    .append(File.separator)
                    .append("contents")
                    .toString();

            File root  = new File(extDir);

            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
