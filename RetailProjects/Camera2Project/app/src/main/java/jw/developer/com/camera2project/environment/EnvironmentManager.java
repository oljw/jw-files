package jw.developer.com.camera2project.environment;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import jw.developer.com.camera2project.R;

//import com.tecace.retail.videomanager.R;
//import com.tecace.retail.appmanager.config.environment.IEnvironments;

/**
 * Created by smheo on 9/29/2015.
 */
public class EnvironmentManager {
    private final String TAG = EnvironmentManager.class.getSimpleName();

    private static EnvironmentManager sInstance = null;
    public static EnvironmentManager getInstance() {
        if (sInstance == null)
            sInstance = new EnvironmentManager();
        return sInstance;
    }

    private HashMap<String,String> stringValues = null;
    private HashMap<String,Boolean> booleanValues = null;

    public String getStringValue(Context context, String key){
        loadEnvironment(context);
        if(stringValues != null && stringValues.containsKey(key))
            return stringValues.get(key);
        else
            return null;
    }

    private void putStringValue(String key, String value){
        if (stringValues == null) return;
        stringValues.put(key,value);
    }

    public boolean getBooleanValue(Context context, String key){
        loadEnvironment(context);
        if(booleanValues != null && booleanValues.containsKey(key))
            return booleanValues.get(key);
        else
            return false;
    }

    private void putBooleanValue(String key, Boolean value){
        if (booleanValues == null) return;
        booleanValues.put(key,value);
    }

    public void loadEnvironment(Context context){
        if (stringValues != null && booleanValues != null) return;
        stringValues = new HashMap<>();
        booleanValues = new HashMap<>();

        try{
            InputStream input = context.getResources().openRawResource(R.raw.environment);
            if (input == null) return;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while((line = reader.readLine()) != null){
                //Log.d(TAG,"env::line: " + line);
                if(line.trim().length() != 0 && line.trim().charAt(0) != '#'){
                    if(line.contains("=")){
                        String[] pair = line.trim().split("=");
                        if(pair.length > 1){
                            String key = pair[0].trim();
                            String val = pair[1].trim().toLowerCase();
                            if(val.equals("true") || val.equals("false")){
                                putBooleanValue(key, Boolean.valueOf(val));
                            }else{
                                putStringValue(key,val);
                            }
                        }
                    }
                }
            }

            if (input != null) input.close();
            if (reader != null) reader.close();

        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
