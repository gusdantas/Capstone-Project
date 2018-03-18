package com.gustavohidalgo.quaiscalingudum.models;

/**
 * Created by hdant on 18/03/2018.
 */

public class StopTime {
    private String mTripId;
    private String mArrivalTime;
    private String mDepartureTime;
    private String mStopId;
    private String mStopSequence;

    public StopTime(String tripId, String arrivalTime, String departureTime, String stopId,
                    String stopSequence) {
        this.mTripId = tripId;
        this.mArrivalTime = arrivalTime;
        this.mDepartureTime = departureTime;
        this.mStopId = stopId;
        this.mStopSequence = stopSequence;
    }

    public String getTripId() {
        return mTripId;
    }

    public void setTripId(String tripId) {
        this.mTripId = tripId;
    }

    public String getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.mArrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.mDepartureTime = departureTime;
    }

    public String getStopId() {
        return mStopId;
    }

    public void setStopId(String stopId) {
        this.mStopId = stopId;
    }

    public String getStopSequence() {
        return mStopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.mStopSequence = stopSequence;
    }
}
