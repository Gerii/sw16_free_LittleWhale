package com.example.andrea.utils;

/**
 * Created by clemens on 14.05.16.
 */
public class NavigationUtils {
    private final static double RADIUS = 6378137;

    public static double distanceInM(double lat1, double lon1, double lat2, double lon2) {
        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = RADIUS * c;

        return d;
    }

    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        return distanceInM(lat1, lon1, lat2, lon2) / 1000.0;
    }

    public static double distanceInNauticalMiles(double lat1, double lon1, double lat2, double lon2) {
        return distanceInM(lat1, lon1, lat2, lon2) / 1852.0;
    }

    public static double angleToTarget(double lat1, double lon1, double lat2, double lon2) {
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);

        double deltagamma = Math.toRadians(lon2 - lon1);

        double y = Math.sin(deltagamma) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) -
                Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltagamma);
        double theta = Math.atan2(y, x);

        return (Math.toDegrees(theta) + 360) % 360;
    }
}
