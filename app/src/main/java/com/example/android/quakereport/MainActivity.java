package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quake>>
{
    public static final String LOG_TAG = MainActivity.class.getName();
    private final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    private ListAdapter adapter;
    private TextView txtEmpty;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = findViewById(R.id.list);
        txtEmpty = findViewById(R.id.txtEmpty);
        progressBar = findViewById(R.id.progressBar);

        earthquakeListView.setEmptyView(txtEmpty);

        adapter = new ListAdapter(this, new ArrayList<Quake>());

        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Quake quake = adapter.getItem(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(quake.getUrl()));
                startActivity(intent);
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(0, null, this);
        }
        else
        {
            progressBar.setVisibility(View.GONE);

            txtEmpty.setText(getResources().getText(R.string.no_internet));
        }
    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int id, Bundle args)
    {
        Log.e(LOG_TAG, "onCreateLoader executed");
        return new QuakeLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Quake>> loader, List<Quake> data)
    {
        progressBar.setVisibility(View.GONE);

        txtEmpty.setText(getResources().getText(R.string.empty_view));

        adapter.clear();



        if (data != null && !data.isEmpty())
        {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Quake>> loader)
    {
        adapter.clear();
    }
}
