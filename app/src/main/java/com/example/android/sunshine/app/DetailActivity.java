package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class DetailActivity extends ActionBarActivity {

    public String LOG_TAG = this.getClass().getSimpleName();

    public String weatherData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherData = getIntent().getExtras().getString(Intent.EXTRA_TEXT);


        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
