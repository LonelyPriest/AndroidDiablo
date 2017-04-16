package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.response.AddRetailerResponse;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/2/24.
 */

public class Retailer extends DiabloEntity{
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
    @SerializedName("cid")
    private Integer city;
    @SerializedName("pid")
    private Integer province;
    @SerializedName("entry_date")
    private String entryDate;
    @SerializedName("merchant")
    private Integer merchant;

    public Retailer(String name, String phone){
        this.id = DiabloEnum.INVALID_INDEX;
        this.name = name;
        this.mobile = phone;
    }

    public Retailer(Retailer retailer) {
        this.id = retailer.getId();
        this.name = retailer.getName();
        this.mobile = retailer.getMobile();
        this.balance = retailer.getBalance();
    }

    public Retailer(Retailer retailer, Float balance) {
        this.id = retailer.getId();
        this.name = retailer.getName();
        this.mobile = retailer.getMobile();
        this.balance = balance;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getViewName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(@Nullable String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getBalance() {
        return this.balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Integer getCity() {
        return this.city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getProvince() {
        return this.province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public String getEntryDate() {
        return this.entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getMerchant() {
        return this.merchant;
    }

    public void setMerchant(Integer merchant) {
        this.merchant = merchant;
    }

    public void newRetailer(final Context context, @Nullable final OnRetailerChangeListener listener) {
        final RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<AddRetailerResponse> call = face.addRetailer(Profile.instance().getToken(), this);

        call.enqueue(new Callback<AddRetailerResponse>() {
            @Override
            public void onResponse(Call<AddRetailerResponse> call, Response<AddRetailerResponse> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final AddRetailerResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.success_to_add_retailer),
                        Toast.LENGTH_LONG);

                    Retailer r = Retailer.this;
                    r.id = res.getInsertId();
                    if (null == r.getAddress()) {
                        r.setAddress(DiabloEnum.EMPTY_STRING);
                    }
                    if (null == r.getBalance()) {
                        r.setBalance(0f);
                    }
                    if (null == r.getMobile()) {
                        r.setMobile(DiabloEnum.EMPTY_STRING);
                    }
                    if (null == r.getCity()) {
                        r.setCity(DiabloEnum.INVALID_INDEX);
                    }
                    if (null == r.getProvince()) {
                        r.setProvince(DiabloEnum.INVALID_INDEX);
                    }
                    r.setEntryDate(DiabloUtils.instance().currentDate());

                    Profile.instance().addRetailer(Retailer.this);
                    if (null != listener) {
                        listener.afterAdd(Retailer.this);
                    }

                } else {
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        context,
                        context.getResources().getString(R.string.title_add_retailer),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<AddRetailerResponse> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getResources().getString(R.string.title_add_retailer),
                    DiabloError.getError(99)).create();
            }
        });
    }

    public static void getRetailer(final Context context, Integer retailerId, final OnRetailerChangeListener listener) {
        final RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<Retailer> call = face.getRetailer(Profile.instance().getToken(), retailerId);

        call.enqueue(new Callback<Retailer>() {
            @Override
            public void onResponse(Call<Retailer> call, Response<Retailer> response) {
                listener.afterGet(response.body());
            }

            @Override
            public void onFailure(Call<Retailer> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getResources().getString(R.string.title_get_retailer),
                    DiabloError.getError(99)).create();
            }
        });
    }

    public interface OnRetailerChangeListener {
        void afterAdd(Retailer retailer);
        void afterGet(Retailer retailer);
    }
}
