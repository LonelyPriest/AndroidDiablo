package com.diablo.dt.diablo.fragment.retailer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;

public class DiabloRetailerDetail extends Fragment {

    public DiabloRetailerDetail() {
        // Required empty public constructor
    }

    public static DiabloRetailerDetail newInstance(String param1, String param2) {
        DiabloRetailerDetail fragment = new DiabloRetailerDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diablo_retailer, container, false);
    }

}
