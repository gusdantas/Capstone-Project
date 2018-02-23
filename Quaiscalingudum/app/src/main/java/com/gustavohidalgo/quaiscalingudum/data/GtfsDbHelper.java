package com.gustavohidalgo.quaiscalingudum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.gustavohidalgo.quaiscalingudum.data.FrequenciesContract.FrequenciesEntry.FREQUENCIES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.StopTimesContract.StopTimesEntry.STOP_TIMES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.TRIPS_TABLE_NAME;

/**
 * Created by gustavo.hidalgo on 18/02/23.
 */

public class GtfsDbHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "GtfsDbHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "gtfs.db";

    private static final String CREATE_TABLE_TRIPS = "CREATE TABLE " +
            TRIPS_TABLE_NAME + " (" +
            TripsContract.TripsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TripsContract.TripsEntry.ROUTE_ID + " TEXT NOT NULL, " +
            TripsContract.TripsEntry.SERVICE_ID + " TEXT NOT NULL, " +
            TripsContract.TripsEntry.TRIP_ID + " TEXT NOT NULL, " +
            TripsContract.TripsEntry.TRIP_HEADSIGN + " TEXT NOT NULL, " +
            TripsContract.TripsEntry.DIRECTION_ID + " INTEGER NOT NULL, " +
            TripsContract.TripsEntry.SHAPE_ID + " INTEGER NOT NULL" +
            "); ";

    // Tag table create statement
    private static final String CREATE_TABLE_STOP_TIMES = "CREATE TABLE " +
            STOP_TIMES_TABLE_NAME + " (" +
            StopTimesContract.StopTimesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            StopTimesContract.StopTimesEntry.TRIP_ID + " TEXT NOT NULL, " +
            StopTimesContract.StopTimesEntry.ARRIVAL_TIME + " TEXT NOT NULL, " +
            StopTimesContract.StopTimesEntry.DEPARTURE_TIME + " TEXT NOT NULL, " +
            StopTimesContract.StopTimesEntry.STOP_ID + " INTEGER NOT NULL" +
            StopTimesContract.StopTimesEntry.STOP_SEQUENCE + " INTEGER NOT NULL" +
            "); ";

    // todo_tag table create statement
    private static final String CREATE_TABLE_FREQUENCIES = "CREATE TABLE " +
            FREQUENCIES_TABLE_NAME + " (" +
            FrequenciesContract.FrequenciesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FrequenciesContract.FrequenciesEntry.TRIP_ID + " TEXT NOT NULL, " +
            FrequenciesContract.FrequenciesEntry.START_TIME + " TEXT NOT NULL, " +
            FrequenciesContract.FrequenciesEntry.END_TIME + " TEXT NOT NULL, " +
            FrequenciesContract.FrequenciesEntry.HEADWAY_SECS + " INTEGER NOT NULL" +
            "); ";

    public GtfsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TRIPS);
        db.execSQL(CREATE_TABLE_STOP_TIMES);
        db.execSQL(CREATE_TABLE_FREQUENCIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STOP_TIMES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FREQUENCIES_TABLE_NAME);

        // create new tables
        onCreate(db);
    }
}
