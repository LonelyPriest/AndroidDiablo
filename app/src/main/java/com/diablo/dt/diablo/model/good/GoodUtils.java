package com.diablo.dt.diablo.model.good;

import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.fragment.good.ColorSelect;
import com.diablo.dt.diablo.fragment.good.SizeSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.List;

/**
 * Created by buxianhui on 17/4/10.
 */

public class GoodUtils {
    public static void switchToColorSelectFrame(
        GoodCalc calc,
        Integer comeFrom,
        Fragment from,
        List<DiabloColor> immutableColors) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        ColorSelect to = (ColorSelect) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_COLOR_SELECT);

        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, comeFrom);
            args.putString(DiabloEnum.BUNDLE_PARAM_GOOD, new Gson().toJson(calc));
            args.putString(DiabloEnum.BUNDLE_PARAM_IMMUTABLE_COLOR, new Gson().toJson(immutableColors));
            to = new ColorSelect();
            to.setArguments(args);
        } else {
            to.setComeFrom(comeFrom);
            to.setGoodCalc(new Gson().toJson(calc));
            to.setImmutableColors(new Gson().toJson(immutableColors));
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_COLOR_SELECT).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }


    public static void switchToSizeSelectFrame(
        GoodCalc calc,
        Integer comeFrom,
        Fragment from,
        List<DiabloSizeGroup> immutableGroups) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        SizeSelect to = (SizeSelect) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_SIZE_SELECT);

        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, comeFrom);
            args.putString(DiabloEnum.BUNDLE_PARAM_GOOD, new Gson().toJson(calc));
            args.putString(DiabloEnum.BUNDLE_PARAM_IMMUTABLE_SIZE, new Gson().toJson(immutableGroups));
            to = new SizeSelect();
            to.setArguments(args);
        } else {
            to.setComeFrom(comeFrom);
            to.setGoodCalc(new Gson().toJson(calc));
            to.setImmutableSizeGroups(new Gson().toJson(immutableGroups));
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_SIZE_SELECT).commit();
        } else {
            transaction.hide(from).show(to).commit();
        }
    }

    public static TextView genCellOfTableHead(Context context, TableRow.LayoutParams lp, String title) {
        TextView cell = new TextView(context);
        cell.setLayoutParams(lp);
        cell.setTextSize(20);
        cell.setTextColor(ContextCompat.getColor(context, R.color.black));
        cell.setTypeface(null, Typeface.BOLD);
        cell.setGravity(Gravity.CENTER);
        cell.setText(title);

        return cell;
    }
}
