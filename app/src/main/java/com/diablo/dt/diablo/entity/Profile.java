package com.diablo.dt.diablo.entity;

import android.content.res.Resources;
import android.util.Log;

import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Profile {
    private static final String LOG_TAG = "Profile:";
    private static final String mSessionId = DiabloEnum.SESSION_ID;

    private static Profile mProfile;
    // private static Integer mTableRows = DiabloEnum.ROW_SIZE;

    // private static final String[] SIZE_TITLES = {"si", "sii", "siii", "siv", "sv", "svi"};

    private Profile() {

    }

    synchronized public static Profile instance() {
        if ( null == mProfile){
            mProfile = new Profile();
        }
        return mProfile;
    }

//    public static Integer getTableRows(){
//        return Profile.mTableRows;
//    }

    // android context
    // private Context mContext;
    // token from server
    private String mToken;
    // server
    private String mServer;
    // resource handler
    private Resources mResource;

//    public void setContext(Context context){
//        this.mContext = context;
//    }
//
//    public Context getContext(){
//        return this.mContext;
//    }

    public void setToken(String token){
        mToken = Profile.mSessionId + "=" + token;
    }

    public String getToken(){
        return this.mToken;
    }

    public void setServer(final String server) {
        this.mServer = server;
    }
    public final String getServer(){
        return mServer;
        // return mContext.getResources().getString(R.string.diablo_server);
    }

    public void setResource (Resources resource) {
        this.mResource = resource;
    }

    public Resources getResource() {
        return mResource;
    }

    // login information
    private Integer mLoginShop = DiabloEnum.INVALID_INDEX;
    private Integer mLoginFirm = DiabloEnum.INVALID_INDEX;
    private Integer mLoginEmployee = DiabloEnum.INVALID_INDEX;
    private Integer mLoginRetailer = DiabloEnum.INVALID_INDEX;
    private Integer mLoginType = DiabloEnum.INVALID_INDEX;
    private List<DiabloRight> mLoginRights;
    private List<DiabloShop> mLoginShops;


    private List<Integer> mAvailableShopIds = new ArrayList<>();
    private List<Integer> mShopIds = new ArrayList<>();
    private List<Integer> mBadRepoIds = new ArrayList<>();
    private List<Integer> mRepoIds = new ArrayList<>();

    private List<DiabloShop> mSortAvailableShop = new ArrayList<>();
    private List<DiabloShop> mSortShop = new ArrayList<>();
    private List<DiabloShop> mSortBadRepo = new ArrayList<>();
    private List<DiabloShop> mSortRepo = new ArrayList<>();

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

    // matched stocks
    private List<MatchStock> mMatchStocks;

    // brand
    private List<DiabloBrand> mBrands;

    // type
    private List<DiabloType> mDiabloTypes;

    // firm
    private List<Firm> mFirms;

    // matched goods
    private List<MatchGood> mMatchGoods;

    // class kind
    private List<DiabloColorKind> mColorKinds;

    // years
    private String [] mDiabloYears;

    // sale type
    private String [] mSaleTypes;

    // stock type
    private String [] mStockTypes;

    public void clear(){
        Log.d(LOG_TAG, "clear called");
        mLoginShop = DiabloEnum.INVALID_INDEX;
        mLoginFirm = DiabloEnum.INVALID_INDEX;
        mLoginEmployee = DiabloEnum.INVALID_INDEX;
        mLoginRetailer = DiabloEnum.INVALID_INDEX;
        mLoginType = DiabloEnum.INVALID_INDEX;
        mLoginRights = null;
        mLoginShops = null;

        mAvailableShopIds.clear();
        mShopIds.clear();
        mBadRepoIds.clear();
        mRepoIds.clear();

        mSortAvailableShop.clear();
        mSortShop.clear();
        mSortBadRepo.clear();
        mSortRepo.clear();

        // employee
        mEmployees.clear();

        // retailer
        // mRetailers.clear();
        clearRetailers();

        // base settings
        mBaseSettings.clear();

        // color
        mColors.clear();

        // size group
        mSizeGroups.clear();

        // matched stocks
        if (null != mMatchStocks) {
            mMatchStocks.clear();
        }

        // brands
        if (null != mBrands) {
            mBrands.clear();
        }

        // types
        if (null != mDiabloTypes) {
            mDiabloTypes.clear();
        }

        // firms
        clearFirms();
//        if (null != mFirms) {
//            mFirms.clear();
//        }

        // matched goods
        if (null != mMatchGoods) {
            mMatchGoods.clear();
        }

        // color kind
        if (null != mColorKinds) {
            mColorKinds.clear();
        }
    }

    /*
    * Login user info
    * */
    public Integer getLoginShop() {
        if ( mLoginShop.equals(DiabloEnum.INVALID_INDEX) ){
          mLoginShop = Profile.instance().getAvailableShopIds().get(0);
        }
        return mLoginShop;
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

    public List<DiabloRight> getLoginRights(){
        return mLoginRights;
    }

    public void setLoginRights(List<DiabloRight> loginRights) {
        this.mLoginRights = loginRights;
    }

    public void setLoginShops(List<DiabloShop> loginShops) {
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

    public List<DiabloShop> getSortShop() {
        return mSortShop;
    }

    public List<DiabloShop> getSortRepo() {
        return mSortRepo;
    }

    public List<DiabloShop> getSortBadRepo() {
        return mSortBadRepo;
    }

    public List<DiabloShop> getSortAvailableShop() {
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
        for (DiabloShop shop: this.mLoginShops){
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
        for (DiabloShop shop: this.mLoginShops){
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
        for (DiabloShop shop: this.mLoginShops){
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
        for (DiabloShop shop: this.mLoginShops){
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

//    private Integer getRetailerIndex(Integer retailerId){
//        Integer index = null;
//        for (Integer i=0; i<mRetailers.size(); i++){
//            if (mRetailers.get(i).getId().equals(retailerId)){
//                index = i;
//                break;
//            }
//        }
//
//        return index;
//    }

    public void addRetailer(Retailer retailer) {
        mRetailers.add(0, retailer);
    }

    public void replaceRetailer(Retailer newRetailer, Integer oldRetailer) {
        Retailer old = getRetailerById(oldRetailer);
        if (null != old) {
            mRetailers.remove(old);
        }
        mRetailers.add(newRetailer);
    }

    public void clearRetailers() {
        if (null != mRetailers){
            mRetailers.clear();
        }
    }

    /*
    * Base setting
    * */

//    public List<BaseSetting> getBaseSettings() {
//        return this.mBaseSettings;
//    }

    public void setBaseSettings(List<BaseSetting> baseSettings) {
        this.mBaseSettings = baseSettings;
    }

    public String getConfig(String name, String defaultValue){
        return this.getConfig(DiabloEnum.INVALID_INDEX, name, defaultValue);
    }

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
    }

    /**
     * color
     */
    public void setColors(List<DiabloColor> colors) {
        for(DiabloColor c: colors){
            mColors.add(c);
        }

        // add free color
        // mColors.add(new DiabloColor());

    }

    public void addColor(DiabloColor color) {
        if (null != getColor(color.getColorId())) {
            mColors.add(0, color);
        }
    }

    public final List<DiabloColor> getColors() {
        return mColors;
    }

    public final String getColorName(Integer colorId){
        String name = DiabloEnum.EMPTY_STRING;

        for (int i = 0; i < mColors.size(); i++) {
            if (mColors.get(i).getColorId().equals(colorId)){
                name = mColors.get(i).getName();
                break;
            }
        }
        return name;
    }

    public final DiabloColor getColor(Integer colorId) {
        DiabloColor found = null;
        for (int i = 0; i < mColors.size(); i++) {
            if (mColors.get(i).getColorId().equals(colorId)){
                found = mColors.get(i);
                break;
            }
        }

        return null == found ? new DiabloColor() : found;
    }

    /**
     * Size Group
     */
    public void setSizeGroups(List<DiabloSizeGroup> sizeGroups){
        for (DiabloSizeGroup s: sizeGroups){
            mSizeGroups.add(s);
        }

        // add free group
        DiabloSizeGroup freeSizeGroup = new DiabloSizeGroup();
        freeSizeGroup.initWithFreeSizeGroup();
        mSizeGroups.add(freeSizeGroup);

        for (DiabloSizeGroup s: mSizeGroups){
            s.genSortedSizeNames();
        }
    }

    public final List<DiabloSizeGroup> getSizeGroups() {
        return mSizeGroups;
    }

    public final DiabloSizeGroup getSizeGroup(Integer groupId) {
        DiabloSizeGroup find = null;
        for (DiabloSizeGroup group: mSizeGroups) {
            if (group.getGroupId().equals(groupId)) {
                find = group;
            }
        }

        return find;
    }

    public final ArrayList<String> genSortedSizeNamesByGroups(String sizeGroups){
        ArrayList<String> sizes = new ArrayList<>();
        String [] groups = sizeGroups.split(DiabloEnum.SIZE_SEPARATOR);
        for (String gId : groups) {
            for (DiabloSizeGroup s: mSizeGroups){
                if (DiabloUtils.instance().toInteger(gId).equals(s.getGroupId())){
                    for (String name: s.getSortedSizeNames()){
                        if (!sizes.contains(name)){
                            sizes.add(name);
                        }
                    }
                }
            }
        }

        return sizes;
    }

    /**
     * match stock
     */
    public List<MatchStock> getMatchStocks() {
        return mMatchStocks;
    }

    public void setMatchStocks(List<MatchStock> mMatchStocks) {
        this.mMatchStocks = new ArrayList<>(mMatchStocks);
    }

    public MatchStock getMatchStock(String styleNumber, Integer brandId){
        MatchStock stock = null;
        for (MatchStock m: mMatchStocks){
            if (styleNumber.equals(m.getStyleNumber())
                    && brandId.equals(m.getBrandId())){
                stock = m;
                break;
            }
        }

        return stock;
    }

    public void addMatchStock(MatchStock stock) {
        if ( null == getMatchStock(stock.getStyleNumber(), stock.getBrandId()) ) {
            mMatchStocks.add(0, stock);
        }
    }

    public MatchStock removeMatchStock(String styleNumber, Integer brandId) {
        MatchStock stock = getMatchStock(styleNumber, brandId);
        if (null != stock) {
            mMatchStocks.remove(stock);
        }

        return stock;
    }

    /**
     * brands
     */
    public void  setBrands(List<DiabloBrand> brands) {
        this.mBrands = new ArrayList<>(brands);
    }

    public void addBrand(DiabloBrand brand) {
        if (null == getBrand(brand.getId())) {
            this.mBrands.add(0, brand);
        }
    }

    public List<DiabloBrand> getBrands() {
        return mBrands;
    }

    public DiabloBrand getBrand(Integer brandId) {
        DiabloBrand brand = null;
        for (DiabloBrand b: mBrands) {
            if (b.getId().equals(brandId)) {
                brand = b;
                break;
            }
        }

        return brand;
    }

    public DiabloBrand getBrand(String name) {
        DiabloBrand brand = null;
        for (DiabloBrand b: mBrands) {
            if (b.getName().equals(name)) {
                brand = b;
                break;
            }
        }

        return brand;
    }

    /**
     * types
     */
    public void setDiabloTypes(List<DiabloType> types) {
        this.mDiabloTypes = new ArrayList<>(types);
    }

    public void addDiabloType(DiabloType type) {
        if (null == getDiabloType(type.getId())) {
            this.mDiabloTypes.add(0, type);
        }
    }

    public List<DiabloType> getDiabloTypes() {
        return mDiabloTypes;
    }

    public DiabloType getDiabloType(Integer typeId) {
        DiabloType type = null;
        for (DiabloType t: mDiabloTypes) {
            if (t.getId().equals(typeId)) {
                type = t;
                break;
            }
        }

        return type;
    }

    public DiabloType getDiabloType(String name) {
        DiabloType type = null;
        for (DiabloType t: mDiabloTypes) {
            if (t.getName().equals(name)) {
                type = t;
                break;
            }
        }

        return type;
    }

    /**
     * firms
     */
    public void setFirms(List<Firm> firms) {
        this.mFirms = new ArrayList<>(firms);
    }

    public void clearFirms() {
        if (null != mFirms) {
            mFirms.clear();
        }
    }

    public void addFirm(Firm firm) {
        if (null == getFirm(firm.getId())){
            this.mFirms.add(0, firm);
        }
    }

    public List<Firm> getFirms() {
        return mFirms;
    }

    public Firm getFirm(Integer firmId) {
        Firm firm = null;
        for (Firm f: mFirms) {
            if (f.getId().equals(firmId)) {
                firm = f;
                break;
            }
        }

        return firm;
    }

    public Firm getFirm(String name) {
        Firm firm = null;
        for (Firm f: mFirms) {
            if (f.getName().equals(name)) {
                firm = f;
                break;
            }
        }

        return firm;
    }


    /**
     * match good
     */
    public void setMatchGoods(List<MatchGood> goods) {
        this.mMatchGoods = new ArrayList<>(goods);
    }

    public void addMatchGood(MatchGood good) {
        if (null == getMatchGood(good.getStyleNumber(), good.getBrandId())) {
            mMatchGoods.add(0, good);
        }
    }

    public void removeMatchGood(String styleNumber, Integer brandId) {
        MatchGood found = getMatchGood(styleNumber, brandId);
        if (null != found) {
            mMatchGoods.remove(found);
        }
    }

    public List<MatchGood> getMatchGoods() {
        return mMatchGoods;
    }

    public MatchGood getMatchGood(String styleNumber, Integer brandId){
        MatchGood good = null;
        for (MatchGood g: mMatchGoods){
            if (styleNumber.equals(g.getStyleNumber())
                && brandId.equals(g.getBrandId())){
                good = g;
                break;
            }
        }

        return good;
    }

    /**
     * color kind
     */

    public final List<DiabloColorKind> getColorKinds() {
        return mColorKinds;
    }

    public final DiabloColorKind getColorKind(Integer kindId) {
        DiabloColorKind kind = null;
        for (DiabloColorKind k: mColorKinds) {
            if (k.getId().equals(kindId)) {
                kind = k;
            }
        }

        return kind;
    }

    public void setColorKinds(List<DiabloColorKind> colorKinds) {
        this.mColorKinds = new ArrayList<>(colorKinds);
    }

    // years
    public void setDiabloYears(String [] years) {
        this.mDiabloYears = years.clone();
    }

    public String[] getDiabloYears() {
        return this.mDiabloYears;
    }

    // sale types
    public void setSaleTypes(String [] types) {
        this.mSaleTypes = types.clone();
    }

    public String[] getSaleTypes() {
        return this.mSaleTypes;
    }

    // stock types
    public void setStockTypes(String [] types) {
        this.mStockTypes = types.clone();
    }

    public String[] getStockTypes() {
        return this.mStockTypes;
    }
}
