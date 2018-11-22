package com.example.android.quakereport;

public class Quake
{
    private double magnitude;
    private String city;
    private String date;
    private String url;

    public Quake(double magnitude, String city, String date, String url)
    {
        this.magnitude = magnitude;
        this.city = city;
        this.date = date;
        this.url = url;
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

    public String getUrl()
    {
        return url;
    }
}
