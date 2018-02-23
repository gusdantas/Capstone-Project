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

import static com.gustavohidalgo.quaiscalingudum.data.TripsContract.TripsEntry.TRIPS_TABLE_NAME;

/**
 * Created by hdant on 18/02/2018.
 */

public class TripsContentProvider extends ContentProvider {

    private TripsDbHelper mTripsDbHelper;

    public static final int TRIPS = 100;
    public static final int TRIPS_WITH_ID = 101;

    private static final UriMatcher sTripsUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTripsDbHelper = new TripsDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mTripsDbHelper.getReadableDatabase();
        int match = sTripsUriMatcher.match(uri);
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
        final SQLiteDatabase db = mTripsDbHelper.getWritableDatabase();
        int match = sTripsUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case TRIPS:
                long id = db.insert(TRIPS_TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(TripsContract.TripsEntry.TRIPS_CONTENT_URI, id);
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
        final SQLiteDatabase db = mTripsDbHelper.getWritableDatabase();

        int match = sTripsUriMatcher.match(uri);
        int tripsDeleted; // starts as 0

        switch (match) {
            case TRIPS_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tripsDeleted = db.delete(TRIPS_TABLE_NAME, "_id=?", new String[]{id});
            break;
        default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tripsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tripsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TripsContract.AUTHORITY, TripsContract.PATH_TRIPS, TRIPS);
        uriMatcher.addURI(TripsContract.AUTHORITY,
                TripsContract.PATH_TRIPS + "/#", TRIPS_WITH_ID);
        return uriMatcher;
    }
}
