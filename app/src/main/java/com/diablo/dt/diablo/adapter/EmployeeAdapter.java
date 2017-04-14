package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/5.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<Employee> items;
    private List<Employee> tempItems;
    private List<Employee> suggestions;

    public EmployeeAdapter(Context context, Integer resource, Integer textViewResourceId, List<Employee> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Employee) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Employee people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Employee> filterList = (ArrayList<Employee>) results.values;
            if (results.count > 0) {
                clear();
                for (Employee people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

    private View getCustomView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.typeahead_employee, parent, false);
        }

        Employee people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(textViewResourceId);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }
}
