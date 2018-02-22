package com.gustavohidalgo.quaiscalingudum.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hdant on 18/02/2018.
 */

public class FrequenciesContract {

    public static final String AUTHORITY = "com.gustavohidalgo.quaiscalingudum";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FREQUENCIES = "frequencies";

    public static final class FrequenciesEntry implements BaseColumns {
        public static final Uri FREQUENCIES_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FREQUENCIES).build();

        public static final String FREQUENCIES_TABLE_NAME = "frequencies";
        public static final String TRIP_ID = "trip_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String HEADWAY_SECS = "headway_secs";

        public static final String[] FREQUENCIES_COLUMNS = {
                TRIP_ID, START_TIME, END_TIME, HEADWAY_SECS
        };
    }
}
