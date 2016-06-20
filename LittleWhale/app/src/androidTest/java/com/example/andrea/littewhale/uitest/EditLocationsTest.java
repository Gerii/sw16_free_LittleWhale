package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.andrea.littewhale.EditLocations;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by clemens on 05.04.16.
 */
public class EditLocationsTest extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public View getViewAtIndex(final ListView listElement, final int indexInList, Instrumentation instrumentation) {
        ListView parent = listElement;
        if (parent != null) {
            if (indexInList <= parent.getAdapter().getCount()) {
                scrollListTo(parent, indexInList, instrumentation);
                int indexToUse = indexInList - parent.getFirstVisiblePosition();
                return parent.getChildAt(indexToUse);
            }
        }
        return null;
    }

    public <T extends AbsListView> void scrollListTo(final T listView,
                                                     final int index, Instrumentation instrumentation) {
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(index);
            }
        });
        instrumentation.waitForIdleSync();
    }

    public EditLocationsTest(){
        super(EditLocations.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());
    }

    public void tearDown() throws Exception {
        mySolo.finishOpenedActivities();
        super.tearDown();

    }

    public void testAddNewElement()  {

        ListView lv = (ListView) mySolo.getView(R.id.listView);
        int oldCount = lv.getAdapter().getCount();

        View fab = getActivity().findViewById(R.id.fabAdd);
        mySolo.clickOnView(fab);
        mySolo.waitForActivity("AddNewLocation");

        if (mySolo.waitForActivity("AddNewLocation")) {
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Markt Hartmannsdorf");
            mySolo.clickOnRadioButton(0);
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "12");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "12");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "12");

            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "12");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "12");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "12");

            mySolo.clickOnButton("Save Location");
            if(mySolo.waitForActivity("EditLocations")) {
                lv = (ListView) mySolo.getView(R.id.listView);
                Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());


                boolean newElementFound = false;

                for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                    if(lv.getItemAtPosition(i).toString().equals("{line2=12.20 N   12.20 E, line1=Markt Hartmannsdorf}")) {
                        newElementFound = true;
                        break;
                    }
                }


                Assert.assertTrue(newElementFound);
            }


        }
    }

    public void testAddNewElementWithEnter()  {

        ListView lv = (ListView) mySolo.getView(R.id.listView);
        int oldCount = lv.getAdapter().getCount();

        View fab = getActivity().findViewById(R.id.fabAdd);
        mySolo.clickOnView(fab);
        mySolo.waitForActivity("AddNewLocation");

        if (mySolo.waitForActivity("AddNewLocation")) {
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Feldbach");
            mySolo.clickOnRadioButton(0);

            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "20");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "0");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "0");

            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "15");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "0");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "0");

            mySolo.clickOnView(mySolo.getView(R.id.newLocation_editTextSecondLongitude));

            mySolo.sendKey(KeyEvent.KEYCODE_ENTER);


            /*if(mySolo.waitForActivity("EditLocations")) {
                lv = (ListView) mySolo.getView(R.id.listView);
                Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());


                boolean newElementFound = false;

                for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                    if(lv.getItemAtPosition(i).toString().equals("{line2=15.0 N   20.0 E, line1=Feldbach}")) {
                        newElementFound = true;
                        break;
                    }
                }

                Assert.assertTrue(newElementFound);
            }*/


        }
    }

    public void testEditElement()  {

        ListView lv = (ListView) mySolo.getView(R.id.listView);
        int oldCount = lv.getAdapter().getCount();

        if (lv.getAdapter().getCount() == 0) {
            View fab = getActivity().findViewById(R.id.fabAdd);
            mySolo.clickOnView(fab);

            if (mySolo.waitForActivity("AddNewLocation")) {
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "Markt Hartmannsdorf");
                mySolo.clickOnRadioButton(0);
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "12");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "12");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "12");

                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "12");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "12");
                mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "12");

                mySolo.clickOnButton("Save Location");
                if (mySolo.waitForActivity("EditLocations")) {
                    lv = (ListView) mySolo.getView(R.id.listView);
                    Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());


                    boolean newElementFound = false;

                    for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                        System.out.println(lv.getItemAtPosition(i).toString());
                        if(lv.getItemAtPosition(i).toString().equals("{line2=12.20 N   12.20 E, line1=Markt Hartmannsdorf}")) {
                            newElementFound = true;
                            break;
                        }
                    }
                    Assert.assertTrue(newElementFound);
                }
            }
        }

        mySolo.clickLongOnView(getViewAtIndex(lv, 0, getInstrumentation()));
        if (mySolo.waitForActivity("AddNewLocation")) {
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_locationName));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude));
            mySolo.clearEditText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude));

            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_locationName), "TEST");
            mySolo.clickOnRadioButton(0);
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLongitude), "13");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLongitude), "13");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLongitude), "13");

            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextDegreeTimeLatitude), "13");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextMinuteLatitude), "13");
            mySolo.enterText((EditText) mySolo.getView(R.id.newLocation_editTextSecondLatitude), "13");

            mySolo.pressSpinnerItem(0, 0);
            mySolo.pressSpinnerItem(1, 0);

            mySolo.clickOnButton("Save Location");
            if (mySolo.waitForActivity("EditLocations")) {
                lv = (ListView) mySolo.getView(R.id.listView);
                Assert.assertEquals(oldCount, lv.getAdapter().getCount());


                boolean newElementFound = false;

                for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                    System.out.println(lv.getItemAtPosition(i).toString());
                    if(lv.getItemAtPosition(i).toString().equals("{line2=13.22 N   13.22 E, line1=TEST}")) {
                        newElementFound = true;
                        break;
                    }
                }
                Assert.assertTrue(newElementFound);
            }
        }

    }
}