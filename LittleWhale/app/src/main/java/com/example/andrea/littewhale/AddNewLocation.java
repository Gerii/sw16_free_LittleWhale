package com.example.andrea.littewhale;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.andrea.model.LocationDb;

public class AddNewLocation extends AppCompatActivity {

    boolean editing = false;
    long locId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            editing = true;

            Log.w("EDIT", "Location");
            String locationName = extras.getString("LocationName");
            Double locationLatitude = extras.getDouble("LocationLatitude");
            Double locationLongitude = extras.getDouble("LocationLongitude");
            Long locationId = extras.getLong("LocationId");

            setViewDecimalToTime(true, locationLongitude, locationLatitude);
            ((TextView) findViewById(R.id.newLocation_locationName)).setText(locationName);
            locId = locationId;

        }

        RadioButton button = (RadioButton) findViewById(R.id.newLocation_rbtnTimeNotation);
        if(button != null) {

            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.newLocation_linearLayoutDecimalCoords);

                    setViewDecimalToTime(false, 0, 0);

                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.newLocation_linearLayoutTimeCoords);
                    if (decimalLayout != null && timeLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.GONE);
                        timeLayout.setVisibility(LinearLayout.VISIBLE);
                    }


                }

            });
        }


        RadioButton button2 = (RadioButton) findViewById(R.id.newLocation_rbtnDecimalNotation);
        if(button2 != null) {
            button2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.newLocation_linearLayoutDecimalCoords);
                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.newLocation_linearLayoutTimeCoords);
                    setViewTimeToDecimal();
                    if (decimalLayout != null && timeLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.VISIBLE);
                        timeLayout.setVisibility(LinearLayout.GONE);
                    }

                }
            });
        }




        Button saveLocation = (Button) findViewById(R.id.newLocation_buttonAddLocation);
        if (saveLocation != null)
            saveLocation.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                TextView newLocationTextView = (TextView) findViewById(R.id.newLocation_locationName);
                String newLocationName = newLocationTextView.getText().toString();

                if(newLocationName.length() == 0 || newLocationName == "")
                {
                    newLocationTextView.setHintTextColor(Color.RED);
                    Log.e("ERROR", "Name is empty");
                    return;
                }

                double targetLatitude = 0;
                double targetLongitude = 0;

                RadioButton button = (RadioButton) findViewById(R.id.newLocation_rbtnTimeNotation);
                RadioButton button2 = (RadioButton) findViewById(R.id.newLocation_rbtnDecimalNotation);

                if (button != null && button.isChecked()) {
                    double[] decimal = readTimeFormat();
                    targetLatitude = decimal[0];
                    targetLongitude = decimal[1];

                } else if (button2 != null && button2.isChecked()) {
                    String latitude = ((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLatitude)).getText().toString();
                    String longitude = ((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLongitude)).getText().toString();

                    targetLatitude = Double.parseDouble(latitude);
                    targetLongitude = Double.parseDouble(longitude);

                } else {
                    Log.e("ERROR", "WTF?!?!?!");
                }

                double[] target = new double[2];

                target[0] = targetLatitude;
                target[1] = targetLongitude;

                String[] cardinalDirection = new String[2];

                cardinalDirection[0] = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLatitude)).getSelectedItem().toString();
                cardinalDirection[1] = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLongitude)).getSelectedItem().toString();


                if(cardinalDirection[1].equals("W")) {
                    targetLongitude *= -1;
                }
                if (cardinalDirection[0].equals("S")) {
                    targetLatitude *= -1;
                }


                LocationDb locationDbInstance = LocationDb.getInstance();

                if(editing) {
                    locationDbInstance.editLocation(getApplicationContext(), locId, newLocationName ,targetLatitude, targetLongitude);
                } else {
                    locationDbInstance.addLocation(getApplicationContext(), newLocationName, targetLatitude ,targetLongitude);

                }

                AddNewLocation.this.finish();


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

        double[] decimal = readTimeFormat();

        ((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLatitude)).setText(Double.toString(decimal[0]));
        ((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLongitude)).setText(Double.toString(decimal[1]));


        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLatitude)).getSelectedItemPosition();
        ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionDecimalLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionDecimalLongitude)).setSelection(longitudeSelectionIndex);


    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double)minute / 60) + ((double) second / 3600);
    }


    private double[] readTimeFormat() {
        double [] decimal = new double[2];
        int degreesLatitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextDegreeTimeLatitude)).getText().toString());
        int minutesLatitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextSecondLatitude)).getText().toString());

        int degreesLongitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextMinuteLongitude)).getText().toString());
        int secondsLongitude = convertToInt(((TextView) findViewById(R.id.newLocation_editTextSecondLongitude)).getText().toString());

        decimal[0] = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        decimal[1] = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);

        return decimal;
    }



    private void setViewDecimalToTime(boolean parameters, double lon, double lat) {
        double longitude = 0;
        double latitude = 0;


         if(parameters == false) {
           try {
               longitude = Double.parseDouble(((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLongitude)).getText().toString());
               latitude = Double.parseDouble(((TextView) findViewById(R.id.newLocation_editTextDegreeDecimalLatitude)).getText().toString());
           } catch (Exception e) {
               Log.w("Exception", "Dont know");
           }

         } else {
           longitude = lon;
           latitude = lat;
         }
        int[] longitudeArray = decimalToTimeConversion(Math.abs(longitude));
        int[] latitudeArray = decimalToTimeConversion(Math.abs(latitude));


        ((TextView) findViewById(R.id.newLocation_editTextDegreeTimeLatitude)).setText(Integer.toString(latitudeArray[0]));
        ((TextView) findViewById(R.id.newLocation_editTextMinuteLatitude)).setText(Integer.toString(latitudeArray[1]));
        ((TextView) findViewById(R.id.newLocation_editTextSecondLatitude)).setText(Integer.toString(latitudeArray[2]));

        ((TextView) findViewById(R.id.newLocation_editTextDegreeTimeLongitude)).setText(Integer.toString(longitudeArray[0]));
        ((TextView) findViewById(R.id.newLocation_editTextMinuteLongitude)).setText(Integer.toString(longitudeArray[1]));
        ((TextView) findViewById(R.id.newLocation_editTextSecondLongitude)).setText(Integer.toString(longitudeArray[2]));


        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionDecimalLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionDecimalLatitude)).getSelectedItemPosition();

        if (editing) {
            if(lon < 0) {
                longitudeSelectionIndex = 1;
            }
            if(lat < 0) {
                latitudeSelectionIndex = 1;
            }
        }

        ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.newLocation_spinnerCardinalDirectionTimeLongitude)).setSelection(longitudeSelectionIndex);




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