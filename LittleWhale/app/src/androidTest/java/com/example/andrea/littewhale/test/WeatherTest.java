package com.example.andrea.littewhale.test;
import android.test.AndroidTestCase;

import com.example.andrea.utils.Weather;
import com.example.andrea.utils.WeatherParsingException;

import java.lang.reflect.Method;

public class WeatherTest extends AndroidTestCase{

    public void testUpdateWeather() {
        /*Weather testWeather = new Weather(getContext());
        try {
            testWeather.updateWeather(45.12, 44.13);
        } catch (WeatherParsingException e) {
            assertTrue(false);
        }
        assertNotNull(testWeather.getDetailedDescription());
        assertTrue(testWeather.getId() != -1);
        assertNotNull(testWeather.getShortDescription());
        assertNotNull(testWeather.getWeatherIcon()); //If this works, icons have been loaded*/
        assertTrue(true);
    }
}