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
    public static final byte SUNDAY    = 0b1000000;
    public static final byte MONDAY    = 0b0100000;
    public static final byte TUESDAY   = 0b0010000;
    public static final byte WEDNESDAY = 0b0001000;
    public static final byte THURSDAY  = 0b0000100;
    public static final byte FRIDAY    = 0b0000010;
    public static final byte SATURDAY  = 0b0000001;
    private int mDaysOfWeek;
    private DateTime mDateTime;
    private String[] mLine, mStop;
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

    public ArrayList<String> getServiceIds(){
        ArrayList<String> serviceId = new ArrayList<>();
        serviceId.add("USD");

        if(mIsWeekly) {
            if ((mDaysOfWeek &= 0b0111110) > 0) {
                serviceId.add("U__");
                serviceId.add("U_D");
                serviceId.add("US_");
            }

            if ((mDaysOfWeek &= 0b0000001) > 0) {
                serviceId.add("_S_");
                serviceId.add("_SD");
                if(!serviceId.contains("US_")) {
                    serviceId.add("US_");
                }
            }

            if ((mDaysOfWeek &= 0b1000000) > 0) {
                serviceId.add("__D");
                if(!serviceId.contains("_SD")) {
                    serviceId.add("_SD");
                }
                if(!serviceId.contains("U_D")) {
                    serviceId.add("U_D");
                }
            }
        } else {
            if (mDateTime.getDayOfWeek() == DateTimeConstants.MONDAY
                    || mDateTime.getDayOfWeek() == DateTimeConstants.TUESDAY
                    || mDateTime.getDayOfWeek() == DateTimeConstants.WEDNESDAY
                    || mDateTime.getDayOfWeek() == DateTimeConstants.THURSDAY
                    || mDateTime.getDayOfWeek() == DateTimeConstants.FRIDAY){
                serviceId.add("U__");
                serviceId.add("U_D");
                serviceId.add("US_");
            } else if (mDateTime.getDayOfWeek() == DateTimeConstants.SATURDAY){
                serviceId.add("_S_");
                serviceId.add("_SD");
                serviceId.add("US_");
            } else if (mDateTime.getDayOfWeek() == DateTimeConstants.SUNDAY){
                serviceId.add("__D");
                serviceId.add("_SD");
                serviceId.add("U_D");
            }
        }

        return serviceId;
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
        return mDateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.mDateTime = dateTime;
    }

    public void setStop(String[] stop){
        this.mStop = stop;
    }

    public String[] getStop(){
        return mStop;
    }
}
