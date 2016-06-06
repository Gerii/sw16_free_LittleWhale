package com.example.andrea.utils;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.example.andrea.littewhale.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class Weather {
    private static final String API_KEY = "d1db7d9ac0033228ce578944c83ac06a";
    private static HashMap<Integer, String> WEATHER_CODES;


    private int id = -1;
    private String weatherIcon;

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

    private static void loadWeatherIds(Context context) {
        if (WEATHER_CODES == null) {
            String[] weatherIconCodes = context.getResources().getStringArray(R.array.weathericoncode);
            int[] weatherIds = context.getResources().getIntArray(R.array.weatherid);
            assert (weatherIconCodes.length == weatherIds.length);

            WEATHER_CODES = new HashMap<>();
            for (int i = 0; i < weatherIconCodes.length; ++i) {
                WEATHER_CODES.put(weatherIds[i], weatherIconCodes[i]);
            }
        }
    }

    public Weather(Context context) {
        loadWeatherIds(context);
    }

    public void updateWeather(double lat, double lon) throws WeatherParsingException {
        final String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lon) + "&appid=" + API_KEY;
        try {
            String response = IOUtils.toString(new URL(url));

            JSONObject responseJSON = new JSONObject(response);
            JSONArray weatherJSONArray = (JSONArray) responseJSON.get("weather");
            JSONObject firstWeatherJSON = (JSONObject) weatherJSONArray.get(0);

            this.id = Integer.parseInt(firstWeatherJSON.get("id").toString());
            this.shortDescription = firstWeatherJSON.get("main").toString();
            this.detailedDescription = firstWeatherJSON.get("description").toString();


            this.weatherIcon = WEATHER_CODES.get((id == 800) ? id : id / 100);

            //TODO parse the rest

        } catch (Exception e) {
            Log.e("Weather Parsing", e.toString());
            throw new WeatherParsingException();
        }
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }
}
