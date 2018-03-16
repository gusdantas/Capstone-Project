package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class Notification implements Parcelable {

    private int mDaysOfWeek;
    private int mMinuteOfHour;
    private int mHourOfDay;
    private int mDayOfMonth;
    private int mMonthOfYear;
    private int mYear;
    private String[] mLine;
    private String[] mStop;
    private boolean mIsWeekly;

    public Notification() {
    }

    protected Notification(Parcel in) {
        mDaysOfWeek = in.readInt();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDaysOfWeek);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.mDaysOfWeek |= dayOfWeek;
    }

    public void resetDayOfWeek(int dayOfWeek) {
        this.mDaysOfWeek &= ~dayOfWeek;
    }

    public int getDaysOfWeek() {
        return mDaysOfWeek;
    }

    public String getServiceIds(){
        ArrayList<String> serviceIdd = new ArrayList<>();
        serviceIdd.add("USD");
        StringBuilder serviceId = new StringBuilder();

        if(mIsWeekly) {
            if ((mDaysOfWeek &= 0b0111110) > 0) {
                serviceIdd.add("U__");
                serviceIdd.add("U_D");
                serviceIdd.add("US_");
                serviceId.append("U");
            } else {
                serviceId.append("_");
            }

            if ((mDaysOfWeek &= 0b0000001) > 0) {
                serviceIdd.add("_S_");
                serviceIdd.add("_SD");
                if(!serviceIdd.contains("US_")) {
                    serviceIdd.add("US_");
                }
                serviceId.append("S");
            } else {
                serviceId.append("_");
            }

            if ((mDaysOfWeek &= 0b1000000) > 0) {
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
            DateTime dateTime = new DateTime(mYear, mMonthOfYear, mDayOfMonth, mHourOfDay,
                    mMinuteOfHour);
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

    public void setLine(String[] line){
        this.mLine = line;
    }

    public String[] getLine(){
        return mLine;
    }

    public void setIsWeekly(boolean isWeekly){
        this.mIsWeekly = isWeekly;
    }

    public boolean isWeekly(){
        return mIsWeekly;
    }

    public DateTime getDateTime() {
        return new DateTime(mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinuteOfHour);
    }

    public void setDateTime(DateTime dateTime) {
        setYear(dateTime.getYear());
        setMonthOfYear(dateTime.getMonthOfYear());
        setDayOfMonth(dateTime.getDayOfMonth());
        setHourOfDay(dateTime.getHourOfDay());
        setMinuteOfHour(dateTime.getMinuteOfHour());
    }

    public void setStop(String[] stop){
        this.mStop = stop;
    }

    public String[] getStop(){
        return mStop;
    }

    public int getMinuteOfHour() {
        return mMinuteOfHour;
    }

    public void setMinuteOfHour(int minuteOfHour) {
        this.mMinuteOfHour = minuteOfHour;
    }

    public int getHourOfDay() {
        return mHourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.mHourOfDay = hourOfDay;
    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.mDayOfMonth = dayOfMonth;
    }

    public int getMonthOfYear() {
        return mMonthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.mMonthOfYear = monthOfYear;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }
}
