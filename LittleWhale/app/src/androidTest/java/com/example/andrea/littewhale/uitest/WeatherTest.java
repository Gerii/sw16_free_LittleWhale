package com.example.andrea.littewhale.uitest;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.example.andrea.littewhale.NavigationActivity;
import com.robotium.solo.Solo;

import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by andrea on 16.06.16.
 */
public class WeatherTest extends ActivityInstrumentationTestCase2 {
    private Solo mySolo;

    public WeatherTest() {
        super(NavigationActivity.class);
    }

    public void setUp() throws Exception {

        super.setUp();
        Intent i = new Intent();
        double[] target = new double[2];
        // target lat
        target[0] = 70.0;
        // target lon
        target[1] = 10.0;
        String[] cardinalDirection = new String[2];
        cardinalDirection[0] = "1";
        cardinalDirection[1] = "2";
        i.putExtra("TargetCoords", target);
        i.putExtra("CardinalDirection", cardinalDirection);
        setActivityIntent(i);

        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 190.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);

        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        mySolo.finishOpenedActivities();
        super.tearDown();
    }



    public void testInvalidWeather() throws Exception{

        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 190.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);
        socket.close();

    }
}
