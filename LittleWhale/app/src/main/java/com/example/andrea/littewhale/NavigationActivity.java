package com.example.andrea.littewhale;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.example.andrea.utils.NavigationUtils;

public class NavigationActivity extends AppCompatActivity {

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
    private LocationManager locationManager;

    //for speed
    private double oldLat = -1000.0;
    private double oldLon = -1000.0;
    private long timestampLastUpdateDecisec = 0;

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
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                double[] target = getIntent().getExtras().getDoubleArray("TargetCoords");
                double curLat = location.getLatitude();
                double curLon = location.getLongitude();
                double targetLat = target[0];
                double targetLon = target[1];

                //speed calc



                Log.e("TargetLatitude", Double.toString(targetLat));
                Log.e("TargetLongitude", Double.toString(targetLon));
                Log.e("CurrentLatitude", String.valueOf(curLat));
                Log.e("CurrentLongitude", String.valueOf(curLon));
                Log.e("Speed", Float.toString(location.getSpeed()));
                Log.e("Distance", Double.toString(
                        NavigationUtils.distanceInKm(curLat, curLon, targetLat, targetLon)));
                double angle = NavigationUtils.angleToTarget(curLat, curLon, targetLat, targetLon);
                Log.e("Angle", Double.toString(angle));

                NumberFormat formatter = new DecimalFormat("#0.000");

                ((TextView) findViewById(R.id.editTextDistance)).setText("Distance: " + formatter.format(NavigationUtils.distanceInKm(curLat, curLon, targetLat, targetLon)) + " km");
                ((TextView) findViewById(R.id.editTextSpeed)).setText("Speed: " + formatter.format(getCurrentSpeed(curLat, curLon)) + " km/h");
                ((TextView) findViewById(R.id.editTextCourseAngle)).setText("Course Angle: " + formatter.format(angle) + " °");
                ((TextView) findViewById(R.id.editTextCurrLongitude)).setText("Longitude: " + formatter.format(curLon));
                ((TextView) findViewById(R.id.editTextCurrLatitude)).setText("Latitude: " + formatter.format(curLat));
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
    public void onPause(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
            }
        }

        super.onPause();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
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
        double speed = 0;
        long currentTimeDecisecond = System.currentTimeMillis() / 100;

        if(oldLat > -90.0 && oldLat < 90.0 && oldLon > -180.0 && oldLon < 180.0 && timestampLastUpdateDecisec > 0){
            long timeBetweenUpdateSec = (currentTimeDecisecond - timestampLastUpdateDecisec);
            double distanceBetweenUpdateMeters = NavigationUtils.distanceInKm(curLat, curLon, oldLat, oldLon) * 1000;

            Log.i("update time: ", Long.toString(timeBetweenUpdateSec / 10));
            Log.i("distance: ", Double.toString(distanceBetweenUpdateMeters));

            speed = (distanceBetweenUpdateMeters / timeBetweenUpdateSec) * 36;
        }

        oldLat = curLat;
        oldLon = curLon;
        timestampLastUpdateDecisec = currentTimeDecisecond;

        return speed;
    }
}
