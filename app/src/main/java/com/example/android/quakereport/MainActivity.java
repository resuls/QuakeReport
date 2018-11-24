package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quake>>
{
    public static final String LOG_TAG = MainActivity.class.getName();
    private final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

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
            updateUi();
        }
        else
        {
            progressBar.setVisibility(View.GONE);

            txtEmpty.setText(getResources().getText(R.string.no_internet));
        }

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener()
                {
                    @Override
                    public void onRefresh()
                    {
                        destroyLoader();
                        updateUi();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    public void updateUi()
    {
        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<List<Quake>> onCreateLoader(int id, Bundle args)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = preferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = preferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new QuakeLoader(this, uriBuilder.toString());
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
//        Toast.makeText(this, "Loader Reset", Toast.LENGTH_LONG).show();
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        destroyLoader();
        updateUi();
    }

    private void destroyLoader()
    {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.destroyLoader(0);
    }
}
