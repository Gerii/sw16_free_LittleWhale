package com.example.andrea.utils;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class Weather {
    private static final String API_KEY = "d1db7d9ac0033228ce578944c83ac06a";
    private int id = -1;

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    private String shortDescription;
    private String detailedDescription;

    public Weather() {

    }

    public void updateWeather(double lat, double lon) throws WeatherParsingException {
        final String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) + "&appid=" + API_KEY;
        //String genreJson = IOUtils.toString(new URL(url));
        try {
            String response = IOUtils.toString(new URL(url));

            JSONObject responseJSON = new JSONObject(response);
            JSONArray weatherJSONArray = (JSONArray) responseJSON.get("weather");
            JSONObject firstWeatherJSON = (JSONObject) weatherJSONArray.get(0);

            this.id = Integer.parseInt(firstWeatherJSON.get("id").toString());
            this.shortDescription = firstWeatherJSON.get("main").toString();
            this.detailedDescription = firstWeatherJSON.get("description").toString();

            //TODO parse the rest

        } catch (Exception e) {
            Log.e("Weather Parsing", e.toString());
            throw new WeatherParsingException();
        }
    }
}
