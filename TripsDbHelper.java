package com.gustavohidalgo.quaiscalingudum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gustavohidalgo.quaiscalingudum.data.TripsContract.*;

/**
 * Created by hdant on 18/02/2018.
 */

public class TripsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trips.db";
    private static final int DATABASE_VERSION = 1;

    public TripsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + TripsEntry.TRIPS_TABLE_NAME + " (" +
                TripsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TripsEntry.ROUTE_ID + " TEXT NOT NULL, " +
                TripsEntry.SERVICE_ID + " TEXT NOT NULL, " +
                TripsEntry.TRIP_ID + " TEXT NOT NULL, " +
                TripsEntry.TRIP_HEADSIGN + " TEXT NOT NULL, " +
                TripsEntry.DIRECTION_ID + " INTEGER NOT NULL, " +
                TripsEntry.SHAPE_ID + " INTEGER NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TripsEntry.TRIPS_TABLE_NAME);
        onCreate(db);
    }
}
