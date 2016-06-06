package com.example.andrea.littewhale;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.andrea.utils.Weather;
import com.example.andrea.utils.WeatherParsingException;

import org.json.JSONException;

import java.net.MalformedURLException;

public class WeatherInformation extends AppCompatActivity {
    static Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Typeface.createFromAsset(this.getAssets(), "fonts/weathericons.ttf");


        TextView editTextTemp = (TextView) findViewById(R.id.editTextTemp);
        editTextTemp.setTypeface(weatherFont);


        super.onCreate(savedInstanceState);
        Weather weather = new Weather(getApplicationContext());
        try {
            double lat = NavigationActivity.getOldLat();
            double lon = NavigationActivity.getOldLon();

            if(lat != NavigationActivity.COORD_DEFAULT_VALUE && lon != NavigationActivity.COORD_DEFAULT_VALUE) {
                weather.updateWeather(lat, lon);
            }
            else {
                //TODO show error
            }
        } catch (WeatherParsingException e) {
            //TODO show error
            e.printStackTrace();
        }
        setContentView(R.layout.activity_weather_information);
    }
}
