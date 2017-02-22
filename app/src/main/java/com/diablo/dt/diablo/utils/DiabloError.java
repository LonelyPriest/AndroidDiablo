package com.diablo.dt.diablo.utils;

import android.content.res.Resources;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.MainProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by buxianhui on 17/2/22.
 */
public class DiabloError {
    private static DiabloError mInstance;

    public static DiabloError getInstance() {
        if (null == mInstance){
            mInstance = new DiabloError();
        }

        return mInstance;
    }

    private DiabloError() {

    }

    private static final Map<Integer, String> mErrors;
    static {
        Resources r = MainProfile.getInstance().getContext().getResources();

        mErrors = new HashMap<Integer, String>();
        mErrors.put(1101, r.getString(R.string.invalid_name_password));
    }

    public String getError(Integer ecode){
        return mErrors.get(ecode);
    }

}
