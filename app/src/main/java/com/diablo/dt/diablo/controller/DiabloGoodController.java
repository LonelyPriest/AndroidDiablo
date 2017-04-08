package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.inventory.GoodCalc;
import com.diablo.dt.diablo.task.MatchBrandTask;
import com.diablo.dt.diablo.task.MatchFirmTask;
import com.diablo.dt.diablo.task.MatchGoodTypeTask;
import com.diablo.dt.diablo.utils.AutoCompleteTextChangeListener;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloTextWatcher;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloGoodCalcView;

import java.util.List;

/**
 * Created by buxianhui on 17/4/8.
 */

public class DiabloGoodController {
    private GoodCalc mGoodCalc;
    private DiabloGoodCalcView mGoodCalcView;

    private AdapterView.OnItemSelectedListener mOnSexSelectedListener;
    private AdapterView.OnItemSelectedListener mOnYearSelectedListener;
    private AdapterView.OnItemSelectedListener mOnSeasonSelectedListener;

    // firm
    private AutoCompleteTextChangeListener  mOnAutoCompletedFirmListener;
    private AdapterView.OnItemClickListener mOnFirmClickListener;

    // brand
    private AutoCompleteTextChangeListener  mOnAutoCompletedBrandListener;
    private AdapterView.OnItemClickListener mOnBrandClickListener;

    // type
    private AutoCompleteTextChangeListener  mOnAutoCompletedGoodTypeListener;
    private AdapterView.OnItemClickListener mOnGoodTypeClickListener;

    // interface
    private OnActionOfGoodValidate mOnActionOfGoodValidateListener;

    public void setOnActionOfGoodValidateListener(OnActionOfGoodValidate listener) {
        mOnActionOfGoodValidateListener = listener;
    }

    /**
     * validate
     */
    private boolean mIsValidStyleNumber;
    private boolean mIsValidBrand;
    private boolean mIsValidGoodType;
    private boolean mIsValidFirm;
    private boolean mIsValidOrgPrice;
    private boolean mIsValidPkgPrice;
    private boolean mIsValidTagPrice;

    public interface OnActionOfGoodValidate {
        void actionOfInvalid();
        void actionOfValid();
    }

    public DiabloGoodController(GoodCalc calc, DiabloGoodCalcView view) {
        mGoodCalc = calc;
        mGoodCalcView = view;

        mIsValidStyleNumber = false;
        mIsValidBrand = false;
        mIsValidGoodType = false;
        mIsValidFirm = false;

        mIsValidOrgPrice = true;
        mIsValidPkgPrice = true;
        mIsValidTagPrice = true;
    }

    public final GoodCalc getModel() {
        return mGoodCalc;
    }

    public void requestFocusOfStyleNumber() {
        mGoodCalcView.getStyleNumber().requestFocus();
        // mGoodCalcView.getStyleNumber().setSelected(true);
    }

    public void reset() {
        /**
         * clear listener
         */
        if (null != mOnSexSelectedListener) {
            ((Spinner)mGoodCalcView.getSex()).setOnItemSelectedListener(null);
        }

        if (null != mOnYearSelectedListener) {
            ((Spinner)mGoodCalcView.getYear()).setOnItemSelectedListener(null);
        }

        if (null != mOnSeasonSelectedListener) {
            ((Spinner)mGoodCalcView.getSeason()).setOnItemSelectedListener(null);
        }

        if (null != mOnAutoCompletedFirmListener) {
            mOnAutoCompletedFirmListener.removeListen();
            if (null != mOnFirmClickListener) {
                ((AutoCompleteTextView) mGoodCalcView.getFirm()).setOnItemClickListener(null);
            }
        }

        if (null != mOnAutoCompletedBrandListener) {
            mOnAutoCompletedBrandListener.removeListen();
            if (null != mOnBrandClickListener) {
                ((AutoCompleteTextView) mGoodCalcView.getBrand()).setOnItemClickListener(null);
            }
        }

        if (null != mOnAutoCompletedGoodTypeListener) {
            mOnAutoCompletedGoodTypeListener.removeListen();
            if (null != mOnGoodTypeClickListener) {
                ((AutoCompleteTextView) mGoodCalcView.getGoodType()).setOnItemClickListener(null);
            }
        }

        /**
         * reset view
         */
        mGoodCalcView.reset();
    }


