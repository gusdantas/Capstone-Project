package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;

import java.util.HashMap;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class Notification implements Parcelable {

    private String name;
//    private Boolean active;
//    private Boolean weekly;
    private int active;
    private int weekly;
    private int daysOfWeek;
    private int minuteOfHour;
    private int hourOfDay;
    private int dayOfMonth;
    private int monthOfYear;
    private int year;
    private Trip trip;
    private StopTime stopTime;

    public Notification() {
    }

    public Notification(DataSnapshot dataSnapshot) {
        fromMap(dataSnapshot);
    }

    protected Notification(Parcel in) {
        name = in.readString();
//        active = in.readByte() != 0;
//        weekly = in.readByte() != 0;
        active = in.readInt();
        weekly = in.readInt();
        daysOfWeek = in.readInt();
        minuteOfHour = in.readInt();
        hourOfDay = in.readInt();
        dayOfMonth = in.readInt();
        monthOfYear = in.readInt();
        year = in.readInt();
        trip = new Trip(in.readString(), in.readString(), in.readString(), in.readString(),
                in.readString(), in.readString());
        stopTime = new StopTime(in.readString(), in.readString(), in.readString(), in.readString(),
                in.readString());
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
        dest.writeString(name);
//        dest.writeByte((byte) (active ? 1 : 0 ));
//        dest.writeByte((byte) (weekly ? 1 : 0 ));
        dest.writeInt(active);
        dest.writeInt(weekly);
        dest.writeInt(daysOfWeek);
        dest.writeInt(minuteOfHour);
        dest.writeInt(hourOfDay);
        dest.writeInt(dayOfMonth);
        dest.writeInt(monthOfYear);
        dest.writeInt(year);
        dest.writeString(trip.getRouteId());
        dest.writeString(trip.getServiceId());
        dest.writeString(trip.getTripId());
        dest.writeString(trip.getTripHeadsign());
        dest.writeString(trip.getDirectionId());
        dest.writeString(trip.getShapeId());
        dest.writeString(stopTime.getTripId());
        dest.writeString(stopTime.getArrivalTime());
        dest.writeString(stopTime.getDepartureTime());
        dest.writeString(stopTime.getStopId());
        dest.writeString(stopTime.getStopSequence());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Boolean getActive(){
//        return active;
//    }
//
//    public void setActive(Boolean active){
//        this.active = active;
//    }
//
//    public Boolean getWeekly(){
//        return weekly;
//    }
//
//    public void setWeekly(Boolean weekly){
//        this.weekly = weekly;
//    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getWeekly() {
        return weekly;
    }

    public void setWeekly(int weekly) {
        this.weekly = weekly;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setDateTime(DateTime dateTime) {
        setYear(dateTime.getYear());
        setMonthOfYear(dateTime.getMonthOfYear());
        setDayOfMonth(dateTime.getDayOfMonth());
        setHourOfDay(dateTime.getHourOfDay());
        setMinuteOfHour(dateTime.getMinuteOfHour());
    }

    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    public void setMinuteOfHour(int minuteOfHour) {
        this.minuteOfHour = minuteOfHour;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setTrip(Trip trip){
        this.trip = trip;
    }

    public Trip getTrip(){
        return trip;
    }

    public void setStopTime(StopTime stopTime){
        this.stopTime = stopTime;
    }

    public StopTime getStopTime(){
        return stopTime;
    }



    private void fromMap(DataSnapshot dataSnapshot) {
        name = dataSnapshot.getKey();
//        active = dataSnapshot.child("active").getValue(Boolean.class);
//        weekly = dataSnapshot.child("weekly").getValue(Boolean.class);
        active = dataSnapshot.child("active").getValue(Integer.class);
        weekly = dataSnapshot.child("weekly").getValue(Integer.class);
        daysOfWeek = dataSnapshot.child("daysOfWeek").getValue(Integer.class);
        minuteOfHour = dataSnapshot.child("minuteOfHour").getValue(Integer.class);
        hourOfDay = dataSnapshot.child("hourOfDay").getValue(Integer.class);
        dayOfMonth = dataSnapshot.child("dayOfMonth").getValue(Integer.class);
        monthOfYear = dataSnapshot.child("monthOfYear").getValue(Integer.class);
        year = dataSnapshot.child("year").getValue(Integer.class);
        trip = dataSnapshot.child("trip").getValue(Trip.class);
        stopTime = dataSnapshot.child("stopTime").getValue(StopTime.class);
    }

}
