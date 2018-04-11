package com.ganscapaul.stormy.ui;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.ganscapaul.stormy.R;
import com.ganscapaul.stormy.adapters.DayAdapter;
import com.ganscapaul.stormy.weather.Day;
//Display Daily data in list view

public abstract class DailyForecastActivity extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        //creating the adapter that will display the content of Daily model
        DayAdapter adapter = new DayAdapter(this, mDays);
    }
}
