package com.example.andrea.littewhale.test;
import android.test.AndroidTestCase;

import com.example.andrea.utils.Weather;
import com.example.andrea.utils.WeatherParsingException;


/**
 * Created by gery on 30.05.16.
 */
public class WeatherTest extends AndroidTestCase{

    public void testUpdateWeather() {
        Weather testWeather = new Weather();
        try {
            testWeather.updateWeather(45.12, 44.13);
        } catch (WeatherParsingException e) {
            assertTrue(false);
        }
        assertNotNull(testWeather.getDetailedDescription());
        assertNotNull(testWeather.getId());
        assertNotNull(testWeather.getShortDescription());
    }
}