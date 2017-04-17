package com.diablo.dt.diablo.controller;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.FirmAdapter;
import com.diablo.dt.diablo.adapter.MatchBrandAdapter;
import com.diablo.dt.diablo.adapter.MatchGoodTypeAdapter;
import com.diablo.dt.diablo.adapter.OnAdjustDropDownViewListener;
import com.diablo.dt.diablo.adapter.StringArrayAdapter;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodCalc;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEditTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.good.DiabloGoodCalcView;

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

    // style number
    private DiabloEditTextWatcher mStyleNumberWatcher;

    // firm
    private DiabloAutoCompleteTextWatcher mFirmWatcher;
    private AdapterView.OnItemClickListener mOnFirmClickListener;

    // brand
    private DiabloAutoCompleteTextWatcher mBrandWatcher;
    private AdapterView.OnItemClickListener mOnBrandClickListener;

    // type
    private DiabloAutoCompleteTextWatcher mGoodTypeWatcher;
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

    public void setValidStyleNumber(boolean valid) {
        mIsValidStyleNumber = valid;
    }

    public void setValidBrand(boolean valid) {
        mIsValidBrand = valid;
    }

    public void setValidGoodType(boolean valid) {
        mIsValidGoodType = valid;
    }

    public void setValidFirm(boolean valid) {
        mIsValidFirm = valid;
    }

