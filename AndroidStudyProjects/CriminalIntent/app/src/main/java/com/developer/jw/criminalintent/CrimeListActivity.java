package com.developer.jw.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by JW on 2016-04-15.
 */
public class CrimeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
