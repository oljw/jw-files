package com.samsung.retailexperience.camerahero.fragment;

import android.app.Fragment;
import android.content.Context;

import com.samsung.retailexperience.camerahero.activity.MainActivity;
import com.samsung.retailexperience.camerahero.util.AppConsts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by icanmobile on 1/14/16.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    abstract public void onBackPressed();

    public void changeFragment(AppConsts.UIState newState, AppConsts.TransactionDir dir) {
        ((MainActivity)getActivity()).changeFragment(newState, dir);
    }

    public String GetTextFromAsset(Context context, String filename) {
        StringBuilder ret = new StringBuilder();

        try {
            InputStream is = context.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader f = new BufferedReader(inputStreamReader);
            String line = f.readLine();
            while (line != null) {
                ret.append(line);
                line = f.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret.toString();
    }
}
