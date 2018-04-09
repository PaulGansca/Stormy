package com.ganscapaul.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//Model class, will display current weather
public class Current {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getmSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public String getmTimeZone() {
        return mTimeZone;
    }

    public void setmTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

    //fields for the data from JSON request
    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public int getIconId(){

        int iconId = R.drawable.clear_day;

        if (mIcon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (mIcon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (mIcon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (mIcon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (mIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (mIcon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (mIcon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (mIcon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (mIcon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (mIcon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public long getmTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getmTimeZone()));
        Date dateTime = new Date(getmTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmTemperature() {

        return (int)(Math.round(mTemperature)-32)*5/9;
    }

    public void setmTemperature(double mTemperature) {
        this.mTemperature = mTemperature;
    }

    public double getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(double mHumidity) {
        this.mHumidity = mHumidity;
    }

    public int getmPrecipChance() {
        double precipPercentage = mPrecipChance*100;
        return (int) Math.round(precipPercentage);
    }

    public void setmPrecipChance(double mPrecipChance) {
        this.mPrecipChance = mPrecipChance;
    }
}
