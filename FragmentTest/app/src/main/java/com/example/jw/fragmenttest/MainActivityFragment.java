package com.example.jw.fragmenttest;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button btn = null;
    private ViewGroup mContainer;
    private RelativeLayout screen = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.next_fragment, container, false);
    }


//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Button btn = (Button) view.findViewById(R.id.frag_button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                NextFragment fragment = new NextFragment();
//
//                transaction.replace(maContainer, fragment);
//                transaction.addToBackStack(null);
//
//                transaction.commit();
//            }
//        });
//    }

}
