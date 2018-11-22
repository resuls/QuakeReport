package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends ArrayAdapter<Quake>
{
    public ListAdapter(@NonNull Context context, ArrayList<Quake> quakes)
    {
        super(context, 0, quakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listView = convertView;

        if (listView == null)
        {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }

        final Quake quake = getItem(position);

        String locCity[] = new String[2];
        String loc = quake.getCity();

        if (loc.contains("of"))
        {
            int n = loc.indexOf("of") + 3;
            locCity[0] = loc.substring(0, n);
            locCity[1] = loc.substring(n);
        }
        else
        {
            locCity[0] = "Near the ";
            locCity[1] = loc;
        }

        TextView txtLocation = listView.findViewById(R.id.txtLocation);
        txtLocation.setText(locCity[0]);

        TextView txtCity = listView.findViewById(R.id.txtCity);
        txtCity.setText(locCity[1]);

        DecimalFormat formatter = new DecimalFormat("0.0");


        TextView txtMagnitude = listView.findViewById(R.id.txtMagnitude);
        txtMagnitude.setText(String.valueOf(formatter.format(quake.getMagnitude())));

        Date date = new Date(Long.parseLong(quake.getDate()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        TextView txtDate = listView.findViewById(R.id.txtDate);
        txtDate.setText(dateFormat.format(date));

        dateFormat = new SimpleDateFormat("HH:mm");

        TextView txtTime = listView.findViewById(R.id.txtTime);
        txtTime.setText(dateFormat.format(date));

        GradientDrawable magnitudeCircle = (GradientDrawable) txtMagnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(quake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listView;
    }

    private int getMagnitudeColor(double magnitude)
    {
        int i;
        switch ((int)magnitude)
        {
            case 0:
            case 1: i = R.color.magnitude1; break;
            case 2: i = R.color.magnitude2; break;
            case 3: i = R.color.magnitude3; break;
            case 4: i = R.color.magnitude4; break;
            case 5: i = R.color.magnitude5; break;
            case 6: i = R.color.magnitude6; break;
            case 7: i = R.color.magnitude7; break;
            case 8: i = R.color.magnitude8; break;
            case 9: i = R.color.magnitude9; break;
            default: i = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), i);
    }
}
