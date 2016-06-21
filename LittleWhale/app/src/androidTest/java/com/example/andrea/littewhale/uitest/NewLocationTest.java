package com.example.andrea.littewhale.uitest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.andrea.littewhale.AddNewLocation;
import com.example.andrea.littewhale.EnterCoordinates;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

/**
 * Created by andrea on 20.06.16.
 */
public class NewLocationTest extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public NewLocationTest() {
        super(AddNewLocation.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());
    }

    public void tearDown() throws Exception {
        mySolo.finishOpenedActivities();
        super.tearDown();

    }

    public void testRadioButton() {
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Markt Hartmannsdorf");
        mySolo.clickOnRadioButton(0);
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "0");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "0");

        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "13");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "0");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "0");

        RadioButton rbDecimal = (RadioButton) mySolo.getView(R.id.newLocation_rbtnDecimalNotation);
        mySolo.clickOnView(rbDecimal);

        Assert.assertTrue(mySolo.searchText("12.0"));
        Assert.assertTrue(mySolo.searchText("13.0"));

        RadioButton rbTime = (RadioButton) mySolo.getView(R.id.newLocation_rbtnTimeNotation);
        mySolo.clickOnView(rbTime);

        Assert.assertTrue(mySolo.searchText("12"));
        Assert.assertTrue(mySolo.searchText("0"));
        Assert.assertTrue(mySolo.searchText("0"));

        Assert.assertTrue(mySolo.searchText("13"));
        Assert.assertTrue(mySolo.searchText("0"));
        Assert.assertTrue(mySolo.searchText("0"));
    }

    public void testExitEnter() {
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Markt Hartmannsdorf");
        mySolo.clickOnRadioButton(0);
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "0");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "0");

        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "13");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "0");
        mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "0");


    }
}
