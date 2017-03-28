package com.diablo.dt.diablo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by buxianhui on 17/3/16.
 */

public class DiabloDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "diablo";
    private static final Integer DB_VERSION = 2;

    private static DiabloDBOpenHelper diabloDBHelper;

    private DiabloDBOpenHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DiabloDBOpenHelper instance(Context context){
        if (null == diabloDBHelper){
            diabloDBHelper = new DiabloDBOpenHelper(context);
        }

        return diabloDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String WSaleCalc = "create table if not exists w_sale ("
                + "_id integer primary key autoincrement"
                + ", retailer integer not null"
                + ", shop integer not null"
                + ", comment text"
                + ", unique(retailer, shop) ON CONFLICT REPLACE)";

        String WSaleStock = "create table if not exists w_sale_detail ("
            + "_id integer primary key autoincrement"
            + ", retailer integer not null"
            + ", shop integer not null"
            + ", style_number text not null"
            + ", brand integer not null"
            + ", sell_type integer not null"
            // + ", type integer not null"
            + ", second integer not null"
            + ", discount real not null"
            + ", price real not null"
            + ", unique(retailer, shop, style_number, brand) ON CONFLICT REPLACE)";

        String WSaleStockAmount = "create table if not exists w_sale_detail_amount ("
            + "_id integer primary key autoincrement"
            + ", retailer integer not null"
            + ", shop integer not null"
            + ", style_number text not null"
            + ", brand integer not null"
            + ", color integer not null"
            + ", size text not null"
            // + ", type integer not null"
            + ", exist integer not null"
            + ", total integer not null"
            + ", unique(retailer, shop, style_number, brand, color, size) ON CONFLICT REPLACE)";

        String User = "create table if not exists user ("
            + "_id integer primary key autoincrement"
            + ", name text not null"
            + ", password text not null"
            + ", unique(name) ON CONFLICT REPLACE)";

        db.execSQL(WSaleCalc);
        db.execSQL(WSaleStock);
        db.execSQL(WSaleStockAmount);
        db.execSQL(User);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String WSale = "drop table if exists w_sale";
        String WSaleDetail = "drop table if exists w_sale_detail";
        String WSaleDetailAmount = "drop table if exists  w_sale_detail_amount";
        String User = "drop table if exists user";

        db.execSQL(WSale);
        db.execSQL(WSaleDetail);
        db.execSQL(WSaleDetailAmount);
        db.execSQL(User);

        onCreate(db);
    }
}
