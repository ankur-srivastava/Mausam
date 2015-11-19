package com.edocent.mausam;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ListView forecastListView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        forecastListView = (ListView) view.findViewById(R.id.listview_forecast);

        ArrayList<String> forecaseArray = new ArrayList<>();
        forecaseArray.add("Today - Sunny - 88/63");
        forecaseArray.add("Tomorrow - Foggy - 88/63");

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, forecaseArray);
        forecastListView.setAdapter(forecastAdapter);

        return view;
    }
}
