package com.mtks.finalproject.dudewheresmycar.gpslocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mtks.finalproject.dudewheresmycar.R;
import com.mtks.finalproject.dudewheresmycar.parkfind.ServerCommunication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPS extends AppCompatActivity {
    String status = "true";
    private Button bbb, statusture, statusfalse, b3, b4;
    ImageButton bb;
    private TextView t, t1;
    private LocationManager locationManager;
    private Criteria criteria;
    private LocationListener listener;
    private Location myloaction;
    private double lon;
    private double la ;
    private TextView name, pla, plo;
    String s, s1, s2;
    Geocoder geo;
    String lane;
    //placepicker to pick the parking space

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        // bb = (ImageButton) findViewById();
        t = (TextView) findViewById(R.id.tw);
        t1 = (TextView) findViewById(R.id.tw1);
        bbb = (Button) findViewById(R.id.button333);
        statusture = (Button)findViewById(R.id.yes);
        statusfalse = (Button)findViewById(R.id.no);
        name = (TextView)findViewById(R.id.Name);
        geo = new Geocoder(this, Locale.ENGLISH);
        //Get the system location service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // update the location by tracking listener
        //Locationlistener to keep tracking locationchanged
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        final Location myloaction = locationManager.getLastKnownLocation(provider);
        this.myloaction = myloaction;
        update(myloaction);



        listener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                // t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                lon = location.getLongitude();
                la = location.getLatitude();
                if (ActivityCompat.checkSelfPermission(GPS.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GPS.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;

                }
                List<Address> lsaddress =null;
                try {
                    lsaddress =   geo.getFromLocation(myloaction.getLatitude(),myloaction.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = lsaddress.get(0);//得到Address实例
                lane = address.getAddressLine(0);
                name.setText(lane);
                locationManager.requestLocationUpdates("gps", 0, 0, listener);

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

        bbb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                t.setVisibility(View.VISIBLE);
                t.setText(String.valueOf(myloaction.getLongitude()));
                t1.setText(String.valueOf(myloaction.getLatitude()));
                la=myloaction.getLatitude();
                lon=myloaction.getLongitude();
                List<Address> lsaddress =null;
                Address address = null;
                try {
                    lsaddress =   geo.getFromLocation(myloaction.getLatitude(),myloaction.getLongitude(),1);
                    address = lsaddress.get(0);//得到Address实例
                    lane = address.getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //assert lsaddress != null;
                //Address address = lsaddress.get(0);//得到Address实例
                //lane = address.getAddressLine(0);
                lane = "ThisLocation";
                name.setText(lane);
            }
        });
        statusture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                status = "false";
                Intent no = new Intent(GPS.this, ServerCommunication.class);
                no.setAction("post");
                no.putExtra("status", status);
                no.putExtra("latitude",String.valueOf(la) );//傳遞Double
                no.putExtra("longitude",String.valueOf(lon) );
                no.putExtra("locatioN_Name",lane);//傳遞String
                startActivityForResult(no,1);
            }
        });
        statusfalse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent yes = new Intent();
                yes.setClass(GPS.this, ServerCommunication.class);
                yes.setAction("post");
                yes.putExtra("status", status);
                yes.putExtra("latitude",String.valueOf(la) );//傳遞Double
                yes.putExtra("lontitude",String.valueOf(lon) );
                yes.putExtra("location_name",lane);//傳遞String
                startActivityForResult(yes,1);
            }
        });
        configure_button();

    }

    private void update(Location myloaction) {
        t.setText(String.valueOf(myloaction.getLongitude()));
        t1.setText(String.valueOf(myloaction.getLatitude()));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 0, listener);

    }

    // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
    @Override
    protected void onResume() {
        super.onResume();
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
        locationManager.requestLocationUpdates(provider, 10000, 10, listener);
    }

}
