package com.example.andrea.utils;

import android.os.AsyncTask;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by gery on 06.06.16.
 */
public class WeatherGetterWhatever extends AsyncTask<Double, Void, String> {
    private Weather weather;
    private String url;
    private NavigationActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    public WeatherGetterWhatever(Weather weather, String url, NavigationActivity.SectionsPagerAdapter mSectionsPagerAdapter) {
        this.weather = weather;
        this.url = url;
        this.mSectionsPagerAdapter = mSectionsPagerAdapter;
    }

    @Override
    protected String doInBackground(Double... doubles) {
        try {
            return IOUtils.toString(new URL(url));
        } catch (IOException e) {
            //TODO handle
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String jsonString) {
        try {
            if(jsonString != null) {
                weather.parseWeather(jsonString);
                ((NavigationActivity.WeatherFragment) mSectionsPagerAdapter.weatherFragment).updateWeather(weather);
            }
        } catch (WeatherParsingException e) {
            //TODO show error
            e.printStackTrace();
        }
    }

}
