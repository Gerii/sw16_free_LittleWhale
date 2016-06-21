package com.example.andrea.littewhale.uitest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
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

        ((NavigationActivity) mySolo.getCurrentActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run()  {
                float values[] = {0.0f, 0.0f, 0.0f};
                ((NavigationActivity) mySolo.getCurrentActivity()).updateCourse(Sensor.TYPE_ACCELEROMETER, values);
            }
        });
    }

    public void tearDown() throws Exception {
        mySolo.finishOpenedActivities();
        super.tearDown();

    }

    public void testWeatherTab() throws Exception{
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 41.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);
        ViewGroup tabs = (ViewGroup) mySolo.getView(R.id.tabs);
        View weather = tabs.getChildAt(0);
        mySolo.clickOnView(weather);
    }

    public void testPermissionHandling() throws Exception {

        ((NavigationActivity) mySolo.getCurrentActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run()  {
                int request_code = 1;
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                int[] grantResults = {PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED};
                ((NavigationActivity) mySolo.getCurrentActivity()).onRequestPermissionsResult(request_code, permissions, grantResults);
                int[] grantResults2 = {PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED};
                ((NavigationActivity) mySolo.getCurrentActivity()).onRequestPermissionsResult(request_code, permissions, grantResults2);
                int[] grantResults3 = {PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_DENIED};
                ((NavigationActivity) mySolo.getCurrentActivity()).onRequestPermissionsResult(request_code, permissions, grantResults3);
                int[] grantResults4 = {PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_DENIED};
                ((NavigationActivity) mySolo.getCurrentActivity()).onRequestPermissionsResult(request_code, permissions, grantResults4);

            }
        });
    }

    public void testCalculatedCourse() throws Exception{
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 41.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);

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
    }

    public void testArrow1() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 69.0;
        double longitude = 10.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow2() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 69.0;
        double longitude = 5.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow3() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 69.0;
        double longitude = 15.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow4() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 70.0;
        double longitude = 5.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow5() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 70.0;
        double longitude = 15.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow6() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 71.0;
        double longitude = 10.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow7() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 71.0;
        double longitude = 5.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testArrow8() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 71.0;
        double longitude = 15.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(500);
    }

    public void testSpeed() throws Exception {
        int updateIntervall = 2000;

        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 71.0;
        double longitude = 15.0;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.000;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.001;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.002;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.003;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.004;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        mySolo.sleep(updateIntervall);

        latitude = 71.0;
        longitude = 15.005;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        Assert.assertTrue(mySolo.searchText("kts"));
    }

    public void testLocationReached() throws Exception {
        mySolo.sleep(2000);
        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 70.0;
        double longitude = 10.0001;
        String str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        latitude = 70.0;
        longitude = 10.000;
        str = "geo fix " + Double.toString(longitude) + " " + Double.toString(latitude) + "\n";
        out.print(str);

        Assert.assertTrue(mySolo.searchText("kts"));
    }
}