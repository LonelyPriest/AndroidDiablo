package com.diablo.dt.diablo.entity;

import android.support.annotation.IdRes;

/**
 * Created by buxianhui on 17/3/13.
 */

public class DiabloButton {
    private @IdRes Integer resId;
    private boolean enable;

    public DiabloButton(@IdRes Integer resId){
        this.resId = resId;
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
    }

    public void enable(){
        this.enable = true;
    }
}