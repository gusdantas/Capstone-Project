package com.gustavohidalgo.quaiscalingudum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.FrequenciesEntry.FREQUENCIES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.StopTimesEntry.STOP_TIMES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.StopsEntry.STOPS_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.TripsEntry.TRIPS_TABLE_NAME;

/**
 * Created by gustavo.hidalgo on 18/02/23.
 */

public class GtfsDbHelper extends SQLiteAssetHelper {
    // Logcat tag
    private static final String LOG = "GtfsDbHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "gtfs.db";

    public GtfsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TRIPS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STOP_TIMES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FREQUENCIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STOPS_TABLE_NAME);

        // create new tables
        onCreate(db);
    }
}
