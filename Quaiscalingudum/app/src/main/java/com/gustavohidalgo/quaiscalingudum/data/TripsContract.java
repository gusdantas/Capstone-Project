package com.gustavohidalgo.quaiscalingudum.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hdant on 18/02/2018.
 */

public class TripsContract {

    public static final String AUTHORITY = "com.gustavohidalgo.quaiscalingudum";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TRIPS = "trips";

    public static final class TripsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRIPS).build();

        public static final String TABLE_NAME = "trips";
        public static final String ROUTE_ID = "route_id";
        public static final String SERVICE_ID = "service_id";
        public static final String TRIP_ID = "trip_id";
        public static final String TRIP_HEADSIGN = "trip_headsign";
        public static final String DIRECTION_ID = "direction_id";
        public static final String SHAPE_ID = "shape_id";
    }
}
