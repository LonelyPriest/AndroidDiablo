package com.diablo.dt.diablo.fragment.inventory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.utils.DiabloEnum;

public class GoodUpdate extends Fragment {

    private Integer   mGoodId;
    private Integer   mLastGoodId;

    private SparseArray<DiabloButton> mButtons;
    private String [] mSeasons;
    // private Integer mBackFrom = R.string.back_from_unknown;

    public void setGoodId(Integer goodId) {
        this.mGoodId = goodId;
    }

    public GoodUpdate() {
        // Required empty public constructor
    }

    public static GoodUpdate newInstance(String param1, String param2) {
        GoodUpdate fragment = new GoodUpdate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGoodId = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initTitle();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_good_update, container, false);
    }

    private void initTitle() {
        String title = getResources().getString(R.string.good_update);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }

}
