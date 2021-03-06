package com.diablo.dt.diablo.fragment.good;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.adapter.FirmAdapter;
import com.diablo.dt.diablo.adapter.MatchBrandAdapter;
import com.diablo.dt.diablo.adapter.MatchGoodTypeAdapter;
import com.diablo.dt.diablo.client.WGoodClient;
import com.diablo.dt.diablo.controller.DiabloGoodController;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloButton;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.model.good.GoodCalc;
import com.diablo.dt.diablo.model.good.GoodColorNewDialog;
import com.diablo.dt.diablo.model.good.GoodUtils;
import com.diablo.dt.diablo.model.sale.SaleUtils;
import com.diablo.dt.diablo.request.good.GoodNewRequest;
import com.diablo.dt.diablo.response.good.InventoryNewResponse;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloAutoCompleteTextWatcher;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloPattern;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.diablo.dt.diablo.view.good.DiabloGoodCalcView;

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

    public GoodNew() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GoodNew newInstance(String param1, String param2) {
        GoodNew fragment = new GoodNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSexes = getResources().getStringArray(R.array.sexes);
        mYears = getResources().getStringArray(R.array.years);
        mSeasons = getResources().getStringArray(R.array.seasons);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_good_new, container, false);

        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();

        View btnAddFirm = view.findViewById(R.id.btn_add_firm);
        btnAddFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFirm(inflater, view);
            }
        });

        View btnAddBrand = view.findViewById(R.id.btn_add_brand);
        btnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBrand(inflater, view);
            }
        });

        View btnAddType = view.findViewById(R.id.btn_add_good_type);
        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addType(inflater, view);
            }
        });

        View btnAddColor = view.findViewById(R.id.btn_add_color);
        btnAddColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoodColorNewDialog(getContext(), null).show();
            }
        });

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

        mButtons = new SparseArray<>();
        mButtons.put(R.id.good_new_save, new DiabloButton(getContext(), R.id.good_new_save));

        initView(view);
        init();

        return view;
    }

    private void addFirm(LayoutInflater inflater, View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(
            R.layout.shortcut_create_firm, (ViewGroup) parent.findViewById(R.id.shortcut_create_firm));
        final AutoCompleteTextView autoFirm = (AutoCompleteTextView) view.findViewById(R.id.firm_name);

        final AlertDialog dialog = builder.setView(view)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = autoFirm.getText().toString().trim();
                    final Firm firm = new Firm(name);
                    firm.addFirm(getContext(), new Firm.OnFirmAddListener() {
                        @Override
                        public void afterAdd(Firm addedFirm) {
                            mGoodController.removeFirmListener();
                            mGoodController.setFirmWatcher(getContext(), addedFirm);
                            mGoodController.checkFocusOfFirm();
                            // editTextFirm.setError(null);
                        }
                    });
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);

        new FirmAdapter(getContext(), R.layout.typeahead_firm, R.id.typeahead_select_firm, autoFirm);

        new DiabloAutoCompleteTextWatcher(
            autoFirm,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    if (!DiabloPattern.isValidFirm(s)) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
                        autoFirm.setError(getString(R.string.invalid_firm));
                    }
                    else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
                        autoFirm.setError(null);
                    }
                }
            });

//        editTextFirm.addTextChangedListener(new DiabloEditTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String name = editable.toString().trim();
//                if (!DiabloPattern.isValidFirm(name)) {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
//                    editTextFirm.setError(getString(R.string.invalid_firm));
//                }
//                else {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
//                }
//            }
//        });
    }

    private void addBrand(LayoutInflater inflater, View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(
            R.layout.shortcut_create_brand, (ViewGroup) parent.findViewById(R.id.shortcut_create_brand));

        final AutoCompleteTextView autoBrand = (AutoCompleteTextView) view.findViewById(R.id.brand_name);

        final AlertDialog dialog = builder.setView(view)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = autoBrand.getText().toString().trim();
                    final DiabloBrand brand = new DiabloBrand(name);

                    brand.addBrand(getContext(), new DiabloBrand.OnBrandAddListener() {
                        @Override
                        public void afterAdd(DiabloBrand addedBrand) {
                            // mGoodController.clearFocusOfBrand();
                            mGoodController.removeBrandListener();
                            mGoodController.setBrandWatcher(getContext(), addedBrand, null);
                            mGoodController.checkFocusOfBrand();
                            // autoBrand.setError(null);
                            // mGoodController.requestFocusOfBrand();
                        }
                    });
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);

        /// final EditText editTextFirm = (EditText) view.findViewById(R.id.brand_name);
        // final AutoCompleteTextView autoBrand = (AutoCompleteTextView) view.findViewById(R.id.brand_name);
        new MatchBrandAdapter(getContext(), R.layout.typeahead_firm, R.id.typeahead_select_firm, autoBrand);

        new DiabloAutoCompleteTextWatcher(
            autoBrand,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    if (!DiabloPattern.isValidBrand(s)) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
                        autoBrand.setError(getString(R.string.invalid_brand));
                    }
                    else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
                        autoBrand.setError(null);
                    }
                }
            });

