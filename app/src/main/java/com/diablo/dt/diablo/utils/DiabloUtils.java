package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Retailer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

/**
 * Created by buxianhui on 17/2/24.
 */

public class DiabloUtils {
    private static DiabloUtils mInstance;

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

    public AuthenShop getShop(List<AuthenShop> shops, Integer index){
        AuthenShop shop = null;
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

    public TextView addCell(Context context, TableRow row, String value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        // cell.setTextColor(context.getResources().getColor(R.color.black));
        cell.setText(value);
        cell.setTextSize(18);
        cell.setHeight(110);
        cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(Context context, TableRow row, Integer value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        if (value < 0) {
            cell.setTextColor(context.getResources().getColor(R.color.red));
        }
        cell.setText(toString(value));
        cell.setTextSize(20);
        cell.setHeight(110);
        cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(Context context, TableRow row, float value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        if (value < 0f) {
            cell.setTextColor(context.getResources().getColor(R.color.red));
        }
        cell.setText(toString(value));
        cell.setTextSize(20);
        cell.setHeight(110);
        cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
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
            return DiabloEnum.SWIP_TOP;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return DiabloEnum.SWIP_LEFT;
        if (angle < -45 && angle>= -135)
            // down
            return DiabloEnum.SWIP_DOWN;
        if (angle > -45 && angle <= 45)
            // right
            return DiabloEnum.SWIP_RIGHT;

        return DiabloEnum.SWIP_NONE;
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(calendar.getTime());
    }

    public String currentDatetime(){
        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return format.format(calendar.getTime());
    }

    public String toString(Float value){
        if (value == Math.round(value)){
            return String.format(Locale.CHINA, "%d", Math.round(value));
        }
        return String.format(Locale.CHINA, "%.2f", value);
    }

    public String toString(Integer value){
        return format(Locale.CHINA, "%d", value);
    }

    public Integer toInteger(String value){
        if (!value.isEmpty())
            return Integer.parseInt(value.trim());

        else
            return 0;
    }

    public Float toFloat(String value){
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

    public void openKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

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

    public void makeToast(Context context, Integer value){
        Toast toast = Toast.makeText(context, toString(value), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void makeToast(Context context, String value){
        Toast toast = Toast.makeText(context, value, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void makeToast(Context context, Float value) {
        Toast toast = Toast.makeText(context, toString(value), Toast.LENGTH_SHORT);
        toast.show();
    }

    public interface Payment{
        public void setPayment(String param);
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
}
