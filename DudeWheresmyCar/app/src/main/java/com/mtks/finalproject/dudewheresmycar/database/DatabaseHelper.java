package com.mtks.finalproject.dudewheresmycar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Krishna on 5/17/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DBName = "Parking.db";
    final static String TableName = "Locations";
    final static String id = "_id";
    final static String parkName = "_locationName";
    final static String location = "_location";
    private final static int CurrentVersion = 1;


    DatabaseHelper(Context context) {
        super(context, DBName, null, CurrentVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDb = "CREATE TABLE " + TableName
                + " ( " + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + parkName + " TEXT, "
                + location + " TEXT);";
        try {
            db.execSQL(createDb);
            Log.d("DatabaseHelper", "Created");
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

   /* public String addToDb(SQLiteDatabase db, String[] data) {
        String addToDb = "INSERT INTO " + TableName + " (" + parkName + ", " +
                location + ") VALUES(\'" + data[0] + "\', \'" + data[1] + "\');";
        try {
            db.execSQL(addToDb);
            return SUCCESS;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return "Could not add";
    }*/

    public void search(SQLiteDatabase db, String[] data) {

    }
}


