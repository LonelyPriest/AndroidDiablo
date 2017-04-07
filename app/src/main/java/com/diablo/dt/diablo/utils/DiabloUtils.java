package com.diablo.dt.diablo.utils;

import static java.lang.String.format;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.response.sale.NewSaleResponse;
import com.diablo.dt.diablo.response.PrintResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/2/24.
 */

public class DiabloUtils {
    private static DiabloUtils mInstance;

    public final static DateFormat mDatetimeFormat
        = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

    public final static DateFormat mDateFormat
        = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public static DiabloUtils getInstance() {
        if (null == mInstance){
            mInstance = new DiabloUtils();
        }

        return mInstance;
    }

    public static DiabloUtils instance() {
        if (null == mInstance){
            mInstance = new DiabloUtils();
        }

        return mInstance;
    }

    private DiabloUtils() {

    }

    public DiabloShop getShop(List<DiabloShop> shops, Integer index){
        DiabloShop shop = null;
        for ( Integer i = 0; i < shops.size(); i++){
            if (index.equals(shops.get(i).getShop())){
                shop = shops.get(i);
                break;
            }
        }

        return shop;
    }

    public Employee getEmployeeByNumber(List<Employee> employees, String number){
        Employee employee = null;
        for ( Integer i = 0; i < employees.size(); i++){
            if (number.equals(employees.get(i).getNumber())){
                employee = employees.get(i);
                break;
            }
        }
        return employee;
    }

    public Retailer getRetailer(List<Retailer> retailers, Integer index){
        Retailer r = null;
        for (Integer i = 0; i < retailers.size(); i++){
            if ( index.equals(retailers.get(i).getId())){
                r = retailers.get(i);
                break;
            }
        }

        return r;
    }



    public void setTextViewValue(TextView view, Integer value){
        view.setText(toString(value));
    }

    public void setTextViewValue(TextView view, Float value){
        view.setText(toString(value));
    }

    public void setEditTextValue(EditText view, Integer value){
        view.setText(toString(value));
    }

    public void setEditTextValue(EditText view, Float value){
        view.setText(toString(value));
    }

    // swip top, left, down, right
    public int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return DiabloEnum.SWIPE_TOP;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return DiabloEnum.SWIPE_LEFT;
        if (angle < -45 && angle>= -135)
            // down
            return DiabloEnum.SWIPE_DOWN;
        if (angle > -45 && angle <= 45)
            // right
            return DiabloEnum.SWIPE_RIGHT;

        return DiabloEnum.SWIPE_NONE;
    }

    public void debugDialog(Context context, String title, String message){
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                // .contentColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .positiveText(context.getResources().getString(R.string.login_ok))
                .positiveColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    public String currentDate(){
        Calendar calendar = Calendar.getInstance();
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return mDateFormat.format(calendar.getTime());
    }

    public String nextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return mDateFormat.format(calendar.getTime()).trim();
    }

    public String currentDatetime(){
        Calendar calendar = Calendar.getInstance();
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return mDatetimeFormat.format(calendar.getTime()).trim();
    }

    public String formatDate(Integer year, Integer month, Integer day) {
        // DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return mDateFormat.format(new Date(year, month, day).getTime()).trim();
    }

    public String toString(Float value){
        if (value == Math.round(value)){
            return String.format(Locale.CHINA, "%d", Math.round(value));
        }
        return String.format(Locale.CHINA, "%.2f", value).trim();
    }

    public String toString(Integer value){
        return format(Locale.CHINA, "%d", value).trim();
    }

    public Integer toInteger(String value){
        if (!value.isEmpty())
            return Integer.parseInt(value.trim());
        else
            return 0;
    }

    public Float toFloat(String value){
        if (value.trim().isEmpty()) {
            return 0f;
        }

        try {
            return Float.parseFloat(value.trim());
        } catch (Exception e){
            return 0f;
        }
    };

    public Float priceWithDiscount(Float price, Float discount){
        return (price * discount) / 100;
    }

    public Float priceWithDiscount(String price, Float discount){
        return  (Float.parseFloat(price) * discount) / 100;
    }

    public void openKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // view.requestFocus();
        imm.showSoftInput(view, 0);
        // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hiddenKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        // ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imm.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
    }

    public void focusAndShowKeyboard(Context context, final View view){
        view.requestFocus();
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//                view.requestFocus(); // needed if you have more then one input
//            }
//        }, 200);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//        view.requestFocus();
        // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        // imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void makeToast(Context context, Integer value, int lengthLong){
        Toast toast = Toast.makeText(context, toString(value), lengthLong);
        toast.show();
    }

    public void makeToast(Context context, String value, int lengthLong){
        Toast toast = Toast.makeText(context, value, lengthLong);
        toast.show();
    }

    public void makeToast(Context context, Float value, int lengthLong) {
        Toast toast = Toast.makeText(context, toString(value), lengthLong);
        toast.show();
    }

    public interface Payment{
        void setPayment(String param);
    }

    public void addTextChangedListenerOfPayment(EditText view, final Payment payment){
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputValue = editable.toString().trim();
                    payment.setPayment(editable.toString().trim());
            }
        });
    }

    public void replaceFragment(FragmentManager ft, Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = ft.beginTransaction();
        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void  replaceFragmentAndAddToBackStack(FragmentManager ft, Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = ft.beginTransaction();
        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Integer calcTotalPage(Integer totalItems, Integer itemsPerPage) {
        if (totalItems < itemsPerPage) {
            return 1;
        }
        else if (0 == totalItems % itemsPerPage) {
            return totalItems / itemsPerPage;
        }
        else {
            return totalItems / itemsPerPage + 1;
        }
    }

    public void startPrint(final Context context, final Integer titleRes, String rsn) {
        final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
        Call<PrintResponse> call = face.startPrint(Profile.instance().getToken(), new NewSaleRequest.DiabloRSN(rsn));

        call.enqueue(new Callback<PrintResponse>() {
            @Override
            public void onResponse(Call<PrintResponse> call, Response<PrintResponse> response) {
                PrintResponse pres = response.body();
                if (pres.getPcode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.print_success),
                        Toast.LENGTH_LONG);
                } else {
                    String eMessage = context.getString(R.string.print_failed);
                    List<NewSaleResponse.printResponse> pInfos = pres.getPinfos();
                    if (null == pInfos || 0 == pInfos.size()) {
                        eMessage = DiabloError.getInstance().getError(pres.getCode());
                    } else {
                        for (NewSaleResponse.printResponse p: pInfos) {
                            eMessage += "[" + p.getDevice().toString() + "]"
                                + DiabloError.getInstance().getError(p.getEcode());
                        }
                    }
                    new DiabloAlertDialog(context, context.getString(titleRes), eMessage).create();
                }
            }

            @Override
            public void onFailure(Call<PrintResponse> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getString(R.string.nav_sale_in),
                    DiabloError.getInstance().getError(99)).create();
            }
        });
    }
}
