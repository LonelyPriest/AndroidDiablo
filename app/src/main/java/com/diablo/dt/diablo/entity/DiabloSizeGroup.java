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

    public void initWithFreeSizeGroup() {
        SI = DiabloEnum.DIABLO_FREE_SIZE;
        gid = DiabloEnum.DIABLO_FREE_SIZE_GROUP;
    }

    public Integer getGroupId() {
        return gid;
    }

    public String getName() {
        return name;
    }

    private String getSI() {
        return SI;
    }

    private String getSII() {
        return SII;
    }

    private String getSIII() {
        return SIII;
    }

    private String getSIV() {
        return SIV;
    }

    private String getSV() {
        return SV;
    }

    private String getSVI() {
        return SVI;
    }

    public void genSortedSizeNames(){
        if (null != SI && !getSI().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSI());

        if (null != SII && !getSII().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSII());

        if (null != SIII && !getSIII().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSIII());

        if (null != SIV && !getSIV().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSIV());

        if (null != SV && !getSV().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSV());

        if (null != SVI && !getSVI().equals(DiabloEnum.EMPTY_STRING))
            mSortedSizeNames.add(getSVI());
    }

    public List<String> getSortedSizeNames(){
        return mSortedSizeNames;
    }

}
