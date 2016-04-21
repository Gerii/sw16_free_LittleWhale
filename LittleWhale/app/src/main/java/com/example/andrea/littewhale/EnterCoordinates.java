package com.example.andrea.littewhale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class EnterCoordinates extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_coordinates);

        RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                decimalLayout.setVisibility(LinearLayout.GONE);

                setViewDecimalToTime();

                LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                timeLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });


        RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                setViewTimeToDecimal();

                decimalLayout.setVisibility(LinearLayout.VISIBLE);
                timeLayout.setVisibility(LinearLayout.GONE);
            }
        });
    }

    private int convertToInt(String numberString) {

        try{
            return Integer.parseInt(numberString);
        } catch(Exception e) {
            return 0;
        }
    }

    private void setViewTimeToDecimal() {
        int degreesLatitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString());
        int minutesLatitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());

        int degreesLongitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLongitude)).getText().toString());
        int secondsLongitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLongitude)).getText().toString());

        double decimalLatitude = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).setText(Double.toString(decimalLatitude));

        double decimalLongitude = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).setText(Double.toString(decimalLongitude));

        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).getSelectedItemPosition();
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).setSelection(longitudeSelectionIndex);


    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double)minute / 60) + ((double) second / 3600);
    }



    private void setViewDecimalToTime() {
        double longitude = 0;
        double latitude = 0;
        try {
            longitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).getText().toString());
            latitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).getText().toString());
        }catch (Exception e) {

        }
        int[] longitudeArray = decimalToTimeConversion(longitude);
        int[] latitudeArray = decimalToTimeConversion(latitude);

        ((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).setText(Integer.toString(latitudeArray[0]));
        ((TextView) findViewById(R.id.editTextMinuteLatitude)).setText(Integer.toString(latitudeArray[1]));
        ((TextView) findViewById(R.id.editTextSecondLatitude)).setText(Integer.toString(latitudeArray[2]));

        ((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).setText(Integer.toString(longitudeArray[0]));
        ((TextView) findViewById(R.id.editTextMinuteLongitude)).setText(Integer.toString(longitudeArray[1]));
        ((TextView) findViewById(R.id.editTextSecondLongitude)).setText(Integer.toString(longitudeArray[2]));

        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).getSelectedItemPosition();
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).setSelection(longitudeSelectionIndex);


    }


    private int[] decimalToTimeConversion(double degree) {
        int[] result = new int[3];
        result[0] = (int) Math.floor(degree);
        degree -= result[0];
        degree *= 60;
        result[1] = (int) Math.floor(degree);
        degree -= result[1];
        degree *= 60;
        result[2] = (int) Math.floor(degree);

        return result;
    }

}
