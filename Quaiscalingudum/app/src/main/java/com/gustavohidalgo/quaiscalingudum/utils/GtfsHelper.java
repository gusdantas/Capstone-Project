package com.gustavohidalgo.quaiscalingudum.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.gustavohidalgo.quaiscalingudum.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.FrequenciesEntry.FREQUENCIES_COLUMNS;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.FrequenciesEntry.FREQUENCIES_CONTENT_URI;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.StopTimesEntry.STOP_TIMES_COLUMNS;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.StopTimesEntry.STOP_TIMES_CONTENT_URI;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.TripsEntry.TRIPS_COLUMNS;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.TripsEntry.TRIPS_CONTENT_URI;

/**
 * Created by gustavo.hidalgo on 18/02/20.
 */

public final class GtfsHelper {

    public static void buildFrequencies(Context context){
        buildDb(context, R.raw.frequencies, FREQUENCIES_COLUMNS, FREQUENCIES_CONTENT_URI);
    }

    public static void buildRoutes(Context context){
        buildDb(context, R.raw.trips, TRIPS_COLUMNS, TRIPS_CONTENT_URI);
    }

    public static void buildStopTimes(Context context){
        buildDb(context, R.raw.stop_times, STOP_TIMES_COLUMNS, STOP_TIMES_CONTENT_URI);
    }

    public static void buildStops(Context context){
        buildDb(context, R.raw.trips, TRIPS_COLUMNS, TRIPS_CONTENT_URI);
    }

    public static void buildTrips(Context context){
        buildDb(context, R.raw.trips, TRIPS_COLUMNS, TRIPS_CONTENT_URI);
    }

    private static void buildDb(Context context, int resource, String[] columns, Uri uri){
        ContentValues contentValues = new ContentValues();

        InputStream inputStream = context.getResources().openRawResource(resource);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] value = line.split(",");
                for (int i = 0; i < columns.length; i++) {
                    contentValues.put(columns[i], value[i].replaceAll("\"", ""));
                }
                context.getContentResolver().insert(uri, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
