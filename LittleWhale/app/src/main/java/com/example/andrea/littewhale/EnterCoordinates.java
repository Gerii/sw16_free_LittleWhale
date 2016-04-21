package com.example.andrea.littewhale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
            return -1;
        }
    }

    private void setViewTimeToDecimal() {
        int degreesLatitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString());
        int minutesLatitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());

        int degreesLongitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLongitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());

        double decimalLatitude = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).setText(Double.toString(decimalLatitude));

        double decimalLongitude = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).setText(Double.toString(decimalLongitude));

    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double)minute / 60) + ((double) second / 3600);
    }



    private void setViewDecimalToTime() {

    }


    private int[] decimalToTimeConversion(double degree) {
        int[] result = new int[3];
        result[0] = (int) Math.floor(degree);
        degree -= result[0];
        degree *= 60;
        result[1] = (int) Math.floor(degree);
        degree -= result[1];
        degree *= 3600;
        result[2] = (int) Math.floor(degree);

        return result;
    }

}
