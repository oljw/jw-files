package com.samsung.retailexperience.retailecosystem.video.config.environment;

import android.content.Context;
import android.util.Log;

import com.samsung.retailexperience.retailecosystem.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by smheo on 9/29/2015.
 */
public class EnvironmentManager implements IEnvironments {
    private final String TAG = EnvironmentManager.class.getSimpleName();

    private HashMap<String,String> stringValues = null;
    private HashMap<String,Boolean> booleanValues = null;

    public EnvironmentManager(Context context){
        stringValues = new HashMap<>();
        booleanValues = new HashMap<>();
        loadEnvironment(context);
    }


    @Override
    public String getStringValue(String key){
        if(stringValues != null && stringValues.containsKey(key))
            return stringValues.get(key);
        else
            return null;
    }

    private void putStringValue(String key, String value){
        if(stringValues != null)
            stringValues.put(key,value);
    }

    @Override
    public boolean getBooleanValue(String key){
        if(booleanValues != null && booleanValues.containsKey(key))
            return booleanValues.get(key);
        else
            return false;
    }

    private void putBooleanValue(String key, Boolean value){
        if(booleanValues != null)
            booleanValues.put(key,value);
    }

    private void loadEnvironment(Context context){
        try{
            InputStream input = context.getResources().openRawResource(R.raw.environment);
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
