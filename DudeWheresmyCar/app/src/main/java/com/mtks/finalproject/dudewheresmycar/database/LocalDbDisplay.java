package com.mtks.finalproject.dudewheresmycar.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mtks.finalproject.dudewheresmycar.R;

import java.util.Objects;

import static com.mtks.finalproject.dudewheresmycar.database.DatabaseHelper.TableName;
import static com.mtks.finalproject.dudewheresmycar.database.DatabaseHelper.location;
import static com.mtks.finalproject.dudewheresmycar.database.DatabaseHelper.parkName;

public class LocalDbDisplay extends AppCompatActivity {

    private final static String SUCCESS = "Successfully added";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_db_display);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ListView dbListView = (ListView) findViewById(R.id.db_list_view);
        Button back_button4 = (Button) findViewById(R.id.back_button_4);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        };

        String currentAction = this.getIntent().getAction();

        if (Objects.equals(currentAction, "SaveInDb")) {
            Log.d("Database", "Adding to Database");
            String[] data = new String[2];
            data[0] = this.getIntent().getExtras().getString("LocationName");
            data[1] = this.getIntent().getExtras().getString("Location");
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            String addToDb = "INSERT INTO " + TableName + " (" + parkName + ", " +
                    location + ") VALUES(\'" + data[0] + "\', \'" + data[1] + "\');";
            try {
                db.execSQL(addToDb);
                Toast.makeText(LocalDbDisplay.this, SUCCESS, Toast.LENGTH_SHORT).show();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            setResult(1);
            finish();
        }


        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Log.d("Database", "Displaying Database");
        String[] projections = {
                parkName,
                location,
                DatabaseHelper.id
        };

        String[] columns = {
                DatabaseHelper.id,
                parkName,
                location
        };

        int[] toView = {
                R.id.single_list_db_view,
                R.id.locationView
        };

        final Cursor cursor = db.query(TableName, columns, null, null, null, null, null);

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                LocalDbDisplay.this,
                R.layout.activity_single_list_db_layout,
                cursor,
                projections,
                toView,
                0
        );

        if (cursor.getCount() != 0) {
            dbListView.setAdapter(simpleCursorAdapter);
            dbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor.moveToPosition(position);
                    Intent intent = new Intent(LocalDbDisplay.this, IndividualParkFromDb.class);
                    intent.putExtra("ParkName", cursor.getString(1));
                    intent.putExtra("ParkLocation", cursor.getString(2));
                    startActivityForResult(intent, 1);
                }
            });

            /*back_button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(1);
                    finish();
                }
            });*/
            back_button4.setOnClickListener(onClickListener);

        } else {
            //cursor.close();
            Toast.makeText(this, "No Locations Saved", Toast.LENGTH_LONG).show();

            /*back_button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(1);
                    finish();
                }
            });*/
            //back_button4.setOnClickListener(onClickListener);

        }
        back_button4.setOnClickListener(onClickListener);

    }
}
