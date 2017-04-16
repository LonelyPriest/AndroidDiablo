package com.diablo.dt.diablo.fragment.good;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.controller.DiabloGoodController;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodCalc;
import com.diablo.dt.diablo.model.good.GoodUtils;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.good.GoodUpdateRequest;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.good.DiabloGoodCalcView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodUpdate extends Fragment {
    private final static String LOG_TAG = "GoodUpdate:";

    private Integer   mGoodId;
    private Integer   mLastGoodId;

    private SparseArray<DiabloButton> mButtons;
    private String [] mSexes;
    private String [] mYears;
    private String [] mSeasons;

    private Integer mComeFrom = R.string.come_from_unknown;
    private DiabloGoodCalcView mGoodCalcView;
    private DiabloGoodController mGoodController;
    private GoodCalc mOldGoodCalc;

    private ColorSelect.OnColorSelectListener mOnColorSelectListener;
    private SizeSelect.OnSizeSelectListener mOnSizeSelectListener;
    private Integer mBackFrom = R.string.back_from_unknown;

    public void setOnColorSelectListener(ColorSelect.OnColorSelectListener listener) {
        mOnColorSelectListener = listener;
    }

    public void  setOnSizeSelectListener(SizeSelect.OnSizeSelectListener listener) {
        mOnSizeSelectListener = listener;
    }

    public void setBackFrom(Integer backFrom) {
        mBackFrom = backFrom;
    }

    public void setGoodId(Integer goodId) {
        this.mGoodId = goodId;
    }

    public GoodUpdate() {
        // Required empty public constructor
    }

    public static GoodUpdate newInstance(String param1, String param2) {
        GoodUpdate fragment = new GoodUpdate();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGoodId = getArguments().getInt(DiabloEnum.BUNDLE_PARAM_ID);
        }

        mSexes = getResources().getStringArray(R.array.sexes);
        mYears = getResources().getStringArray(R.array.years);
        mSeasons = getResources().getStringArray(R.array.seasons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initTitle();
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_good_update, container, false);
        mButtons = new SparseArray<>();
        mButtons.put(R.id.good_update_save, new DiabloButton(getContext(), R.id.good_update_save));
        initView(view);

        View btnSelectColor = view.findViewById(R.id.btn_select_color);
        btnSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor();
            }
        });

        View btnSelectSize = view.findViewById(R.id.btn_select_size);
        btnSelectSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSize();
            }
        });

        getGoodFromServer();
        return view;
    }

    private void selectColor() {
        if (null != mGoodController) {
            GoodUtils.switchToColorSelectFrame(
                mGoodController.getModel(),
                DiabloEnum.GOOD_UPDATE,
                this,
                mOldGoodCalc.getColors());
        }
    }

    private void selectSize() {
        if (null != mGoodController) {
            GoodUtils.switchToSizeSelectFrame(
                mGoodController.getModel(),
                DiabloEnum.GOOD_UPDATE,
                this,
                mOldGoodCalc.getSizeGroups());
        }
    }

    private void initTitle() {
        String title = getResources().getString(R.string.good_update);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (null != bar) {
            bar.setTitle(title);
        }
    }

    private void initView(View view) {
        mGoodCalcView = new DiabloGoodCalcView();

        mGoodCalcView.setStyleNumber(view.findViewById(R.id.good_style_number));
        mGoodCalcView.setBrand(view.findViewById(R.id.good_brand));
        mGoodCalcView.setGoodType(view.findViewById(R.id.good_type));
        mGoodCalcView.setFirm(view.findViewById(R.id.good_firm));

        mGoodCalcView.setSex(view.findViewById(R.id.good_sex));
        mGoodCalcView.setYear(view.findViewById(R.id.good_year));
        mGoodCalcView.setSeason(view.findViewById(R.id.good_season));

        mGoodCalcView.setOrgPrice(view.findViewById(R.id.good_org_price));
        mGoodCalcView.setPkgPrice(view.findViewById(R.id.good_pkg_price));
        mGoodCalcView.setTagPrice(view.findViewById(R.id.good_tag_price));


        mGoodCalcView.setColor(view.findViewById(R.id.good_colors));
        mGoodCalcView.setSize(view.findViewById(R.id.good_sizes));
    }


    private void init(GoodCalc calc) {
        mLastGoodId = mGoodId;

        if (null != mGoodController) {
            mGoodController.reset();
        }

        mGoodController = new DiabloGoodController(calc, mGoodCalcView);
        mGoodController.initViewText();
        mGoodController.setValidStyleNumber(true);
        mGoodController.setValidBrand(true);
        mGoodController.setValidFirm(true);
        mGoodController.setValidGoodType(true);

        mGoodController.setSexAdapter(getContext(), mSexes);
        mGoodController.setYearAdapter(getContext(), mYears, calc.getYear());
        mGoodController.setSeasonAdapter(
            getContext(), mSeasons, ((calc.getSeason() + 1) * 3) - 1);

        mGoodController.setSexSelectedListener();
        mGoodController.setYearSelectedListener();
        mGoodController.setSeasonSelectedListener();


        mGoodController.setValidateWatcherOfStyleNumber(getContext(), null, mOldGoodCalc);
        // firm
        mGoodController.setFirmWatcher(getContext(),  null);
        // brand
        mGoodController.setBrandWatcher(getContext(), null, mOldGoodCalc);
        // type
        mGoodController.setGoodTypeWatcher(getContext(), null);

        // price
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getOrgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getPkgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getTagPrice());

        // callback
        mGoodController.setOnActionOfGoodValidateListener(mOnActionOfGoodValidate);
        mButtons.get(R.id.good_update_save).enable();
    }

    private DiabloGoodController.OnActionOfGoodValidate mOnActionOfGoodValidate
        = new DiabloGoodController.OnActionOfGoodValidate() {
        @Override
        public void actionOfInvalid() {
            mButtons.get(R.id.good_update_save).disable();
        }

        @Override
        public void actionOfValid() {
            mButtons.get(R.id.good_update_save).enable();
        }
    };

    /**
     * option menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for (Integer i=0; i<mButtons.size(); i++){
            Integer key = mButtons.keyAt(i);
            DiabloButton button = mButtons.get(key);
            menu.findItem(button.getResId()).setEnabled(button.isEnabled());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();

        inflater.inflate(R.menu.action_on_good_update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.good_update_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_GOOD_DETAIL);
                break;
            case R.id.good_update_save:
                startUpdate();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
            initTitle();
            if (mBackFrom.equals(R.string.back_from_color_select)){
                switch (mOnColorSelectListener.getCurrentOperation()){
                    case R.string.action_save:
                        GoodCalc calc = mOnColorSelectListener.afterSelectColor();
                        mGoodController.setColors(calc.getColors());
                        break;
                    case R.string.action_cancel:
                        break;
                    default:
                        break;
                }
            }
            else if (mBackFrom.equals(R.string.back_from_size_select)) {
                switch (mOnSizeSelectListener.getCurrentOperation()){
                    case R.string.action_save:
                        GoodCalc calc = mOnSizeSelectListener.afterSelectSizeGroup();
                        mGoodController.setSizeGroups(calc.getSizeGroups());
                        break;
                    case R.string.action_cancel:
                        break;
                    default:
                        break;
                }

            }
            else {
                if (!mGoodId.equals(mLastGoodId)) {
                    getGoodFromServer();
                }
            }

            mBackFrom = R.string.back_from_unknown;
        }
    }

    private void getGoodFromServer() {
        WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
        Call<MatchGood> call = face.getGood(Profile.instance().getToken(), mGoodId);

        call.enqueue(new Callback<MatchGood>() {
            @Override
            public void onResponse(Call<MatchGood> call, Response<MatchGood> response) {
                MatchGood good = response.body();
                mOldGoodCalc = new GoodCalc(good);
                init(new GoodCalc(mOldGoodCalc));
            }

            @Override
            public void onFailure(Call<MatchGood> call, Throwable t) {
                Log.d(LOG_TAG, "fail to get stock new by rsn:" + mGoodId);
            }
        });
    }

    private void startUpdate() {
        GoodCalc calc = mGoodController.getModel();

        MatchGood updateGood = new MatchGood();
        if (!calc.getStyleNumber().equals(mOldGoodCalc.getStyleNumber())){
            updateGood.setStyleNumber(calc.getStyleNumber());
        }
        if (!calc.getBrand().getId().equals(mOldGoodCalc.getBrand().getId())) {
            updateGood.setBrand(calc.getBrand().getName());
        }
        if (!calc.getGoodType().getId().equals(mOldGoodCalc.getGoodType().getId())) {
            updateGood.setType(calc.getGoodType().getName());
        }
        if (!calc.getFirm().getId().equals(mOldGoodCalc.getFirm().getId())) {
            updateGood.setFirmId(calc.getFirm().getId());
        }
        if (!calc.getSex().equals(mOldGoodCalc.getSex())) {
            updateGood.setSex(calc.getSex());
        }
        if (!calc.getYear().equals(mOldGoodCalc.getYear())) {
            updateGood.setYear(calc.getYear());
        }
        if (!calc.getSeason().equals(mOldGoodCalc.getSeason())) {
            updateGood.setSeason(calc.getSeason());
        }

        if (!calc.getOrgPrice().equals(mOldGoodCalc.getOrgPrice())) {
            updateGood.setOrgPrice(calc.getOrgPrice());
        }
        if (!calc.getTagPrice().equals(mOldGoodCalc.getTagPrice())) {
            updateGood.setTagPrice(calc.getTagPrice());
        }
        if (!calc.getPkgPrice().equals(mOldGoodCalc.getPkgPrice())) {
            updateGood.setPkgPrice(calc.getPkgPrice());
        }
        if (!calc.getPrice3().equals(mOldGoodCalc.getPrice3())) {
            updateGood.setPrice3(calc.getPrice3());
        }
        if (!calc.getPrice4().equals(mOldGoodCalc.getPrice4())) {
            updateGood.setPrice4(calc.getPrice4());
        }
        if (!calc.getPrice5().equals(mOldGoodCalc.getPrice5())) {
            updateGood.setPrice5(calc.getPrice5());
        }
        if (!calc.getDiscount().equals(mOldGoodCalc.getDiscount())) {
            updateGood.setDiscount(calc.getDiscount());
        }

        // color
        if (!calc.getStringColorIds().equals(mOldGoodCalc.getStringColorIds())) {
            updateGood.setColor(calc.getStringColorIds());
        }
        // size group
        if (!calc.getStringSizeGroups().equals(mOldGoodCalc.getStringSizeGroups())) {
            updateGood.setSize(calc.getStringSizeGroups());
            updateGood.setsGroup(calc.getStringSizeGroupIds());
        }

        updateGood.setGoodId(mGoodId);
        updateGood.setShopId(Profile.instance().getLoginShop());

        updateGood.setOStyleNumber(mOldGoodCalc.getStyleNumber());
        updateGood.setOBrand(mOldGoodCalc.getBrand().getId());
        updateGood.setOPath(mOldGoodCalc.getPath());
        updateGood.setOFirm(mOldGoodCalc.getFirm().getId());

        startRequest(updateGood);
    }

    public void startRequest(MatchGood updateGood) {
        mButtons.get(R.id.good_update_save).disable();

        WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
        Call<com.diablo.dt.diablo.response.Response> call = face.updateGood(
            Profile.instance().getToken(),
            new GoodUpdateRequest(updateGood));

        call.enqueue(new Callback<com.diablo.dt.diablo.response.Response>() {
            @Override
            public void onResponse(Call<com.diablo.dt.diablo.response.Response> call,
                                   Response<com.diablo.dt.diablo.response.Response> response) {
                com.diablo.dt.diablo.response.Response result = response.body();

                if (DiabloEnum.HTTP_OK.equals(response.code()) && result.getCode().equals(DiabloEnum.SUCCESS)) {
                    // add good to match

                    Profile.instance().removeMatchGood(
                        mOldGoodCalc.getStyleNumber(), mOldGoodCalc.getBrand().getId());

                    GoodCalc calc = mGoodController.getModel();
                    MatchGood newGood = new MatchGood();
                    newGood.setId(mGoodId);
                    newGood.setStyleNumber(calc.getStyleNumber());
                    newGood.setBrandId(calc.getBrand().getId());
                    newGood.setBrand(calc.getBrand().getName());
                    newGood.setTypeId(calc.getGoodType().getId());
                    newGood.setType(calc.getGoodType().getName());
                    newGood.setFirmId(calc.getFirm().getId());

                    newGood.setSex(calc.getSex());
                    newGood.setYear(calc.getYear());
                    newGood.setSeason(calc.getSeason());

                    newGood.setOrgPrice(calc.getOrgPrice());
                    newGood.setTagPrice(calc.getTagPrice());
                    newGood.setPkgPrice(calc.getPkgPrice());
                    newGood.setPrice3(calc.getPrice3());
                    newGood.setPrice4(calc.getPrice4());
                    newGood.setPrice5(calc.getPrice5());
                    newGood.setDiscount(calc.getDiscount());

                    newGood.setAlarmDay(calc.getAlarmDay());

                    newGood.setColor(calc.getStringColorIds());
                    newGood.setSize(calc.getStringSizeGroups());
                    newGood.setsGroup(calc.getStringSizeGroupIds());

                    if (newGood.getColor().equals(
                        DiabloUtils.instance().toString(DiabloEnum.DIABLO_FREE_COLOR))
                        && newGood.getSize().equals(DiabloEnum.DIABLO_FREE_SIZE)) {
                        newGood.setFree(DiabloEnum.DIABLO_FREE);
                    }else {
                        newGood.setFree(DiabloEnum.DIABLO_NON_FREE);
                    }

                    Profile.instance().addMatchGood(newGood);

                    new DiabloAlertDialog(
                        getContext(),
                        false,
                        getResources().getString(R.string.good_update),
                        getContext().getString(R.string.good_update_success),
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                SaleUtils.switchToSlideMenu(GoodUpdate.this, DiabloEnum.TAG_GOOD_DETAIL);
//                                if (!mGoodId.equals(mLastGoodId)) {
//                                    getGoodFromServer();
//                                }
                            }
                        }).create();
                }
                else {
                    mButtons.get(R.id.good_update_save).enable();
                    Integer errorCode = response.code() == 0 ? result.getCode() : response.code();
                    String extraMessage = result == null ? "" : result.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.good_update),
                        DiabloError.getError(errorCode) + extraMessage).create();
                }
            }

            @Override
            public void onFailure(Call<com.diablo.dt.diablo.response.Response> call, Throwable t) {
                mButtons.get(R.id.good_update_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.good_update),
                    DiabloError.getError(99)).create();
            }
        });
    }

}
