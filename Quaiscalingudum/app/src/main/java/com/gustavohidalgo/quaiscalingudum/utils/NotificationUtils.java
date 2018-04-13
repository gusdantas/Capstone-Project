package com.gustavohidalgo.quaiscalingudum.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.gustavohidalgo.quaiscalingudum.models.BusDateTime;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.services.NotificationJobService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.ArrayList;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.FRIDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.MONDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.ONE_MINUTE;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.SATURDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.SUNDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.THURSDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.TUESDAY;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.WEDNESDAY;

/**
 * Created by gustavo.hidalgo on 18/03/23.
 */

public class NotificationUtils {

    public static String getServiceIds(BusNotification busNotification){
        ArrayList<String> serviceIdd = new ArrayList<>();
        serviceIdd.add("USD");
        StringBuilder serviceId = new StringBuilder();
        int daysOfWeek = busNotification.getDaysOfWeek();

        if(busNotification.getWeekly() == 1) {
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
            DateTime dateTime = NotificationUtils.getDateTime(busNotification.getArriveDateTime());
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

    public static DateTime getDateTime(BusDateTime busDateTime) {
        return new DateTime(busDateTime.getYear(), busDateTime.getMonthOfYear(),
                busDateTime.getDayOfMonth(), busDateTime.getHourOfDay(), busDateTime.getMinuteOfHour());
    }

    public static BusDateTime dateTimeToBus(DateTime dateTime){
        return new BusDateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
                dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
    }

    public static void setNewDaysOfWeek(int daysOfWeek, BusNotification busNotification) {
        int oldDaysOfWeek = busNotification.getDaysOfWeek();
        daysOfWeek = oldDaysOfWeek | daysOfWeek;
        busNotification.setDaysOfWeek(daysOfWeek);
    }

    public static void resetDaysOfWeek(int daysOfWeek, BusNotification busNotification) {
        int oldDaysOfWeek = busNotification.getDaysOfWeek();
        daysOfWeek = oldDaysOfWeek & ~daysOfWeek;
        busNotification.setDaysOfWeek(daysOfWeek);
    }

    public static ArrayList<Integer> secondsToAlarm(BusNotification busNotification){
        Log.i("gugu", "secondsToAlarm");
        Duration alarm;
        DateTime departure;
        DateTime arrive;
        int daysOfWeek = busNotification.getDaysOfWeek();
        if (busNotification.getWeekly() == 1){
            int today = dtConstantToBusDowConstant(DateTime.now().getDayOfWeek());
            int daysToTarget = 0;

            while ((daysOfWeek & today) == 0){
                today >>= 1;
                if (today == 0b0000000){
                    today = 0b1000000;
                }
                daysToTarget++;
            }

            departure = new DateTime(DateTime.now().getYear(),
                    DateTime.now().getMonthOfYear(),
                    DateTime.now().getDayOfMonth(),
                    busNotification.getDepartureDateTime().getHourOfDay(),
                    busNotification.getDepartureDateTime().getMinuteOfHour());
            arrive = new DateTime(DateTime.now().getYear(),
                    DateTime.now().getMonthOfYear(),
                    DateTime.now().getDayOfMonth(),
                    busNotification.getArriveDateTime().getHourOfDay(),
                    busNotification.getArriveDateTime().getMinuteOfHour());
            if(daysToTarget > 0) {
                departure.plusDays(daysToTarget);
                arrive.plusDays(daysToTarget);
            }
        } else {
            departure = getDateTime(busNotification.getDepartureDateTime());
            arrive = getDateTime(busNotification.getArriveDateTime());
        }

        Duration travelTimeRemaining = new Duration(DateTime.now(), arrive);

        if (departure.isBeforeNow()){
            if (arrive.isBeforeNow()){
                alarm = new Duration(DateTime.now(), departure.plusDays(1));
            } else {
                if (travelTimeRemaining.getStandardMinutes() < 2){
                    alarm = new Duration(DateTimeConstants.MILLIS_PER_MINUTE);
                } else {
                    Duration alarmToArrival = new Duration(departure, arrive);
                    while (alarmToArrival.getMillis() > travelTimeRemaining.getMillis()){
                        alarmToArrival = alarmToArrival.dividedBy(2);
                    }
                    alarm = new Duration(travelTimeRemaining.getMillis()-alarmToArrival.getMillis());
                }
            }
        } else {
            alarm = new Duration(DateTime.now(), departure);
        }

        ArrayList<Integer> result = new ArrayList<>();
        result.add((int) alarm.getStandardSeconds());
        result.add((int) travelTimeRemaining.getStandardSeconds());

        return result;
    }

    public static void scheduleJob(Context context, BusNotification busNotification,
                                  int secondsToAlarm){
        String msg = "scheduleJob " + String.valueOf(secondsToAlarm);
        Log.i("gugu", msg);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Gson g = new Gson();
        String json = g.toJson(busNotification);
        PersistableBundle myExtrasBundle = new PersistableBundle();
        myExtrasBundle.putString(NOTIFICATION, json);
        Bundle bundle = new Bundle(myExtrasBundle);

        Job notificationJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(NotificationJobService.class)
                // uniquely identifies the job
                .setTag(busNotification.getName())
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // one-off job
                .setRecurring(true)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(secondsToAlarm, secondsToAlarm + 30))
                .setExtras(bundle)
                .build();

        dispatcher.mustSchedule(notificationJob);
    }

    public static void deleteJob(Context context, BusNotification busNotification){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        dispatcher.cancel(busNotification.getName());
    }

    public static int dtConstantToBusDowConstant(int dateTimeConstant){
        int busDowConstant;
        switch (dateTimeConstant){
            case DateTimeConstants.SUNDAY:
                busDowConstant = SUNDAY;
                break;
            case DateTimeConstants.MONDAY:
                busDowConstant = MONDAY;
                break;
            case DateTimeConstants.TUESDAY:
                busDowConstant = TUESDAY;
                break;
            case DateTimeConstants.WEDNESDAY:
                busDowConstant = WEDNESDAY;
                break;
            case DateTimeConstants.THURSDAY:
                busDowConstant = THURSDAY;
                break;
            case DateTimeConstants.FRIDAY:
                busDowConstant = FRIDAY;
                break;
            default:
                busDowConstant = SATURDAY;
                break;
        }
        return busDowConstant;
    }

    public static int busDowConstantToDtConstant(int busDowConstant){
        int dateTimeConstant;
        switch (busDowConstant){
            case SUNDAY:
                dateTimeConstant = DateTimeConstants.SUNDAY;
                break;
            case MONDAY:
                dateTimeConstant = DateTimeConstants.MONDAY;
                break;
            case TUESDAY:
                dateTimeConstant = DateTimeConstants.TUESDAY;
                break;
            case WEDNESDAY:
                dateTimeConstant = DateTimeConstants.WEDNESDAY;
                break;
            case THURSDAY:
                dateTimeConstant = DateTimeConstants.THURSDAY;
                break;
            case FRIDAY:
                dateTimeConstant = DateTimeConstants.FRIDAY;
                break;
            default:
                dateTimeConstant = DateTimeConstants.SATURDAY;
                break;
        }
        return dateTimeConstant;
    }
}
