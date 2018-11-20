package com.example.android.quakereport;

public class Quake
{
    private double magnitude;
    private String city;
    private String date;

    public Quake(double magnitude, String city, String date)
    {
        this.magnitude = magnitude;
        this.city = city;
        this.date = date;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public String getCity()
    {
        return city;
    }

    public String getDate()
    {
        return date;
    }
}
