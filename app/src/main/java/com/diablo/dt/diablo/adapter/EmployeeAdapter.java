package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.Employee;

import java.util.List;

/**
 * Created by buxianhui on 17/3/5.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    // private Context context;
    private List<Employee> filterItems;
    private Spinner spinner;
    private boolean verticalOffset;

    public EmployeeAdapter(Context context, Spinner spinner, List<Employee> items, boolean verticalOffset) {
        super(context, android.R.layout.simple_spinner_item, items);

        this.filterItems = items;
        this.spinner = spinner;
        this.verticalOffset = verticalOffset;

        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(this);
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

        if (verticalOffset) {
            spinner.setDropDownVerticalOffset(spinner.getHeight());
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
