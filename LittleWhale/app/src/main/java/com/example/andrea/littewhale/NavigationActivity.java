package com.example.andrea.littewhale;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.andrea.utils.NavigationUtils;

public class NavigationActivity extends AppCompatActivity implements SensorEventListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private LocationListener locationListener;

    LocationManager locationManager;

    //for speed
    private double oldLat = -1000.0;
    private double oldLon = -1000.0;
    private long timestampLastUpdateTimestamp = 0;
    private ArrayList<Pair<Long, Double>> speedHistory = new ArrayList<>();

    //for compass orientation
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;

    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotation = new float[9];
    private float[] orientation = new float[3];
    private GeomagneticField geomagneticField;
    private double bearing = 0;
    private final float alpha = 0.8f;

    //textviews
    /*private TextView tvDistance;
    private TextView tvSpeed;
    private TextView tvCourseAngle;
    private TextView tvCurrlon;
    private TextView tvCurrlat;
    private TextView tvBearing;*/


    private ArrayList<String> updateLog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        if(mViewPager != null)
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if(tabLayout != null)
        tabLayout.setupWithViewPager(mViewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        //compass
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                double[] target = getIntent().getExtras().getDoubleArray("TargetCoords");
                double curLat = location.getLatitude();
                double curLon = location.getLongitude();
                double targetLat = 0;
                double targetLon = 0;

                if(target.length == 2){
                    targetLat = target[0];
                    targetLon = target[1];
                }else{
                    return;
                }

                double angle = NavigationUtils.angleToTarget(curLat, curLon, targetLat, targetLon);

                Log.e("TargetLatitude", Double.toString(targetLat));
                Log.e("TargetLongitude", Double.toString(targetLon));
                Log.e("CurrentLatitude", String.valueOf(curLat));
                Log.e("CurrentLongitude", String.valueOf(curLon));
                Log.e("Speed", Float.toString(location.getSpeed()));
                Log.e("Distance", Double.toString(
                        NavigationUtils.distanceInKm(curLat, curLon, targetLat, targetLon)));
                Log.e("Angle", Double.toString(angle));

                NumberFormat formatter = new DecimalFormat("#0.000");

                geomagneticField = new GeomagneticField(
                        (float) curLat,
                        (float) curLon,
                        (float) location.getAltitude(),
                        System.currentTimeMillis());

                TextView tvDistance = ((TextView) findViewById(R.id.editTextDistance));
                TextView tvSpeed = ((TextView) findViewById(R.id.editTextSpeed));
                TextView tvCourseAngle = ((TextView) findViewById(R.id.editTextCourseAngle));
                TextView tvCurrlon = ((TextView) findViewById(R.id.editTextCurrLongitude));
                TextView tvCurrlat = ((TextView) findViewById(R.id.editTextCurrLatitude));
                //TextView tvBearing = ((TextView) findViewById(R.id.editTextBearing));

                if(tvDistance != null && tvSpeed != null && tvCourseAngle != null && tvCurrlon != null && tvCurrlat != null){
                    tvDistance.setText("Distance: " + formatter.format(NavigationUtils.distanceInKm(curLat, curLon, targetLat, targetLon)) + " km");
                    tvSpeed.setText("Speed: " + formatter.format(getCurrentSpeed(curLat, curLon)) + " km/h");
                    tvCourseAngle.setText("Course Angle: " + formatter.format(angle) + " °");
                    tvCurrlon.setText("Longitude: " + formatter.format(curLon));
                    tvCurrlat.setText("Latitude: " + formatter.format(curLat));
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);
    }

    @Override
    public void onResume(){
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListener);
            }
        }

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }
        }

        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("accuracy", sensor.getName() + " " + accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
        //http://www.ssaurel.com/blog/learn-how-to-make-a-compass-application-for-android/
        boolean accelOrMagnetic = false;

        // get accelerometer data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            accelOrMagnetic = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            geomagnetic[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            geomagnetic[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            accelOrMagnetic = true;
        }

        SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
        SensorManager.getOrientation(rotation, orientation);
        bearing = orientation[0];
        bearing = Math.toDegrees(bearing);

        if (geomagneticField != null) {
            bearing += geomagneticField.getDeclination();
        }

        if (bearing < 0) {
            bearing += 360;
        }

        /*if(tvBearing != null){
            tvBearing.setText("bearing: " + bearing + "°");
        }*/

        TextView tvBearing = ((TextView) findViewById(R.id.editTextBearing));

        if(tvBearing != null){
            tvBearing.setText("bearing: " + bearing + "°");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class NavigationFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public NavigationFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NavigationFragment newInstance(int sectionNumber) {
            NavigationFragment fragment = new NavigationFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class WeatherFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public WeatherFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WeatherFragment newInstance(int sectionNumber) {
            WeatherFragment fragment = new WeatherFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
            return rootView;
        }
    }

    public static class MapFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public MapFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MapFragment newInstance(int sectionNumber) {
            MapFragment fragment = new MapFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NavigationFragment.newInstance(position);
                case 1:
                    return WeatherFragment.newInstance(position);
                case 2:
                    return MapFragment.newInstance(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private double getCurrentSpeed(double curLat, double curLon){
        double currentSpeed = 0;
        long currentTimestamp = System.currentTimeMillis();

        updateLog.add(new SimpleDateFormat("HH:mm:ss").format(currentTimestamp));

        if(updateLog.size() > 9){
            updateLog.remove(0);
        }

        ((TextView) findViewById(R.id.editTextLogUpdate)).setText("Log\n");

        for(int i = 0; i < updateLog.size(); i++){
            ((TextView) findViewById(R.id.editTextLogUpdate)).append(updateLog.get(i) + "\n");
        }

        if(oldLat > -90.0 && oldLat < 90.0 && oldLon > -180.0 && oldLon < 180.0 && timestampLastUpdateTimestamp > 0){
            long timeBetweenUpdateMilliSec = (currentTimestamp - timestampLastUpdateTimestamp);
            double distanceBetweenUpdateMeters = NavigationUtils.distanceInM(curLat, curLon, oldLat, oldLon);

            Log.i("update time: ", Double.toString(timeBetweenUpdateMilliSec / 1000.0));
            Log.i("distance: ", Double.toString(distanceBetweenUpdateMeters));

            currentSpeed = distanceBetweenUpdateMeters * 3600 / timeBetweenUpdateMilliSec;

            speedHistory.add(new Pair(timeBetweenUpdateMilliSec, currentSpeed));

            if(speedHistory.size() > 3){
                speedHistory.remove(0);
            }
        }

        oldLat = curLat;
        oldLon = curLon;
        timestampLastUpdateTimestamp = currentTimestamp;

        //calculate avg speed from last positions
        long cumulatedTimeSpan = 0;
        double cumulatedSpeeds = 0;

        for(int i = 0; i < speedHistory.size(); i++){
            cumulatedTimeSpan += speedHistory.get(i).first;
            cumulatedSpeeds += speedHistory.get(i).second * speedHistory.get(i).first ;
            Log.w("speed hist", speedHistory.get(i).first + " " + speedHistory.get(i).second);
        }

        if(cumulatedTimeSpan > 0){
            return cumulatedSpeeds / cumulatedTimeSpan;
        }

        return 0;
    }
}
