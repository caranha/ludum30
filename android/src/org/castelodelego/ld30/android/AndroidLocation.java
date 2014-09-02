package org.castelodelego.ld30.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import org.castelodelego.ld30.LocationServer;

/**
 *
 * Uses system services to obtain some sort of GPS location from the system.
 *
 */

public class AndroidLocation implements LocationServer {

    static final int UPDATE_TIME_THRESHOLD = 200;
    static final int UPDATE_DISTANCE_THRESHOLD = 0;

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    int locationsReceived = 0;
    int locationsAccepted = 0;

    public AndroidLocation(AndroidApplication app)
    {
        locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };

        startListener();
        lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void updateLocation(Location location)
    {
        locationsReceived++;
        if (isBetterLocation(location, lastKnownLocation)) {
            lastKnownLocation = location;
            locationsAccepted++;
        }
    }

    @Override
    public double[] getLocation() {
        double[] ret = new double[2];

        if (lastKnownLocation == null) {
            ret[0] = 1;
            ret[1] = 1;
        }
        else {
            ret[0] = lastKnownLocation.getLongitude();
            ret[1] = lastKnownLocation.getLatitude();
        }

        return ret;
    }

    @Override
    public String getLocationString() {

        if (lastKnownLocation == null)
            return "No Location Info :(";

        return new StringBuilder()
                .append("Lon: ").append(String.format("%.4f", lastKnownLocation.getLongitude()))
                .append( " Lat:").append(String.format("%.4f", lastKnownLocation.getLatitude()))
                .toString();
    }

    @Override
    public void pauseService() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void restartService() {
       startListener();
    }

    @Override
    public String toString()
    {
       return "Locations Received/Accepted: " + locationsReceived+"/"+locationsAccepted + " Current Provider: "+ lastKnownLocation.getProvider() + " Accuracy: " + lastKnownLocation.getAccuracy() + " Timestamp: "+ lastKnownLocation.getElapsedRealtimeNanos();
    }

    /** Determines whether to use the new location reading
     *
     * Current Policy is very simple: reject older positions, and reject positions
     * that are too inaccurate.
     *
     * @param location the location received by the location manager
     * @param currentLocation the location we are currently using
     */
    private boolean isBetterLocation(Location location, Location currentLocation) {

        if (currentLocation == null) {
            return true;
        }

        long timeDelta = location.getElapsedRealtimeNanos()- currentLocation.getElapsedRealtimeNanos();
        double accuracyDelta = location.getAccuracy() - currentLocation.getAccuracy();

        if (timeDelta < 0)
            return false; // reject old location info

        if (accuracyDelta > 200)
            return false; // reject inaccurate location info

        return true;
    }

    private void startListener()
    {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME_THRESHOLD, UPDATE_DISTANCE_THRESHOLD, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME_THRESHOLD, UPDATE_DISTANCE_THRESHOLD, locationListener);
    }


}
