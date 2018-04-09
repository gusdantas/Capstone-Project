package com.gustavohidalgo.quaiscalingudum.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hdant on 18/02/2018.
 */

public class GtfsContract {

    public static final String AUTHORITY = "com.gustavohidalgo.quaiscalingudum";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class TripsEntry implements BaseColumns {
        public static final String PATH_TRIPS = "trips";
        public static final String TRIPS_TABLE_NAME = "trips";
        public static final Uri TRIPS_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRIPS).build();

        public static final String ROUTE_ID = "route_id";
        public static final String SERVICE_ID = "service_id";
        public static final String TRIP_ID = "trip_id";
        public static final String TRIP_HEADSIGN = "trip_headsign";
        public static final String DIRECTION_ID = "direction_id";
        public static final String SHAPE_ID = "shape_id";

        public static final String[] TRIPS_COLUMNS = {
                ROUTE_ID, SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, DIRECTION_ID, SHAPE_ID
        };
    }

    public static final class StopTimesEntry implements BaseColumns {
        public static final String PATH_STOP_TIMES = "stop_times";
        public static final String STOP_TIMES_TABLE_NAME = "stop_times";
        public static final Uri STOP_TIMES_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOP_TIMES).build();

        public static final String TRIP_ID = "trip_id";
        public static final String ARRIVAL_TIME = "arrival_time";
        public static final String DEPARTURE_TIME = "departure_time";
        public static final String STOP_ID = "stop_id";
        public static final String STOP_SEQUENCE = "stop_sequence";

        public static final String[] STOP_TIMES_COLUMNS = {
                TRIP_ID, ARRIVAL_TIME, DEPARTURE_TIME, STOP_ID, STOP_SEQUENCE
        };
    }

    public static final class FrequenciesEntry implements BaseColumns {
        public static final String PATH_FREQUENCIES = "frequencies";
        public static final String FREQUENCIES_TABLE_NAME = "frequencies";
        public static final Uri FREQUENCIES_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FREQUENCIES).build();

        public static final String TRIP_ID = "trip_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String HEADWAY_SECS = "headway_secs";

        public static final String[] FREQUENCIES_COLUMNS = {
                TRIP_ID, START_TIME, END_TIME, HEADWAY_SECS
        };
    }

    public static final class StopsEntry implements BaseColumns {
        public static final String PATH_STOPS = "stops";
        public static final String STOPS_TABLE_NAME = "stops";
        public static final Uri STOPS_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOPS).build();

        public static final String STOP_ID = "stop_id";
        public static final String STOP_NAME = "stop_name";
        public static final String STOP_DESC = "stop_desc";
        public static final String STOP_LAT = "stop_lat";
        public static final String STOP_LON = "stop_lon";

        public static final String[] STOPS_COLUMNS = {
                STOP_ID, STOP_NAME, STOP_DESC, STOP_LAT, STOP_LON
        };
    }
}
