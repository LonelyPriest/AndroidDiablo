package com.diablo.dt.diablo.utils;

/**
 * Created by buxianhui on 17/2/22.
 */

public class DiabloEnum {
    public static final Integer TABLET = 1;
    public static final Integer ROW_SIZE = 10;
    public static final String  SESSION_ID = "qzg_dyty_session";
    public static final String  SIZE_SEPARATOR = ",";
    public static final String  DATE_SEPARATOR = "-";
    public static final String  DAILY_REPORT_BY_SHOP = "by_shop";
    public static final String  DAILY_REPORT_BY_GOOD = "by_good";

    public static final Integer INVALID_INDEX = -1;
    public static final Integer DEFAULT_INDEX = 1;
    public static final Integer DIABLO_FREE = 0;
    public static final Integer DIABLO_NON_FREE = 1;
    public static final Integer DIABLO_FALSE = 0;
    public static final Integer DIABLO_TRUE = 1;
    public static final Integer DIABLO_FREE_COLOR = 0;
    public static final String  DIABLO_FREE_SIZE = "0";
    public static final Integer DIABLO_FREE_SIZE_GROUP = 0;
    public static final Integer DIABLO_MASTER = 1;
    public static final Integer DIABLO_USER = 2;

    public static final Integer DIABLO_FAMAN = 0;
    public static final Integer DIABLO_MAN = 1;

    public static final String  DIABLO_STRING_ZERO = "0";
    public static final String  DIABLO_INVALID_RSN = "INVALID_RSN";

    public static final String BUNDLE_PARAM_SALE_STOCK = "SALE_STOCK";
    public static final String BUNDLE_PARAM_SHOP       = "SHOP";
    public static final String BUNDLE_PARAM_RETAILER   = "RETAILER";
    public static final String BUNDLE_PARAM_ACTION     = "ACTION";
    public static final String BUNDLE_PARAM_COME_FORM  = "COME_FROM";
    public static final String BUNDLE_PARAM_RSN        = "RSN";
    public static final String BUNDLE_PARAM_FIRM       = "FIRM";

    public static final String BUNDLE_PARAM_GOOD       = "GOOD";
    public static final String BUNDLE_PARAM_ID         = "ID";
    public static final String BUNDLE_PARAM_IMMUTABLE_COLOR = "immutable_color";
    public static final String BUNDLE_PARAM_IMMUTABLE_SIZE = "immutable_size";

    //shop
    public static final Integer SHOP_ONLY = 0;
    public static final Integer REPO_ONLY = 1;
    public static final Integer REPO_BAD  = 2;
    public static final Integer BIND_NONE = -1;

    public static final String  EMPTY_STRING = "";

    // fling
    public static final Integer SWIPE_NONE = 0;
    public static final Integer SWIPE_TOP = 1;
    public static final Integer SWIPE_LEFT = 2;
    public static final Integer SWIPE_DOWN = 3;
    public static final Integer SWIPE_RIGHT = 4;

    public static final Integer SWIPE_MIN_DISTANCE = 500;
    public static final Integer SWIPE_THRESHOLD_VELOCITY = 500;

    public static final long DAY_SECONDS = 86400;

    // pagination
    public static final Integer DEFAULT_PAGE = 1;
    public static final Integer DEFAULT_ITEMS_PER_PAGE = 15;
    public static final Integer START_DEFAULT_INDEX = 1;

    // base setting
    public static final String  START_TIME = "qtime_start";
    public static final String  START_PRICE = "price_type";
    public static final String  START_RETAILER = "s_customer";
    public static final String  START_SHOW_DISCOUNT = "show_discount";
    public static final String  START_SHOW_PRICE_TYPE = "show_price_type";
    public static final String  START_TRACE_PRICE = "ptrace_price";
    public static final String  START_SYS_RETAILER = "s_customer";
    public static final String  START_REJECT_SECOND_ORDER = "reject_pgood";
    public static final String  START_BLUETOOTH = "bluetooth";
    public static final String  START_REVERSE_SALE_TITLE = "head_amount";
    public static final String  START_PAPER_WIDTH = "prn_width";
    public static final String  START_PRINT_HTTP_SERVER = "prn_server";
    public static final Integer NON_REPO = 0;
    public static final Integer USE_REPO = 1;

    public static final String DIABLO_CONFIG_YES = "1";
    public static final String DIABLO_CONFIG_NO  = "0";
    public static final String DIABLO_CONFIG_INVALID_INDEX = "-1";
    public static final String TAG_PRICE = "1";
    public static final String PKG_PRICE = "2";
    public static final String PRICE3    = "3";
    public static final String PRICE4    = "4";
    public static final String PRICE5    = "5";

