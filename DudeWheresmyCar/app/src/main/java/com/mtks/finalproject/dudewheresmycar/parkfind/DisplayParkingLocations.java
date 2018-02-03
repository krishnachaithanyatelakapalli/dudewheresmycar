package com.mtks.finalproject.dudewheresmycar.parkfind;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtks.finalproject.dudewheresmycar.R;

import java.util.Locale;

public class DisplayParkingLocations extends AppCompatActivity {

    String[] data;
    String s, s1, s2;
    Geocoder geo;
    String lane;
    String status = "true";
    private LocationManager locationManager;
    private Criteria criteria;
    private LocationListener listener;
    private Location myloaction;
    private double lon;
    private double la;
    String latitude,lontitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_parking_locations);

        TextView nameView = (TextView) findViewById(R.id.locName);
        TextView latView = (TextView) findViewById(R.id.lat);
        TextView longView = (TextView) findViewById(R.id.lon);
        Button backButton2 = (Button) findViewById(R.id.back_button_3);
        Button takeMeThere = (Button) findViewById(R.id.googlemaps);
        geo = new Geocoder(this, Locale.ENGLISH);
        //Get the system location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // update the location by tracking listener
        //Locationlistener to keep tracking locationchanged
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Location myloaction = locationManager.getLastKnownLocation(provider);
        this.myloaction = myloaction;
        update(myloaction);
        data = this.getIntent().getStringArrayExtra("data");

        nameView.setText(data[0]);
        latView.setText("Latitude: " + data[2]);
        longView.setText("Longitude: " + data[3]);
        String status = data[1];
        if (Boolean.parseBoolean(status)) {
            nameView.setBackgroundColor(Color.GREEN);
        } else {
            nameView.setBackgroundColor(Color.RED);
        }

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        takeMeThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "https://maps.google.com/maps?saddr=" + latitude + ", "
                        + lontitude + "&daddr=" + data[2] + ", " + data[3];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


    }
    private void update(Location myloaction) {
        lontitude=(String.valueOf(myloaction.getLongitude()));
        latitude=(String.valueOf(myloaction.getLatitude()));

    }
}
