package com.gustavohidalgo.quaiscalingudum.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hdant on 18/02/2018.
 */

public class StopTimesContract {

    public static final String AUTHORITY = "com.gustavohidalgo.quaiscalingudum";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STOP_TIMES = "stop_times";

    public static final class StopTimesEntry implements BaseColumns {
        public static final Uri STOP_TIMES_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STOP_TIMES).build();

        public static final String STOP_TIMES_TABLE_NAME = "stop_times";
        public static final String TRIP_ID = "trip_id";
        public static final String ARRIVAL_TIME = "arrival_time";
        public static final String DEPARTURE_TIME = "departure_time";
        public static final String STOP_ID = "stop_id";
        public static final String STOP_SEQUENCE = "stop_sequence";

        public static final String[] STOP_TIMES_COLUMNS = {
                TRIP_ID, ARRIVAL_TIME, DEPARTURE_TIME, STOP_ID, STOP_SEQUENCE
        };
    }
}
