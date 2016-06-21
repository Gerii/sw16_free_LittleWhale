package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;
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

        Assert.assertTrue(mySolo.waitForActivity("NavigationActivity"));
    }

    public void testStartDecimalNavigation() throws Exception{
        mySolo.clickOnRadioButton(1);

        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLatitude), "15");
        mySolo.clickOnView(mySolo.getView(R.id.buttonStartNavigating));

        Assert.assertTrue(mySolo.waitForActivity("NavigationActivity"));
    }

    public void testStartNavigationDecimalWithEnter() throws Exception{
        mySolo.clickOnRadioButton(1);

        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLatitude), "15");
        mySolo.clickOnView(mySolo.getView(R.id.editTextDegreeDecimalLongitude));

        mySolo.sendKey(KeyEvent.KEYCODE_ENTER);

        Assert.assertTrue(mySolo.waitForActivity("NavigationActivity"));
    }


    public void testDecimalWithEnter() {
        mySolo.clickOnRadioButton(1);
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLongitude), "12");
        mySolo.enterText((EditText) mySolo.getView(R.id.editTextDegreeDecimalLatitude), "15");
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

                    mySolo.goBack();

                    if (mySolo.waitForActivity("EnterCoordinates")) {
                        mySolo.clickOnButton("Use existing location");
                        if(mySolo.waitForActivity("EditLocations")) {
                            lv = (ListView) mySolo.getCurrentActivity().findViewById(R.id.listView);
                            mySolo.clickOnView(getViewAtIndex(lv, position_of_new_element, getInstrumentation()));
                            if (mySolo.waitForActivity("EnterCoordinates")) {
                                Assert.assertTrue(mySolo.searchText("Graz"));
                            }
                        }
                    }
                }
            }
        }
    }

    public void testUseNewActivityAfterDecimal() {

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

                    mySolo.goBack();

                    if (mySolo.waitForActivity("EnterCoordinates")) {
                        mySolo.clickOnRadioButton(1);
                        mySolo.clickOnButton("Use existing location");
                        if(mySolo.waitForActivity("EditLocations")) {
                            lv = (ListView) mySolo.getCurrentActivity().findViewById(R.id.listView);
                            mySolo.clickOnView(getViewAtIndex(lv, position_of_new_element, getInstrumentation()));
                            if (mySolo.waitForActivity("EnterCoordinates")) {
                                Assert.assertTrue(mySolo.searchText("Graz"));
                            }
                        }
                    }
                }
            }
        }
    }


    public void testAbout() {
        View aboutButton = mySolo.getCurrentActivity().findViewById(R.id.action_about);
        mySolo.clickOnView(aboutButton);
        mySolo.waitForActivity("AboutActivity");
        mySolo.searchText("Authors");

        mySolo.scrollToSide(Solo.RIGHT);

        mySolo.searchText("Arrrrr!");
    }

}