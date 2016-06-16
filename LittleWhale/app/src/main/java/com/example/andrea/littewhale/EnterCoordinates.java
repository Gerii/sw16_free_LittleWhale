package com.example.andrea.littewhale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.andrea.utils.InputFilterDouble;
import com.example.andrea.utils.InputFilterInt;

public class EnterCoordinates extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enter_coordinates);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);

        RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
        if (button != null)
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                    if (decimalLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.INVISIBLE);
                        //  decimalLayout.invalidate();

                    }
                    setViewDecimalToTime(false, 0, 0);
                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                    if (timeLayout != null) {
                        timeLayout.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            });


        RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);
        if (button2 != null)
            button2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    LinearLayout decimalLayout = (LinearLayout) findViewById(R.id.linearLayoutDecimalCoords);
                    LinearLayout timeLayout = (LinearLayout) findViewById(R.id.linearLayoutTimeCoords);
                    setViewTimeToDecimal(false, null);

                    if (decimalLayout != null) {
                        decimalLayout.setVisibility(LinearLayout.VISIBLE);
                    }
                    if (timeLayout != null) {
                        timeLayout.setVisibility(LinearLayout.INVISIBLE);
                    }
                }
            });


        Button startNavigationButton = (Button) findViewById(R.id.buttonStartNavigating);
        if (startNavigationButton != null)
            startNavigationButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(EnterCoordinates.this, NavigationActivity.class);
                    double targetLatitude = 0;
                    double targetLongitude = 0;

                    RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
                    RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);

                    if (button != null && button.isChecked()) {
                        double[] decimal = readTimeFormat();
                        targetLatitude = decimal[0];
                        targetLongitude = decimal[1];

                    } else if (button2 != null && button2.isChecked()) {
                        String latitude = ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).getText().toString();
                        String longitude = ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).getText().toString();

                        targetLatitude = Double.parseDouble(latitude);
                        targetLongitude = Double.parseDouble(longitude);

                    } else {
                        Log.e("ERROR", "WTF?!?!?!");
                    }

                    double[] target = new double[2];

                    target[0] = targetLatitude;
                    target[1] = targetLongitude;

                    String[] cardinalDirection = new String[2];

                    cardinalDirection[0] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).getSelectedItem().toString();
                    cardinalDirection[1] = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).getSelectedItem().toString();
                    myIntent.putExtra("TargetCoords", target);
                    myIntent.putExtra("CardinalDirection", cardinalDirection);
                    myIntent.putExtra("TargetCoords", target);
                    EnterCoordinates.this.startActivity(myIntent);
                }
            });

        Button useExistingLocationBtn = (Button) findViewById(R.id.useExistingLocation);
        if (useExistingLocationBtn != null) {
            useExistingLocationBtn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(EnterCoordinates.this, EditLocations.class);
                    EnterCoordinates.this.startActivityForResult(myIntent, 1);
                }
            });
        }


        EditText editTextSecondLatitude = (EditText) findViewById(R.id.editTextSecondLatitude);
        EditText editTextSecondLongitude = (EditText) findViewById(R.id.editTextSecondLongitude);

        EditText editTextMinuteLatitude = (EditText) findViewById(R.id.editTextMinuteLatitude);
        EditText editTextMinuteLongitude = (EditText) findViewById(R.id.editTextMinuteLongitude);

        EditText editTextDegreeTimeLatitude = (EditText) findViewById(R.id.editTextDegreeTimeLatitude);
        EditText editTextDegreeTimeLongitude = (EditText) findViewById(R.id.editTextDegreeTimeLongitude);

        EditText editTextDegreeDecimalLatitude = (EditText) findViewById(R.id.editTextDegreeDecimalLatitude);
        EditText editTextDegreeDecimalLongitude = (EditText) findViewById(R.id.editTextDegreeDecimalLongitude);

        editTextSecondLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextSecondLatitude)});
        editTextSecondLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextSecondLongitude)});
        editTextMinuteLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextMinuteLatitude)});
        editTextMinuteLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 60, editTextMinuteLongitude)});

        editTextDegreeTimeLatitude.setFilters(new InputFilter[]{new InputFilterInt(0, 90, editTextDegreeTimeLatitude)});
        editTextDegreeTimeLongitude.setFilters(new InputFilter[]{new InputFilterInt(0, 180, editTextDegreeTimeLongitude)});

        editTextDegreeDecimalLatitude.setFilters(new InputFilter[]{new InputFilterDouble(0, 90, editTextDegreeDecimalLatitude)});
        editTextDegreeDecimalLongitude.setFilters(new InputFilter[]{new InputFilterDouble(0, 180, editTextDegreeDecimalLongitude)});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entercoordinates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {


            new AlertDialog.Builder(this)
                    .setTitle("Little Whale")
                    .setMessage(Html.fromHtml("<b>" + "Developed by:" + "</b><br>" + " Clemens Hofer<br>Gerald Palfinger<br>" +
                            "Andrea Pferscher<br>Angela Promitzer"))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w("ON ACTIVITY RESULT", "Enter coordinates");

        if (requestCode == 1) {

            if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                if (data == null) {
                    Log.e("ERROR", "data null");

                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Log.w("ON ACTIVITY RESULT", "set lon and lat");
                    Double lon = extras.getDouble("LocationLongitude");
                    Double lat = extras.getDouble("LocationLatitude");
                    String name = extras.getString("LocationName");

                    RadioButton button = (RadioButton) findViewById(R.id.rbtnTimeNotation);
                    RadioButton button2 = (RadioButton) findViewById(R.id.rbtnDecimalNotation);

                    if (button != null && button.isChecked()) {
                        setViewDecimalToTime(true, lon, lat);

                    } else if (button2 != null && button2.isChecked()) {
                        double[] coords = {lat, lon};
                        setViewTimeToDecimal(true, coords);
                    } else {
                        Log.e("ERROR", "WTF?!?!?!");
                    }

                    TextView locationName = (TextView) findViewById(R.id.cityName);
                    if (locationName != null) {
                        locationName.setText(name);
                    }


                } else {
                    Log.e("ERROR", "EXTRAS NULL");


                }
            } else {
                Log.e("ERROR", "Result code not okay");
                Log.e("ERROR", Integer.toString(resultCode));

            }
        }

    }


    private int convertToInt(String numberString) {

        try {
            return Integer.parseInt(numberString);
        } catch (Exception e) {
            return 0;
        }
    }

    private void setViewTimeToDecimal(boolean parameters, double[] coords) {
        double[] decimal = null;
        if (!parameters) {
            decimal = readTimeFormat();
        } else {
            decimal = coords;
        }

        ((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).setText(Double.toString(decimal[0]));
        ((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).setText(Double.toString(decimal[1]));


        int longitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLongitude)).getSelectedItemPosition();
        int latitudeSelectionIndex = ((Spinner) findViewById(R.id.spinnerCardinalDirectionTimeLatitude)).getSelectedItemPosition();
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLatitude)).setSelection(latitudeSelectionIndex);
        ((Spinner) findViewById(R.id.spinnerCardinalDirectionDecimalLongitude)).setSelection(longitudeSelectionIndex);


    }

    private double timeToDecimalConversion(int degree, int minute, int second) {

        return degree + ((double) minute / 60) + ((double) second / 3600);
    }


    private double[] readTimeFormat() {
        double[] decimal = new double[2];
        int degreesLatitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLatitude)).getText().toString());
        int minutesLatitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLatitude)).getText().toString());
        int secondsLatitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLatitude)).getText().toString());

        int degreesLongitude = convertToInt(((TextView) findViewById(R.id.editTextDegreeTimeLongitude)).getText().toString());
        int minutesLongitude = convertToInt(((TextView) findViewById(R.id.editTextMinuteLongitude)).getText().toString());
        int secondsLongitude = convertToInt(((TextView) findViewById(R.id.editTextSecondLongitude)).getText().toString());

        decimal[0] = timeToDecimalConversion(degreesLatitude, minutesLatitude, secondsLatitude);
        decimal[1] = timeToDecimalConversion(degreesLongitude, minutesLongitude, secondsLongitude);

        return decimal;
    }


    private void setViewDecimalToTime(boolean parameters, double lon, double lat) {
        double longitude = 0;
        double latitude = 0;

        if (!parameters) {
            try {
                longitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLongitude)).getText().toString());
                latitude = Double.parseDouble(((TextView) findViewById(R.id.editTextDegreeDecimalLatitude)).getText().toString());
            } catch (Exception e) {

            }
        } else {
            longitude = Math.abs(lon);
            latitude = Math.abs(lat);
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

        if (parameters) {
            if (lon < 0) {
                longitudeSelectionIndex = 1;
            }
            if (lat < 0) {
                latitudeSelectionIndex = 1;
            }
        }

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
