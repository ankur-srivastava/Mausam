package com.edocent.mausam;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    public static String LOCATION_PREF;
    public static String TEMP_PREF;
    public static final String DEFAULT_LOCATION = "48390";
    public static final String DEFAULT_TEMP = "metric";
    ListView forecastListView;
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
                if (mDetailActivityInterface != null) {
                    String forecast = "";
                    if (weekForecast != null) {
                        forecast = weekForecast.get(position);
                    }
                    mDetailActivityInterface.displayDetails(forecast);
                }

            }
        });


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
            //Log.v(TAG, "Going to start Async Task ");
            new FetchWeatherTask().execute(LOCATION_PREF);
            //Log.v(TAG, "Done ");
            return true;
        } else if (id == R.id.locationId) {
            //Log.v(TAG, "Going to start Google Map Intent ");

            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", LOCATION_PREF).build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }

            //Log.v(TAG, "Done ");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String[] params) {
            String zipCode = params[0];
            String tempPref = params[1];
            String jsonResponse = "";
            String[] weatherData = new String[]{""};

            jsonResponse = ServiceUtility.connect(zipCode, tempPref);

            try {
                weatherData = ServiceUtility.getWeatherDataFromJson(jsonResponse);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return weatherData;
        }

        @Override
        public void onPostExecute(String[] jsonData){
            //Log.v(TAG, "Set the adapter with jsonData");
            if(jsonData != null){
                //Log.v(TAG, "jsonData size "+jsonData.length);
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

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(sharedPref != null) {
            LOCATION_PREF = sharedPref.getString(getString(R.string.pref_location_key), DEFAULT_LOCATION);
            TEMP_PREF = sharedPref.getString(getString(R.string.pref_temp_key), DEFAULT_TEMP);
        }
        updateWeather();
    }

    private void updateWeather() {
        //Log.v(TAG, "Service call using "+LOCATION_PREF+" and "+TEMP_PREF);
        new FetchWeatherTask().execute(LOCATION_PREF, TEMP_PREF);
    }
}
