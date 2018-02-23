package com.gustavohidalgo.quaiscalingudum.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gustavohidalgo.quaiscalingudum.data.FrequenciesContract.FrequenciesEntry;

/**
 * Created by hdant on 18/02/2018.
 */

public class FrequenciesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "frequencies.db";
    private static final int DATABASE_VERSION = 1;

    public FrequenciesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                FrequenciesEntry.FREQUENCIES_TABLE_NAME + " (" +
                FrequenciesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FrequenciesEntry.TRIP_ID + " TEXT NOT NULL, " +
                FrequenciesEntry.START_TIME + " TEXT NOT NULL, " +
                FrequenciesEntry.END_TIME + " TEXT NOT NULL, " +
                FrequenciesEntry.HEADWAY_SECS + " INTEGER NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FrequenciesEntry.FREQUENCIES_TABLE_NAME);
        onCreate(db);
    }
}
