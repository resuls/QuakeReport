package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils
{
    private QueryUtils()
    {}

    public static ArrayList<Quake> fetchQuakeData(String urlString)
    {
        // Create URL object
        URL url = createUrl(urlString);
        Log.e(MainActivity.LOG_TAG, "fetchQuakeData executed");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try
        {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e)
        {
            Log.e(MainActivity.LOG_TAG, "Error closing input stream", e);
        }

        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


        ArrayList<Quake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    private static ArrayList<Quake> extractEarthquakes(String jsonData)
    {
        ArrayList<Quake> earthquakes = new ArrayList<>();

        try
        {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("features");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject a = jsonArray.getJSONObject(i).getJSONObject("properties");

                earthquakes.add(new Quake(a.getDouble("mag"), a.getString("place"),
                        a.getString("time"), a.getString("url")));
            }
        }
        catch (JSONException e)
        {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    private static URL createUrl(String stringUrl)
    {
        URL url = null;
        try
        {
            url = new URL(stringUrl);
        } catch (MalformedURLException e)
        {
            Log.e(MainActivity.LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(MainActivity.LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e)
        {
            Log.e(MainActivity.LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally
        {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}