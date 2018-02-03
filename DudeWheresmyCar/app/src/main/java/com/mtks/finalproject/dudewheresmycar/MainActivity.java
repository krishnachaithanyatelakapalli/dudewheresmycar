package com.mtks.finalproject.dudewheresmycar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.mtks.finalproject.dudewheresmycar.database.LocalDbDisplay;
import com.mtks.finalproject.dudewheresmycar.gpslocation.GPS;
import com.mtks.finalproject.dudewheresmycar.parkfind.ServerCommunication;

public class MainActivity extends AppCompatActivity {

    ImageButton arrive, leave, car, history;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener imageButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.arrive:
                        Intent intentar = new Intent(MainActivity.this, GPS.class);
                        startActivity(intentar);
                        break;

                    case R.id.leave:
                        Intent serverPutIntent = new Intent(MainActivity.this, ServerCommunication.class);
                        serverPutIntent.setAction("put");
                        serverPutIntent.putExtra("Location_Name", "IHOP");
                        serverPutIntent.putExtra("Status", "true");
                        startActivityForResult(serverPutIntent, 1);
                        break;

                    case R.id.findPark:
                        Intent serverPostIntent = new Intent(MainActivity.this, ServerCommunication.class);
                        serverPostIntent.setAction("get");
                        startActivityForResult(serverPostIntent, 1);
                        break;

                    case R.id.history:
                        Intent historyIntent = new Intent(MainActivity.this, LocalDbDisplay.class);
                        historyIntent.setAction("DisplayDB");
                        startActivityForResult(historyIntent, 1);
                        break;
                }
            }
        };

        arrive = (ImageButton) findViewById(R.id.arrive);
        leave = (ImageButton) findViewById(R.id.leave);
        car = (ImageButton) findViewById(R.id.findPark);
        history = (ImageButton) findViewById(R.id.history);

        arrive.setOnClickListener(imageButtonOnClickListener);
        leave.setOnClickListener(imageButtonOnClickListener);
        car.setOnClickListener(imageButtonOnClickListener);
        history.setOnClickListener(imageButtonOnClickListener);

    }
}
