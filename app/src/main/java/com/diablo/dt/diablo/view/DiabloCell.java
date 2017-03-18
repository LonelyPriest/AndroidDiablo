package com.diablo.dt.diablo.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by buxianhui on 17/3/18.
 */

public abstract class DiabloCell {
    private View     mView;
    private String   mText;
    private Integer  mColor;
    private Integer  mSize;
    private Integer  mGravity;
    private Float    mWeight;

    public DiabloCell(Context context, String text, Integer color, Integer size, Float weight) {
        this.mText  = text;
        this.mColor = color;
        this.mSize  = size;
        this.mWeight = weight;

        buildView(context);
    }

    public DiabloCell(Context context, String text, Integer color, Integer size, Float weight, Integer gravity) {
        this.mText  = text;
        this.mColor = color;
        this.mSize  = size;
        this.mWeight = weight;
        this.mGravity = gravity;

        buildViewWithGravity(context);
    }

    public View getView(){
        return mView;
    }

    public String getText(){
        return mText;
    }

    public abstract View createView(Context context);
    public abstract void setLayout(String text, Integer color, Integer size);
    public abstract void setGravity(Integer gravity);

    private void buildView(Context context) {
        this.mView = createView(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, mWeight);
        this.mView.setLayoutParams(lp);

        setLayout(mText, mColor, mSize);
        this.mView.setTag(mText);
    }

    private void buildViewWithGravity(Context context) {
        this.mView = createView(context);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, mWeight);
        this.mView.setLayoutParams(lp);

        setLayout(mText, mColor, mSize);
        setGravity(mGravity);

        this.mView.setTag(mText);
    }

    public void setText() {

    }

    public void setTextBold() {

    }

    public void setColor(Integer color) {

    }

    public void setSize(Integer size){

    }

    public void setInputType(Integer inputType){

    }

    public Integer getColor(){
        return mColor;
    }

    public Integer getSize(){
        return mSize;
    }

    public Float getWeight(){
        return mWeight;
    }
}
