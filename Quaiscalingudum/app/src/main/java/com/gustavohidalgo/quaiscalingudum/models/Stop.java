package com.gustavohidalgo.quaiscalingudum.models;

/**
 * Created by hdant on 18/03/2018.
 */

public class Stop {
    private String mStopId;
    private String mStopName;
    private String mStopDesc;
    private String mStopLat;
    private String mStopLon;

    public Stop(){

    }

    public Stop(String mStopId, String mStopName, String mStopDesc, String mStopLat, String mStopLon) {
        this.mStopId = mStopId;
        this.mStopName = mStopName;
        this.mStopDesc = mStopDesc;
        this.mStopLat = mStopLat;
        this.mStopLon = mStopLon;
    }

    public String getStopId() {
        return mStopId;
    }

    public void setStopId(String stopId) {
        this.mStopId = stopId;
    }

    public String getStopName() {
        return mStopName;
    }

    public void setStopName(String stopName) {
        this.mStopName = stopName;
    }

    public String getStopDesc() {
        return mStopDesc;
    }

    public void setStopDesc(String stopDesc) {
        this.mStopDesc = stopDesc;
    }

    public String getStopLat() {
        return mStopLat;
    }

    public void setStopLat(String stopLat) {
        this.mStopLat = stopLat;
    }

    public String getStopLon() {
        return mStopLon;
    }

    public void setStopLon(String stopLon) {
        this.mStopLon = stopLon;
    }
}
