package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.client.utils.URIUtils;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ArrayAdapter<String> weatherAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_refresh){
            //La Plata city Id is 3432043, days is 7
            FetchWeatherTask t = new FetchWeatherTask(3432043,7);
            t.execute();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FetchWeatherTask t = new FetchWeatherTask(3432043,7);
        t.execute();

        weatherAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview, new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview.setAdapter(weatherAdapter);

        return rootView;
    }


    private  class FetchWeatherTask extends AsyncTask<Void, Void, String[]> {
        int cityId;
        int days;
        public String result;

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            weatherAdapter.clear();
            for(String s :strings){
                weatherAdapter.add(s);
            }
        }

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        FetchWeatherTask(int cityId, int days){
            super();
            this.cityId = cityId;
            this.days = days;
        }

        private String forecastJsonString(int cityId, int days){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            final String WEATHER_API = "dc8b5e3bab874dc0c5e45687949095ef";
            try{
                Uri.Builder weatherUrl = new Uri.Builder();
                weatherUrl.scheme("http");
                weatherUrl.authority("api.openweathermap.org");
                weatherUrl.path("/data/2.5/forecast/daily");
                weatherUrl.appendQueryParameter("id", ((Integer) cityId).toString());
                weatherUrl.appendQueryParameter("appId","2de143494c0b295cca9337e1e96b00e0");
                weatherUrl.appendQueryParameter("cnt", ((Integer)days).toString());
                weatherUrl.appendQueryParameter("units", "metric");
                URL url = new URL(weatherUrl.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    Log.e(LOG_TAG,"Input Stream was null");
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0){
                    return null;
                }
                String result = buffer.toString();
                return result;

            } catch (java.net.ProtocolException e){
                Log.e(LOG_TAG, "Protocol Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (java.io.IOException e){
                Log.e(LOG_TAG, "IO Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(final IOException e){
                        Log.e("Placeholder Fragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            String weatherJson = forecastJsonString(this.cityId, this.days);
            WeatherDataParser parser = new WeatherDataParser();
            try {
                String[] forecasts = parser.getWeatherDataFromJson(weatherJson, days);
                return forecasts;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }


    }
}