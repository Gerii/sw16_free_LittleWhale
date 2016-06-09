package com.example.andrea.littewhale.uitest;

import android.content.Intent;
import android.hardware.Sensor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;

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
        double latitude = 41.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);


        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        super.tearDown();

    }

    public void testCalculatedCourse() {
        Assert.assertTrue(mySolo.searchText("0.000 kts"));
        Assert.assertTrue(mySolo.searchText("347.773 °"));
        Assert.assertTrue(mySolo.searchText("3383.015 NM"));
        Assert.assertTrue(mySolo.searchText("3 °"));
        Assert.assertTrue(mySolo.searchText("17.000 °"));
        Assert.assertTrue(mySolo.searchText("41.000 °"));
        Assert.assertTrue(mySolo.searchText("15 °"));
        Assert.assertTrue(mySolo.searchText("left"));

        ImageView upArrow = ((ImageView) mySolo.getView(R.id.upArrow));
        ImageView downArrow = ((ImageView) mySolo.getView(R.id.downArrow));
        ImageView rightArrow = ((ImageView) mySolo.getView(R.id.rightArrow));
        ImageView leftArrow = ((ImageView) mySolo.getView(R.id.leftArrow));
        ImageView upLeftArrow = ((ImageView) mySolo.getView(R.id.upLeftArrow));
        ImageView upRightArrow = ((ImageView) mySolo.getView(R.id.upRightArrow));
        ImageView downLeftArrow = ((ImageView) mySolo.getView(R.id.downLeftArrow));
        ImageView downRightArrow = ((ImageView) mySolo.getView(R.id.downRightArrow));

        Assert.assertEquals(upArrow.getAlpha(), 1f);
        Assert.assertEquals(downArrow.getAlpha(), 0.3f);
        Assert.assertEquals(rightArrow.getAlpha(), 0.3f);
        Assert.assertEquals(leftArrow.getAlpha(), 0.3f);
        Assert.assertEquals(upLeftArrow.getAlpha(), 0.3f);
        Assert.assertEquals(upRightArrow.getAlpha(), 0.3f);
        Assert.assertEquals(downLeftArrow.getAlpha(), 0.3f);
        Assert.assertEquals(downRightArrow.getAlpha(), 0.3f);

        //float values[] = {0.064f, 0.1155f, 9.6576f};
        //((NavigationActivity) getActivity()).updateCourse(Sensor.TYPE_ACCELEROMETER, values);


        ((NavigationActivity) mySolo.getCurrentActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run()  {
                float values[] = {0.064f, 0.1155f, 9.6576f};
                ((NavigationActivity) mySolo.getCurrentActivity()).updateCourse(Sensor.TYPE_ACCELEROMETER, values);
            }
        });


    }
}