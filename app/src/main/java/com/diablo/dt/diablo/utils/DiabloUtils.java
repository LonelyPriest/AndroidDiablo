package com.diablo.dt.diablo.utils;

import static java.lang.String.format;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.BlueToothPrinter;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloShop;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.jolimark.model.PrinterController;
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.response.PrintResponse;
import com.diablo.dt.diablo.response.sale.NewSaleResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.github.promeg.pinyinhelper.Pinyin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public String firstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return mDateFormat.format(calendar.getTime()).trim();
    }

    public Integer currentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public Integer currentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     *
     * @param month 0 based
     * @return 0 based
     */
    public Integer getSeasonByMonth(Integer month) {
        Integer season = DiabloEnum.INVALID_INDEX;
        switch (month) {
            case 0:
                season = 0;
                break;
            case 1:
                season = 0;
                break;
            case 2:
                season = 0;
                break;

            case 3:
                season = 1;
                break;
            case 4:
                season = 1;
                break;
            case 5:
                season = 1;
                break;

            case 6:
                season = 2;
                break;
            case 7:
                season = 2;
                break;
            case 8:
                season = 2;
                break;

            case 9:
                season = 3;
                break;
            case 10:
                season = 3;
                break;
            case 11:
                season = 3;
                break;

            default:
                break;
        }

        return season;
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

    public void hiddenKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        // ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(Context context, final View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.showSoftInput(view, 0);
    }

    public void makeToast(Context context, int stringId) {
        Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
    }

    public void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

    public void setError(Context context, Integer titleId, Integer errorCode) {
        new DiabloAlertDialog(
            context,
            context.getResources().getString(titleId),
            DiabloError.getError(errorCode)).create();
    }

    public void setError(Context context, Integer titleId, Integer errorCode, String extraError) {
        new DiabloAlertDialog(
            context,
            context.getResources().getString(titleId),
            DiabloError.getError(errorCode) + extraError).create();
    }

    public Float calcGrossMargin(Float balance, Float cost) {
        BigDecimal v = new BigDecimal(((balance - cost) / balance) * 100).setScale(2, RoundingMode.HALF_EVEN);
        return v.floatValue();
    }

    public Float calcGrossProfit(Float balance, Float cost) {
        return balance - cost;
    }

    public String toPinYinWithFirstCharacter(String chinese) {
        char [] name = chinese.toCharArray();
        String py = DiabloEnum.EMPTY_STRING;
        for (char c: name) {
            py += Pinyin.toPinyin(c).charAt(0);
        }

        return py;
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
            & Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public TextView addCell(Context context, TableRow row, String value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(context);
        cell.setLayoutParams(lp);
        // cell.setTextColor(context.getResources().getColor(R.color.black));
        cell.setText(value.trim());
        cell.setTextSize(18);
        // cell.setHeight(105);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(Context context, TableRow row, Integer value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(context);
        if (value < 0) {
            cell.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        cell.setLayoutParams(lp);
        cell.setText(DiabloUtils.instance().toString(value).trim());
        cell.setTextSize(20);
        // cell.setHeight(120);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public TextView addCell(Context context, TableRow row, float value, TableRow.LayoutParams lp){
        // TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        TextView cell = new TextView(context);
        if (value < 0f) {
            cell.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        cell.setLayoutParams(lp);
        cell.setText(DiabloUtils.instance().toString(value).trim());
        cell.setTextSize(20);
        // cell.setHeight(120);
        // cell.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        row.addView(cell);
        return  cell;
    }

    public void formatPageInfo(final TextView cell) {
        cell.setGravity(Gravity.END);
        cell.setTextColor(Color.BLACK);
        cell.setTypeface(null, Typeface.BOLD);
        cell.setTextSize(20);
    }

    public void formatTableStatistic(final TextView cell) {
        cell.setGravity(Gravity.START);
        cell.setTextColor(Color.BLACK);
        cell.setTypeface(null, Typeface.BOLD);
        cell.setTextSize(20);
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
                if (pres.getPCode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.print_success),
                        Toast.LENGTH_LONG);
                } else {
                    String eMessage = context.getString(R.string.print_failed);
                    List<NewSaleResponse.printResponse> pInfos = pres.getPInfos();
                    if (null == pInfos || 0 == pInfos.size()) {
                        eMessage = DiabloError.getError(pres.getCode());
                    } else {
                        for (NewSaleResponse.printResponse p: pInfos) {
                            eMessage += "[" + p.getDevice().toString() + "]"
                                + DiabloError.getError(p.getEcode());
                        }
                    }
                    new DiabloAlertDialog(context, context.getString(titleRes), eMessage).create();
                }
            }

            @Override
            public void onFailure(Call<PrintResponse> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getString(titleRes),
                    DiabloError.getError(99)).create();
            }
        });
    }

    public void startBlueToothPrint(final Context context, final Integer resTitle, final BlueToothPrinter printer, String rsn) {
        PrinterController.PrintProducer producer = new PrinterController.PrintProducer(context, resTitle, rsn);
        PrinterController.PrintConsumer consumer = new PrinterController.PrintConsumer(printer);
        new Thread(producer).start();
        new Thread(consumer).start();
//        final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
//        Call<SalePrintContentResponse> call = face.getPrintContent(Profile.instance().getToken(), new NewSaleRequest.DiabloRSN(rsn));
//
//        call.enqueue(new Callback<SalePrintContentResponse>() {
//            @Override
//            public void onResponse(Call<SalePrintContentResponse> call, Response<SalePrintContentResponse> response) {
//                SalePrintContentResponse res = response.body();
//                if (response.code() == DiabloEnum.HTTP_OK) {
//                    if (res.getCode().equals(DiabloEnum.SUCCESS)) {
//                        if (printer.getName().equals(DiabloEnum.PRINTER_JOLIMARK)) {
//                            PrinterManager pManager = PrinterManager.getInstance();
//                            pManager.initRemotePrinter(VAR.TransType.TRANS_BT, printer.getMac());
//                            pManager.sendData(pManager.string2Byte(res.getContent()), context);
//                        }
//                    }
//                    else {
//                        new DiabloAlertDialog(
//                            context,
//                            context.getString(titleRes), DiabloError.getError(res.getCode())).create();
//                    }
//                }
//                else {
//                    new DiabloAlertDialog(
//                        context,
//                        context.getString(titleRes),
//                        DiabloError.getError(99)).create();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SalePrintContentResponse> call, Throwable t) {
//                new DiabloAlertDialog(
//                    context,
//                    context.getString(titleRes),
//                    DiabloError.getError(99)).create();
//            }
//        });
    }



    public List<DiabloColor> stringColorToArray(final String stringColors) {
        List<DiabloColor> colors = new ArrayList<>();
        for (String colorId:stringColors.split(DiabloEnum.SIZE_SEPARATOR)) {
            DiabloColor color = Profile.instance().getColor(DiabloUtils.instance().toInteger(colorId));
            if (null != color) {
                colors.add(color);
            }
        }

        return colors;
    }

    public List<DiabloSizeGroup> stringSizeGroupToArray(final String stringGroups) {
        List<DiabloSizeGroup> sizeGroups = new ArrayList<>();
        for (String groupId: stringGroups.split(DiabloEnum.SIZE_SEPARATOR)) {
            DiabloSizeGroup group = Profile.instance().getSizeGroup(DiabloUtils.instance().toInteger(groupId));
            if (null != group) {
                sizeGroups.add(group);
            }
        }

        return sizeGroups;
    }

    public TableRow.LayoutParams createTableRowParams(Float weight){
        return new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
    }

