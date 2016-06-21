package com.example.andrea.littewhale.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.example.andrea.utils.NavigationUtils;

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
        double angle = NavigationUtils.angleToTarget(0, 0, 90, 0);
        assertTrue(assertDelta(0.001, 0.0, angle));
    }


    public void testCourse2() {
        double angle = NavigationUtils.angleToTarget(50, 50, 0, 0);
        assertTrue(assertDelta(0.001, 237.26759, angle));
    }

    public void testCourse3() {
        double angle = NavigationUtils.angleToTarget(0, 0, 50, -50);
        assertTrue(assertDelta(0.001, 327.26759, angle));
    }

    public void testCourse4() {
        double angle = NavigationUtils.angleToTarget(46.95279, 15.88784, 47.070714, 15.439504);
        assertTrue(assertDelta(0.001, 291.25821, angle));
    }

    public void testCourse5() {
        double angle = NavigationUtils.angleToTarget(47.070714, 15.439504, 46.95279, 15.88784);
        assertTrue(assertDelta(0.001, 110.93025, angle));
    }

    public void testCourse6() {
        double angle = NavigationUtils.angleToTarget(-10, -10, 0, 0);
        assertTrue(assertDelta(0.001, 45.4385485867424, angle));
    }

    public void testCourse7() {
        double angle = NavigationUtils.angleToTarget(-10, 10, 5, -5);
        assertTrue(assertDelta(0.001, 314.4492782292224, angle));
    }

    public void testDistance1() {
        double dist = NavigationUtils.distanceInKm(47.070714, 15.439504, 46.95279, 15.88784);
        assertTrue(assertDelta(0.1, 36.43, dist));
    }

    public void testDistance2() {
        double dist = NavigationUtils.distanceInKm(0, 0, 0, 0);
        assertTrue(assertDelta(0.0001, 0.0, dist));
    }

    public void testDistance3() {
        double dist = NavigationUtils.distanceInKm(0, 0, 50, 50);
        assertTrue(assertDelta(0.01, 7302.05, dist));
    }

    public void testDistance4() {
        double dist = NavigationUtils.distanceInKm(0, 0, -50, -50);
        assertTrue(assertDelta(0.01, 7302.05, dist));
    }

    public void testDistance5() {
        double dist = NavigationUtils.distanceInKm(45.0, 17.0, 45.0, 17.1);
        assertTrue(assertDelta(0.01, 7.87, dist));
    }

    private boolean assertDelta(double delta, double angle1, double angle2){
        if(angle2 < (angle1 + delta) && angle2 > (angle1 - delta)){
            return true;
        }

        Log.e("Assert Failed", "Expected: " + angle1 + " but was: " + angle2);
        return false;
    }
}
