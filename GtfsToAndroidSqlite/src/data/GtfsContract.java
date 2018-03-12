package data;

public class GtfsContract {
    public static final int TABLE_CREATE = 0;
    public static final int TABLE_FILE = 1;
    public static final int TABLE_NAME = 2;
    public static final int TABLE_COLUMNS = 3;
    public static final String[][] TABLES = {/*TripsEntry.TRIPS, StopTimesEntry.STOP_TIMES,
            FrequenciesEntry.FREQUENCIES, */StopsEntry.STOPS};

    public static final class MetadataEntry {
        public static final String METADATA_TABLE_NAME = "android_metadata";

        public static final String CREATE_TABLE_METADATA = "CREATE TABLE " +
                METADATA_TABLE_NAME + " (locale TEXT DEFAULT en_US)";

    }

    public static final class TripsEntry {

        public static final String TRIPS_FILE = "trips.txt";
        public static final String TRIPS_TABLE_NAME = "trips";

        public static final String _ID = "_id";
        public static final String ROUTE_ID = "route_id";
        public static final String SERVICE_ID = "service_id";
        public static final String TRIP_ID = "trip_id";
        public static final String TRIP_HEADSIGN = "trip_headsign";
        public static final String DIRECTION_ID = "direction_id";
        public static final String SHAPE_ID = "shape_id";

//        public static final String[] TRIPS_COLUMNS = {
//                ROUTE_ID, SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, DIRECTION_ID, SHAPE_ID
//        };

        public static final String TRIPS_COLUMNS = _ID + "," +
                ROUTE_ID + "," + SERVICE_ID + "," + TRIP_ID + "," + TRIP_HEADSIGN + ","
                + DIRECTION_ID + "," + SHAPE_ID;

        public static final String TRIPS_CREATE_TABLE = "CREATE TABLE " +
                TRIPS_TABLE_NAME + " (" +
                TripsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TripsEntry.ROUTE_ID + " TEXT NOT NULL, " +
                TripsEntry.SERVICE_ID + " TEXT NOT NULL, " +
                TripsEntry.TRIP_ID + " TEXT NOT NULL, " +
                TripsEntry.TRIP_HEADSIGN + " TEXT NOT NULL, " +
                TripsEntry.DIRECTION_ID + " TEXT NOT NULL, " +
                TripsEntry.SHAPE_ID + " TEXT NOT NULL" +
                "); ";

        public static final String[] TRIPS = {TRIPS_CREATE_TABLE, TRIPS_FILE, TRIPS_TABLE_NAME,
                TRIPS_COLUMNS};
    }

    public static final class StopTimesEntry {

        public static final String STOP_TIMES_FILE = "stop_times.txt";
        public static final String STOP_TIMES_TABLE_NAME = "stop_times";

        public static final String _ID = "_id";
        public static final String TRIP_ID = "trip_id";
        public static final String ARRIVAL_TIME = "arrival_time";
        public static final String DEPARTURE_TIME = "departure_time";
        public static final String STOP_ID = "stop_id";
        public static final String STOP_SEQUENCE = "stop_sequence";

//        public static final String[] STOP_TIMES_COLUMNS = {
//                TRIP_ID, ARRIVAL_TIME, DEPARTURE_TIME, STOP_ID, STOP_SEQUENCE
//        };

        public static final String STOP_TIMES_COLUMNS = _ID + "," +
                TRIP_ID + "," + ARRIVAL_TIME + "," + DEPARTURE_TIME + "," + STOP_ID + ","
                        + STOP_SEQUENCE;

        public static final String STOP_TIMES_CREATE_TABLE = "CREATE TABLE " +
                STOP_TIMES_TABLE_NAME + " (" +
                StopTimesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StopTimesEntry.TRIP_ID + " TEXT NOT NULL, " +
                StopTimesEntry.ARRIVAL_TIME + " TEXT NOT NULL, " +
                StopTimesEntry.DEPARTURE_TIME + " TEXT NOT NULL, " +
                StopTimesEntry.STOP_ID + " TEXT NOT NULL, " +
                StopTimesEntry.STOP_SEQUENCE + " TEXT NOT NULL" +
                "); ";

        public static final String[] STOP_TIMES = {STOP_TIMES_CREATE_TABLE, STOP_TIMES_FILE,
                STOP_TIMES_TABLE_NAME, STOP_TIMES_COLUMNS};
    }

    public static final class FrequenciesEntry {

        public static final String FREQUENCIES_FILE = "frequencies.txt";
        public static final String FREQUENCIES_TABLE_NAME = "frequencies";

        public static final String _ID = "_id";
        public static final String TRIP_ID = "trip_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String HEADWAY_SECS = "headway_secs";

//        public static final String[] FREQUENCIES_COLUMNS = {
//                TRIP_ID, START_TIME, END_TIME, HEADWAY_SECS
//        };

        public static final String FREQUENCIES_COLUMNS = _ID + "," +
                TRIP_ID + "," + START_TIME + "," + END_TIME + "," + HEADWAY_SECS;

        public static final String FREQUENCIES_CREATE_TABLE = "CREATE TABLE " +
                FREQUENCIES_TABLE_NAME + " (" +
                FrequenciesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FrequenciesEntry.TRIP_ID + " TEXT NOT NULL, " +
                FrequenciesEntry.START_TIME + " TEXT NOT NULL, " +
                FrequenciesEntry.END_TIME + " TEXT NOT NULL, " +
                FrequenciesEntry.HEADWAY_SECS + " TEXT NOT NULL" +
                "); ";

        public static final String[] FREQUENCIES = {FREQUENCIES_CREATE_TABLE, FREQUENCIES_FILE,
                FREQUENCIES_TABLE_NAME, FREQUENCIES_COLUMNS};
    }

    public static final class StopsEntry {

        public static final String STOPS_FILE = "stops.txt";
        public static final String STOPS_TABLE_NAME = "stops";

        public static final String _ID = "_id";
        public static final String STOP_ID = "stop_id";
        public static final String STOP_NAME = "stop_name";
        public static final String STOP_DESC = "stop_desc";
        public static final String STOP_LAT = "stop_lat";
        public static final String STOP_LON = "stop_lon";

//        public static final String[] FREQUENCIES_COLUMNS = {
//                TRIP_ID, START_TIME, END_TIME, HEADWAY_SECS
//        };

        public static final String STOPS_COLUMNS = _ID + "," +
                STOP_ID + "," + STOP_NAME + "," + STOP_DESC + "," + STOP_LAT + "," + STOP_LON;

        public static final String STOPS_CREATE_TABLE = "CREATE TABLE " +
                STOPS_TABLE_NAME + " (" +
                StopsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StopsEntry.STOP_ID + " TEXT NOT NULL, " +
                StopsEntry.STOP_NAME + " TEXT NOT NULL, " +
                StopsEntry.STOP_DESC + " TEXT NOT NULL, " +
                StopsEntry.STOP_LAT + " TEXT NOT NULL, " +
                StopsEntry.STOP_LON + " TEXT NOT NULL" +
                "); ";

        public static final String[] STOPS = {STOPS_CREATE_TABLE, STOPS_FILE,
                STOPS_TABLE_NAME, STOPS_COLUMNS};
    }
}
