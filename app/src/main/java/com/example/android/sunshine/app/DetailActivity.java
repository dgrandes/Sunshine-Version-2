package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class DetailActivity extends ActionBarActivity {

    public String LOG_TAG = this.getClass().getSimpleName();

    public String weatherData;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherData = getIntent().getExtras().getString(Intent.EXTRA_TEXT);

        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        return true;
    }

    private Intent createSharingIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setType("text/plan");
        intent.putExtra(Intent.EXTRA_TEXT, weatherData + " #SunshineApp");
        return intent;
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
        if (id == R.id.action_share) {
            Intent intent = createSharingIntent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(intent);
                }
                startActivity(Intent.createChooser(intent, getResources().getText(R.string.action_share)));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
