package com.example.andrea.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gery on 06.06.16.
 */

class WeatherJSONs {
    private String currentWeatherJSON;
    private String fiveDayWeatherJSON;

    public WeatherJSONs(String currentWeatherJSON, String fiveDayWeatherJSON) {
        this.currentWeatherJSON = currentWeatherJSON;
        this.fiveDayWeatherJSON = fiveDayWeatherJSON;
    }

    public boolean isReady() {
        return currentWeatherJSON != null && fiveDayWeatherJSON != null;
    }

    public String getCurrentWeatherJSON() {
        return currentWeatherJSON;
    }

    public String getFiveDayWeatherJSON() {
        return fiveDayWeatherJSON;
    }
}

public class WeatherGetterWhatever extends AsyncTask<Double, Void, WeatherJSONs> {
    private static final String API_KEY = "d1db7d9ac0033228ce578944c83ac06a";
    private String dailyURL, fiveDayURL;
    private NavigationActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private Context context;


    public WeatherGetterWhatever(double lat, double lon, Context context, NavigationActivity.SectionsPagerAdapter mSectionsPagerAdapter) {
        this.context = context;
        this.mSectionsPagerAdapter = mSectionsPagerAdapter;
        dailyURL = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) + "&appid=" + API_KEY;
        fiveDayURL = "http://api.openweathermap.org/data/2.5/forecast?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) + "&appid=" + API_KEY;
    }

    @Override
    protected WeatherJSONs doInBackground(Double... doubles) {
        try {
            String currentWeatherJSON = IOUtils.toString(new URL(dailyURL));
            String fiveDayWeatherJSON = IOUtils.toString(new URL(fiveDayURL));
            return new WeatherJSONs(currentWeatherJSON, fiveDayWeatherJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(WeatherJSONs weatherJSONs) {
        WeatherStorage weatherStorage = null;
        try {
            if (weatherJSONs != null && weatherJSONs.isReady()) {
                weatherStorage = new WeatherStorage();
                weatherStorage.parseWeather(weatherJSONs.getCurrentWeatherJSON(), weatherJSONs.getFiveDayWeatherJSON(), context);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        ((NavigationActivity.WeatherFragment) mSectionsPagerAdapter.weatherFragment).updateWeather(weatherStorage);
    }
}
