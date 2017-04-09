package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.response.inventory.AddFirmResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/28.
 */

public class DiabloBrand {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("supplier_id")
    private Integer firmId;
    @SerializedName("supplier")
    private String firm;

    public DiabloBrand() {
        name = DiabloEnum.EMPTY_STRING;
        init();
    }

    public DiabloBrand(String name) {
        this.name = name;
        init();
    }

    private void init() {
        id = 0;
        firmId = DiabloEnum.INVALID_INDEX;
        firm = DiabloEnum.EMPTY_STRING;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getFirmId() {
        return firmId;
    }

    public String getFirm() {
        return firm;
    }

    public void addBrand(final Context context, final OnBrandAddListener listener) {
        final WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
        Call<AddFirmResponse> call = face.addBrand(Profile.instance().getToken(), this);

        call.enqueue(new Callback<AddFirmResponse>() {
            @Override
            public void onResponse(Call<AddFirmResponse> call, Response<AddFirmResponse> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final AddFirmResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.success_to_add_brand),
                        Toast.LENGTH_LONG);

                    DiabloBrand brand = DiabloBrand.this;
                    brand.id = res.getInsertId();

                    Profile.instance().addBrand(brand);
                    listener.afterAdd(brand);

                } else {
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        context,
                        context.getResources().getString(R.string.title_add_firm),
                        DiabloError.getInstance().getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<AddFirmResponse> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getResources().getString(R.string.title_add_firm),
                    DiabloError.getInstance().getError(99)).create();
            }
        });
    }

    public interface OnBrandAddListener {
        void afterAdd(DiabloBrand addedBrand);
    }
}
