package com.example.andrea.littewhale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andrea.utils.Weather;
import com.example.andrea.utils.WeatherParsingException;

import org.json.JSONException;

import java.net.MalformedURLException;

public class WeatherInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Weather weather = new Weather(getApplicationContext());
        try {
            weather.updateWeather(1.11, 2.22);
        } catch (WeatherParsingException e) {
            //TODO
            e.printStackTrace();
        }
        setContentView(R.layout.activity_weather_information);
    }
}
