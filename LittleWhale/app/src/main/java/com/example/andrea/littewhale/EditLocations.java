package com.example.andrea.littewhale;

import com.example.andrea.model.Location;
import com.example.andrea.model.LocationDb;

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

    private Boolean readOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_locations);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            String caller = extras.getString("Caller");

            if(caller.equals("enterCoordinates"))
            {

               readOnly = true;
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<Location> locationList = LocationDb.getInstance().getAllLocations(getApplicationContext());

        for(int i = 0; i < locationList.size(); i++){
            Location l = locationList.get(i);
            Map<String, String> tmp = new HashMap<String, String>(2);
            tmp.put(line1, l.placeName);
            String lon = "E";
            String lat = "N";

            if(l.latitude < 0 ) {
                lat = "S";
            }
            if(l.longitude < 0) {
                lon = "W";
            }


            String latValue = String.format("%.2f", Math.abs(l.latitude));
            String lonValue = String.format("%.2f", Math.abs(l.longitude));

            tmp.put(line2, latValue + " " + lat + "   "+ lonValue  + " " + lon);

            listIdToDbId.put(Long.valueOf(i), l.id);
            valueListLocations.add(tmp);
        }

        final ListAdapter adapter = new SimpleAdapter(this, valueListLocations,
                android.R.layout.simple_list_item_2,
                new String[] {line1, line2 },
                new int[] {android.R.id.text1, android.R.id.text2 });

        final ListView lv = (ListView)findViewById(R.id.listView);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Long dbId = listIdToDbId.get(id);

                LocationDb locationDbInstance = LocationDb.getInstance();
                Location selectedLocation = locationDbInstance.getLocation(getApplicationContext(), dbId);

                if (!readOnly) {
                    Intent myIntent = new Intent(EditLocations.this, AddNewLocation.class);
                    myIntent.putExtra("LocationName", selectedLocation.placeName);
                    myIntent.putExtra("LocationLatitude", selectedLocation.latitude);
                    myIntent.putExtra("LocationLongitude", selectedLocation.longitude);
                    myIntent.putExtra("LocationId", dbId);

                    EditLocations.this.startActivity(myIntent);
                } else {
                    Intent myIntent = new Intent();
                    myIntent.putExtra("LocationLatitude", selectedLocation.latitude);
                    myIntent.putExtra("LocationLongitude", selectedLocation.longitude);
                    setResult(RESULT_OK, myIntent);
                    finish();
                }


            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DELETE", "Delete element");
                //locationList.remove(position);
                //adapter.notify();
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);

        if(readOnly)
        {
            fab.setVisibility(LinearLayout.GONE);
        }

        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.w("fab", "fab pressed");

                    try {
                        //LocationDb.getInstance().addLocation(getApplicationContext(), "blub", 12.0, 15.3);
                        Intent myIntent = new Intent(EditLocations.this, AddNewLocation.class);
                        EditLocations.this.startActivity(myIntent);

                        /*ArrayList<Location> locationList = LocationDb.getInstance().getAllLocations(getApplicationContext());

                        for(Location l : locationList){
                            Log.w("id", l.id.toString());
                            Log.w("name", l.placeName);
                            Log.w("lat", l.latitude.toString());
                            Log.w("lon", l.longitude.toString());
                        }*/

                        /*Location tmpLoc = LocationDb.getInstance().getLocation(getApplicationContext(), Long.valueOf(2));

                        Log.w("id", tmpLoc.id.toString());
                        Log.w("name", tmpLoc.placeName);
                        Log.w("lat", tmpLoc.latitude.toString());
                        Log.w("lon", tmpLoc.longitude.toString());*/
                    }
                    catch (Exception e)
                    {
                        Log.w("graz", e);
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
        Log.w("RESUME", "method is called");
    }
}
