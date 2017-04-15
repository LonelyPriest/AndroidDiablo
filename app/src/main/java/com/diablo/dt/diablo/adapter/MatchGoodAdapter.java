package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.MatchGood;

import java.util.List;

/**
 * Created by buxianhui on 17/4/4.
 */

public class MatchGoodAdapter extends ArrayAdapter<MatchGood> {
    private Context context;
    private Integer resource;
    private Integer textViewResourceId;
    private List<MatchGood> filterGoods;
    private Filter filter;

    public MatchGoodAdapter(Context context,
                             Integer resource,
                             Integer textViewResourceId,
                             List<MatchGood> goods) {
        super(context, resource, textViewResourceId, goods);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.filterGoods = goods;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }

        MatchGood good = filterGoods.get(position);
        if (null != good) {
            TextView dropdownView = (TextView) view.findViewById(textViewResourceId);
            if (dropdownView != null)
                dropdownView.setText(good.getName());
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (null == filter)
            filter = new GoodFilter();
        return filter;
    }

    private class GoodFilter extends Filter{
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MatchGood) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterGoods;
            filterResults.count = filterGoods.size();
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notifyDataSetChanged();
        }
    }
}
