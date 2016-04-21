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
                LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                timeLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });


        RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);



                decimalLayout.setVisibility(LinearLayout.VISIBLE);
                timeLayout.setVisibility(LinearLayout.GONE);
            }
        });
    }

    private void setViewTimeToDecimal() {
        int degreesLatitude = 0;

        String degreesLatitudeString = ((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString();
        if (degreesLatitudeString != "") {
            degreesLatitude = Integer.parseInt(degreesLatitudeString);
        }
        Integer.parseInt(((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString());


        int minutesLatitude = Integer.parseInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = Integer.parseInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());
        double decimalLatitude = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).setText(Double.toString(decimalLatitude));

        int degreesLongitude = Integer.parseInt(((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = Integer.parseInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLongitude = Integer.parseInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());
        double decimalLongitude = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);
        ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).setText(Double.toString(decimalLongitude));

    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double)minute / 60) + ((double) second / 3600);
    }

}
