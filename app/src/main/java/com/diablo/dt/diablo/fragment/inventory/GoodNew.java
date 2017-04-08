package com.diablo.dt.diablo.fragment.inventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.WgoodClient;
import com.diablo.dt.diablo.controller.DiabloGoodController;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.inventory.GoodCalc;
import com.diablo.dt.diablo.request.inventory.InventoryNewRequest;
import com.diablo.dt.diablo.response.inventory.InventoryNewResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.DiabloGoodCalcView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodNew extends Fragment {
    public final static DiabloUtils UTILS = DiabloUtils.instance();
    private String [] mSexes;
    private String [] mYears;
    private String [] mSeasons;

    private SparseArray<DiabloButton> mButtons;

    private DiabloGoodCalcView mGoodCalcView;
    // private GoodCalc mGoodCalc;
    private DiabloGoodController mGoodController;

    public GoodNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoodNew.
     */
    // TODO: Rename and change types and number of parameters
    public static GoodNew newInstance(String param1, String param2) {
        GoodNew fragment = new GoodNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSexes = getResources().getStringArray(R.array.sexes);
        mYears = getResources().getStringArray(R.array.years);
        mSeasons = getResources().getStringArray(R.array.seasons);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_good_new, container, false);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        mButtons = new SparseArray<>();
        mButtons.put(R.id.good_new_save, new DiabloButton(getContext(), R.id.good_new_save));

        initView(view);
        init();

        return view;
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

    private void init() {
        DiabloBrand lastSelectBrand = null;
        Firm lastSelectFirm = null;
        String lastStyleNumber = null;

        if (null != mGoodController) {
            lastStyleNumber = mGoodController.getModel().getStyleNumber();
            lastSelectBrand = mGoodController.getModel().getBrand();
            lastSelectFirm = mGoodController.getModel().getFirm();

            mGoodController.reset();
            mGoodController = null;
        }

        mGoodController = new DiabloGoodController(new GoodCalc(), mGoodCalcView);
        mGoodController.setSexAdapter(getContext(), mSexes);
        mGoodController.setYearAdapter(getContext(), mYears, UTILS.currentYear());
        mGoodController.setSeasonAdapter(getContext(), mSeasons, UTILS.currentMonth());

        mGoodController.setSexSelectedListener();
        mGoodController.setYearSelectedListener();
        mGoodController.setSeasonSelectedListener();

        mGoodController.setValidateWatcherOfStyleNumber(getContext(), lastStyleNumber);
//        mGoodController.setValidateWatcherOfBrand(getContext());
//        mGoodController.setValidateWatcherOfGoodType(getContext());

        // firm
        mGoodController.setFirmWatcher(getContext(), Profile.instance().getFirms(), lastSelectFirm);
        // brand
        mGoodController.setBrandWatcher(getContext(), Profile.instance().getBrands(), lastSelectBrand);
        // type
        mGoodController.setGoodTypeWatcher(getContext(), Profile.instance().getDiabloTypes());

        // price
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getOrgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getPkgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getTagPrice());

        // callback
        mGoodController.setOnActionOfGoodValidateListener(mOnActionOfGoodValidate);

        mButtons.get(R.id.good_new_save).disable();
    }

    private DiabloGoodController.OnActionOfGoodValidate mOnActionOfGoodValidate
        = new DiabloGoodController.OnActionOfGoodValidate() {
        @Override
        public void actionOfInvalid() {
            mButtons.get(R.id.good_new_save).disable();
        }

        @Override
        public void actionOfValid() {
            mButtons.get(R.id.good_new_save).enable();
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
        inflater.inflate(R.menu.action_on_good_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.good_new_back:
                // SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_DETAIL);
                break;
            case R.id.good_new_save:
                startAdd();
                break;
            default:
                // return super.onOptionsItemSelected(item);
                break;

        }

        return true;
    }

    public void startAdd() {
        GoodCalc calc = mGoodController.getModel();

        InventoryNewRequest.Inventory inv = new InventoryNewRequest.Inventory(
            calc.getColors(), calc.getSizeGroups());


        inv.setStyleNumber(calc.getStyleNumber());
        inv.setBrandId(calc.getBrand().getName());
        inv.setTypeId(calc.getGoodType().getName());

        inv.setSex(calc.getSex());
        inv.setFirm(calc.getFirm().getId());
        inv.setSeason(calc.getSeason());
        inv.setYear(calc.getYear());

        inv.setOrgPrice(calc.getOrgPrice());
        inv.setTagPrice(calc.getTagPrice());
        inv.setPkgPrice(calc.getPkgPrice());
        inv.setPrice3(calc.getPrice3());
        inv.setPrice4(calc.getPrice4());
        inv.setPrice5(calc.getPrice5());
        inv.setDiscount(calc.getDiscount());

        inv.setPath(calc.getPath());
        inv.setAlarmDay(calc.getAlarmDay());

        InventoryNewRequest request = new InventoryNewRequest(inv);

        startRequest(request);
    }

    public void startRequest(final InventoryNewRequest request) {
        mButtons.get(R.id.good_new_save).disable();

        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<InventoryNewResponse> call = face.addGood(Profile.instance().getToken(), request);

        call.enqueue(new Callback<InventoryNewResponse>() {
            @Override
            public void onResponse(Call<InventoryNewResponse> call, Response<InventoryNewResponse> response) {
                InventoryNewResponse result = response.body();
                if (DiabloEnum.HTTP_OK.equals(response.code()) && result.getCode().equals(DiabloEnum.SUCCESS)) {
                    // add good to match
                    MatchGood good = new MatchGood();
                    GoodCalc calc = mGoodController.getModel();

                    good.setStyleNumber(calc.getStyleNumber());
                    good.setBrandId(calc.getBrand().getId());
                    good.setBrand(calc.getBrand().getName());
                    good.setTypeId(calc.getGoodType().getId());
                    good.setType(calc.getGoodType().getName());
                    good.setFirmId(calc.getFirm().getId());
                    good.setSex(calc.getSex());

                    good.setColor(request.getInventory().getColorsWithComma());
                    good.setYear(calc.getYear());
                    good.setSeason(calc.getSeason());

                    good.setsGroup(request.getInventory().getSizeGroupsWithComma());
                    good.setSize(request.getInventory().getSizesWithComma());

                    good.setFree(calc.getFree());

                    good.setOrgPrice(calc.getOrgPrice());
                    good.setTagPrice(calc.getTagPrice());
                    good.setPkgPrice(calc.getPkgPrice());
                    good.setPrice3(calc.getPrice3());
                    good.setPrice4(calc.getPrice4());
                    good.setPrice5(calc.getPrice5());
                    good.setDiscount(calc.getDiscount());

                    good.setPath(calc.getPath());
                    good.setAlarmDay(calc.getAlarmDay());

                    Profile.instance().addMatchGood(good);

                    new DiabloAlertDialog(
                        getContext(),
                        false,
                        getResources().getString(R.string.nav_good_new),
                        getContext().getString(R.string.add_good_success),
                        new DiabloAlertDialog.OnOkClickListener() {
                            @Override
                            public void onOk() {
                                init();
                            }
                        }).create();
                }
                else {
                    mButtons.get(R.id.good_new_save).enable();
                    Integer errorCode = response.code() == 0 ? result.getCode() : response.code();
                    String extraMessage = result == null ? "" : result.getError();
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_good_new),
                        DiabloError.getInstance().getError(errorCode) + extraMessage).create();
                    }
            }

            @Override
            public void onFailure(Call<InventoryNewResponse> call, Throwable t) {
                mButtons.get(R.id.good_new_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.nav_good_new),
                    DiabloError.getInstance().getError(99)).create();
            }
        });
    }

}
