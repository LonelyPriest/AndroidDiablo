package com.diablo.dt.diablo.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by buxianhui on 17/3/8.
 */

public class SpinnerStringAdapter extends ArrayAdapter<String>{
    TableRow mRow;
    public SpinnerStringAdapter(Context context, int resource, String[] items, TableRow row) {
        super(context, resource, items);
        mRow = row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);
        return view;
    }
}
