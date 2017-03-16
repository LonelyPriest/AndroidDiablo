package com.diablo.dt.diablo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.diablo.dt.diablo.entity.SaleCalc;
import com.diablo.dt.diablo.entity.SaleStock;
import com.diablo.dt.diablo.entity.SaleStockAmount;

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

    public void deleteAllSaleStock(SaleCalc calc){
        String [] args = { utils.toString(calc.getRetailer()), utils.toString(calc.getShop())};
        mSQLiteDB.beginTransaction();
        try {
            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL, "retailer=? and shop=?", args);
            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL_AMOUNT, "retailer=? and shop=?", args);
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }

    public void deleteSaleStock(SaleCalc calc, SaleStock stock){
        String [] args = {
                utils.toString(calc.getRetailer()),
                utils.toString(calc.getShop()),
                stock.getStyleNumber(),
                utils.toString(stock.getBrandId())};

        mSQLiteDB.beginTransaction();
        try {
            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL, "retailer=? and shop=?", args);
            mSQLiteDB.delete(DiabloEnum.W_SALE_DETAIL_AMOUNT, "retailer=? and shop=?", args);
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
    }


    public void addSaleStock(SaleCalc calc, SaleStock stock) {
        mSQLiteDB.beginTransaction();

        try {
//            String sql1 = "insert into " + DiabloEnum.W_SALE + "(retailer, shop, comment)"
//                    + " values(?, ?, ?)";
//            SQLiteStatement s1 = mSQLiteDB.compileStatement(sql1);
//            s1.bindString(1, utils.toString(calc.getRetailer()));
//            s1.bindString(2, utils.toString(calc.getShop()));
//            s1.bindString(3, calc.getComment());
//            s1.execute();
//            s1.clearBindings();

            String sql1 = "delete from " + DiabloEnum.W_SALE_DETAIL
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
                    + "(retailer, shop, style_number, brand, color, size, total)"
                    + " values(?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement s3 = mSQLiteDB.compileStatement(sql3);

            for (SaleStockAmount a : stock.getAmounts()) {
                s3.bindString(1, utils.toString(calc.getRetailer()));
                s3.bindString(2, utils.toString(calc.getShop()));
                s3.bindString(3, stock.getStyleNumber());
                s3.bindString(4, utils.toString(stock.getBrandId()));
                s3.bindString(5, utils.toString(a.getColorId()));
                s3.bindString(6, a.getSize());
                s3.bindString(7, utils.toString(stock.getSaleTotal()));
                s3.execute();
                s3.clearBindings();
            }

//        ContentValues v = new ContentValues();
//        v.put("style_number", stock.getStyleNumber());
//        v.put("brand", stock.getBrandId());
//        v.put("sell_type", stock.getSelectedPrice());
//        v.put("second", stock.getSecond());
//        v.put("discount", stock.getDiscount());
//        v.put("price", stock.getFinalPrice());
//
//        for (SaleStockAmount a: stock.getAmounts()){
//            ContentValues v1 = new ContentValues();
//            v1.put("style_number", stock.getStyleNumber());
//            v1.put("brand", stock.getBrandId());
//            v1.put("color", a.getColorId());
//            v1.put("size", a.getSize());
//            v1.put("total", a.getSellCount());
//            mSQLiteDB.insert(DiabloEnum.W_SALE_DETAIL_AMOUNT, null, v1);
//        }
            mSQLiteDB.setTransactionSuccessful();
        } finally {
            mSQLiteDB.endTransaction();
        }
        // mSQLiteDB.endTransaction();
    }

    public void close(){
        mSQLiteDB.close();
    }
}
