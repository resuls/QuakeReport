package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class QuakeLoader extends AsyncTaskLoader<List<Quake>>
{
    /** Tag for log messages */
    private static final String LOG_TAG = QuakeLoader.class.getName();

    /** Query URL */
    private String url;

    public QuakeLoader (Context context, String url)
    {
        super(context);
        this.url = url;
        Log.e(MainActivity.LOG_TAG, "QuakeLoader constructor executed");
    }

    @Override
    protected void onStartLoading()
    {
        Log.e(MainActivity.LOG_TAG, "QuakeLoader.onStartLoading executed");
        forceLoad();
    }


    @Override
    public List<Quake> loadInBackground()
    {
        Log.e(MainActivity.LOG_TAG, "QuakeLoader.loadInBackground executed");
        if (url == null)
            return null;

        return QueryUtils.fetchQuakeData(url);
    }
}
