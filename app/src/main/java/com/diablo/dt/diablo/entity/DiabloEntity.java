package com.diablo.dt.diablo.entity;

/**
 * Created by buxianhui on 17/4/17.
 */

public abstract class DiabloEntity {
    /**
     *
     * @return the name of dropdown list
     */
    public abstract String getName();

    /**
     *
     * @return the name showed on the edit text when the user click or select the dropdown list
     */
    public abstract String getViewName();
}
