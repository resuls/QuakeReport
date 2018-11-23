package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String LOG_TAG = MainActivity.class.getName();
    private final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private ArrayList<Quake> earthquakes;
    private ListView earthquakeListView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = findViewById(R.id.list);

        new getAsyncData().execute(URL);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Quake quake = earthquakes.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(quake.getUrl()));
                startActivity(intent);
            }
        });
    }

    public void updateUi(ArrayList<Quake> quakes)
    {
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new ListAdapter(this, quakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }

    private class getAsyncData extends AsyncTask<String, Void, List<Quake>>
    {
        @Override
        protected List<Quake> doInBackground(String... strings)
        {
            if (strings.length < 1 || strings[0] == null)
                return null;
            // Create a fake list of earthquake locations.
            earthquakes = QueryUtils.fetchQuakeData(strings[0]);

            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Quake> quakes)
        {
            if (quakes == null)
                return;

            updateUi((ArrayList<Quake>) quakes);
        }
    }
}
