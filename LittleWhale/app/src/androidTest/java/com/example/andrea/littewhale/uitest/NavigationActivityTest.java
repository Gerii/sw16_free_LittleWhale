package com.example.andrea.littewhale.uitest;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by andrea on 08.06.16.
 */
public class NavigationActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo mySolo;

    public NavigationActivityTest() {
        super(NavigationActivity.class);
    }

    public void setUp() throws Exception {

        super.setUp();
        Intent i = new Intent();
        double[] target = new double[2];
        target[0] = 88.0;
        target[1] = 10.0;
        String[] cardinalDirection = new String[2];
        cardinalDirection[0] = "1";
        cardinalDirection[1] = "2";
        i.putExtra("TargetCoords", target);
        i.putExtra("CardinalDirection", cardinalDirection);
        setActivityIntent(i);

        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        System.out.println(socket);

        System.out.println(socket.getOutputStream());
        PrintStream out = new PrintStream(socket.getOutputStream());

        System.out.println(out);

        double longitude = 42.0, latitude = 17.0;
        String str;

        str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        System.out.println(str);

        out.print(str);
        System.out.println(str);


        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        super.tearDown();

    }

    public void testTabs() throws Exception {
        Assert.assertTrue(mySolo.searchText("0.000 kts"));


    }
}