//    public void setValidOrgPrice(boolean valid) {
//        mIsValidOrgPrice = valid;
//    }
//
//    public void setValidPkgPrice(boolean valid) {
//        mIsValidPkgPrice = valid;
//    }
//
//    public void setValidTagPrice(boolean valid) {
//        mIsValidTagPrice = valid;
//    }

    public void initViewText() {
        mGoodCalcView.setStyleNumberText(mGoodCalc.getStyleNumber());
        mGoodCalcView.setBrandText(mGoodCalc.getBrand().getName());
        mGoodCalcView.setGoodTypeText(mGoodCalc.getGoodType().getName());
        mGoodCalcView.setFirmText(mGoodCalc.getFirm().getName());

        mGoodCalcView.setOrgPriceText(mGoodCalc.getOrgPrice());
        mGoodCalcView.setPkgPriceText(mGoodCalc.getPkgPrice());
        mGoodCalcView.setTagPriceText(mGoodCalc.getTagPrice());

        mGoodCalcView.setColorText(mGoodCalc.getStringColors());
        mGoodCalcView.setSizeText(mGoodCalc.getStringSizeGroups());
    }

    public final GoodCalc getModel() {
        return mGoodCalc;
    }

    private void requestFocusOfStyleNumber() {
        EditText cell = (EditText) mGoodCalcView.getStyleNumber();
        cell.requestFocus();
//        cell.setSelectAllOnFocus(true);
//        cell.selectAll();
        cell.setSelection(0, cell.length());
        // mGoodCalcView.getStyleNumber().setSelected(true);
    }

    public void checkFocusOfFirm() {
        EditText cell = (EditText) mGoodCalcView.getFirm();
        cell.requestFocus();
        cell.setSelection(cell.length());
    }

    public void checkFocusOfBrand() {
        EditText cell = (EditText) mGoodCalcView.getBrand();
        cell.requestFocus();
        cell.setSelection(cell.length());
    }

    public void checkFocusOfType() {
        EditText cell = (EditText) mGoodCalcView.getGoodType();
        cell.requestFocus();
        cell.setSelection(cell.length());
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

        /**
         * clear watcher
         */
        if (null != mStyleNumberWatcher) {
            mStyleNumberWatcher.remove();
        }

        removeFirmListener();
        removeBrandListener();
        removeGoodTypeListener();

        /**
         * reset view
         */
        // mGoodCalcView.reset();
    }


    public void removeFirmListener() {
        if (null != mFirmWatcher) {
            mFirmWatcher.remove();
        }

        if (null != mOnFirmClickListener) {
            ((AutoCompleteTextView) mGoodCalcView.getFirm()).setOnItemClickListener(null);
        }
    }

    public void removeBrandListener() {
        if (null != mBrandWatcher) {
            mBrandWatcher.remove();

        }
        if (null != mOnBrandClickListener) {
            ((AutoCompleteTextView) mGoodCalcView.getBrand()).setOnItemClickListener(null);
        }
    }

    public void removeGoodTypeListener() {
        if (null != mGoodTypeWatcher) {
            mGoodTypeWatcher.remove();
        }

        if (null != mOnGoodTypeClickListener) {
            ((AutoCompleteTextView) mGoodCalcView.getGoodType()).setOnItemClickListener(null);
        }
    }

    public void setFirmWatcher(final Context context, final Firm selectFirm) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getFirm();

        if (null != selectFirm) {
            f.setText(selectFirm.getName());
            mGoodCalc.setFirm(selectFirm);
            mIsValidFirm = true;
            checkValidAction();
        }

        mFirmWatcher = new DiabloAutoCompleteTextWatcher(
            f,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
            @Override
            public void afterTextChanged(String s) {
                if (null == Profile.instance().getBrand(s)) {
                    mIsValidFirm = false;
                    mGoodCalc.setFirm(null);
                    checkValidAction();
                }
            }
        });


        new FirmAdapter(context, R.layout.typeahead_firm, R.id.typeahead_select_firm, f);

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

    public void setBrandWatcher(final Context context,
                                final DiabloBrand selectBrand,
                                final GoodCalc oldGoodCalc) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getBrand();
        f.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        if (null != selectBrand) {
            f.setText(selectBrand.getName());
            mGoodCalc.setBrand(selectBrand);

            mIsValidBrand = true;
            checkValidAction();
        }

        mBrandWatcher = new DiabloAutoCompleteTextWatcher(
            f,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    if (null == Profile.instance().getBrand(s)) {
                        mIsValidBrand = false;
                        mGoodCalc.setBrand(null);
                        checkValidAction();
                    }
                }
            });

        new MatchBrandAdapter(context, R.layout.typeahead_firm, R.id.typeahead_select_firm, f);

        mOnBrandClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DiabloBrand brand = (DiabloBrand) adapterView.getItemAtPosition(i);
                mIsValidBrand = true;

                String styleNumber = mGoodCalc.getStyleNumber();

                if (null != styleNumber && styleNumber.length() > 0) {
                    MatchGood good = Profile.instance().getMatchGood(styleNumber, brand.getId());
                    if ( null != good ) {
                        mIsValidBrand = false;

                        if (null != oldGoodCalc) {
                            if ( good.getStyleNumber().equals(oldGoodCalc.getStyleNumber())
                                && good.getBrandId().equals(oldGoodCalc.getBrand().getId()) ) {
                                mIsValidBrand = true;
                            }
                        }

                        if (!mIsValidBrand) {
                            DiabloUtils.instance().makeToast(
                                context,
                                context.getString(R.string.same_good),
                                Toast.LENGTH_SHORT);
                        }
                    } else {
                        mIsValidStyleNumber = true;
                    }
                }

                mGoodCalc.setBrand(brand);
                checkValidAction();
            }
        };

        f.setOnItemClickListener(mOnBrandClickListener);
    }

    public void setGoodTypeWatcher(final Context context,
                                   final  DiabloType selectGoodType) {
        final AutoCompleteTextView f = (AutoCompleteTextView) mGoodCalcView.getGoodType();

        if (null != selectGoodType) {
            f.setText(selectGoodType.getName());
            mGoodCalc.setGoodType(selectGoodType);

            mIsValidGoodType = true;
            checkValidAction();
        }

        mGoodTypeWatcher = new DiabloAutoCompleteTextWatcher(
            f,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    if (null == Profile.instance().getDiabloType(s)) {
                        mIsValidGoodType = false;
                        mGoodCalc.setGoodType(null);
                        checkValidAction();
                    }
                }
            });

        new MatchGoodTypeAdapter(context, R.layout.typeahead_firm, R.id.typeahead_select_firm, f);

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
        final Spinner spinner = (Spinner)mGoodCalcView.getSex();

        StringArrayAdapter adapter = new StringArrayAdapter(
            context,
            R.layout.diablo_spinner_item,
            sexes);

        adapter.setDropDownViewListener(new OnAdjustDropDownViewListener() {
            @Override
            public void setDropDownVerticalOffset() {
                spinner.setDropDownVerticalOffset(spinner.getHeight());
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mGoodCalc.getSex());
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
        final Spinner spinner = (Spinner)mGoodCalcView.getYear();

        StringArrayAdapter adapter = new StringArrayAdapter(
            context,
            R.layout.diablo_spinner_item,
            years);

        adapter.setDropDownViewListener(new OnAdjustDropDownViewListener() {
            @Override
            public void setDropDownVerticalOffset() {
                spinner.setDropDownVerticalOffset(spinner.getHeight());
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Integer position = adapter.getPosition(DiabloUtils.instance().toString(currentYear));
        spinner.setSelection(position);
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
        final Spinner spinner = (Spinner)mGoodCalcView.getSeason();

        StringArrayAdapter adapter = new StringArrayAdapter(
            context,
            R.layout.diablo_spinner_item,
            seasons);

        adapter.setDropDownViewListener(new OnAdjustDropDownViewListener() {
            @Override
            public void setDropDownVerticalOffset() {
                spinner.setDropDownVerticalOffset(spinner.getHeight());
            }
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // get season
        Integer season = DiabloUtils.instance().getSeasonByMonth(currentMonth);
        spinner.setSelection(season);
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

    /**
     *
     * @param context
     * @param lastStyleNumber last style number
     * @param oldGoodCalc  when success to add a good, the old information remain on the view
     *                     to convenient to input next good,
     *                     make sure the error information not appear
     */
    public void setValidateWatcherOfStyleNumber(
        final Context context,
        final String lastStyleNumber,
        final GoodCalc oldGoodCalc) {
        final EditText view = (EditText)mGoodCalcView.getStyleNumber();
        view.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        if (null != lastStyleNumber) {
            view.setText(lastStyleNumber);
            mGoodCalc.setStyleNumber(lastStyleNumber);

            mIsValidStyleNumber = true;
            checkValidAction();

            requestFocusOfStyleNumber();
        }

        mStyleNumberWatcher = new DiabloEditTextWatcher(
            view,
            new DiabloEditTextWatcher.DiabloEditTextChangListener() {
            @Override
            public void afterTextChanged(String styleNumber) {
                if (styleNumber.length() == 0) {
                    mGoodCalc.setStyleNumber(DiabloEnum.EMPTY_STRING);
                    mIsValidStyleNumber = false;
                    checkValidAction();
                }
                else {
                    if (!DiabloPattern.isValidStyleNumber(styleNumber)) {
                        view.setError(context.getResources().getString(R.string.invalid_style_number));
                        mIsValidStyleNumber = false;
                    } else {
                        mIsValidStyleNumber = true;
                        // check brand
                        DiabloBrand brand = mGoodCalc.getBrand();
                        if (null != brand) {
                            MatchGood good = Profile.instance().getMatchGood(styleNumber.toUpperCase(), brand.getId());
                            if (null != good) {
                                mIsValidStyleNumber = false;

                                if (null != oldGoodCalc) {
                                    if ( good.getStyleNumber().equals(oldGoodCalc.getStyleNumber())
                                        && good.getBrandId().equals(oldGoodCalc.getBrand().getId()) ) {
                                        mIsValidStyleNumber = true;
                                    }
                                }

                                if (!mIsValidStyleNumber) {
                                    DiabloUtils.instance().makeToast(
                                        context,
                                        context.getString(R.string.same_good),
                                        Toast.LENGTH_SHORT);
                                }
                            } else {
                                mIsValidBrand = true;
                            }
                        }
                    }

                    mGoodCalc.setStyleNumber(styleNumber.toUpperCase());
                    checkValidAction();
                }
            }
        });
    }

    public void setValidateWatcherOfPrice(final Context context, final View view) {
        final EditText editText = (EditText) view;
        new DiabloEditTextWatcher(editText, new DiabloEditTextWatcher.DiabloEditTextChangListener() {
            @Override
            public void afterTextChanged(String price) {
                if (price.length() > 0 && !DiabloPattern.isValidDecimal(price)) {
                    editText.setError(context.getResources().getString(R.string.invalid_price));
                    if(editText.getTag().equals(context.getString(R.string.org_price))) {
                        mIsValidOrgPrice = false;
                        mGoodCalc.setOrgPrice(0f);
                    }
                    else if (editText.getTag().equals(context.getString(R.string.pkg_price))) {
                        mIsValidPkgPrice = false;
                        mGoodCalc.setPkgPrice(0f);
                    }
                    else if (editText.getTag().equals(context.getString(R.string.tag_price))) {
                        mIsValidTagPrice = false;
                        mGoodCalc.setTagPrice(0f);
                    }
                }
                else {
                    if(editText.getTag().equals(context.getString(R.string.org_price))) {
                        mIsValidOrgPrice = true;
                        mGoodCalc.setOrgPrice(DiabloUtils.instance().toFloat(price));
                    }
                    else if (editText.getTag().equals(context.getString(R.string.pkg_price))) {
                        mIsValidPkgPrice = true;
                        mGoodCalc.setPkgPrice(DiabloUtils.instance().toFloat(price));
                    }
                    else if (editText.getTag().equals(context.getString(R.string.tag_price))) {
                        mIsValidTagPrice = true;
                        mGoodCalc.setTagPrice(DiabloUtils.instance().toFloat(price));
                    }
                }

                checkValidAction();
            }
        });
    }

    /**
     * adapter model operation
     */
    public void setColors(List<DiabloColor> colors) {
        // List<String> colorNames = new ArrayList<>();
        mGoodCalc.clearColor();
        for(DiabloColor color: colors) {
            mGoodCalc.addColor(color);
            // colorNames.add(color.getName());
        }

        mGoodCalcView.setColorText(mGoodCalc.getStringColors());
    }

    public void setSizeGroups(List<DiabloSizeGroup> groups) {
        // List<String> groupNames = new ArrayList<>();
        mGoodCalc.clearSizeGroups();
        for (DiabloSizeGroup g: groups) {
            mGoodCalc.addSizeGroup(g);
            // groupNames.add(g.getName());
        }

        mGoodCalcView.setSizeText(mGoodCalc.getStringSizeGroups());
    }

    public void setOrgPrice(Float orgPrice) {
        mGoodCalcView.setOrgPriceText(orgPrice);
        mGoodCalc.setOrgPrice(orgPrice);
    }

    public void setPkgPrice(Float pkgPrice) {
        mGoodCalcView.setPkgPriceText(pkgPrice);
        mGoodCalc.setPkgPrice(pkgPrice);
    }

    public void setTagPrice(Float tagPrice) {
        mGoodCalcView.setTagPriceText(tagPrice);
        mGoodCalcView.setTagPriceText(tagPrice);
    }
}
