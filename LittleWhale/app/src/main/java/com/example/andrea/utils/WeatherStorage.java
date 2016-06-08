package com.example.andrea.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gery on 08.06.16.
 */
public class WeatherStorage implements Iterable<Weather> {
    private ArrayList<Weather> weatherList = new ArrayList<>();
    boolean isLoaded = false;

    public boolean isLoaded() {
        return isLoaded;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private String errorMessage;


    public WeatherStorage() {
    }

    public void parseWeather(String currentWeather, String fiveDayWeather, Context context) throws Exception {
        try {
            Weather curWeather = new Weather(context);
            curWeather.parseWeather(new JSONObject(currentWeather), true);
            if(!curWeather.isSuccess()) {
                errorMessage = curWeather.getErrorMessage();
                return;
            }
            weatherList.add(curWeather);


            JSONObject fiveDayWeatherJSON = new JSONObject(fiveDayWeather);

            JSONArray weatherJSONArray = (JSONArray) fiveDayWeatherJSON.get("list");
            for(int i = 0; i < weatherJSONArray.length(); ++i) {
                JSONObject weatherJSON = (JSONObject) weatherJSONArray.get(0);
                Weather weather = new Weather(context);
                curWeather.parseWeather(weatherJSON, false);
                weatherList.add(weather);
            }
        } catch (Exception e) {
            weatherList.clear();
            throw e;
        }
        isLoaded = true;
    }

    @Override
    public Iterator<Weather> iterator() {
        return weatherList.iterator();
    }
}
