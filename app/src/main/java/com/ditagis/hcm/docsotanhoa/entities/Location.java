package com.ditagis.hcm.docsotanhoa.entities;

/**
 * Created by ThanLe on 4/17/2018.
 */

public class Location {
    private double longtitue;
    private double latitude;
    private String id;

    public Location( String id,double longtitue, double latitude) {
        this.longtitue = longtitue;
        this.latitude = latitude;
        this.id = id;
    }

    public Location() {
    }

    public double getLongtitue() {
        return longtitue;
    }

    public void setLongtitue(double longtitue) {
        this.longtitue = longtitue;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
