package com.example.ykm.findfriends;

/**
 * Created by ykm on 17-Sep-17.
 */

public class User {
    private String name;
    private String email;
    private long latitude,longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public double getPhone() {
        return phone;
    }

    public void setPhone(double phone) {
        this.phone = phone;
    }

    private double phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, long latitude, long longitude, double phone, String email) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
    }

}
