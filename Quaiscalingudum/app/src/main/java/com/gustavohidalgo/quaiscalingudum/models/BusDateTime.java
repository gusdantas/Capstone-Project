package com.gustavohidalgo.quaiscalingudum.models;

/**
 * Created by Gustavo on 08/04/2018.
 */

public class BusDateTime {
    private int mMinuteOfHour;
    private int mHourOfDay;
    private int mDayOfMonth;
    private int mMonthOfYear;
    private int mYear;

    public BusDateTime(){

    }

    public BusDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour){
        this.mMinuteOfHour = minuteOfHour;
        this.mHourOfDay = hourOfDay;
        this.mDayOfMonth = dayOfMonth;
        this.mMonthOfYear = monthOfYear;
        this.mYear = year;
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
