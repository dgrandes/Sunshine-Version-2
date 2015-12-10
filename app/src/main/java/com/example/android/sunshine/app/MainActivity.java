package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_map){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String cityId = preferences.getString("city_name_list","");
            String cityName = getCityName(cityId);
            showMap(cityName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getCityName(String cityId){
        String[] cityIds = getResources().getStringArray(R.array.pref_city_name_list_values);
        String[] cityNames = getResources().getStringArray(R.array.pref_city_name_list_titles);
        try{
            Integer index = Arrays.asList(cityIds).indexOf(cityId);
            String response =  cityNames[index];
            return  response;
        }catch (Exception e){
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return "Buenos Aires, Argentina";
        }
    }

    public void showMap(String location){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Log.d("Sunshine", uri.toString());
        intent.setData(uri);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        }
    }
}
