package com.example.andrea.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class Weather {
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

    public Calendar getDate() {
        return date;
    }

    public int getClouds() {
        return clouds;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    private String shortDescription;
    private String detailedDescription;
    private Calendar date;
    private int clouds;
    private int humidity;
    private double pressure;
    private double temperature;
    private double windSpeed;
    private double windDirection;

    private boolean success = false;

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    private String errorMessage;

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

    public void parseWeather(JSONObject responseJSON, boolean checkHTTPCode) throws WeatherParsingException {
        try {

            if (checkHTTPCode) {
                int httpcode = (int) Integer.parseInt(responseJSON.get("cod").toString());
                if (httpcode != 200) {
                    success = false;
                    try {
                        errorMessage = (String) responseJSON.get("message");
                    } catch (JSONException e) {

                    }
                    return;
                }
            }

            JSONArray weatherJSONArray = (JSONArray) responseJSON.get("weather");
            JSONObject firstWeatherJSON = (JSONObject) weatherJSONArray.get(0);

            this.id = Integer.parseInt(firstWeatherJSON.get("id").toString());
            this.shortDescription = firstWeatherJSON.get("main").toString();
            this.detailedDescription = firstWeatherJSON.get("description").toString();

            Calendar cal = Calendar.getInstance();
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            //TODO we could use sunrise and sunset data of the weather api
            this.weatherIcon = WEATHER_CODES.get((id == 800) ? ((hourOfDay >= 7 && hourOfDay < 20) ? id : 0) : id / 100);

            cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(responseJSON.get("dt").toString() + "000"));
            this.date = cal;

            JSONObject clouds = (JSONObject) responseJSON.get("clouds");
            this.clouds = Integer.parseInt(clouds.get("all").toString());

            JSONObject main = (JSONObject) responseJSON.get("main");
            this.humidity = Integer.parseInt(main.get("humidity").toString());
            this.pressure = Double.parseDouble(main.get("pressure").toString());
            this.temperature = Double.parseDouble(main.get("temp").toString()) - 273.15;

            JSONObject wind = (JSONObject) responseJSON.get("wind");
            this.windSpeed = Double.parseDouble(wind.get("speed").toString());
            this.windDirection = Double.parseDouble(wind.get("deg").toString());
            success = true;
        } catch (JSONException e) {
            Log.e("Weather Parsing", e.toString());
            throw new WeatherParsingException();
        }
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

}
