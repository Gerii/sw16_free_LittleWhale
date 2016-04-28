package com.example.andrea.littewhale;

import com.example.andrea.model.Location;
import com.example.andrea.model.LocationDb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Location> locationList = LocationDb.getInstance().getAllLocations(getApplicationContext());

        for(int i = 0; i < locationList.size(); i++){
            Location l = locationList.get(i);
            Map<String, String> tmp = new HashMap<String, String>(2);
            tmp.put(line1, l.placeName);
            tmp.put(line2, l.latitude.toString() + " " + l.longitude.toString() + " " + l.id.toString());

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
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String blub = lv.getAdapter().getItem(arg2).toString();
                Log.w("w", blub + " " + arg3);
                Log.w("w", listIdToDbId.get(arg3).toString());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);

        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.w("fab", "fab pressed");

                    try {
                        LocationDb.getInstance().addLocation(getApplicationContext(), "blub", 12.0, 15.3);

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
}