//    private Animation createRotateAnimation() {
//        Animation animation = new RotateAnimation(0.0f, 360.0f,
//            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//            0.5f);
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setDuration(2000);
//        return animation;
//    }
//
//    public ImageView loadRefreshView(Context context, LayoutInflater inflater, ViewGroup root) {
//        ImageView refreshView = (ImageView) inflater.inflate(R.layout.diablo_loading, root);
//        // Animation rotateAnimation = createRotateAnimation();
//        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.refresh_rotate);
//        rotation.setRepeatCount(Animation.INFINITE);
//        refreshView.startAnimation(rotation);
//        return refreshView;
//    }

    public Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.diablo_loading, null);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.diablo_loading);
        ImageView image = (ImageView) view.findViewById(R.id.diablo_loading_image);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.refresh_rotate);
        animation.setRepeatCount(Animation.INFINITE);
        image.startAnimation(animation);

        Dialog loadingDialog = new Dialog(context, R.style.RefreshDialog);
        loadingDialog.setCancelable(false);

        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }

    public static void linearLayoutAddView(LinearLayout parent, View view,  @IdRes Integer childResId) {
        Integer index = DiabloEnum.DEFAULT_INDEX;
        // Integer index = parent.indexOfChild(child);
        for (int i = DiabloEnum.DEFAULT_INDEX; i<parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getId() == childResId) {
                index = i;
            }
        }

        parent.addView(view, index);
        // parent.requestLayout();
    }

    public static void switchToFrame(Fragment from, String toClassName, String toTag) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        Fragment to = from.getFragmentManager().findFragmentByTag(toTag);

        if (null == to){
            try {
                Class<?> clazz = Class.forName(toClassName);
                to = (Fragment) clazz.newInstance();
            } catch (Exception e) {
                to = null;
            }

        }

        if (null != to) {
            if (!to.isAdded()){
                transaction.hide(from).add(R.id.frame_container, to, toTag).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }
}
