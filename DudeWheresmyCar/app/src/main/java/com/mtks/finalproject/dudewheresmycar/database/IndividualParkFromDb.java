package com.mtks.finalproject.dudewheresmycar.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mtks.finalproject.dudewheresmycar.R;

public class IndividualParkFromDb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_park_from_db);

        TextView parkView = (TextView) findViewById(R.id.parkingName);
        TextView locationView =(TextView) findViewById(R.id.locationName);
        Button back_button_5 = (Button) findViewById(R.id.back_button_5);

        String parkName = this.getIntent().getExtras().getString("ParkName");
        String locateName = this.getIntent().getExtras().getString("LocationName");

        parkView.setText(parkName);
        locationView.setText(locateName);

        back_button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

    }
}
