package com.diablo.dt.diablo.entity;

import android.content.Context;

import com.diablo.dt.diablo.R;

/**
 * Created by buxianhui on 17/2/22.
 */
public class MainProfile {
    private static MainProfile mPofile = new MainProfile();

    public static MainProfile getInstance() {
        if ( null == mPofile ){
            mPofile = new MainProfile();
        }
        return mPofile;
    }

    private Context mContext;

    // token to server
    private String mToken;

    private MainProfile() {

    }

    public void setContext(Context context){
        this.mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    public void setToken(String token){
        mToken = token;
    }

    public String getToken(){
        return mToken;
    }

    public String getServer(){
        return this.mContext.getResources().getString(R.string.diablo_server);
    }
}
