package com.diablo.dt.diablo.request.good;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.entity.MatchGood;

/**
 * Created by buxianhui on 17/4/11.
 */

public class GoodUpdateRequest {
    @SerializedName("good")
    private MatchGood updateGood;

    public GoodUpdateRequest(MatchGood good) {
        this.updateGood = good;
    }
}
