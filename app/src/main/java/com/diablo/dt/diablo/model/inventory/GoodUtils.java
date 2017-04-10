package com.diablo.dt.diablo.model.inventory;

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
import com.diablo.dt.diablo.fragment.inventory.ColorSelect;
import com.diablo.dt.diablo.utils.DiabloEnum;

/**
 * Created by buxianhui on 17/4/10.
 */

public class GoodUtils {
    public static void switchToColorSelectFrame(
        GoodCalc calc,
        Integer comeFrom,
        Fragment from) {

        FragmentTransaction transaction = from.getFragmentManager().beginTransaction();
        // find
        ColorSelect to = (ColorSelect) from.getFragmentManager().findFragmentByTag(DiabloEnum.TAG_COLOR_SELECT);

        if (null == to){
            Bundle args = new Bundle();
            args.putInt(DiabloEnum.BUNDLE_PARAM_COME_FORM, comeFrom);
            args.putString(DiabloEnum.BUNDLE_PARAM_GOOD, new Gson().toJson(calc));
            to = new ColorSelect();
            to.setArguments(args);
        } else {
            to.setComeFrom(comeFrom);
            to.setGoodCalc(new Gson().toJson(calc));
        }

        if (!to.isAdded()){
            transaction.hide(from).add(R.id.frame_container, to, DiabloEnum.TAG_COLOR_SELECT).commit();
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
