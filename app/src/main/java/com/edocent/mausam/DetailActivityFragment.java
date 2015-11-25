package com.edocent.mausam;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    TextView forecastTextView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        forecastTextView = (TextView) view.findViewById(R.id.forecastTextId);

        if(getActivity().getIntent() != null){
            Intent intent = getActivity().getIntent();
            if(intent.getExtras() != null){
                String forecastText = intent.getExtras().getString(MainActivity.INTENT_EXTRA);
                forecastTextView.setText(forecastText);
            }
        }

        return view;
    }
}
