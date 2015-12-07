package com.edocent.mausam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.DetailActivityInterface{

    public static final String INTENT_EXTRA = "forecast";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.locationId) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            if(sharedPref != null) {
                MainActivityFragment.LOCATION_PREF = sharedPref.getString(getString(R.string.pref_location_key), MainActivityFragment.DEFAULT_LOCATION);
                MainActivityFragment.TEMP_PREF = sharedPref.getString(getString(R.string.pref_temp_key), MainActivityFragment.DEFAULT_TEMP);
            }

            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", MainActivityFragment.LOCATION_PREF).build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayDetails(String forecast) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(INTENT_EXTRA, forecast);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}