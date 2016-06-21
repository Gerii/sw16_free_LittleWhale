package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andrea.littewhale.EditLocations;
import com.example.andrea.littewhale.EnterCoordinates;
import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import org.w3c.dom.Text;

import java.io.PrintStream;
import java.net.Socket;

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
        mySolo.finishOpenedActivities();
        super.tearDown();
    }


    public void testStartNavigation() throws Exception{
        mySolo.clickOnRadioButton(0);

        mySolo.clickOnView(mySolo.getView(R.id.buttonStartNavigating));

        mySolo.waitForActivity("NavigationActivity");
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

        mySolo.pressSpinnerItem(1, 1);

        mySolo.clickOnRadioButton(1);

     //   assertEquals(1, ((Spinner) mySolo.getView(R.id.spinnerCardinalDirectionDecimalLatitude)).getSelectedItemPosition());
       // assertEquals(1, ((Spinner) mySolo.getView(R.id.spinnerCardinalDirectionDecimalLongitude)).getSelectedItemPosition());

    }

    public void testUseNewActivity() {

        mySolo.clickOnButton("Use existing location");

        if (mySolo.waitForActivity("EditLocations")) {

            ListView lv = (ListView) mySolo.getCurrentActivity().findViewById(R.id.listView);
            int oldCount = lv.getAdapter().getCount();

            View fab = mySolo.getCurrentActivity().findViewById(R.id.fabAdd);
            mySolo.clickOnView(fab);

            if (mySolo.waitForActivity("AddNewLocation")) {
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Graz");
                mySolo.clickOnRadioButton(0);

                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "50");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "0");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "0");

                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "30");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "0");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "0");

                mySolo.clickOnButton("Save Location");
                if (mySolo.waitForActivity("EditLocations")) {
                    lv = (ListView) mySolo.getCurrentActivity().findViewById(R.id.listView);
                    Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());
                    boolean newElementFound = false;
                    int position_of_new_element = 0;
                    for (int i = 0; i < lv.getAdapter().getCount(); i++) {
                        System.out.println(lv.getItemAtPosition(i).toString());
                        if (lv.getItemAtPosition(i).toString().equals("{line2=50.00 N   30.00 E, line1=Graz}")) {
                            newElementFound = true;
                            position_of_new_element = i;
                            break;
                        }
                    }
                    Assert.assertTrue(newElementFound);


                }
            }
        }
    }

}