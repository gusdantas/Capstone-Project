package com.gustavohidalgo.quaiscalingudum.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gustavo.hidalgo on 18/02/06.
 */

public class Notification implements Parcelable {
    private int mDaysOfWeek;

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

    //TODO: setDaysOfWeek
    public void setDaysOfWeek(int daysOfWeek) {
        this.mDaysOfWeek = mDaysOfWeek & daysOfWeek;
    }

    public int getDaysOfWeek() {
        return mDaysOfWeek;
    }
}
