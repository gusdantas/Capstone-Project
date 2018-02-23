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

import static com.gustavohidalgo.quaiscalingudum.data.FrequenciesContract.FrequenciesEntry.*;

/**
 * Created by hdant on 18/02/2018.
 */

public class FrequenciesContentProvider extends ContentProvider {

    private FrequenciesDbHelper mFrequenciesDbHelper;

    public static final int FREQUENCIES = 300;
    public static final int FREQUENCIES_WITH_ID = 301;

    private static final UriMatcher sFrequenciesUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFrequenciesDbHelper = new FrequenciesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFrequenciesDbHelper.getReadableDatabase();
        int match = sFrequenciesUriMatcher.match(uri);
        Cursor retCursor;
            switch (match) {
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
        final SQLiteDatabase db = mFrequenciesDbHelper.getWritableDatabase();
        int match = sFrequenciesUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case FREQUENCIES:
                long id = db.insert(FREQUENCIES_TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FREQUENCIES_CONTENT_URI, id);
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
        final SQLiteDatabase db = mFrequenciesDbHelper.getWritableDatabase();

        int match = sFrequenciesUriMatcher.match(uri);
        int frequenciesDeleted; // starts as 0

        switch (match) {
            case FREQUENCIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                frequenciesDeleted = db.delete(FREQUENCIES_TABLE_NAME, "_id=?", new String[]{id});
            break;
        default:
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (frequenciesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return frequenciesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FrequenciesContract.AUTHORITY, FrequenciesContract.PATH_FREQUENCIES,
                FREQUENCIES);
        uriMatcher.addURI(FrequenciesContract.AUTHORITY,
                FrequenciesContract.PATH_FREQUENCIES + "/#", FREQUENCIES_WITH_ID);
        return uriMatcher;
    }
}
