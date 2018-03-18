package com.gustavohidalgo.quaiscalingudum.models;

/**
 * Created by hdant on 18/03/2018.
 */

public class Trip {
    private String mRouteId;
    private String mServiceId;
    private String mTripId;
    private String mTripHeadsign;
    private String mDirectionId;
    private String mShapeId;

    public Trip(String routeId, String serviceId, String tripId, String tripHeadsign,
                String directionId, String shapeId) {
        this.mRouteId = routeId;
        this.mServiceId = serviceId;
        this.mTripId = tripId;
        this.mTripHeadsign = tripHeadsign;
        this.mDirectionId = directionId;
        this.mShapeId = shapeId;
    }

    public String getRouteId() {
        return mRouteId;
    }

    public void setRouteId(String routeId) {
        this.mRouteId = routeId;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String serviceId) {
        this.mServiceId = serviceId;
    }

    public String getTripId() {
        return mTripId;
    }

    public void setTripId(String tripId) {
        this.mTripId = tripId;
    }

    public String getTripHeadsign() {
        return mTripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.mTripHeadsign = tripHeadsign;
    }

    public String getDirectionId() {
        return mDirectionId;
    }

    public void setDirectionId(String directionId) {
        this.mDirectionId = directionId;
    }

    public String getShapeId() {
        return mShapeId;
    }

    public void setShapeId(String shapeId) {
        this.mShapeId = shapeId;
    }
}
