package com.gustavohidalgo.quaiscalingudum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gustavohidalgo.quaiscalingudum.data.StopTimesContract.StopTimesEntry;

/**
 * Created by hdant on 18/02/2018.
 */

public class StopTimesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stop_times.db";
    private static final int DATABASE_VERSION = 1;

    public StopTimesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                StopTimesEntry.STOP_TIMES_TABLE_NAME + " (" +
                StopTimesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StopTimesEntry.TRIP_ID + " TEXT NOT NULL, " +
                StopTimesEntry.ARRIVAL_TIME + " TEXT NOT NULL, " +
                StopTimesEntry.DEPARTURE_TIME + " TEXT NOT NULL, " +
                StopTimesEntry.STOP_ID + " INTEGER NOT NULL" +
                StopTimesEntry.STOP_SEQUENCE + " INTEGER NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StopTimesEntry.STOP_TIMES_TABLE_NAME);
        onCreate(db);
    }
}
