package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.andrea.littewhale.EditLocations;
import com.example.andrea.littewhale.EnterCoordinates;
import com.example.andrea.littewhale.MainActivity;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

/**
 * Created by clemens on 05.04.16.
 */
public class EnterCoordinatesTest extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public EnterCoordinatesTest() {
        super(EnterCoordinates.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDecimalToTime() {
        mySolo.clickOnRadioButton(1);
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLongitude), "12.234");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLatitude), "15.678");
        mySolo.clickOnRadioButton(0);

    }
    public void testTimeToDecimal() {
        mySolo.clickOnRadioButton(0);
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeTimeLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextMinuteLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextSecondLongitude), "12");

        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeTimeLatitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextMinuteLatitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextSecondLatitude), "12");
        mySolo.clickOnRadioButton(1);

    }
}