package com.ganscapaul.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ganscapaul.stormy.ui.AlertDialogFragment;
import com.ganscapaul.stormy.weather.Current;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Current mCurrent;

/*
    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.degreeImageView) ImageView mDegreeImageView;
    @BindView(R.id.refreshImageView) ImageView mRefreshImageView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 51.556316;
        final double longitude = -0.085210;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });
        getForecast(latitude, longitude);


        //this is the main thread
        Log.d(TAG, "Main UI code running!");
    }

    private void getForecast(double latitude, double longitude) {
        //Darkski api
        String apiKey = "9cd787e66f31e55681fff3a67a873d47";
        String forecastUrl = "https://api.darksky.net/forecast/" + apiKey +
                "/" + latitude + "," + longitude;
        //check for availability
        if (isNetworkAvailable()) {

            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            //call and request enters the que but not on main thread
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                //bollocks
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                //get lucky and get weather data
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrent = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception Caught: ", e);
                    }
                }
            });
        } else {
            //display dialog with error
            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.show(getFragmentManager(), "error_dialog");
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrent.getmTemperature() + "");
        mTimeLabel.setText("At " + mCurrent.getFormattedTime() + " it will be");
        mHumidityValue.setText(mCurrent.getmHumidity() + "");
        mPrecipValue.setText(mCurrent.getmPrecipChance() + "%");
        mSummaryLabel.setText(mCurrent.getmSummary());

        Drawable drawable = getResources().getDrawable(mCurrent.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    //exception gets handled where the method is called
    private Current getCurrentDetails(String jsonData) throws JSONException {
        //JSONObject used to get data
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        Log.i(TAG, "FROM JSON: " +  timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setmHumidity(currently.getDouble("humidity"));
        current.setmTime(currently.getLong("time"));
        current.setmIcon(currently.getString("icon"));
        current.setmPrecipChance(currently.getDouble("precipProbability"));
        current.setmTemperature(currently.getDouble("temperature"));
        current.setmTimeZone(timezone);
        current.setSummary(currently.getString("summary"));

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    //re-usable code in checking for Network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }
    //created dialog to show error
    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
