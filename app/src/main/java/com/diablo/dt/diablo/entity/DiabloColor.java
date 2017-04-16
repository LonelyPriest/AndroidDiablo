package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.response.good.AddColorResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/3/14.
 */

public class DiabloColor {
    @SerializedName("id")
    private Integer colorId;
    @SerializedName("name")
    private String name;
    @SerializedName("tid")
    private Integer typeId;
    @SerializedName("type")
    private String typeName;
    @SerializedName("remark")
    private String remark;

    /**
     * default color is free color, notice do not changed
     */
    public DiabloColor(){
        this.colorId = DiabloEnum.DIABLO_FREE_COLOR;
    }

    public DiabloColor(String name, Integer typeId) {
        this.name = name;
        this.typeId = typeId;
        this.colorId = DiabloEnum.INVALID_INDEX;
    }

    public DiabloColor(Integer colorId){
        this.colorId = colorId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public String getName() {
        return name;
    }

    public Integer getTypeId() {
        return typeId;
    }

//    public String getTypeName() {
//        return typeName;
//    }
//
//    public String getRemark() {
//        return remark;
//    }

    public boolean includeIn(List<DiabloColor> colors){
        boolean include = false;
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).getColorId().equals(colorId)){
                include = true;
                break;
            }
        }
        return include;
    }

    public void newColor(final Context context, final OnGoodColorChangeListener listener) {
        final WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
        Call<AddColorResponse> call = face.addColor(Profile.instance().getToken(), this);

        call.enqueue(new Callback<AddColorResponse>() {
            @Override
            public void onResponse(Call<AddColorResponse> call, Response<AddColorResponse> response) {
                // mButtons.get(R.id.sale_out_save).enable();

                final AddColorResponse res = response.body();
                if ( DiabloEnum.HTTP_OK == response.code() && res.getCode().equals(DiabloEnum.SUCCESS)) {
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getResources().getString(R.string.success_to_add_color),
                        Toast.LENGTH_LONG);

                    DiabloColor color = DiabloColor.this;
                    color.colorId = res.getInsertId();


                    Profile.instance().addColor(color);

                    if (null != listener) {
                        listener.afterAdd(color);
                    }

                } else {
                    Integer errorCode = response.code() == 0 ? res.getCode() : response.code();
                    String extraMessage = res == null ? "" : res.getError();
                    new DiabloAlertDialog(
                        context,
                        context.getResources().getString(R.string.title_add_color),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<AddColorResponse> call, Throwable t) {
                new DiabloAlertDialog(
                    context,
                    context.getResources().getString(R.string.title_add_color),
                    DiabloError.getError(99)).create();
            }
        });
    }

    public interface OnGoodColorChangeListener {
        void afterAdd(DiabloColor color);
    }
}
