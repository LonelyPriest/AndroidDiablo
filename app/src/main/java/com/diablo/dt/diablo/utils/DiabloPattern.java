package com.diablo.dt.diablo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by buxianhui on 17/4/8.
 */

public class DiabloPattern {
    private final static String PATTERN_STYLE_NUMBER = "^(?!-)(?!.*?-$)[A-Za-z0-9-]{2,16}$";
    private final static String PATTERN_BRAND= "[\\u4e00-\\u9fa5A-Za-z0-9]{1,10}$";
    private final static String PATTERN_DECIMAL_2 = "^[+|\\-]?\\d+(.\\d{1,2})?$";

    private final static String PATTERN_TYPE= "[\\u4e00-\\u9fa5A-Za-z0-9]{1,15}$";
    private final static String PATTERN_FIRM = "^(?![0-9-])(?!.*?-$)[\\u4e00-\\u9fa5A-Za-z0-9-]{2,15}$";

    private final static String PATTERN_RETAILER_NAME = "^(?![0-9-])(?!.*?-$)[\\u4e00-\\u9fa5A-Za-z0-9-]{2,20}$";

    private final static String PATTERN_COLOR_NAME = "^[\\u4e00-\\u9fa5]{1,3}";

    private final static String PATTERN_PHONE = "^((13[0-9])|(15[^4])|(18[01235-9])|(17[0-8])|(147))\\d{8}$";
    private final static String PATTERN_PHONE_HK = "^(5|6|8|9)\\d{7}$";

    private final static String PATTERN_ADDRESS = "^(?!-)(?!.*?-$)[\\u4e00-\\u9fa5A-Za-z0-9-]{2,30}$";

    private final static String PATTERN_NOT_START_WITH_NUBMER = "^(?![0-9]).*$";

    public static boolean isValidStyleNumber(String styleNumber) {
        Pattern pattern = Pattern.compile(PATTERN_STYLE_NUMBER);
        Matcher matcher = pattern.matcher(styleNumber);
        return matcher.matches();
    }

    public static boolean isValidBrand(String brand) {
        Pattern pattern = Pattern.compile(PATTERN_BRAND);
        Matcher matcher = pattern.matcher(brand);
        return matcher.matches();
    }

    public static boolean isValidDecimal(String value) {
        Pattern pattern = Pattern.compile(PATTERN_DECIMAL_2);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isValidGoodType(String type) {
        Pattern pattern = Pattern.compile(PATTERN_TYPE);
        Matcher matcher = pattern.matcher(type);
        return matcher.matches();
    }

    public static boolean isValidFirm(String name) {
        Pattern pattern = Pattern.compile(PATTERN_FIRM);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidRetailer(String name) {
        Pattern pattern = Pattern.compile(PATTERN_RETAILER_NAME);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidColorName(String name) {
        Pattern pattern = Pattern.compile(PATTERN_COLOR_NAME);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPhone(String name) {
        Pattern pattern = Pattern.compile(PATTERN_PHONE);
        Pattern patternHK = Pattern.compile(PATTERN_PHONE_HK);
        return pattern.matcher(name).matches() || patternHK.matcher(name).matches();
    }

    public static boolean isValidAddress(String address) {
        Pattern pattern = Pattern.compile(PATTERN_ADDRESS);
        return pattern.matcher(address).matches();
    }

    public static boolean isNotStartWithNumber(String value) {
        Pattern pattern = Pattern.compile(PATTERN_NOT_START_WITH_NUBMER);
        return pattern.matcher(value).matches();
    }
}
