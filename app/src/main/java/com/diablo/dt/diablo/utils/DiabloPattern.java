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
}
