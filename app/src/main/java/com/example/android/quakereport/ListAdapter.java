package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        TextView txtCity = listView.findViewById(R.id.txtCity);
        txtCity.setText(quake.getCity());

        TextView txtMagnitude = listView.findViewById(R.id.txtMagnitude);
        txtMagnitude.setText(String.valueOf(quake.getMagnitude()));

        TextView txtDate = listView.findViewById(R.id.txtDate);
        txtDate.setText(quake.getDate());

        return listView;
    }
}
