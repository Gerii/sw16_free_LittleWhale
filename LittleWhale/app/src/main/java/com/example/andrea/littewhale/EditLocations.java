package com.example.andrea.littewhale;

import com.example.andrea.model.Location;
import com.example.andrea.model.LocationDb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditLocations extends AppCompatActivity {

    private List<Map<String, String>> valueListLocations = new ArrayList<Map<String, String>>();
    private HashMap<Long, Long> listIdToDbId = new HashMap<>();

    private static String line1 = "line1";
    private static String line2 = "line2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locations);
        Log.w("ON CREATE", "EditLocations");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<Location> locationList = LocationDb.getInstance().getAllLocations(getApplicationContext());

        for(int i = 0; i < locationList.size(); i++){
            Location l = locationList.get(i);
            Map<String, String> tmp = new HashMap<String, String>(2);
            tmp.put(line1, l.placeName);
            String lonCardinalDir = "E";
            String latCardinalDir = "N";

            if(l.latitude < 0 ) {
                latCardinalDir = "S";
            }
            if(l.longitude < 0) {
                lonCardinalDir = "W";
            }

            int[] lat = EnterCoordinates.decimalToTimeConversion(Math.abs(l.latitude));
            int[] lon = EnterCoordinates.decimalToTimeConversion(Math.abs(l.longitude));


            String latString = Integer.toString(lat[0]) + "° " +
                    Integer.toString(lat[1]) + "' " + Integer.toString(lat[2]) + "'' " + latCardinalDir;
            String lonString = Integer.toString(lon[0]) + "°" +
                    Integer.toString(lon[1]) + "' " + Integer.toString(lon[2]) + "'' " + lonCardinalDir;



          //  String test = String.format("%1$-25s", latString);
          //  String test2 = String.format("%1$-25s", lonString);

            tmp.put(line2, latString + "   "+ lonString);

            listIdToDbId.put(Long.valueOf(i), l.id);
            valueListLocations.add(tmp);
        }

        final ListAdapter adapter = new SimpleAdapter(this, valueListLocations,
                android.R.layout.simple_list_item_2,
                new String[] {line1, line2 },
                new int[] {android.R.id.text1, android.R.id.text2 });

        final ListView lv = (ListView)findViewById(R.id.listView);

        if (lv != null) {
            lv.setAdapter(adapter);
        }

        if (lv != null) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Long dbId = listIdToDbId.get(id);

                    LocationDb locationDbInstance = LocationDb.getInstance();
                    Location selectedLocation = locationDbInstance.getLocation(getApplicationContext(), dbId);

                        Intent myIntent = new Intent();
                        myIntent.putExtra("LocationLatitude", selectedLocation.latitude);
                        myIntent.putExtra("LocationLongitude", selectedLocation.longitude);
                        myIntent.putExtra("LocationName", selectedLocation.placeName);


                    Log.e("SETTING FINISHED", "");

                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK,myIntent);
                        Log.e("SETTING FINISHED", "Parent was null");

                    }
                    else {
                        getParent().setResult(Activity.RESULT_OK, myIntent);
                        Log.e("SETTING FINISHED", "Parent was NOT null");

                    }
                        finish();

                }
            });
        }


        if (lv != null) {
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.w("EDIT", "Edit element");

                    Long dbId = listIdToDbId.get(id);

                    LocationDb locationDbInstance = LocationDb.getInstance();
                    Location selectedLocation = locationDbInstance.getLocation(getApplicationContext(), dbId);


                    Intent myIntent = new Intent(EditLocations.this, AddNewLocation.class);
                    myIntent.putExtra("LocationName", selectedLocation.placeName);
                    myIntent.putExtra("LocationLatitude", selectedLocation.latitude);
                    myIntent.putExtra("LocationLongitude", selectedLocation.longitude);
                    myIntent.putExtra("LocationId", dbId);

                    EditLocations.this.startActivity(myIntent);
                    return true;
                }
            });
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);

        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent myIntent = new Intent(EditLocations.this, AddNewLocation.class);
                        EditLocations.this.startActivity(myIntent);

                    }
                    catch (Exception e)
                    {
                        Log.w("FAIL", e);
                    }
                }
            });
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
        Log.w("RESTART", "method is called");

    }
}
