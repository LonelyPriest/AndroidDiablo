package com.diablo.dt.diablo.entity;

/**
 * Created by buxianhui on 17/2/22.
 */

public class DiabloEnum {
    public static final Integer TABLET = 1;
    public static final Integer ROW_SIZE = 10;
    public static final String  SESSION_ID = "qzg_dyty_session";

    public static final Integer INVALID_INDEX = -1;

    //shop
    public static final Integer SHOP_ONLY = 0;
    public static final Integer REPO_ONLY = 1;
    public static final Integer REPO_BAD  = 2;
    public static final Integer BIND_NONE = -1;

    // fling
    public static final Integer SWIP_NONE = 0;
    public static final Integer SWIP_TOP  = 1;
    public static final Integer SWIP_LEFT = 2;
    public static final Integer SWIP_DOWN = 3;
    public static final Integer SWIP_RIGHT = 4;

    public static final Integer SWIPE_MIN_DISTANCE = 500;
    public static final Integer SWIPE_THRESHOLD_VELOCITY = 500;

    // pagination
    public static final Integer DEFAULT_PAGE = 1;
    public static final Integer DEFAULT_ITEMS_PER_PAGE = 10;

    private DiabloEnum(){

    }
}