//        editTextFirm.addTextChangedListener(new DiabloEditTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String name = editable.toString().trim();
//                if (!DiabloPattern.isValidBrand(name)) {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
//                    editTextFirm.setError(getString(R.string.invalid_brand));
//                }
//                else {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
//                }
//            }
//        });
    }

    private void addType(LayoutInflater inflater, View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = inflater.inflate(
            R.layout.shortcut_create_good_type, (ViewGroup) parent.findViewById(R.id.shortcut_create_good_type));

        final AutoCompleteTextView autoType = (AutoCompleteTextView) view.findViewById(R.id.good_type_name);

        final AlertDialog dialog = builder.setView(view)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String name = autoType.getText().toString().trim();

                    final DiabloType goodType = new DiabloType(name);
                    goodType.addGoodType(getContext(), new DiabloType.OnGoodTypeAddListener() {
                        @Override
                        public void afterAdd(DiabloType addedType) {
                            mGoodController.removeGoodTypeListener();
                            mGoodController.setGoodTypeWatcher(getContext(), addedType);
                            mGoodController.checkFocusOfType();
                            // autoType.setError(null);
                        }
                    });
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);

        new MatchGoodTypeAdapter(getContext(), R.layout.typeahead_firm, R.id.typeahead_select_firm, autoType);

        new DiabloAutoCompleteTextWatcher(
            autoType,
            new DiabloAutoCompleteTextWatcher.DiabloAutoCompleteTextChangListener() {
                @Override
                public void afterTextChanged(String s) {
                    if (!DiabloPattern.isValidGoodType(s)) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
                        autoType.setError(getString(R.string.invalid_good_type));
                    }
                    else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
                        autoType.setError(null);
                    }
                }
            });

        // final EditText editTextFirm = (EditText) view.findViewById(R.id.good_type_name);
