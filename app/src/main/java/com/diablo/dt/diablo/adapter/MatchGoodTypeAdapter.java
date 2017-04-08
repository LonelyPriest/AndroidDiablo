package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.DiabloType;

import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class MatchGoodTypeAdapter extends ArrayAdapter<DiabloType> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<DiabloType> filterGoodTypes;

    public MatchGoodTypeAdapter(Context context, Integer resource, Integer textViewResourceId, List<DiabloType> types) {
        super(context, resource, textViewResourceId, types);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.filterGoodTypes = types;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        DiabloType goodType = filterGoodTypes.get(position);
        if (goodType != null) {
            TextView retailerView = (TextView) view.findViewById(textViewResourceId);
            retailerView.setText(goodType.getName());
        }
        return view;
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
            return ((DiabloType) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterGoodTypes;
            filterResults.count = filterGoodTypes.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };
}
