package com.diablo.dt.diablo.entity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;

/**
 * Created by buxianhui on 17/3/13.
 */

public class DiabloButton {
    private Context context;

    private @IdRes Integer resId;
    private boolean enable;

    public DiabloButton(Context context, @IdRes Integer resId){
        this.context = context;
        this.resId = resId;
        enable = true;
    }

    public Integer getResId() {
        return resId;
    }

    private void setResId(@IdRes Integer resId) {
        this.resId = resId;
    }

    public boolean isEnabled() {
        return enable;
    }

    public void disable() {
        this.enable = false;
        ((Activity)context).invalidateOptionsMenu();
    }

    public void enable(){
        this.enable = true;
        ((Activity)context).invalidateOptionsMenu();
    }
}
