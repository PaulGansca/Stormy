package com.ganscapaul.stormy.ui;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ganscapaul.stormy.R;
import com.ganscapaul.stormy.adapters.HourAdapter;
import com.ganscapaul.stormy.weather.Hour;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity  {

    private Hour[] mHours;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable [] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);

        //displaying hourly data as a List with the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //maximise efficiency
        mRecyclerView.setHasFixedSize(true);
    }
}
