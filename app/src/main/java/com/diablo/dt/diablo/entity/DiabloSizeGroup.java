package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by buxianhui on 17/3/14.
 */

public class DiabloSizeGroup {
    @SerializedName("id")
    private Integer gid;
    @SerializedName("name")
    private String name;
    @SerializedName("si")
    private String SI;
    @SerializedName("sii")
    private String SII;
    @SerializedName("siii")
    private String SIII;
    @SerializedName("siv")
    private String SIV;
    @SerializedName("sv")
    private String SV;
    @SerializedName("svi")
    private String SVI;

    public DiabloSizeGroup(){

    }

    public Integer getGroupId() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public String getSI() {
        return SI;
    }

    public String getSII() {
        return SII;
    }

    public String getSIII() {
        return SIII;
    }

    public String getSIV() {
        return SIV;
    }

    public String getSV() {
        return SV;
    }

    public String getSVI() {
        return SVI;
    }

    public List<String> getAllSize(){
        ArrayList<String> sizes = new ArrayList<>();

        sizes.add(getSI());
        sizes.add(getSII());
        sizes.add(getSIII());
        sizes.add(getSIV());
        sizes.add(getSV());
        sizes.add(getSVI());

        return sizes;
    }

}
