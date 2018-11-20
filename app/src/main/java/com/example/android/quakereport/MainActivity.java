package com.example.android.quakereport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        ArrayList<Quake> earthquakes = new ArrayList<>();

        earthquakes.add(new Quake(7.2, "San Francisco", "Feb 2, 2016"));
        earthquakes.add(new Quake(6.1, "London", "July 2, 2016"));
        earthquakes.add(new Quake(3.9, "Tokyo", "Nov 2, 2016"));
        earthquakes.add(new Quake(5.4, "Mexico City", "May 2, 2016"));
        earthquakes.add(new Quake(2.8, "Moscow", "Jan 2, 2016"));
        earthquakes.add(new Quake(4.9, "Rio de Janeiro", "Aug 2, 2016"));
        earthquakes.add(new Quake(1.6, "Paris", "Oct 2, 2016"));

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        ListAdapter adapter = new ListAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }
}
