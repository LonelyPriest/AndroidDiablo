package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.Employee;

import java.util.List;

/**
 * Created by buxianhui on 17/3/5.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    // private Context context;
    private List<Employee> filterItems;

    public EmployeeAdapter(Context context, Integer resource, List<Employee> items) {
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

        Employee employee = filterItems.get(position);
        if (null != employee) {
            view.setText(employee.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);

        Employee employee = filterItems.get(position);
        if (null != employee) {
            view.setText(employee.getName());
        }
        return view;
    }
}
