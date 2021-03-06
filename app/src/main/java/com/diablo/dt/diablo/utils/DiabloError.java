package com.diablo.dt.diablo.utils;

import android.content.res.Resources;
import android.util.SparseArray;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.Profile;

/**
 * Created by buxianhui on 17/2/22.
 */
public class DiabloError {
//    private static DiabloError mInstance;
//
//    public static DiabloError getInstance() {
//        if (null == mInstance){
//            mInstance = new DiabloError();
//        }
//
//        return mInstance;
//    }
//
//    private DiabloError() {
//
//    }

    private static SparseError mSparseError;

    private DiabloError() {

    }

    private static SparseError instance() {
        if (null == mSparseError) {
            mSparseError = new SparseError();
        }

        return mSparseError;
    }

    private static class SparseError {
        private SparseArray<String> mErrors = new SparseArray<>();

        SparseError() {
            Resources r = Profile.instance().getResource();
            mErrors.put(99, r.getString(R.string.network_invalid));
            mErrors.put(500, r.getString(R.string.internal_error));
            mErrors.put(598, r.getString(R.string.authen_error));
            mErrors.put(1101, r.getString(R.string.invalid_name_password));
            mErrors.put(9009, r.getString(R.string.network_unreachable));
            mErrors.put(9001, r.getString(R.string.database_error));

            /**
             * login
             */
            mErrors.put(1105, r.getString(R.string.login_user_active));
            mErrors.put(1106, r.getString(R.string.login_execced_user));
            mErrors.put(1107, r.getString(R.string.login_no_user_fire));
            mErrors.put(1199, r.getString(R.string.login_out_of_service));

            mErrors.put(200, r.getString(R.string.failed_to_get_employee));
            mErrors.put(201, r.getString(R.string.failed_to_get_base_setting));
            mErrors.put(202, r.getString(R.string.failed_to_get_retailer));
            mErrors.put(203, r.getString(R.string.failed_to_get_color));
            mErrors.put(204, r.getString(R.string.failed_to_get_size_group));
            mErrors.put(205, r.getString(R.string.failed_to_get_stock));
            mErrors.put(206, r.getString(R.string.failed_to_get_brand));
            mErrors.put(207, r.getString(R.string.failed_to_get_good_type));
            mErrors.put(208, r.getString(R.string.failed_to_get_firm));
            mErrors.put(209, r.getString(R.string.failed_to_get_good));
            mErrors.put(210, r.getString(R.string.failed_to_get_color_kind));
            mErrors.put(211, r.getString(R.string.failed_to_get_user_info));
            mErrors.put(212, r.getString(R.string.failed_to_get_bankcard));

            mErrors.put(2699, r.getString(R.string.update_same));
            mErrors.put(2703, r.getString(R.string.error_style_number));
            mErrors.put(2799, r.getString(R.string.error_reject_with_second_good));

            mErrors.put(2098, r.getString(R.string.good_has_been_used));

            mErrors.put(2411, r.getString(R.string.print_wrong_printer_sn));
            mErrors.put(2412, r.getString(R.string.print_failed_to_deliver_recode));
            mErrors.put(2413, r.getString(R.string.print_long_content));
            mErrors.put(2414, r.getString(R.string.print_error_params));
            mErrors.put(2415, r.getString(R.string.print_timeout));
            mErrors.put(2416, r.getString(R.string.print_unknown_error));

            mErrors.put(2419, r.getString(R.string.rg_print_not_connect));
            mErrors.put(2420, r.getString(R.string.rg_print_no_paper));
            mErrors.put(2421, r.getString(R.string.rg_print_wrong_state));
            mErrors.put(2422, r.getString(R.string.rg_print_wrong_printer_conn));
            mErrors.put(2423, r.getString(R.string.rg_print_lack_size));

            mErrors.put(2705, r.getString(R.string.invalid_balance_of_retailer));

            /**
             * color
             */
            mErrors.put(1901, r.getString(R.string.color_has_been_exist));

            /**
             * firm
             */
            mErrors.put(1601, r.getString(R.string.firm_exist));

            /**
             * good
             */
            mErrors.put(2001, r.getString(R.string.good_exist));
            mErrors.put(2098, r.getString(R.string.good_in_use));

        }
    }

    public static String getError(Integer code){
        return instance().mErrors.get(code);
    }

}
