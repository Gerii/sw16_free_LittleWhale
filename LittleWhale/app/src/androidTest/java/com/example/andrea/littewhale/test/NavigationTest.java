package com.example.andrea.littewhale.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.model.LocationContract;
import com.example.andrea.model.LocationDbHelper;

/**
 * Created by clemens on 13.05.16.
 * http://planetcalc.com/722/
 *
 * N +
 * S -
 * O +
 * W -
 */
public class NavigationTest extends AndroidTestCase{

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void testCourse1() {
        double angle = NavigationActivity.angleToTarget(0, 0, 90, 0);
        assertTrue(assertDelta(0.001, 0.0, angle));
    }


    public void testCourse2() {
        double angle = NavigationActivity.angleToTarget(50, 50, 0, 0);
        assertTrue(assertDelta(0.001, 237.26759, angle));
    }

    public void testCourse3() {
        double angle = NavigationActivity.angleToTarget(0, 0, 50, -50);
        assertTrue(assertDelta(0.001, 327.26759, angle));
    }

    public void testCourse4() {
        double angle = NavigationActivity.angleToTarget(46.95279, 15.88784, 47.070714, 15.439504);
        assertTrue(assertDelta(0.001, 291.25821, angle));
    }

    public void testCourse5() {
        double angle = NavigationActivity.angleToTarget(47.070714, 15.439504, 46.95279, 15.88784);
        assertTrue(assertDelta(0.001, 110.93025, angle));
    }

    public void testDistance1() {
        double dist = NavigationActivity.distanceInKm(47.070714, 15.439504, 46.95279, 15.88784);
        assertTrue(assertDelta(0.1, 36.43, dist));
    }

    public void testDistance2() {
        double dist = NavigationActivity.distanceInKm(0, 0, 0, 0);
        assertTrue(assertDelta(0.1, 0.0, dist));
    }

    public void testDistance3() {
        double dist = NavigationActivity.distanceInKm(0, 0, 50, 50);
        assertTrue(assertDelta(0.1, 7302.05, dist));
    }

    public void testDistance4() {
        double dist = NavigationActivity.distanceInKm(0, 0, -50, -50);
        assertTrue(assertDelta(0.1, 7302.05, dist));
    }

    private boolean assertDelta(double delta, double angle1, double angle2){
        if(angle2 < (angle1 + delta) && angle2 > (angle1 - delta)){
            return true;
        }

        Log.e("Assert Failed", "Expected: " + angle1 + " but was: " + angle2);
        return false;
    }
}