    // on sale
    public static final Integer STARTING_SALE = 0;
    public static final Integer FINISHED_SALE = 1;
    public static final Integer UPDATING_SALE = 2;

    // use to update operation
    public static final String  ADD_THE_STOCK    = "a";
    public static final String  DELETE_THE_STOCK = "d";
    public static final String  UPDATE_THE_STOCK = "u";
    public static final String  NONE_THE_STOCK   = "n";

    // frame tag
    public static final String TAG_SALE_IN = "saleIn";
    public static final String TAG_SALE_OUT = "saleOut";
    public static final String TAG_SALE_DETAIL = "saleDetail";
    public static final String TAG_SALE_NOTE = "saleNote";
    public static final String TAG_SALE_DETAIL_TO_NOTE = "saleDetailToNote";

    public static final String TAG_STOCK_IN = "stockIn";
    public static final String TAG_STOCK_OUT = "stockOut";
    public static final String TAG_STOCK_DETAIL = "stockDetail";
    public static final String TAG_STOCK_NOTE = "stockNote";

    public static final String TAG_STOCK_SELECT = "stockSelect";
    public static final String TAG_SALE_IN_UPDATE = "saleInUpdate";
    public static final String TAG_SALE_OUT_UPDATE = "saleOutUpdate";

    public static final String TAG_STOCK_STORE_DETAIL = "stockStoreDetail";
    public static final String TAG_GOOD_NEW = "goodNew";
    public static final String TAG_GOOD_DETAIL = "goodDetail";
    public static final String TAG_GOOD_COLOR = "goodColor";
    public static final String TAG_GOOD_SIZE = "goodSize";

    public static final String TAG_RETAILER_DETAIL = "retailerDetail";
    public static final String TAG_RETAILER_NEW = "retailerNew";
    public static final String TAG_RETAILER_UPDATE = "retailerUpdate";
    public static final String TAG_RETAILER_CHECK_SALE_TRANS = "retailerCheckSaleTrans";

    public static final String TAG_FIRM_PAGER = "firmPager";
    public static final String TAG_FIRM_CHECK_STOCK_TRANS = "firmCheckStockTrans";
    public static final String TAG_PRINT_SETTING = "printSetting";
    public static final String TAG_PRINT_WITH_COMPUTER = "printWithCompute";

    /*
    * report
     */
    public static final String TAG_REPORT_REAL = "ReportReal";
    public static final String TAG_REPORT_DAILY = "ReportDaily";
    public static final String TAG_REPORT_MONTH = "ReportMonth";

    public static final String TAG_GOOD_SELECT = "goodSelect";
    public static final String TAG_STOCK_IN_UPDATE = "stockInUpdate";
    public static final String TAG_STOCK_OUT_UPDATE = "stockOutUpdate";
    public static final String TAG_GOOD_UPDATE = "goodUpdate";
    public static final String TAG_COLOR_SELECT = "colorSelect";
    public static final String TAG_SIZE_SELECT = "sizeSelect";

    public static final Integer SALE_IN = 0;
    public static final Integer SALE_OUT = 1;
    public static final Integer SALE_IN_UPDATE = 2;
    public static final Integer SALE_OUT_UPDATE = 3;

    public static final Integer SUCCESS = 0;
    public static final Integer HTTP_OK = 200;

    public static final Integer STOCK_IN = 0;
    public static final Integer STOCK_OUT = 1;
    public static final Integer STOCK_IN_UPDATE = 2;
    public static final Integer STOCK_OUT_UPDATE = 3;

    public static final Integer GOOD_NEW = 4;
    public static final Integer GOOD_UPDATE = 5;

    /**
     * printer
     */
    public static final String PRINTER_JOLIMARK = "jolimark";
    public static final Integer PRINTER_CONNECT_SUCCESS = 0;
    public static final Integer PRINTER_CONNECT_FAILED = 1;
    public static final Integer PRINTER_CONNECT_EMPTY_PARAMS = 2;
    

    /**
     * DB table
     */
    public static final String W_SALE = "w_sale";
    public static final String W_SALE_DETAIL = "w_sale_detail";
    public static final String W_SALE_DETAIL_AMOUNT = "w_sale_detail_amount";
    public static final String W_USER = "user";
    public static final String BLUETOOTH_PRINTER = "bluetooth_printer";


    /**
     * View type
     */
    public static final Integer DIABLO_TEXT = 1;
    public static final Integer DIABLO_EDIT = 2;
    public static final Integer DIABLO_AUTOCOMPLETE = 3;
    public static final Integer DIABLO_SPINNER = 4;

    private DiabloEnum(){

    }
}
