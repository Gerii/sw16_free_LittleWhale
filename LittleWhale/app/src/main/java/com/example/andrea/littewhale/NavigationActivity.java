package com.example.andrea.littewhale;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
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

import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import com.example.andrea.utils.NavigationUtils;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.*;


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

    private double angle = 0;

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
        mViewPager.setOffscreenPageLimit(2);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

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

                angle = NavigationUtils.angleToTarget(curLat, curLon, targetLat, targetLon);

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

                TextView tvSpeed = ((TextView) findViewById(R.id.editTextSpeedKts));
                TextView tvDistance = ((TextView) findViewById(R.id.editTextDistance));
                TextView tvCurrlat = ((TextView) findViewById(R.id.editTextLat));
                TextView tvCurrlon = ((TextView) findViewById(R.id.editTextLon));
                TextView tvCourseAngle = ((TextView) findViewById(R.id.editTextBearing));

                if(tvDistance != null && tvSpeed != null && tvCourseAngle != null && tvCurrlon != null && tvCurrlat != null){
                    tvSpeed.setText(formatter.format(getCurrentSpeedNm(curLat, curLon)) + " kts");
                    tvDistance.setText(formatter.format(NavigationUtils.distanceInNauticalMiles(curLat, curLon, targetLat, targetLon)) + " NM");
                    tvCurrlon.setText(formatter.format(curLon) + " °");
                    tvCurrlat.setText(formatter.format(curLat) + " °");
                    tvCourseAngle.setText(formatter.format(angle) + " °");
                }

                MapView mapView = (MapView) findViewById(R.id.mapView);
                MapController mMapController = (MapController) mapView.getController();

                Log.e("Map Long", String.valueOf(curLon * 1E6) );
                Log.e("Map Lat", String.valueOf(curLat * 1E6));

                GeoPoint gPt = new GeoPoint(curLat ,curLon );
                mMapController.animateTo(gPt);

                GeoPoint curLocation = new GeoPoint(location);

                Marker marker = new Marker(mapView);
                marker.setPosition(curLocation);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().clear();
                mapView.getOverlays().add(marker);
                mapView.invalidate();

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

        TextView tvHeading = ((TextView) findViewById(R.id.editTextHeading));
        NumberFormat formatter = new DecimalFormat("#0");

        if(tvHeading != null){
            tvHeading.setText(formatter.format(bearing) + " °");
        }

        resetAllArrows();

        double deviation = angle - bearing ;

        deviation += 22.5;
        deviation += (2*360);
        deviation = deviation % 360;

        // Log.e("Deviation", deviation);

        if(deviation > 0 && deviation < 45) {
            //Log.e("DIRECTION", "UP");
            ImageView arrow = ((ImageView) findViewById(R.id.upArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        } else if(deviation > 45 && deviation < 90) {
            //Log.e("DIRECTION", "UP RIGHT");
            ImageView arrow = ((ImageView) findViewById(R.id.upRightArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        } else if(deviation > 90 && deviation < 135) {
            //Log.e("DIRECTION", "RIGHT");
            ImageView arrow = ((ImageView) findViewById(R.id.rightArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        }else if(deviation > 135 && deviation < 180) {
            //Log.e("DIRECTION", "DOWN RIGHT");
            ImageView arrow = ((ImageView) findViewById(R.id.downRightArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        }
        else if(deviation > 180 && deviation < 225) {
            //Log.e("DIRECTION", "DOWN");
            ImageView arrow = ((ImageView) findViewById(R.id.downArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        } else if(deviation > 225 && deviation < 270) {
            //Log.e("DIRECTION", "DOWN LEFT");
            ImageView arrow = ((ImageView) findViewById(R.id.downLeftArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        } else if(deviation > 270 && deviation < 315) {
            //Log.e("DIRECTION", "LEFT");
            ImageView arrow = ((ImageView) findViewById(R.id.leftArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        } else if(deviation > 315 && deviation < 360) {
            //Log.e("DIRECTION", "UP LEFT");
            ImageView arrow = ((ImageView) findViewById(R.id.upLeftArrow));
            if(arrow != null) {
                arrow.setAlpha(1f);
            }
        }
    }

    private void resetArrow(int id) {
        ImageView arrow = ((ImageView) findViewById(id));
        if(arrow != null)
            arrow.setAlpha(0.3f);
    }

    private void resetAllArrows() {
        resetArrow(R.id.upArrow);
        resetArrow(R.id.downArrow);
        resetArrow(R.id.rightArrow);
        resetArrow(R.id.leftArrow);
        resetArrow(R.id.upLeftArrow);
        resetArrow(R.id.upRightArrow);
        resetArrow(R.id.downLeftArrow);
        resetArrow(R.id.downRightArrow);

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
            Log.w("TEST", "ON CREATE VIEW");
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);

            Context context = getContext();
            final ITileSource seamarks = new XYTileSource("seamarks",
                    0,
                    19,
                    256,
                    ".png",
                    new String[] {"http://t1.openseamap.org/seamark/"});
            final MapTileProviderBasic tileProvider = new MapTileProviderBasic(context, seamarks);
            final TilesOverlay seamarksOverlay = new TilesOverlay(tileProvider, context);
            seamarksOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
//            seamarksOverlay.setUseSafeCanvas(true);

            MapView mapView = (MapView) rootView.findViewById(R.id.mapView);

            // mapView = new MapView(context, 256);
            mapView.setMultiTouchControls(true);
            //  mapView.setUseSafeCanvas(true);
            mapView.getOverlays().add(seamarksOverlay);

//            mapView.getController().setZoom(7);

            mapView.setTilesScaledToDpi(true);
            MapController mMapController = (MapController) mapView.getController();
            mMapController.setZoom(13);
            GeoPoint gPt = new GeoPoint(51500000, -150000);
            mMapController.setCenter(gPt);
/*

            MapView mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
            mMapView.setBuiltInZoomControls(true);
            MapController mMapController = (MapController) mMapView.getController();
            mMapController.setZoom(13);
            GeoPoint gPt = new GeoPoint(51500000, -150000);
            mMapController.setCenter(gPt);
*/
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
                    return "Navigation";
                case 1:
                    return "Weather";
                case 2:
                    return "Map";
            }
            return null;
        }
    }

    private double getCurrentSpeed(double curLat, double curLon){
        double currentSpeed = 0;
        long currentTimestamp = System.currentTimeMillis();

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

    private double getCurrentSpeedNm(double curLat, double curLon){
        double speedKmh = getCurrentSpeed(curLat, curLon);

        return speedKmh * 0.53995680346039;
    }
}
