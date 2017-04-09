package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.FirmClient;
import com.diablo.dt.diablo.response.inventory.AddFirmResponse;
import com.diablo.dt.diablo.rest.FirmInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/4/3.
 */

public class Firm {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("address")
    private String address;
    @SerializedName("balance")
    private Float balance;
    @SerializedName("entry_date")
    private String datetime;

    public Firm() {
        init();
    }

    public Firm(String name) {
        this.name = name;
        init();
    }

    private void init() {
        id = 0;
        balance = 0f;
        mobile = DiabloEnum.EMPTY_STRING;
        address = DiabloEnum.EMPTY_STRING;
        datetime = DiabloEnum.EMPTY_STRING;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void addFirm(final Context context, final OnFirmAddListener listener) {
        final FirmInterface face = FirmClient.getClient().create(FirmInterface.class);
        Call<AddFirmResponse> call = face.addFirm(Profile.instance().getToken(), this);

        call.enqueue(new Callback<AddFirmResponse>() {
            @Override
            public void onResponse(Call<AddFirmResponse> call, Response<AddFirmResponse> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final AddFirmResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.success_to_add_firm),
                        Toast.LENGTH_LONG);

                    Firm f = Firm.this;
                    f.id = res.getInsertId();

                    Profile.instance().addFirm(f);
                    listener.afterAdd(f);

                } else {
                    if (DiabloEnum.HTTP_OK != response.code()) {
                        DiabloUtils.instance().setErrorInfo(context, R.string.title_add_firm, response.code());
                    } else {
                        DiabloUtils.instance().setErrorInfo(context, R.string.title_add_firm, res.getCode());
                    }
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

    public interface OnFirmAddListener {
        void afterAdd(Firm firm);
    }
}