//        editTextFirm.addTextChangedListener(new DiabloEditTextWatcher() {
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String name = editable.toString().trim();
//                if (!DiabloPattern.isValidGoodType(name)) {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(false);
//                    editTextFirm.setError(getString(R.string.invalid_good_type));
//                }
//                else {
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setClickable(true);
//                }
//            }
//        });
    }

    private void selectColor() {
        if (null != mGoodController) {
            GoodUtils.switchToColorSelectFrame(
                mGoodController.getModel(),
                DiabloEnum.GOOD_NEW,
                this,
                null);
        }
    }

    private void selectSize() {
        if (null != mGoodController) {
            GoodUtils.switchToSizeSelectFrame(
                mGoodController.getModel(),
                DiabloEnum.GOOD_NEW,
                this,
                null);
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

        mGoodCalcView.setComment(view.findViewById(R.id.good_comment));
    }

    private void init() {
        DiabloBrand lastSelectBrand = null;
        Firm lastSelectFirm = null;
        DiabloType lastSelectGoodType = null;
        String lastStyleNumber = null;

        Float lastOrgPrice = null;
        Float lastPkgPrice = null;
        Float lastTagPrice = null;

        String lastOrgPriceOfView = null;
        String lastPkgPriceOfView = null;
        String lastTagPriceOfView = null;

        if (null != mGoodController) {
            GoodCalc oldCalc = mGoodController.getModel();
            lastStyleNumber = oldCalc.getStyleNumber();
            lastSelectBrand = oldCalc.getBrand();
            lastSelectFirm = oldCalc.getFirm();
            lastSelectGoodType = oldCalc.getGoodType();

            lastOrgPrice = oldCalc.getOrgPrice();
            lastPkgPrice = oldCalc.getPkgPrice();
            lastTagPrice = oldCalc.getTagPrice();

            lastOrgPriceOfView = mGoodCalcView.getOrgPriceStringValue();
            lastPkgPriceOfView = mGoodCalcView.getPkgPriceStringValue();
            lastTagPriceOfView = mGoodCalcView.getTagPriceStringValue();

            mGoodController.reset();
            mGoodController = null;
        }

        GoodCalc calc = new GoodCalc();
        calc.setSex(DiabloEnum.DIABLO_FAMAN);

        mGoodController = new DiabloGoodController(calc, mGoodCalcView);
        if (null != lastOrgPriceOfView && lastOrgPriceOfView.length() != 0) {
            mGoodController.setOrgPrice(lastOrgPrice);
        }
        if (null != lastPkgPriceOfView && lastPkgPriceOfView.length() != 0) {
            mGoodController.setPkgPrice(lastPkgPrice);
        }
        if (null != lastTagPriceOfView && lastTagPriceOfView.length() != 0) {
            mGoodController.setTagPrice(lastTagPrice);
        }

        mGoodController.setSexAdapter(getContext(), mSexes);
        mGoodController.setYearAdapter(getContext(), mYears, UTILS.currentYear());
        mGoodController.setSeasonAdapter(getContext(), mSeasons, UTILS.currentMonth());

        mGoodController.setSexSelectedListener();
        mGoodController.setYearSelectedListener();
        mGoodController.setSeasonSelectedListener();

        mGoodController.setValidateWatcherOfStyleNumber(getContext(), lastStyleNumber, null);
//        mGoodController.setValidateWatcherOfBrand(getContext());
//        mGoodController.setValidateWatcherOfGoodType(getContext());

        // firm
        mGoodController.setFirmWatcher(getContext(), lastSelectFirm);
        // brand
        mGoodController.setBrandWatcher(getContext(), lastSelectBrand, null);
        // type
        mGoodController.setGoodTypeWatcher(getContext(), lastSelectGoodType);

        // price
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getOrgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getPkgPrice());
        mGoodController.setValidateWatcherOfPrice(getContext(), mGoodCalcView.getTagPrice());

        // comment
        mGoodController.setValidateWatcherOfComment(getContext(), mGoodCalcView.getComment());

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        // utils.makeToast(getContext(), hidden ? "hidden" : "show");
        if (!hidden){
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

            mBackFrom = R.string.back_from_unknown;
        }
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
        // menu.clear();

        inflater.inflate(R.menu.action_on_good_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.good_new_back:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_GOOD_DETAIL);
                break;
            case R.id.good_new_back_to_stock_in:
                SaleUtils.switchToSlideMenu(this, DiabloEnum.TAG_STOCK_IN);
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

        GoodNewRequest.Inventory inv = new GoodNewRequest.Inventory(
            calc.getColors(), calc.getSizeGroups());


        inv.setStyleNumber(calc.getStyleNumber());
        inv.setBrandId(calc.getBrand().getId());
        inv.setBrand(calc.getBrand().getName());
        inv.setTypeId(calc.getGoodType().getId());
        inv.setType(calc.getGoodType().getName());

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

        inv.setComment(calc.getComment());

        GoodNewRequest request = new GoodNewRequest(inv);

        startRequest(request);
    }

    public void startRequest(final GoodNewRequest request) {
        mButtons.get(R.id.good_new_save).disable();

        WGoodInterface face = WGoodClient.getClient().create(WGoodInterface.class);
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

                    if (0 == calc.getColors().size() && 0 == calc.getSizeGroups().size()) {
                        good.setFree(DiabloEnum.DIABLO_FREE);
                    } else {
                        if (1 == calc.getColors().size()
                            && calc.getColors().get(0).getColorId().equals(DiabloEnum.DIABLO_FREE_COLOR)
                            && 1 == calc.getSizeGroups().size()
                            && calc.getSizeGroups().get(0).getGroupId().equals(DiabloEnum.DIABLO_FREE_SIZE_GROUP)
                            ){
                            good.setFree(DiabloEnum.DIABLO_FREE);
                        }
                        else {
                            good.setFree(DiabloEnum.DIABLO_NON_FREE);
                        }
                    }

                    good.setOrgPrice(calc.getOrgPrice());
                    good.setTagPrice(calc.getTagPrice());
                    good.setPkgPrice(calc.getPkgPrice());
                    good.setPrice3(calc.getPrice3());
                    good.setPrice4(calc.getPrice4());
                    good.setPrice5(calc.getPrice5());
                    good.setDiscount(calc.getDiscount());

                    good.setPath(calc.getPath());
                    good.setAlarmDay(calc.getAlarmDay());
                    good.setComment(calc.getComment());

                    Profile.instance().addMatchGood(good);

                    UTILS.makeToast(getContext(), getContext().getString(R.string.add_good_success));
                    init();

//                    new DiabloAlertDialog(
//                        getContext(),
//                        false,
//                        getResources().getString(R.string.nav_good_new),
//                        getContext().getString(R.string.add_good_success),
//                        new DiabloAlertDialog.OnOkClickListener() {
//                            @Override
//                            public void onOk() {
//                                init();
//                            }
//                        }).create();
                }
                else {
                    mButtons.get(R.id.good_new_save).enable();
                    Integer errorCode;
                    String extraMessage = DiabloEnum.EMPTY_STRING;

                    if (DiabloEnum.HTTP_OK == response.code()) {
                        errorCode = result.getCode();
                        extraMessage = result.getError();
                    } else {
                        errorCode = response.code();
                    }
                    
                    new DiabloAlertDialog(
                        getContext(),
                        getResources().getString(R.string.nav_good_new),
                        DiabloError.getError(errorCode) + extraMessage).create();
                    }
            }

            @Override
            public void onFailure(Call<InventoryNewResponse> call, Throwable t) {
                mButtons.get(R.id.good_new_save).enable();
                new DiabloAlertDialog(
                    getContext(),
                    getResources().getString(R.string.nav_good_new),
                    DiabloError.getError(99)).create();
            }
        });
    }
}
