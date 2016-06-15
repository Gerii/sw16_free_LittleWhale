package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
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

    }

    /*
    public void testPlus(){
     //   View fab = getActivity().findViewById(R.id.fabAdd);
      //  mySolo.clickOnView(fab);
    }*/


    /*public void testClickListElement() {
        ListView lv = (ListView) getActivity().findViewById(R.id.listView);

        if(lv.getAdapter().getCount() > 0) {
            mySolo.clickOnView(getViewAtIndex(lv, 0, getInstrumentation()));
            mySolo.waitForActivity("EnterCoordinates");
        }
    }*/

    public  void testList(){
       /* ListView lv = (ListView) getActivity().findViewById(R.id.listView);


        for(int i = 0; i < lv.getAdapter().getCount() ; i++){
            mySolo.clickOnView(getViewAtIndex(lv, 0, getInstrumentation()));

            //mySolo.goBack();

//            mySolo.clickOnView(getViewAtIndex(lv, i, getInstrumentation()));
//            Log.w("w", i + "blub bla bla");
//            mySolo.sleep(500);
        }*/
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
            mySolo.waitForActivity("EditLocations");
            lv = (ListView) mySolo.getView(R.id.listView);
            Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());


            boolean newElementFound = false;

            for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                if(lv.getItemAtPosition(i).toString().equals("{line1=Markt Hartmannsdorf, line2=12,20 N   12,20 E}")) {
                    newElementFound = true;
                    break;
                }
            }

            Assert.assertTrue(newElementFound);

        }

        //FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabAdd);
    }

    public void testEditElement()  {

        ListView lv = (ListView) mySolo.getView(R.id.listView);
        int oldCount = lv.getAdapter().getCount();

        if (lv.getAdapter().getCount() == 0) {
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
                if (mySolo.waitForActivity("EditLocations")) {
                    lv = (ListView) mySolo.getView(R.id.listView);
                    Assert.assertEquals(oldCount + 1, lv.getAdapter().getCount());


                    boolean newElementFound = false;

                    for(int i = 0; i < lv.getAdapter().getCount() ; i++) {
                        if(lv.getItemAtPosition(i).toString().equals("{line1=Markt Hartmannsdorf, line2=12,20 N   12,20 E}")) {
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
                    if(lv.getItemAtPosition(i).toString().equals("{line1=TEST, line2=13,22 N   13,22 E}")) {
                        newElementFound = true;
                        break;
                    }
                }
                Assert.assertTrue(newElementFound);
            }


        }

    }
}