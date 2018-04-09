package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class BusNotification implements Parcelable {

    private String name;
    private int active;
    private int weekly;
    private int daysOfWeek;
    private Trip trip;
    private String departureStop;
    private String arriveStop;
    private BusDateTime departureDateTime;
    private BusDateTime arriveDateTime;
    private StopTime departureStopTime;
    private StopTime arriveStopTime;

    public BusNotification() {
    }

    public BusNotification(DataSnapshot dataSnapshot) {
        fromMap(dataSnapshot);
    }

    protected BusNotification(Parcel in) {
        name = in.readString();
        active = in.readInt();
        weekly = in.readInt();
        daysOfWeek = in.readInt();
        trip = new Trip(in.readString(), in.readString(), in.readString(), in.readString(),
                in.readString(), in.readString());
        departureStop = in.readString();
        arriveStop = in.readString();
        departureDateTime = new BusDateTime(in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                in.readInt());
        arriveDateTime = new BusDateTime(in.readInt(), in.readInt(), in.readInt(), in.readInt(),
                in.readInt());
        departureStopTime = new StopTime(in.readString(), in.readString(), in.readString(),
                in.readString(), in.readString());
        arriveStopTime = new StopTime(in.readString(), in.readString(), in.readString(),
                in.readString(), in.readString());
    }

    public static final Creator<BusNotification> CREATOR = new Creator<BusNotification>() {
        @Override
        public BusNotification createFromParcel(Parcel in) {
            return new BusNotification(in);
        }

        @Override
        public BusNotification[] newArray(int size) {
            return new BusNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(active);
        dest.writeInt(weekly);
        dest.writeInt(daysOfWeek);
        dest.writeString(trip.getRouteId());
        dest.writeString(trip.getServiceId());
        dest.writeString(trip.getTripId());
        dest.writeString(trip.getTripHeadsign());
        dest.writeString(trip.getDirectionId());
        dest.writeString(trip.getShapeId());
        dest.writeString(departureStop);
        dest.writeString(arriveStop);
        dest.writeInt(departureDateTime.getYear());
        dest.writeInt(departureDateTime.getMonthOfYear());
        dest.writeInt(departureDateTime.getDayOfMonth());
        dest.writeInt(departureDateTime.getHourOfDay());
        dest.writeInt(departureDateTime.getMinuteOfHour());
        dest.writeInt(arriveDateTime.getYear());
        dest.writeInt(arriveDateTime.getMonthOfYear());
        dest.writeInt(arriveDateTime.getDayOfMonth());
        dest.writeInt(arriveDateTime.getHourOfDay());
        dest.writeInt(arriveDateTime.getMinuteOfHour());
        dest.writeString(departureStopTime.getTripId());
        dest.writeString(departureStopTime.getArrivalTime());
        dest.writeString(departureStopTime.getDepartureTime());
        dest.writeString(departureStopTime.getStopId());
        dest.writeString(departureStopTime.getStopSequence());
        dest.writeString(arriveStopTime.getTripId());
        dest.writeString(arriveStopTime.getArrivalTime());
        dest.writeString(arriveStopTime.getDepartureTime());
        dest.writeString(arriveStopTime.getStopId());
        dest.writeString(arriveStopTime.getStopSequence());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setTrip(Trip trip){
        this.trip = trip;
    }

    public Trip getTrip(){
        return trip;
    }

    public String getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(String departureStop) {
        this.departureStop = departureStop;
    }

    public String getArriveStop() {
        return arriveStop;
    }

    public void setArriveStop(String arriveStop) {
        this.arriveStop = arriveStop;
    }

    public void setDepartureDateTime(BusDateTime departureDateTime){
        this.departureDateTime = departureDateTime;
    }

    public BusDateTime getDepartureDateTime(){
        return departureDateTime;
    }

    public void setArriveDateTime(BusDateTime arriveDateTime){
        this.arriveDateTime = arriveDateTime;
    }

    public BusDateTime getArriveDateTime(){
        return arriveDateTime;
    }

    public void setDepartureStopTime(StopTime departureStopTime){
        this.departureStopTime = departureStopTime;
    }

    public StopTime getDepartureStopTime(){
        return departureStopTime;
    }

    public void setArriveStopTime(StopTime arriveStopTime){
        this.arriveStopTime = arriveStopTime;
    }

    public StopTime getArriveStopTime(){
        return arriveStopTime;
    }



    private void fromMap(DataSnapshot dataSnapshot) {
        name = dataSnapshot.getKey();
        active = dataSnapshot.child("active").getValue(Integer.class);
        weekly = dataSnapshot.child("weekly").getValue(Integer.class);
        daysOfWeek = dataSnapshot.child("daysOfWeek").getValue(Integer.class);
        trip = dataSnapshot.child("trip").getValue(Trip.class);
        departureStop = dataSnapshot.child("departureStop").getValue(String.class);
        arriveStop = dataSnapshot.child("arriveStop").getValue(String.class);
        departureDateTime = dataSnapshot.child("departureDateTime").getValue(BusDateTime.class);
        arriveDateTime = dataSnapshot.child("arriveDateTime").getValue(BusDateTime.class);
        departureStopTime = dataSnapshot.child("departureStopTime").getValue(StopTime.class);
        arriveStopTime = dataSnapshot.child("arriveStopTime").getValue(StopTime.class);
    }

}
