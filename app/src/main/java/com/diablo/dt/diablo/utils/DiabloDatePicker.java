package com.diablo.dt.diablo.utils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.diablo.dt.diablo.model.sale.SaleUtils;

/**
 * Created by buxianhui on 17/4/15.
 */

public class DiabloDatePicker {
    private Fragment mFragment;
    private View mBtnStartDate;
    private View mBtnEndDate;

    private EditText mStartDateText;
    private EditText mEndDateText;

    private String mStartSelectDate;
    private String mEndSelectDate;

    public DiabloDatePicker(Fragment fragment,
                            View btnStart,
                            View btnEndDate,
                            EditText startDateText,
                            EditText endDateText,
                            String startDate) {

        this.mFragment = fragment;
        this.mBtnStartDate = btnStart;
        this.mBtnEndDate = btnEndDate;
        this.mStartDateText = startDateText;
        this.mEndDateText = endDateText;

        this.mStartSelectDate = startDate;
        this.mEndSelectDate = DiabloUtils.instance().nextDate();
        init();
    }

    private void init() {
        mStartDateText.setText(mStartSelectDate);
        mBtnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleUtils.DiabloDatePicker.build(mFragment, new SaleUtils.DiabloDatePicker.OnDateSetListener() {
                    @Override
                    public void onDateSet(String date, String nextDate) {
                        mStartDateText.setText(date);
                        mStartSelectDate = date;
                        // mStartTime = date;
                    }
                });
            }
        });

        mEndDateText.setText(DiabloUtils.instance().currentDate());
        mBtnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleUtils.DiabloDatePicker.build(mFragment, new SaleUtils.DiabloDatePicker.OnDateSetListener() {
                    @Override
                    public void onDateSet(String date, String nextDate) {
                        mEndDateText.setText(date);
                        mEndSelectDate = nextDate;
//                        ((EditText)mViewFragment.findViewById(R.id.text_end_date)).setText(date);
//                        mEndTime = nextDate;
                    }
                });
            }
        });
    }

    public String startTime() {
        return mStartSelectDate;
    }

    public String endTime() {
        return mEndSelectDate;
    }
}
