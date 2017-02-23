package com.diablo.dt.diablo.utils;

import com.diablo.dt.diablo.response.LoginUserInfoResponse;

import java.util.List;

/**
 * Created by buxianhui on 17/2/24.
 */

public class DiabloUtils {
    private static DiabloUtils mInstance;

    public static DiabloUtils getInstance() {
        if (null == mInstance){
            mInstance = new DiabloUtils();
        }

        return mInstance;
    }

    private DiabloUtils() {

    }

    public LoginUserInfoResponse.Shop getShop(List<LoginUserInfoResponse.Shop> shops, Integer index){
        LoginUserInfoResponse.Shop shop = null;
        for ( Integer i = 0; i < shops.size(); i++){
            if (index.equals(shops.get(i).getShop())){
                shop = shops.get(i);
                break;
            }
        }

        return shop;
    }
}
