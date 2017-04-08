package com.diablo.dt.diablo.entity;

import com.google.gson.annotations.SerializedName;

import com.diablo.dt.diablo.utils.DiabloEnum;

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

    private List<String> mSortedSizeNames;
    public DiabloSizeGroup(){
        this.mSortedSizeNames = new ArrayList<>();
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

    public void genSortedSizeNames(){
        if (!getSI().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSI());

        if (!getSII().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSII());

        if (!getSIII().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSIII());

        if (!getSIV().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSIV());

        if (!getSV().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSV());

        if (!getSVI().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSVI());
    }

    public List<String> getSortedSizeNames(){
        return mSortedSizeNames;
    }

}
