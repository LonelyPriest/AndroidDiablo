package com.diablo.dt.diablo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.diablo.dt.diablo.entity.DiabloShop;

import java.util.List;

/**
 * Created by buxianhui on 17/4/14.
 */

public class SpinnerShopAdapter extends ArrayAdapter<DiabloShop> {
    // private Context context;
    private List<DiabloShop> filterShops;

    public SpinnerShopAdapter(Context context, Integer resource, List<DiabloShop> shops) {
        super(context, resource, shops);
        // this.context = context;
        this.filterShops = shops;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);
        // view.setTextColor(Color.MAGENTA);

        DiabloShop shop = filterShops.get(position);
        if (null != shop) {
            view.setText(shop.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(18);
        view.setTextColor(Color.BLACK);

        DiabloShop shop = filterShops.get(position);
        if (null != shop) {
            view.setText(shop.getName());
        }

        return view;
    }
}
