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
import com.example.andrea.littewhale.MainActivity;
import com.example.andrea.littewhale.NavigationActivity;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

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
        super.tearDown();
    }


    public void testStartNavigation() throws Exception{
        mySolo.clickOnButton("Start Navigating");
        mySolo.waitForActivity("");

        Socket socket = new Socket("10.0.2.2", 5554); // usually 5554
        PrintStream out = new PrintStream(socket.getOutputStream());
        double latitude = 41.0;
        double longitude = 17.0;
        String str = "geo fix " + Double.toString(latitude) + " " +  Double.toString(longitude) + "\n";
        out.print(str);
        socket.close();

        mySolo.goBack();
        mySolo.waitForActivity("EnterCoordinates");
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
}