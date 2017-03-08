package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.AuthenShop;
import com.diablo.dt.diablo.entity.DiabloEnum;
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

    public TableRow addCell(Context context, TableRow row, String value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(value);
        cell.setTextSize(22);
        cell.setHeight(125);
        row.addView(cell);
        return  row;
    }

    public TableRow addCell(Context context, TableRow row, Integer value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(value.toString());
        cell.setTextSize(22);
        cell.setHeight(125);
        row.addView(cell);
        return  row;
    }

    public TableRow addCell(Context context, TableRow row, float value){
        TextView cell = new TextView(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.setMargins(0, 0, 25, 0);
        cell.setText(Float.toString(value));
        cell.setTextSize(22);
        cell.setHeight(125);
        row.addView(cell);
        return  row;
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
            return Integer.parseInt(value);

        else
            return 0;
    }

    public Float toFloat(String value){
        return Float.parseFloat(value);
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
}
