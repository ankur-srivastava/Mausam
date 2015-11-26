package com.edocent.mausam;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.edocent.mausam.utility.ServiceUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    ListView forecastListView;
    String zipCode = "48390";
    DetailActivityInterface mDetailActivityInterface;
    List<String> weekForecast;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        forecastListView = (ListView) view.findViewById(R.id.listview_forecast);

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mDetailActivityInterface != null){
                    String forecast = "";
                    if(weekForecast != null){
                        forecast = weekForecast.get(position);
                    }
                    mDetailActivityInterface.displayDetails(forecast);
                }

            }
        });

        new FetchWeatherTask().execute(zipCode);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refreshId) {
            Log.v(TAG, "Going to start Async Task ");
            new FetchWeatherTask().execute(zipCode);
            Log.v(TAG, "Done ");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String[] params) {
            String zipCode = params[0];
            String jsonResponse = "";
            String[] weatherData = new String[]{""};

            jsonResponse = ServiceUtility.connect(zipCode);

            try {
                weatherData = ServiceUtility.getWeatherDataFromJson(jsonResponse);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return weatherData;
        }

        @Override
        public void onPostExecute(String[] jsonData){
            Log.v(TAG, "Set the adapter with jsonData");
            if(jsonData != null){
                Log.v(TAG, "jsonData size "+jsonData.length);
                if(weekForecast == null) {
                    weekForecast = new ArrayList<String>(Arrays.asList(jsonData));
                }
                ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                        R.id.list_item_forecast_textview, weekForecast);
                forecastListView.setAdapter(forecastAdapter);
            }
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mDetailActivityInterface = (DetailActivityInterface) activity;
    }

    public interface DetailActivityInterface{
        void displayDetails(String forecast);
    }
}
