package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class Notification implements Parcelable {
    public static final byte SUNDAY    = 0b1000000;
    public static final byte MONDAY    = 0b0100000;
    public static final byte TUESDAY   = 0b0010000;
    public static final byte WEDNESDAY = 0b0001000;
    public static final byte THURSDAY  = 0b0000100;
    public static final byte FRIDAY    = 0b0000010;
    public static final byte SATURDAY  = 0b0000001;
    private int mDaysOfWeek, mHour, mMinute;
    private String[] mLine;

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

    public void setHour(int hour) {
        this.mHour = hour;
    }

    public int getHour() {
        return mHour;
    }

    public void setMinute(int minute) {
        this.mMinute = minute;
    }

    public int getMinute() {
        return mMinute;
    }

    public String getServiceId(){
        StringBuilder serviceId = new StringBuilder();
        if ((mDaysOfWeek &= 0b0111110) > 0){
            serviceId.append("U");
        } else {
            serviceId.append("_");
        }

        if ((mDaysOfWeek &= 0b0000001) > 0){
            serviceId.append("S");
        } else {
            serviceId.append("_");
        }

        if ((mDaysOfWeek &= 0b1000000) > 0) {
            serviceId.append("D");
        } else {
            serviceId.append("_");
        }

        return serviceId.toString();
    }

    public void setLine(String[] line){
        this.mLine = line;
    }

    public String[] getLine(){
        return mLine;
    }
}
