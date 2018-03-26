package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class Notification implements Parcelable {

    private String mName;
    private boolean mIsActive;
    private boolean mIsWeekly;
    private int mDaysOfWeek;
    private int mMinuteOfHour;
    private int mHourOfDay;
    private int mDayOfMonth;
    private int mMonthOfYear;
    private int mYear;
    private Trip mTrip;
    private StopTime mStopTime;

    public Notification() {
    }

    public Notification(DataSnapshot dataSnapshot) {
        fromMap(dataSnapshot);
    }

    protected Notification(Parcel in) {
        mName = in.readString();
        mIsActive = in.readByte() != 0;
        mIsWeekly = in.readByte() != 0;
        mDaysOfWeek = in.readInt();
        mMinuteOfHour = in.readInt();
        mHourOfDay = in.readInt();
        mDayOfMonth = in.readInt();
        mMonthOfYear = in.readInt();
        mYear = in.readInt();
        mTrip = new Trip(in.readString(), in.readString(), in.readString(), in.readString(),
                in.readString(), in.readString());
        mStopTime = new StopTime(in.readString(), in.readString(), in.readString(), in.readString(),
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
        dest.writeString(mName);
        dest.writeByte((byte) (mIsActive ? 1 : 0 ));
        dest.writeByte((byte) (mIsWeekly ? 1 : 0 ));
        dest.writeInt(mDaysOfWeek);
        dest.writeInt(mMinuteOfHour);
        dest.writeInt(mHourOfDay);
        dest.writeInt(mDayOfMonth);
        dest.writeInt(mMonthOfYear);
        dest.writeInt(mYear);
        dest.writeString(mTrip.getRouteId());
        dest.writeString(mTrip.getServiceId());
        dest.writeString(mTrip.getTripId());
        dest.writeString(mTrip.getTripHeadsign());
        dest.writeString(mTrip.getDirectionId());
        dest.writeString(mTrip.getShapeId());
        dest.writeString(mStopTime.getTripId());
        dest.writeString(mStopTime.getArrivalTime());
        dest.writeString(mStopTime.getDepartureTime());
        dest.writeString(mStopTime.getStopId());
        dest.writeString(mStopTime.getStopSequence());
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

    public void setIsActive(boolean isActive){
        this.mIsActive = isActive;
    }

    public boolean isActive(){
        return mIsActive;
    }

    public void setIsWeekly(boolean isWeekly){
        this.mIsWeekly = isWeekly;
    }

    public boolean isWeekly(){
        return mIsWeekly;
    }

    public void setDateTime(DateTime dateTime) {
        setYear(dateTime.getYear());
        setMonthOfYear(dateTime.getMonthOfYear());
        setDayOfMonth(dateTime.getDayOfMonth());
        setHourOfDay(dateTime.getHourOfDay());
        setMinuteOfHour(dateTime.getMinuteOfHour());
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

    public void setTrip(Trip trip){
        this.mTrip = trip;
    }

    public Trip getTrip(){
        return mTrip;
    }

    public void setStopTime(StopTime stopTime){
        this.mStopTime = stopTime;
    }

    public StopTime getStopTime(){
        return mStopTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    private void fromMap(DataSnapshot dataSnapshot) {
        mName = dataSnapshot.getKey();
        mIsActive = dataSnapshot.child("mIsActive").getValue(Boolean.class);
        mIsWeekly = dataSnapshot.child("mIsWeekly").getValue(Boolean.class);
        mDaysOfWeek = dataSnapshot.child("daysOfWeek").getValue(Integer.class);
        mMinuteOfHour = dataSnapshot.child("mMinuteOfHour").getValue(Integer.class);
        mHourOfDay = dataSnapshot.child("mHourOfDay").getValue(Integer.class);
        mDayOfMonth = dataSnapshot.child("mDayOfMonth").getValue(Integer.class);
        mMonthOfYear = dataSnapshot.child("mMonthOfYear").getValue(Integer.class);
        mYear = dataSnapshot.child("mYear").getValue(Integer.class);
        mTrip = dataSnapshot.child("mTrip").getValue(Trip.class);
        mStopTime = dataSnapshot.child("mStopTime").getValue(StopTime.class);
    }

}
