package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diablo.dt.diablo.filter.DiabloFilter;

import java.util.List;

/**
 * Created by buxianhui on 17/4/14.
 */

public class DiabloFilterAdapter extends ArrayAdapter<DiabloFilter> {
    // private Context context;
    private List<DiabloFilter> filterItems;

    public DiabloFilterAdapter(Context context, Integer resource, List<DiabloFilter> items) {
        super(context, resource, items);
        // this.context = context;
        this.filterItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.MAGENTA);

        DiabloFilter entity = filterItems.get(position);
        if (null != entity) {
            view.setText(entity.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);

        DiabloFilter entity = filterItems.get(position);
        if (null != entity) {
            view.setText(entity.getName());
        }
        return view;
    }
}
