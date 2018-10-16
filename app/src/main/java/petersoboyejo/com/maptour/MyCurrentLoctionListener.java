package petersoboyejo.com.maptour;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyCurrentLoctionListener implements LocationListener {

    public String myLocation;

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}