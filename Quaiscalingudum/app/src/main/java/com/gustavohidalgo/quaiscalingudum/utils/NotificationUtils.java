package com.gustavohidalgo.quaiscalingudum.utils;

import com.gustavohidalgo.quaiscalingudum.models.Notification;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

/**
 * Created by gustavo.hidalgo on 18/03/23.
 */

public class NotificationUtils {

    public static String getServiceIds(Notification notification){
        ArrayList<String> serviceIdd = new ArrayList<>();
        serviceIdd.add("USD");
        StringBuilder serviceId = new StringBuilder();
        int daysOfWeek = notification.getDaysOfWeek();
        int year = notification.getYear();
        int monthOfYear = notification.getMonthOfYear();
        int dayOfMonth = notification.getDayOfMonth();
        int hourOfDay = notification.getHourOfDay();
        int minuteOfHour = notification.getMinuteOfHour();

        if(notification.getWeekly() == 1) {
            if ((daysOfWeek &= 0b0111110) > 0) {
                serviceIdd.add("U__");
                serviceIdd.add("U_D");
                serviceIdd.add("US_");
                serviceId.append("U");
            } else {
                serviceId.append("_");
            }

            if ((daysOfWeek &= 0b0000001) > 0) {
                serviceIdd.add("_S_");
                serviceIdd.add("_SD");
                if(!serviceIdd.contains("US_")) {
                    serviceIdd.add("US_");
                }
                serviceId.append("S");
            } else {
                serviceId.append("_");
            }

            if ((daysOfWeek &= 0b1000000) > 0) {
                serviceIdd.add("__D");
                if(!serviceIdd.contains("_SD")) {
                    serviceIdd.add("_SD");
                }
                if(!serviceIdd.contains("U_D")) {
                    serviceIdd.add("U_D");
                }
                serviceId.append("D");
            } else {
                serviceId.append("_");
            }
        } else {
            DateTime dateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay,
                    minuteOfHour);
            if (dateTime.getDayOfWeek() == DateTimeConstants.MONDAY
                    || dateTime.getDayOfWeek() == DateTimeConstants.TUESDAY
                    || dateTime.getDayOfWeek() == DateTimeConstants.WEDNESDAY
                    || dateTime.getDayOfWeek() == DateTimeConstants.THURSDAY
                    || dateTime.getDayOfWeek() == DateTimeConstants.FRIDAY){
                serviceIdd.add("U__");
                serviceIdd.add("U_D");
                serviceIdd.add("US_");
                serviceId.append("U__");
            } else if (dateTime.getDayOfWeek() == DateTimeConstants.SATURDAY){
                serviceIdd.add("_S_");
                serviceIdd.add("_SD");
                serviceIdd.add("US_");
                serviceId.append("_S_");
            } else if (dateTime.getDayOfWeek() == DateTimeConstants.SUNDAY){
                serviceIdd.add("__D");
                serviceIdd.add("_SD");
                serviceIdd.add("U_D");
                serviceId.append("__D");
            }
        }

        return serviceId.toString();
    }

    public static DateTime getDateTime(Notification notification) {
        int year = notification.getYear();
        int monthOfYear = notification.getMonthOfYear();
        int dayOfMonth = notification.getDayOfMonth();
        int hourOfDay = notification.getHourOfDay();
        int minuteOfHour = notification.getMinuteOfHour();
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour);
    }

    public static void setNewDaysOfWeek(int daysOfWeek, Notification notification) {
        int oldDaysOfWeek = notification.getDaysOfWeek();
        daysOfWeek = oldDaysOfWeek |= daysOfWeek;
        notification.setDaysOfWeek(daysOfWeek);
    }

    public static void resetDaysOfWeek(int daysOfWeek, Notification notification) {
        int oldDaysOfWeek = notification.getDaysOfWeek();
        daysOfWeek = oldDaysOfWeek &= ~daysOfWeek;
        notification.setDaysOfWeek(daysOfWeek);
    }
}
