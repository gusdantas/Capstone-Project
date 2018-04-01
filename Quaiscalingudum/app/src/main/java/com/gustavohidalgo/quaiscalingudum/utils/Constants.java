package com.gustavohidalgo.quaiscalingudum.utils;

/**
 * Created by gustavo.hidalgo on 18/03/16.
 */

public final class Constants {
    public static final String NOTIFICATION = "notification";
    public static final String OLD_NOTIFICATION = "old_notification";
    public static final String NOTIFICATION_LIST = "notification_list";
    public static final String ETA = "eta";
    public static final String PICK_LINE = "pick_line";
    public static final String DETAILS = "details";
    public static final String SET_NOTIFICATIONS = "set_notifications";

    public static final byte SUNDAY    = 0b1000000;
    public static final byte MONDAY    = 0b0100000;
    public static final byte TUESDAY   = 0b0010000;
    public static final byte WEDNESDAY = 0b0001000;
    public static final byte THURSDAY  = 0b0000100;
    public static final byte FRIDAY    = 0b0000010;
    public static final byte SATURDAY  = 0b0000001;

    public static final int NOT_ACTIVE = 0;
    public static final int IS_ACTIVE = 1;
    public static final int NOT_WEEKLY = 0;
    public static final int IS_WEEKLY = 1;

    public static final int HOUR = 0;
    public static final int MINUTE = 1;
    public static final int SECOND = 2;

    public static final int TRIPS__ID = 0;
    public static final int TRIPS_ROUTE_ID = 1;
    public static final int TRIPS_SERVICE_ID = 2;
    public static final int TRIPS_TRIP_ID = 3;
    public static final int TRIPS_TRIP_HEADSIGN = 4;
    public static final int TRIPS_TRIP_DIRECTION_ID = 5;
    public static final int TRIPS_TRIP_SHAPE_ID = 6;

    public static final int STOP_TIMES__ID = 0;
    public static final int STOP_TIMES_TRIP_ID = 1;
    public static final int STOP_TIMES_ARRIVAL_TIME = 2;
    public static final int STOP_TIMES_DEPARTURE_TIME = 3;
    public static final int STOP_TIMES_STOP_ID = 4;
    public static final int STOP_TIMES_STOP_SEQUENCE = 5;

    public static final int STOPS_STOP_ID = 1;
    public static final int STOPS_STOP_NAME = 2;


}
