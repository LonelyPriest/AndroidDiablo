package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.response.good.AddFirmResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/29.
 */

public class DiabloType extends DiabloEntity{
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("py")
    private String py;

    public DiabloType() {
        id = DiabloEnum.INVALID_INDEX;
        name = DiabloEnum.EMPTY_STRING;
        py = DiabloEnum.EMPTY_STRING;
    }

    public DiabloType(DiabloType type) {
        this.id = type.getId();
        this.name = type.getName();
    }

    public DiabloType(String name) {
        this.id = DiabloEnum.INVALID_INDEX;
        this.name = name;
        this.py = DiabloEnum.EMPTY_STRING;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getViewName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGoodType(final Context context, final OnGoodTypeAddListener listener) {
        DiabloType find = Profile.instance().getDiabloType(this.getName());
        if (null != find) {
            listener.afterAdd(find);
        } else {
            final WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
            Call<AddFirmResponse> call = face.addGoodType(Profile.instance().getToken(), this);

            call.enqueue(new Callback<AddFirmResponse>() {
                @Override
                public void onResponse(Call<AddFirmResponse> call, Response<AddFirmResponse> response) {
                    // mButtons.get(R.id.sale_out_save).enable();

                    final AddFirmResponse res = response.body();
                    if (DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                        DiabloUtils.instance().makeToast(
                            context,
                            context.getResources().getString(R.string.success_to_add_good_type),
                            Toast.LENGTH_LONG);

                        DiabloType goodType = DiabloType.this;
                        goodType.id = res.getInsertId();

                        Profile.instance().addDiabloType(goodType);
                        listener.afterAdd(goodType);

                    } else {
                        Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                        String extraMessage = res == null ? "" : res.getError();
                        new DiabloAlertDialog(
                            context,
                            context.getResources().getString(R.string.title_add_good_type),
                            DiabloError.getError(errorCode) + extraMessage).create();
                    }
                }

                @Override
                public void onFailure(Call<AddFirmResponse> call, Throwable t) {
                    new DiabloAlertDialog(
                        context,
                        context.getResources().getString(R.string.title_add_good_type),
                        DiabloError.getError(99)).create();
                }
            });
        }
    }

    public interface OnGoodTypeAddListener {
        void afterAdd(DiabloType addedType);
    }
}
