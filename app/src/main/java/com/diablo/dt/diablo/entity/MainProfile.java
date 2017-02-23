package com.diablo.dt.diablo.entity;

import android.content.Context;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.response.LoginUserInfoResponse;

import java.util.List;

/**
 * Created by buxianhui on 17/2/22.
 */
public class MainProfile {
    private static MainProfile mPofile = new MainProfile();
    private static final String mSessionId = DiabloEnum.SESSION_ID;
    private static Integer mTableRows = DiabloEnum.ROW_SIZE;

    private MainProfile() {

    }

    public static MainProfile getInstance() {
        if ( null == mPofile ){
            mPofile = new MainProfile();
        }
        return mPofile;
    }

    public static Integer getTableRows(){
        return MainProfile.mTableRows;
    }

    // android context
    private Context mContext;
    // token from server
    private String mToken;

    public void setContext(Context context){
        this.mContext = context;
    }

    public Context getContext(){
        return this.mContext;
    }

    public void setToken(String token){
        mToken = MainProfile.mSessionId + "=" + token;
    }

    public String getToken(){
        return this.mToken;
    }

    public String getServer(){
        return this.mContext.getResources().getString(R.string.diablo_server);
    }

    // login information
    Integer mLoginShop;
    Integer mLoginFirm;
    Integer mLoginEmployee;
    Integer mLoginRetailer;
    Integer mLoginType;

    List<LoginUserInfoResponse.Right> mLoginRights;
    List<LoginUserInfoResponse.Shop> mLoginShops;

    public Integer getLoginShop() {
        return this.mLoginShop;
    }

    public void setLoginShop(Integer loginShop) {
        this.mLoginShop = loginShop;
    }

    public Integer getLoginFirm() {
        return this.mLoginFirm;
    }

    public void setLoginFirm(Integer loginFirm) {
        this.mLoginFirm = loginFirm;
    }

    public Integer getLoginEmployee() {
        return this.mLoginEmployee;
    }

    public void setLoginEmployee(Integer loginEmployee) {
        this.mLoginEmployee = loginEmployee;
    }

    public Integer getLoginRetailer() {
        return this.mLoginRetailer;
    }

    public void setLoginRetailer(Integer loginRetailer) {
        this.mLoginRetailer = loginRetailer;
    }

    public Integer getLoginType() {
        return this.mLoginType;
    }

    public void setLoginType(Integer loginType) {
        this.mLoginType = loginType;
    }

    public void setLoginRights(List<LoginUserInfoResponse.Right> loginRights) {
        this.mLoginRights = loginRights;
    }

    public void setLoginShops(List<LoginUserInfoResponse.Shop> loginShops) {
        this.mLoginShops = loginShops;
    }

}
