package com.diablo.dt.diablo.task;

import android.util.Log;

import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Stock;
import com.diablo.dt.diablo.request.sale.StockRequest;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.utils.DiabloEnum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/21.
 */

public class MatchSingleStockTask {
    private final static String LOG_TAG = "MatchSingleStockTask:";
    private String  mStyleNumber;
    private Integer mBrandId;
    private Integer mShopId;
    private Integer mUseRepo;

    private OnMatchSingleStockTaskListener mOnMatchSingleStockTaskListener;

    public MatchSingleStockTask(String styleNumber, Integer brand, Integer shop){
        this.mStyleNumber = styleNumber;
        this.mBrandId = brand;
        this.mShopId = shop;
        this.mUseRepo = DiabloEnum.USE_REPO;
    }

    public void getStock() {
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Call<List<Stock>> call = face.getStock(
            Profile.instance().getToken(),
            new StockRequest(mStyleNumber, mBrandId, mShopId, mUseRepo));

        call.enqueue(new Callback<List<Stock>>() {
            @Override
            public void onResponse(final Call<List<Stock>> call, Response<List<Stock>> response) {
                Log.d(LOG_TAG, "success to get stock");
                mOnMatchSingleStockTaskListener.onMatchSuccess(new ArrayList<>(response.body()));
            }

            @Override
            public void onFailure(Call<List<Stock>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get stock");
                mOnMatchSingleStockTaskListener.onMatchFailed(t);
            }
        });
    }

    public void setMatchSingleStockTaskListener(OnMatchSingleStockTaskListener listener) {
        this.mOnMatchSingleStockTaskListener = listener;
    }

    public interface OnMatchSingleStockTaskListener {
        void onMatchSuccess(List<Stock> stocks);
        void onMatchFailed(Throwable t);
    }

}
