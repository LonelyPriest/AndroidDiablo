package com.diablo.dt.diablo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.diablo.dt.diablo.entity.DiabloUser;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.sale.SaleCalc;
import com.diablo.dt.diablo.model.sale.SaleStock;
import com.diablo.dt.diablo.model.sale.SaleStockAmount;
import com.diablo.dt.diablo.model.sale.SaleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/16.
 */
public class DiabloDBManager {
    private static DiabloDBManager mDiabloDBManager;

    private SQLiteDatabase mSQLiteDB;
    private DiabloUtils utils = DiabloUtils.instance();

    public static DiabloDBManager instance(){
        if (null == mDiabloDBManager){
            mDiabloDBManager = new DiabloDBManager();
        }

        return mDiabloDBManager;
    }

    public void init (Context context) {
        if (null != mSQLiteDB){
            mSQLiteDB.close();
        }

        this.mSQLiteDB = DiabloDBOpenHelper.instance(context).getWritableDatabase();
        // DiabloUtils.instance().makeToast(context, mSQLiteDB.getPath());
    }

    private DiabloDBManager() {

    }

    public long addSaleCalc(SaleCalc calc){
        ContentValues v = new ContentValues();
        v.put("retailer", calc.getRetailer());
        v.put("shop", calc.getShop());
        v.put("comment", calc.getComment());
        return mSQLiteDB.insert(DiabloEnum.W_SALE, "comment", v);
    }

    public boolean deleteSaleCalc(SaleCalc calc){
        String [] args = {utils.toString(calc.getRetailer()), utils.toString(calc.getShop())};
        return mSQLiteDB.delete(DiabloEnum.W_SALE, "retailer=? and shop=?", args) > 0;
    }

    public SaleCalc querySaleCalc(SaleCalc calc){
        String [] fields = {"retailer", "shop", "comment"};
        String [] args = {utils.toString(calc.getRetailer()), utils.toString(calc.getShop())};

        Cursor cursor = mSQLiteDB.query(DiabloEnum.W_SALE, fields, "retailer=? and shop=?", args, null, null, null);
        if (cursor.moveToFirst()){
            SaleCalc result = new SaleCalc();
            result.setRetailer(cursor.getInt(cursor.getColumnIndex("retailer")));
            result.setShop(cursor.getInt(cursor.getColumnIndex("shop")));
            result.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            cursor.close();

            return result;
        }

        return null;

    }

    public List<SaleCalc> queryAllSaleCalc(){
        String [] fields = {"retailer", "shop", "comment"};
        List<SaleCalc> calcs = new ArrayList<>();

        Cursor cursor = mSQLiteDB.query(DiabloEnum.W_SALE, fields, null, null, null, null, null);
        try {
            while (cursor.moveToNext()){
                SaleCalc c = new SaleCalc();
                c.setRetailer(cursor.getInt(cursor.getColumnIndex("retailer")));
                c.setShop(cursor.getInt(cursor.getColumnIndex("shop")));
                c.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                calcs.add(c);
            }
        } finally {
            cursor.close();
        }

        return 0 == calcs.size() ? null : calcs;
    }

    public void deleteAllSaleStock(SaleCalc calc){
        mSQLiteDB.beginTransaction();
        try {
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL, "retailer=? and shop=?", args);
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL_AMOUNT, "retailer=? and shop=?", args);
            String sql0 = "delete from " + DiabloEnum.W_SALE_DETAIL
                    + " where retailer=? and shop=?";
            SQLiteStatement s0 = mSQLiteDB.compileStatement(sql0);
            s0.bindString(1, utils.toString(calc.getRetailer()));
            s0.bindString(2, utils.toString(calc.getShop()));
            s0.execute();
            s0.clearBindings();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL_AMOUNT
                    + " where retailer=? and shop=?";
            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
            s1.bindString(1, utils.toString(calc.getRetailer()));
            s1.bindString(2, utils.toString(calc.getShop()));
            s1.execute();
            s1.clearBindings();
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    public void clearRetailerSaleStock(SaleCalc calc){
        mSQLiteDB.beginTransaction();
        try {
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL, "retailer=? and shop=?", args);
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL_AMOUNT, "retailer=? and shop=?", args);
            String sql0 = "delete from " + DiabloEnum.W_SALE
                + " where retailer=? and shop=?";
            SQLiteStatement s0 = mSQLiteDB.compileStatement(sql0);
            s0.bindString(1, utils.toString(calc.getRetailer()));
            s0.bindString(2, utils.toString(calc.getShop()));
            s0.execute();
            s0.clearBindings();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL
                + " where retailer=? and shop=?";
            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
            s1.bindString(1, utils.toString(calc.getRetailer()));
            s1.bindString(2, utils.toString(calc.getShop()));
            s1.execute();
            s1.clearBindings();

