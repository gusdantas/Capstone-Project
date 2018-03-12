package com.gustavohidalgo.quaiscalingudum.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.FrequenciesEntry.FREQUENCIES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.StopTimesEntry.STOP_TIMES_TABLE_NAME;
import static com.gustavohidalgo.quaiscalingudum.data.GtfsContract.TripsEntry.TRIPS_TABLE_NAME;

/**
 * Created by gustavo.hidalgo on 18/02/23.
 */

public class GtfsContentProvider extends ContentProvider {
    private GtfsDbHelper mGtfsDbHelper;

    public static final int TRIPS = 100;
    public static final int TRIPS_WITH_ID = 101;
    public static final int STOP_TIMES = 200;
    public static final int STOP_TIMES_WITH_ID = 201;
    public static final int FREQUENCIES = 300;
    public static final int FREQUENCIES_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mGtfsDbHelper = new GtfsDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mGtfsDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case TRIPS:
                retCursor =  db.query(TRIPS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STOP_TIMES:
                retCursor =  db.query(STOP_TIMES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FREQUENCIES:
                retCursor =  db.query(FREQUENCIES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mGtfsDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case TRIPS:
                long tripsId = db.insert(TRIPS_TABLE_NAME, null, values);
                if ( tripsId > 0 ) {
                    returnUri = ContentUris.withAppendedId(
                            GtfsContract.TripsEntry.TRIPS_CONTENT_URI, tripsId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case STOP_TIMES:
                long stopTimesId = db.insert(STOP_TIMES_TABLE_NAME, null, values);
                if ( stopTimesId > 0 ) {
                    returnUri = ContentUris.withAppendedId(
                            GtfsContract.StopTimesEntry.STOP_TIMES_CONTENT_URI, stopTimesId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case FREQUENCIES:
                long frequenciesId = db.insert(FREQUENCIES_TABLE_NAME, null, values);
                if ( frequenciesId > 0 ) {
                    returnUri = ContentUris.withAppendedId(
                            GtfsContract.FrequenciesEntry.FREQUENCIES_CONTENT_URI, frequenciesId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mGtfsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int itemDeleted; // starts as 0

        String id = uri.getPathSegments().get(1);
        switch (match) {
            case TRIPS_WITH_ID:
                itemDeleted = db.delete(TRIPS_TABLE_NAME, "_id=?", new String[]{id});
                break;
            case STOP_TIMES_WITH_ID:
                itemDeleted = db.delete(STOP_TIMES_TABLE_NAME, "_id=?", new String[]{id});
                break;
            case FREQUENCIES_WITH_ID:
                itemDeleted = db.delete(FREQUENCIES_TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (itemDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return itemDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.TripsEntry.PATH_TRIPS, TRIPS);
        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.TripsEntry.PATH_TRIPS + "/#", TRIPS_WITH_ID);
        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.StopTimesEntry.PATH_STOP_TIMES, STOP_TIMES);
        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.StopTimesEntry.PATH_STOP_TIMES + "/#", STOP_TIMES_WITH_ID);
        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.FrequenciesEntry.PATH_FREQUENCIES, FREQUENCIES);
        uriMatcher.addURI(GtfsContract.AUTHORITY,
                GtfsContract.FrequenciesEntry.PATH_FREQUENCIES + "/#", FREQUENCIES_WITH_ID);
        return uriMatcher;
    }
}
