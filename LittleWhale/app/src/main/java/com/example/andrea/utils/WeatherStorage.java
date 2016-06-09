package com.example.andrea.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gery on 08.06.16.
 */
public class WeatherStorage extends ArrayList<Weather> {
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
            this.add(curWeather);

            JSONObject fiveDayWeatherJSON = new JSONObject(fiveDayWeather);

            //TODO this can also be {"cod":"404","message":"Error: Not found city"}
            JSONArray weatherJSONArray = (JSONArray) fiveDayWeatherJSON.get("list");
            for(int i = 0; i < weatherJSONArray.length(); ++i) {
                JSONObject weatherJSON = (JSONObject) weatherJSONArray.get(i);
                Weather weather = new Weather(context);
                Log.e("parsn", weatherJSON.toString());

                weather.parseWeather(weatherJSON, false);

                this.add(weather);
            }
        } catch (Exception e) {
            this.clear();
            throw e;
        }
        isLoaded = true;
    }
}
