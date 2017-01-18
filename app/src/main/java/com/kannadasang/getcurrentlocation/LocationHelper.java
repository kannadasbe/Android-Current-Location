package com.kannadasang.getcurrentlocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by KannadasanG on 1/18/2017.
 */

public class LocationHelper extends Service implements LocationListener {
    private final Context mContext;


    boolean isGPSEnabled = false;


    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public LocationHelper(Context mContext) {
        this.mContext = mContext;
        // Initialize locationManager
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        getCurrentLocation();
    }

    public Location getCurrentLocation() {
        // Network provider can give location faster
        // than gps and also accurate than gps location
        // Check network provider location is there else check for gps location
        location = getNetworkProviderLocation();
        if (location == null)
            location = getGPSLocation();
        return location;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void disableGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationHelper.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(mContext, "Location change detected.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        //Toast.makeText(mContext, "Network provider is enabled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        //Toast.makeText(mContext, "Network provider is disabled.", Toast.LENGTH_SHORT).show();
    }


    public Location getNetworkProviderLocation() {
        Location loc = null;
        try {
            // check network is enabled
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // If network is disabled
            if (!isNetworkEnabled) {
                Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                // Get location from Network Provider
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            loc = locationManager.
                                    getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                    } catch (SecurityException e) {
                        // Handle exception here
                    }
                } else {
                    // Show network setting alert
                    showSettingsAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }

    public Location getGPSLocation() {
        Location loc = null;
        try {

            // check GPS is enabled
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } catch (SecurityException e) {

                }
            } else {
                // Show network setting alert
                showSettingsAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }
}
