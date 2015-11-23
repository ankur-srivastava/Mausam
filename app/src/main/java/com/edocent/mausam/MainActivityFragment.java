package com.edocent.mausam;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edocent.mausam.utility.ServiceUtility;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    ListView forecastListView;
    String zipCode = "48390";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        forecastListView = (ListView) view.findViewById(R.id.listview_forecast);

        ArrayList<String> forecaseArray = new ArrayList<>();
        forecaseArray.add("Today - Sunny - 88/63");
        forecaseArray.add("Tomorrow - Foggy - 88/63");

        new FetchWeatherTask().execute(zipCode);

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                R.id.list_item_forecast_textview, forecaseArray);
        forecastListView.setAdapter(forecastAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.forecastmenu, menu);
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

        }
    }
}