            String sql2 = "delete from " + DiabloEnum.W_SALE_DETAIL_AMOUNT
                + " where retailer=? and shop=?";
            SQLiteStatement s2 = mSQLiteDB.compileStatement(sql2);
            s2.bindString(1, utils.toString(calc.getRetailer()));
            s2.bindString(2, utils.toString(calc.getShop()));
            s2.execute();
            s2.clearBindings();
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    public void deleteSaleStock(SaleCalc calc, SaleStock stock){

//        String [] args = {
//                utils.toString(calc.getRetailer()),
//                utils.toString(calc.getShop()),
//                stock.getStyleNumber(),
//                utils.toString(stock.getBrandId())};

        mSQLiteDB.beginTransaction();
        try {
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL, "retailer=? and shop=?", args);
//            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL_AMOUNT, "retailer=? and shop=?", args);
//            mSQLiteDB.setTransactionSuccessful();
            String sql0 = "delete from " + DiabloEnum.W_SALE_DETAIL
                    + " where"
                    + " retailer=? and shop=? and style_number=? and brand=?";
            SQLiteStatement s0 = mSQLiteDB.compileStatement(sql0);
            s0.bindString(1, utils.toString(calc.getRetailer()));
            s0.bindString(2, utils.toString(calc.getShop()));
            s0.bindString(3, stock.getStyleNumber());
            s0.bindString(4, utils.toString(stock.getBrandId()));
            s0.execute();
            s0.clearBindings();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL_AMOUNT
                    + " where"
                    + " retailer=? and shop=? and style_number=? and brand=?";
            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
            s1.bindString(1, utils.toString(calc.getRetailer()));
            s1.bindString(2, utils.toString(calc.getShop()));
            s1.bindString(3, stock.getStyleNumber());
            s1.bindString(4, utils.toString(stock.getBrandId()));
            s1.execute();
            s1.clearBindings();
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

//    private SaleStock getSaleStocks(List<SaleStock> stocks, String styleNumber, Integer brandId){
//        SaleStock stock = null;
//        for (SaleStock s: stocks){
//            if (styleNumber.equals(s.getStyleNumber()) && brandId.equals(s.getBrandId())){
//                stock = s;
//                break;
//            }
//        }
//
//        return stock;
//    }

    public List<SaleStock> querySaleStock(SaleCalc calc){
        String sql0 = "select a.style_number, a.brand, a.sell_type, a.second, a.discount, a.price"
                + ", b.color, b.size, b.exist, b.total"
                + " from w_sale_detail a"
                + " left join w_sale_detail_amount b"
                + " on a.retailer=b.retailer and a.shop=b.shop"
                + " and a.style_number=b.style_number"
                + " and a.brand=b.brand"
                + " where a.retailer=? and a.shop=?";
        Cursor c = mSQLiteDB.rawQuery(
                sql0,
                new String[] {utils.toString(calc.getRetailer()), utils.toString(calc.getShop())});

        List<SaleStock> saleStocks = new ArrayList<>();
        Integer orderId = 0;
        try {
            while (c.moveToNext()){
                String styleNumber = c.getString(c.getColumnIndex("style_number"));
                Integer brand = c.getInt(c.getColumnIndex("brand"));

                SaleStock stock = SaleUtils.getSaleStocks(saleStocks, styleNumber, brand);
                if (null == stock){
                    MatchStock matchStock = Profile.instance().getMatchStock(styleNumber, brand);

                    Integer selectPrice = c.getInt(c.getColumnIndex("sell_type"));
                    SaleStock s = new SaleStock(matchStock, selectPrice);

                    orderId++;
                    s.setOrderId(orderId);
                    s.setState(DiabloEnum.FINISHED_SALE);
                    s.setSecond(c.getInt(c.getColumnIndex("second")));
                    s.setDiscount(c.getFloat(c.getColumnIndex("discount")));
                    s.setFinalPrice(c.getFloat(c.getColumnIndex("price")));

                    SaleStockAmount amount = new SaleStockAmount(
                            c.getInt(c.getColumnIndex("color")),
                            c.getString(c.getColumnIndex("size")));

                    Integer saleTotal = c.getInt(c.getColumnIndex("total"));
                    Integer stockExist = c.getInt(c.getColumnIndex("exist"));
                    amount.setSellCount(saleTotal);
                    amount.setStock(stockExist);

                    s.setSaleTotal(saleTotal);
                    s.setStockExist(stockExist);
                    s.addAmount(amount);
                    saleStocks.add(s);
                } else {
                    SaleStockAmount amount = new SaleStockAmount(
                            c.getInt(c.getColumnIndex("color")),
                            c.getString(c.getColumnIndex("size")));

                    Integer saleTotal = c.getInt(c.getColumnIndex("total"));
                    Integer stockExist = c.getInt(c.getColumnIndex("exist"));
                    amount.setSellCount(saleTotal);
                    amount.setStock(stockExist);

                    stock.setSaleTotal(stock.getSaleTotal() + saleTotal);
                    stock.setStockExist(stock.calcExistStock() + stockExist);
                    stock.addAmount(amount);
                }
            }
        } finally {
            c.close();
        }

        return saleStocks.size() == 0 ? null : saleStocks;

    }

    public void replaceSaleStock(SaleCalc calc, SaleStock stock, Integer startRetailer) {
        mSQLiteDB.beginTransaction();

        try {
            String sql0 = "delete from " + DiabloEnum.W_SALE_DETAIL
                    + " where"
                    + " retailer=? and shop=? and style_number=? and brand=?";
            SQLiteStatement s0 = mSQLiteDB.compileStatement(sql0);
            s0.bindString(1, utils.toString(calc.getRetailer()));
            s0.bindString(2, utils.toString(calc.getShop()));
            s0.bindString(3, stock.getStyleNumber());
            s0.bindString(4, utils.toString(stock.getBrandId()));
            s0.execute();
            s0.clearBindings();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL_AMOUNT
                    + " where"
                    + " retailer=? and shop=? and style_number=? and brand=?";
            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
            s1.bindString(1, utils.toString(calc.getRetailer()));
            s1.bindString(2, utils.toString(calc.getShop()));
            s1.bindString(3, stock.getStyleNumber());
            s1.bindString(4, utils.toString(stock.getBrandId()));
            s1.execute();
            s1.clearBindings();

            String sql2 = "insert into " + DiabloEnum.W_SALE_DETAIL
                    + "(retailer, shop, style_number, brand, sell_type, second, discount, price)"
                    + " values(?, ?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement s2 = mSQLiteDB.compileStatement(sql2);
            s2.bindString(1, utils.toString(calc.getRetailer()));
            s2.bindString(2, utils.toString(calc.getShop()));
            s2.bindString(3, stock.getStyleNumber());
            s2.bindString(4, utils.toString(stock.getBrandId()));
            s2.bindString(5, utils.toString(stock.getSelectedPrice()));
            s2.bindString(6, utils.toString(stock.getSecond()));
            s2.bindString(7, utils.toString(stock.getDiscount()));
            s2.bindString(8, utils.toString(stock.getFinalPrice()));
            s2.execute();
            s2.clearBindings();

            String sql3 = "insert into " + DiabloEnum.W_SALE_DETAIL_AMOUNT
                    + "(retailer, shop, style_number, brand, color, size, exist, total)"
                    + " values(?, ?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement s3 = mSQLiteDB.compileStatement(sql3);

            for (SaleStockAmount a : stock.getAmounts()) {
                s3.bindString(1, utils.toString(calc.getRetailer()));
                s3.bindString(2, utils.toString(calc.getShop()));
                s3.bindString(3, stock.getStyleNumber());
                s3.bindString(4, utils.toString(stock.getBrandId()));

                s3.bindString(5, utils.toString(a.getColorId()));
                s3.bindString(6, a.getSize());
                s3.bindString(7, utils.toString(a.getStock()));
                s3.bindString(8, utils.toString(a.getSellCount()));
                s3.execute();
                s3.clearBindings();
            }

            if (!calc.getRetailer().equals(startRetailer)) {
                String sql4 = "update " + DiabloEnum.W_SALE + " set retailer=? where retailer=?";
                SQLiteStatement s4 = mSQLiteDB.compileStatement(sql4);
                s4.bindString(1, utils.toString(calc.getRetailer()));
                s4.bindString(2, utils.toString(startRetailer));
                s4.execute();
                s4.clearBindings();

                String sql5 = "update " + DiabloEnum.W_SALE_DETAIL + " set retailer=? where retailer=?";
                SQLiteStatement s5 = mSQLiteDB.compileStatement(sql5);
                s5.bindString(1, utils.toString(calc.getRetailer()));
                s5.bindString(2, utils.toString(startRetailer));
                s5.execute();
                s5.clearBindings();

                String sql6 = "update " + DiabloEnum.W_SALE_DETAIL_AMOUNT + " set retailer=? where retailer=?";
                SQLiteStatement s6 = mSQLiteDB.compileStatement(sql6);
                s6.bindString(1, utils.toString(calc.getRetailer()));
                s6.bindString(2, utils.toString(startRetailer));
                s6.execute();
                s6.clearBindings();
            }
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
        // mSQLiteDB.endTransaction();
    }

    public void clearAll(){
        mSQLiteDB.beginTransaction();
        try {
            String sql0 = "delete from " + DiabloEnum.W_SALE;
            SQLiteStatement s0 = mSQLiteDB.compileStatement(sql0);
            s0.execute();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL;
            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
            s1.execute();

            String sql2 = "delete from " + DiabloEnum.W_SALE_DETAIL_AMOUNT;
            SQLiteStatement s2 = mSQLiteDB.compileStatement(sql2);
            s2.execute();
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    public void addUser(String name, String password) {
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("password", password);
        mSQLiteDB.insert(DiabloEnum.W_USER, null, v);
    }

    public DiabloUser getFirstLoginUser(){
        String [] fields = {"name", "password"};

        Cursor cursor = mSQLiteDB.query(DiabloEnum.W_USER, fields, null, null, null, null, null);
        if (cursor.moveToFirst()){
            DiabloUser user = new DiabloUser();
            user.setName(cursor.getString(cursor.getColumnIndex("name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            cursor.close();

            return user;
        }

        return null;

    }

    public void updateUser(String name, String password) {
        mSQLiteDB.beginTransaction();
        try {
            String sql = "update " + DiabloEnum.W_USER + " set password=? where name=?";
            SQLiteStatement s = mSQLiteDB.compileStatement(sql);
            s.bindString(1, name);
            s.bindString(2, password);
            s.execute();
            s.clearBindings();

            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    public void clearUser() {
        mSQLiteDB.beginTransaction();
        try {
            String sql = "delete from " + DiabloEnum.W_USER;
            SQLiteStatement s = mSQLiteDB.compileStatement(sql);
            s.execute();
            s.clearBindings();

            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    synchronized public void close(){
        if (null != mSQLiteDB) {
            mSQLiteDB.close();
        }
    }
}