    public void setFirmWatcher(final Context context, final List<Firm> firms, final Firm selectFirm) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getFirm();

        if (null != selectFirm) {
            f.setText(selectFirm.getName());
            mGoodCalc.setFirm(selectFirm);
            mIsValidFirm = true;
            checkValidAction();
        }

        mOnAutoCompletedFirmListener = new AutoCompleteTextChangeListener(f);

        mOnAutoCompletedFirmListener.addListen(new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                mIsValidFirm = false;
                checkValidAction();

                if (s.trim().length() > 0) {
                    new MatchFirmTask(context, f, firms).execute(s);
                }
            }
        });

        mOnFirmClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Firm selectFirm = (Firm) adapterView.getItemAtPosition(i);
                mGoodCalc.setFirm(selectFirm);

                mIsValidFirm = true;
                checkValidAction();
            }
        };

        f.setOnItemClickListener(mOnFirmClickListener);
    }

    public void setBrandWatcher(final Context context, final List<DiabloBrand> brands, final  DiabloBrand selectBrand) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getBrand();

        if (null != selectBrand) {
            f.setText(selectBrand.getName());
            mGoodCalc.setBrand(selectBrand);

            mIsValidBrand = true;
            checkValidAction();
        }

        mOnAutoCompletedBrandListener = new AutoCompleteTextChangeListener(f);

        mOnAutoCompletedBrandListener.addListen(new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                mIsValidBrand = false;
                checkValidAction();
                mGoodCalc.setBrand(null);

                if (s.trim().length() > 0) {
                    new MatchBrandTask(context, f, brands).execute(s);
                }
            }
        });

        mOnBrandClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiabloBrand brand = (DiabloBrand) adapterView.getItemAtPosition(i);

                String styleNumber = mGoodCalc.getStyleNumber();
                if (styleNumber.length() > 0
                    && null != Profile.instance().getMatchGood(styleNumber, brand.getId())) {
                    mIsValidBrand = false;
                    DiabloUtils.instance().makeToast(
                        context,
                        context.getString(R.string.same_good),
                        Toast.LENGTH_SHORT);
                } else {
                    mIsValidStyleNumber = true;
                    mIsValidBrand = true;
                }

                mGoodCalc.setBrand(brand);

                checkValidAction();
            }
        };

        f.setOnItemClickListener(mOnBrandClickListener);
    }

    public void setGoodTypeWatcher(final Context context, final List<DiabloType> goodTypes) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getGoodType();
        mOnAutoCompletedGoodTypeListener = new AutoCompleteTextChangeListener(f);

        mOnAutoCompletedGoodTypeListener.addListen(new AutoCompleteTextChangeListener.TextWatch() {
            @Override
            public void afterTextChanged(String s) {
                mIsValidGoodType = false;
                checkValidAction();

                if (s.trim().length() > 0) {
                    new MatchGoodTypeTask(context, f, goodTypes).execute(s);
                }
            }
        });

        mOnGoodTypeClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiabloType goodType = (DiabloType) adapterView.getItemAtPosition(i);
                mGoodCalc.setGoodType(goodType);

                mIsValidGoodType = true;
                checkValidAction();
            }
        };

        f.setOnItemClickListener(mOnGoodTypeClickListener);
    }

    public void setSexAdapter(Context context, String[] sexes){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.diablo_spinner_item,
            sexes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mGoodCalcView.getSex()).setAdapter(adapter);
    }

    public void setSexSelectedListener() {
        mOnSexSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGoodCalc.setSex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        ((Spinner)mGoodCalcView.getSex()).setOnItemSelectedListener(mOnSexSelectedListener);
    }

    public void setYearAdapter(Context context, String[] years, Integer currentYear) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.diablo_spinner_item,
            years);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mGoodCalcView.getYear()).setAdapter(adapter);

        Integer position = adapter.getPosition(DiabloUtils.instance().toString(currentYear));
        ((Spinner)mGoodCalcView.getYear()).setSelection(position);
    }

    public void setYearSelectedListener() {
        mOnYearSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = (String) parent.getItemAtPosition(position);
                mGoodCalc.setYear(DiabloUtils.instance().toInteger(year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        ((Spinner)mGoodCalcView.getYear()).setOnItemSelectedListener(mOnYearSelectedListener);
    }

    public void setSeasonAdapter(Context context, String[] seasons, Integer currentMonth){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            context,
            R.layout.diablo_spinner_item,
            seasons);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)mGoodCalcView.getSeason()).setAdapter(adapter);

        // get season
        Integer season = DiabloUtils.instance().getSeasonByMonth(currentMonth);
        ((Spinner)mGoodCalcView.getSeason()).setSelection(season);
    }

    public void setSeasonSelectedListener() {
        mOnSeasonSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGoodCalc.setSeason(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        ((Spinner)mGoodCalcView.getSeason()).setOnItemSelectedListener(mOnSeasonSelectedListener);
    }

    /**
     * validate
     */
    private void checkValidAction() {
        if (!mIsValidStyleNumber
            || !mIsValidBrand
            || !mIsValidGoodType
            || !mIsValidFirm
            || !mIsValidOrgPrice
            || !mIsValidPkgPrice
            || !mIsValidTagPrice
            ) {
            if (null != mOnActionOfGoodValidateListener) {
                mOnActionOfGoodValidateListener.actionOfInvalid();
            }
        }
        else {
            if (null != mOnActionOfGoodValidateListener) {
                mOnActionOfGoodValidateListener.actionOfValid();
            }
        }
    }

    // style number
    public void setValidateWatcherOfStyleNumber(final Context context, final String lastStyleNumber) {
        final EditText view = (EditText)mGoodCalcView.getStyleNumber();

        if (null != lastStyleNumber) {
            view.setText(lastStyleNumber);
            mGoodCalc.setStyleNumber(lastStyleNumber);

            mIsValidStyleNumber = true;
            checkValidAction();

            requestFocusOfStyleNumber();
        }

        view.addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String styleNumber = editable.toString().trim();
                if (styleNumber.length() == 0) {
                    mGoodCalc.setStyleNumber(DiabloEnum.EMPTY_STRING);
                    mIsValidStyleNumber = false;
                    checkValidAction();
                }
                else {
                    if (!DiabloPattern.isValidStyleNumber(styleNumber)) {
                        view.setError(context.getResources().getString(R.string.invalid_style_number));
                        mIsValidStyleNumber = false;
                    }
                    else {
                        DiabloBrand brand = mGoodCalc.getBrand();
                        if (null != brand
                            && null != Profile.instance().getMatchGood(styleNumber.toUpperCase(), brand.getId())) {
                            mIsValidStyleNumber = false;
                            DiabloUtils.instance().makeToast(
                                context,
                                context.getString(R.string.same_good),
                                Toast.LENGTH_SHORT);
                        }
                        else {
                            mIsValidBrand = true;
                            mIsValidStyleNumber = true;
                        }

                        mGoodCalc.setStyleNumber(styleNumber.toUpperCase());
                        checkValidAction();
                    }
                }
            }
        });
    }

    public void setValidateWatcherOfPrice(final Context context, final View view) {
        ((EditText)view).addTextChangedListener(new DiabloTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String price = editable.toString().trim();
                if (price.length() > 0 && !DiabloPattern.isValidDecimal(price)) {
                    ((EditText)view).setError(context.getResources().getString(R.string.invalid_price));
                    if(view.getTag().equals(context.getString(R.string.org_price))) {
                        mIsValidOrgPrice = false;
                        mGoodCalc.setOrgPrice(0f);
                    }
                    else if (view.getTag().equals(context.getString(R.string.pkg_price))) {
                        mIsValidPkgPrice = false;
                        mGoodCalc.setPkgPrice(0f);
                    }
                    else if (view.getTag().equals(context.getString(R.string.tag_price))) {
                        mIsValidTagPrice = false;
                        mGoodCalc.setTagPrice(0f);
                    }
                }
                else {
                    if(view.getTag().equals(context.getString(R.string.org_price))) {
                        mIsValidOrgPrice = true;
                        mGoodCalc.setOrgPrice(DiabloUtils.instance().toFloat(price));
                    }
                    else if (view.getTag().equals(context.getString(R.string.pkg_price))) {
                        mIsValidPkgPrice = true;
                        mGoodCalc.setPkgPrice(DiabloUtils.instance().toFloat(price));
                    }
                    else if (view.getTag().equals(context.getString(R.string.tag_price))) {
                        mIsValidTagPrice = true;
                        mGoodCalc.setTagPrice(DiabloUtils.instance().toFloat(price));
                    }
                }

                checkValidAction();
            }
        });
    }

}
