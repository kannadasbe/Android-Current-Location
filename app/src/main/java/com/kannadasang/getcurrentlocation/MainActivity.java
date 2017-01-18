package com.kannadasang.getcurrentlocation;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LocationHelper locationHelper;
    Location location;

    TextView tvNPLatitude, tvNPLongitude, tvNPAccuracy, tvGPSLatitude, tvGPSLongitude, tvGPSAccuracy;
    Button btnNPLocation, btGPSLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationHelper = new LocationHelper(this);

        //Get Current location - It will return network
        // provider location. It it not exists then check
        // for gps location and return the result
        location = locationHelper.getCurrentLocation();
        if (location != null) {
            Toast.makeText(this, "Latitude : " + String.valueOf(location.getLatitude())
                            + " Longitude : " + String.valueOf(location.getLongitude()),
                    Toast.LENGTH_SHORT).show();
        }

        btnNPLocation = (Button) findViewById(R.id.btnNetworkLocation);
        btGPSLocation = (Button) findViewById(R.id.btnGPSLocation);
        tvNPLatitude = (TextView) findViewById(R.id.tvNPLatitude);
        tvNPLongitude = (TextView) findViewById(R.id.tvNPLongitude);
        tvNPAccuracy = (TextView) findViewById(R.id.tvNPAccuracy);

        tvGPSLatitude = (TextView) findViewById(R.id.tvGPSLatitude);
        tvGPSLongitude = (TextView) findViewById(R.id.tvGPSLongitude);
        tvGPSAccuracy = (TextView) findViewById(R.id.tvGPSAccuracy);

        // Getting Network provider location
        btnNPLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = locationHelper.getNetworkProviderLocation();
                if (location != null) {
                    tvNPAccuracy.setText("Accuracy : ".concat(String.valueOf(location.getAccuracy())));
                    tvNPLatitude.setText("Latitude : ".concat(String.valueOf(location.getLatitude())));
                    tvNPLongitude.setText("Longitude : ".concat(String.valueOf(location.getLongitude())));
                }
            }
        });

        // Getting GPS location
        btGPSLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = locationHelper.getGPSLocation();
                if (location != null) {
                    tvGPSAccuracy.setText("Accuracy : ".concat(String.valueOf(location.getAccuracy())));
                    tvGPSLatitude.setText("Latitude : ".concat(String.valueOf(location.getLatitude())));
                    tvGPSLongitude.setText("Longitude : ".concat(String.valueOf(location.getLongitude())));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "GPS Location information is unavailable.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationHelper.disableGPS();
    }
}
