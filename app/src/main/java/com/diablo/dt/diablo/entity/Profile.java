package com.diablo.dt.diablo.entity;

import android.content.Context;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Profile {
    private static Profile mPofile;
    private static final String mSessionId = DiabloEnum.SESSION_ID;
    private static Integer mTableRows = DiabloEnum.ROW_SIZE;

    private static final String[] SIZE_TITLES = {"si", "sii", "siii", "siv", "sv", "svi"};

    private Profile() {}

    public static Profile instance() {
        if ( null == mPofile ){
            mPofile = new Profile();
        }
        return mPofile;
    }

    public static Integer getTableRows(){
        return Profile.mTableRows;
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
        mToken = Profile.mSessionId + "=" + token;
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
    private List<AuthenRight> mLoginRights;
    private List<AuthenShop> mLoginShops;


    private List<Integer> mAvailableShopIds = new ArrayList<>();
    private List<Integer> mShopIds = new ArrayList<>();
    private List<Integer> mBadRepoIds = new ArrayList<>();
    private List<Integer> mRepoIds = new ArrayList<>();

    private List<AuthenShop> mSortAvailableShop = new ArrayList<>();
    private List<AuthenShop> mSortShop = new ArrayList<>();
    private List<AuthenShop> mSortBadRepo = new ArrayList<>();
    private List<AuthenShop> mSortRepo = new ArrayList<>();

    // employee
    private List<Employee> mEmployees = new ArrayList<>();

    // retailer
    private List<Retailer> mRetailers = new ArrayList<>();

    // base settings
    private List<BaseSetting> mBaseSettings;

    // color
    private List<DiabloColor> mColors = new ArrayList<>();

    // size group
    private List<DiabloSizeGroup> mSizeGroups = new ArrayList<>();

    /*
    * Login user info
    * */
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

    public List<AuthenRight> getLoginRights(){
        return mLoginRights;
    }

    public void setLoginRights(List<AuthenRight> loginRights) {
        this.mLoginRights = loginRights;
    }

    public void setLoginShops(List<AuthenShop> loginShops) {
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

    public List<AuthenShop> getSortShop() {
        return mSortShop;
    }

    public List<AuthenShop> getSortRepo() {
        return mSortRepo;
    }

    public List<AuthenShop> getSortBadRepo() {
        return mSortBadRepo;
    }

    public List<AuthenShop> getSortAvailableShop() {
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
        for (AuthenShop shop: this.mLoginShops){
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
        for (AuthenShop shop: this.mLoginShops){
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
        for (AuthenShop shop: this.mLoginShops){
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
        for (AuthenShop shop: this.mLoginShops){
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

    /*
    * Employee
    * */
    public void setEmployees(List<Employee> employees) {
        for(Employee e: employees){
            if (e.getId().equals(mLoginEmployee)){
                mEmployees.add(0, e);
            } else {
                mEmployees.add(e);
            }
        }
    }

    public List<Employee> getEmployees(){
        return this.mEmployees;
    }

    public Integer getIndexOfEmployee(Integer employeeId){
        Integer index = DiabloEnum.INVALID_INDEX;
        for (Integer i=0; i<mEmployees.size(); i++){
            if (mEmployees.get(i).getId().equals(employeeId)){
                index = i;
                break;
            }
        }

        return index;
    }

    /*
    * Retailer
    * */
    public void setRetailers(List<Retailer> retailers){
        for (Retailer r: retailers){
            mRetailers.add(r);
        }
    }

    public List<Retailer> getRetailers(){
        return this.mRetailers;
    }

    public Retailer getRetailerById(Integer retailerId){
        Retailer retailer = null;
        for (Integer i=0; i<mRetailers.size(); i++){
            if (mRetailers.get(i).getId().equals(retailerId)){
                retailer = mRetailers.get(i);
                break;
            }
        }

        return retailer;
    }

    /*
    * Base setting
    * */

    public List<BaseSetting> getBaseSettings() {
        return this.mBaseSettings;
    }

    public void setBaseSettings(List<BaseSetting> baseSettings) {
        this.mBaseSettings = baseSettings;
    }

    public String getConfig(String name, String defaultValue){
        return this.getConfig(DiabloEnum.INVALID_INDEX, name, defaultValue);
    };

    public String getConfig(Integer shop, String name, String defaultValue){
        String find = defaultValue;
        for (BaseSetting base: mBaseSettings){
            if (base.getShop().equals(shop) && base.getEName().equals(name)){
               find = base.getValue();
            }
        }

        if (find.equals(defaultValue)){
            for (BaseSetting base: mBaseSettings){
                if (base.getShop().equals(DiabloEnum.INVALID_INDEX) && base.getEName().equals(name)){
                    find = base.getValue();
                }
            }
        }

        return find;
    };

    /**
     * color
     */
    public void setColors(List<DiabloColor> colors) {
        for(DiabloColor c: colors){
            mColors.add(c);
        }
    }

    public String getColorName(Integer colorId){
        String name = DiabloEnum.EMPTY_STRING;

        for (int i = 0; i < mColors.size(); i++) {
            if (mColors.get(i).getColorId().equals(colorId)){
                name = mColors.get(i).getName();
                break;
            }
        }
        return name;
    }

    public DiabloColor getColor(Integer colorId) {
        for (int i = 0; i < mColors.size(); i++) {
            if (mColors.get(i).getColorId().equals(colorId)){
               return mColors.get(i);
            }
        }

        return new DiabloColor();
    }

    /**
     * Size Group
     */
    public void setSizeGroups(List<DiabloSizeGroup> sizeGroups){
        for (DiabloSizeGroup s: sizeGroups){
            mSizeGroups.add(s);
        }

        for (DiabloSizeGroup s: mSizeGroups){
            s.genSortedSizeNames();
        }
    }

    public ArrayList<String> genSortedSizeNamesByGroups(String sizeGroups){
        ArrayList<String> sizes = new ArrayList<>();
        String [] groups = sizeGroups.split(DiabloEnum.SIZE_SEPARATOR);
        for (String gId : groups) {
            for (DiabloSizeGroup s: mSizeGroups){
                if (DiabloUtils.instance().toInteger(gId).equals(s.getGroupId())){
                    sizes.addAll(s.getSortedSizeNames());
                }
            }
        }

        return sizes;
    }
}
