package com.example.andrea.utils;

import android.content.Context;
import android.util.Log;

import com.example.andrea.littewhale.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.*;

public class Weather {
    private static HashMap<Integer, String> WEATHER_CODES;


    private int id = -1;
    private String weatherIcon;
    private Date time;

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
            if (checkHTTPCode && weatherAPIFailed(responseJSON)) {
                success = false;
                try {
                    errorMessage = (String) responseJSON.get("message");
                } catch (JSONException e) {
                    errorMessage = "Weather fetching failed.";
                }
                return;
            }

            /*if (responseJSON.has("dt_txt")) {
                try {
                    timeText = (String) responseJSON.get("dt_txt");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = formatter.parse(timeText);
                    timeText = formatter.format(date);
                }
                catch (ParseException pE) {
                    Log.e("Weather Parsing", pE.toString());
                    throw new WeatherParsingException();
                }
            }*/

            JSONArray weatherJSONArray = (JSONArray) responseJSON.get("weather");
            JSONObject firstWeatherJSON = (JSONObject) weatherJSONArray.get(0);

            this.id = Integer.parseInt(firstWeatherJSON.get("id").toString());
            this.shortDescription = firstWeatherJSON.get("main").toString();
            this.detailedDescription = firstWeatherJSON.get("description").toString();

            Calendar cal = Calendar.getInstance();
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            //TODO we could use sunrise and sunset data of the weather api
            this.weatherIcon = WEATHER_CODES.get((id == 800) ? ((hourOfDay >= 7 && hourOfDay < 20) ? id : 0) : id / 100);

            long time = Long.parseLong(responseJSON.get("dt").toString() + "000");
            cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            this.time = new Date(time);
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
            Log.e("PARSING", Double.toString(this.temperature));
        } catch (JSONException e) {
            Log.e("Weather Parsing", e.toString());
            throw new WeatherParsingException();
        }
    }

    public static boolean weatherAPIFailed(JSONObject responseJSON) throws JSONException {
        int httpcode = Integer.parseInt(responseJSON.get("cod").toString());
        return httpcode != 200;
    }

    public String getFormattedDate() {
        Calendar cal = getDate();
        return cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR);
    }

    public String getFormattedTime() {
        return new SimpleDateFormat("HH:mm").format(time);
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

}
