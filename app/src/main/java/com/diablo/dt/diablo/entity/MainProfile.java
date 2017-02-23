package com.diablo.dt.diablo.entity;

import android.content.Context;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.response.LoginUserInfoResponse;

import java.util.ArrayList;
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
    private Integer mLoginShop = DiabloEnum.INVALID_INDEX;
    private Integer mLoginFirm = DiabloEnum.INVALID_INDEX;
    private Integer mLoginEmployee = DiabloEnum.INVALID_INDEX;
    private Integer mLoginRetailer = DiabloEnum.INVALID_INDEX;
    private Integer mLoginType = DiabloEnum.INVALID_INDEX;
    private List<LoginUserInfoResponse.Right> mLoginRights;
    private List<LoginUserInfoResponse.Shop> mLoginShops;


    private List<Integer> mAvailableShopIds = new ArrayList<>();
    private List<Integer> mShopIds = new ArrayList<>();
    private List<Integer> mBadRepoIds = new ArrayList<>();
    private List<Integer> mRepoIds = new ArrayList<>();

    private List<LoginUserInfoResponse.Shop> mSortAvailableShop = new ArrayList<>();
    private List<LoginUserInfoResponse.Shop> mSortShop = new ArrayList<>();
    private List<LoginUserInfoResponse.Shop> mSortBadRepo = new ArrayList<>();
    private List<LoginUserInfoResponse.Shop> mSortRepo = new ArrayList<>();


    public Integer getmLoginRetailer() {
        return mLoginRetailer;
    }

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

    public List<LoginUserInfoResponse.Right> getLoginRights(){
        return mLoginRights;
    }

    public void setLoginRights(List<LoginUserInfoResponse.Right> loginRights) {
        this.mLoginRights = loginRights;
    }

    public void setLoginShops(List<LoginUserInfoResponse.Shop> loginShops) {
        this.mLoginShops = loginShops;
    }

    public List<Integer> getAvailableShopIds() {
        return mAvailableShopIds;
    }

    public List<Integer> getShopIds() {
        return mShopIds;
    }

    public List<Integer> getBadRepoIds() {
        return mBadRepoIds;
    }

    public List<Integer> getRepoIds() {
        return mRepoIds;
    }

    public List<LoginUserInfoResponse.Shop> getSortShop() {
        return mSortShop;
    }

    public List<LoginUserInfoResponse.Shop> getSortRepo() {
        return mSortRepo;
    }

    public List<LoginUserInfoResponse.Shop> getSortBadRepo() {
        return mSortBadRepo;
    }

    public List<LoginUserInfoResponse.Shop> getSortAvailableShop() {
        return mSortAvailableShop;
    }

    public void initLoginUser(){
        setAllAvailableShop();
        setAllShop();
        setAllRepo();
        setAllBadRepo();
    }

    // shop without any repo bind and repo only
    private void setAllAvailableShop(){
        for (LoginUserInfoResponse.Shop shop: this.mLoginShops){
            if ( ((shop.getType().equals(DiabloEnum.SHOP_ONLY)
                    && shop.getRepo().equals(DiabloEnum.BIND_NONE))
                    || shop.getType().equals(DiabloEnum.REPO_ONLY)) ){
                if (!this.mAvailableShopIds.contains(shop.getShop())){
                    if (shop.getShop().equals(this.mLoginShop)){
                        this.mAvailableShopIds.add(0, shop.getShop());
                    } else {
                        this.mAvailableShopIds.add(shop.getShop());
                    }
                }

                if (!this.mSortAvailableShop.contains(shop)) {
                    if (shop.getShop().equals(this.mLoginShop)){
                        this.mSortAvailableShop.add(0, shop);
                    } else {
                        this.mSortAvailableShop.add(shop);
                    }
                }
            }
        }
    }

    // shop or shop that bind to repo
    private void setAllShop(){
        for (LoginUserInfoResponse.Shop shop: this.mLoginShops){
            if ( shop.getType().equals(DiabloEnum.SHOP_ONLY) ){
                if (!this.mShopIds.contains(shop.getShop())){
                    if (shop.getShop().equals(this.mLoginShop)){
                        this.mShopIds.add(0, shop.getShop());
                    } else {
                        this.mShopIds.add(shop.getShop());
                    }
                }

                if (!this.mSortShop.contains(shop)){
                    if (shop.getShop().equals(this.mLoginShop)){
                        this.mSortShop.add(0, shop);
                    } else {
                        this.mSortShop.add(shop);
                    }
                }
            }
        }
    }

    // repo only
    private void setAllRepo(){
        for (LoginUserInfoResponse.Shop shop: this.mLoginShops){
            if ( shop.getType().equals(DiabloEnum.REPO_ONLY ) ){
                if (!this.mRepoIds.contains(shop.getShop())){
                    this.mRepoIds.add(shop.getShop());
                }

                if (!this.mSortRepo.contains(shop)){
                    this.mSortRepo.add(shop);
                }
            }
        }
    }

    // bad repo only
    private void setAllBadRepo(){
        for (LoginUserInfoResponse.Shop shop: this.mLoginShops){
            if ( shop.getType().equals(DiabloEnum.REPO_BAD) ){
                if (!this.mBadRepoIds.contains(shop.getShop())){
                    this.mBadRepoIds.add(shop.getShop());
                }
                if (!this.mSortBadRepo.contains(shop)){
                    this.mSortBadRepo.add(shop);
                }
            }
        }
    }
}
