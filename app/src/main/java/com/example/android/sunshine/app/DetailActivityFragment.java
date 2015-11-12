package com.example.android.sunshine.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public String LOG_TAG = this.getClass().getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DetailActivity activity = (DetailActivity)getActivity();
        String weatherData = activity.weatherData;
        Log.d(LOG_TAG, weatherData);
        View fragmentView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView t = (TextView) fragmentView.findViewById(R.id.weather_data_textview);
        t.setText(weatherData);
        return fragmentView;
    }
}
