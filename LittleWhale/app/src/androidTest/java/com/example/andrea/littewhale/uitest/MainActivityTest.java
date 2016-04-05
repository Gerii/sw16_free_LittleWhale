package com.example.andrea.littewhale.uitest;

import android.test.ActivityInstrumentationTestCase2;
import com.example.andrea.littewhale.MainActivity;
import com.robotium.solo.Solo;


/**
 * Created by andrea on 05.04.16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo mySolo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testButtons() {
        mySolo.clickOnButton("Start Navigating");
        mySolo.goBack();
        mySolo.clickOnButton("Edit Locations");

    }
}