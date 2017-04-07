package com.diablo.dt.diablo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.client.BaseSettingClient;
import com.diablo.dt.diablo.client.EmployeeClient;
import com.diablo.dt.diablo.client.FirmClient;
import com.diablo.dt.diablo.client.RetailerClient;
import com.diablo.dt.diablo.client.RightClient;
import com.diablo.dt.diablo.client.StockClient;
import com.diablo.dt.diablo.client.WLoginClient;
import com.diablo.dt.diablo.client.WgoodClient;
import com.diablo.dt.diablo.entity.BaseSetting;
import com.diablo.dt.diablo.entity.DiabloBrand;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.DiabloType;
import com.diablo.dt.diablo.entity.DiabloUser;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.Firm;
import com.diablo.dt.diablo.entity.MatchGood;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.request.MatchGoodRequest;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.response.LoginResponse;
import com.diablo.dt.diablo.response.LoginUserInfoResponse;
import com.diablo.dt.diablo.rest.BaseSettingInterface;
import com.diablo.dt.diablo.rest.EmployeeInterface;
import com.diablo.dt.diablo.rest.FirmInterface;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.rest.RightInterface;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.rest.WLoginInterface;
import com.diablo.dt.diablo.utils.DiabloDBManager;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.diablo.dt.diablo.utils.DiabloUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final static String LOG_TAG = "LOGIN:";

    Button mBtnLogin;
    TextInputLayout mLoginWrap, mPasswordWrap;
    Context mContext;

    private final LoginHandler mLoginHandler = new LoginHandler(this);
    // private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // init db
        DiabloDBManager.instance().init(this);

        mContext = this;
        Profile.instance().setContext(this.getApplicationContext());

        mLoginWrap = (TextInputLayout) findViewById(R.id.login_name_holder);
        mPasswordWrap = (TextInputLayout) findViewById(R.id.login_password_holder);

        final DiabloUser user = DiabloDBManager.instance().getFirstLoginUser();
        if (null != user) {
            if (null != mLoginWrap.getEditText())
                mLoginWrap.getEditText().setText(user.getName());
            if (null != mPasswordWrap.getEditText())
                mPasswordWrap.getEditText().setText(user.getPassword());
        }

        // InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mLoginWrap.getEditText().getText().toString();
                final String password = mPasswordWrap.getEditText().getText().toString();
                if (name.trim().equals(""))
                    mLoginWrap.getEditText().setError("请输入用户名");
                else if (password.trim().equals(""))
                    mPasswordWrap.getEditText().setError("请输入用户密码");
                 else {
                    // login
                    mBtnLogin.setClickable(false);
                    WLoginInterface face = WLoginClient.getClient().create(WLoginInterface.class);
                    Call<LoginResponse> call = face.login(name, password, DiabloEnum.TABLET);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Log.d(LOG_TAG, "success to login");
                            // AlertDialogLayout dialog = (AlertDialogLayout)findViewById(R.id.alert_dialog);
                            if (0 != response.body().getCode()){
                                mBtnLogin.setClickable(true);
                                loginError(response.body().getCode());
                            } else {
                                Profile.instance().setToken(response.body().getToken());
                                if (null == user) {
                                    DiabloDBManager.instance().addUser(name, password);
                                }
                                else {
                                    if(!user.getName().equals(name)){
                                        DiabloDBManager.instance().addUser(name, password);
                                    } else {
                                        if (!user.getPassword().equals(password)) {
                                            DiabloDBManager.instance().updateUser(name, password);
                                        }
                                    }
                                }

                                getLoginUserInfo();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            mBtnLogin.setClickable(true);
                            loginError(1199);
                        }
                    });
                }
            }
        });
    }

    public void gotoMain(){
        DiabloDBManager.instance().close();
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void loginError(Integer code){
        String error = DiabloError.getInstance().getError(code);
        new MaterialDialog.Builder(mContext)
                .title(R.string.user_login)
                .content(error)
                // .contentColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .positiveText(mContext.getResources().getString(R.string.login_ok))
                .positiveColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    @Override
    public void onBackPressed() {

    }

    private void getLoginUserInfo(){
        RightInterface rightInterface = RightClient.getClient().create(RightInterface.class);
        Call<LoginUserInfoResponse> rightCall = rightInterface.getLoginUserInfo(
                Profile.instance().getToken());

        rightCall.enqueue(new Callback<LoginUserInfoResponse>() {
            @Override
            public void onResponse(Call<LoginUserInfoResponse> call, Response<LoginUserInfoResponse> response) {
                Log.d(LOG_TAG, "success to get login information");
                LoginUserInfoResponse user = response.body();
                Profile.instance().setLoginEmployee(user.getLoginEmployee());
                Profile.instance().setLoginFirm(user.getLoginFirm());
                Profile.instance().setLoginRetailer(user.getLoginRetailer());
                Profile.instance().setLoginShop(user.getLoginShop());
                Profile.instance().setLoginShops(user.getShops());
                Profile.instance().setLoginRights(user.getRights());
                Profile.instance().initLoginUser();

                Message message = Message.obtain(mLoginHandler);
                message.what = 10;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<LoginUserInfoResponse> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 11;
                message.sendToTarget();
            }
        });
    }

    private void getRetailer(){
        RetailerInterface face = RetailerClient.getClient().create(RetailerInterface.class);
        Call<List<Retailer>> call = face.listRetailer(Profile.instance().getToken());
        call.enqueue(new Callback<List<Retailer>>() {
            @Override
            public void onResponse(Call<List<Retailer>> call, Response<List<Retailer>> response) {
                Log.d(LOG_TAG, "success to get retailer");
                Profile.instance().setRetailers(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 20;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<Retailer>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get retailer");
                Message message = Message.obtain(mLoginHandler);
                message.what = 21;
                message.sendToTarget();
            }
        });
    }

    private void getEmployee(){
        EmployeeInterface face = EmployeeClient.getClient().create(EmployeeInterface.class);
        Call<List<Employee>> call = face.listEmployee(Profile.instance().getToken());
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                Log.d(LOG_TAG, "success to get employee");
                Profile.instance().setEmployees(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 30;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 31;
                message.sendToTarget();
            }
        });
    }

    private void getBaseSetting(){
        BaseSettingInterface face = BaseSettingClient.getClient().create(BaseSettingInterface.class);
        Call<List<BaseSetting>> call = face.listBaseSetting(Profile.instance().getToken());
        call.enqueue(new Callback<List<BaseSetting>>() {
            @Override
            public void onResponse(Call<List<BaseSetting>> call, Response<List<BaseSetting>> response) {
                Log.d(LOG_TAG, "success to get base setting");
                Profile.instance().setBaseSettings(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 40;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<BaseSetting>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 41;
                message.sendToTarget();
            }
        });
    }

    private void getColor(){
        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<List<DiabloColor>> call = face.listColor(Profile.instance().getToken());
        call.enqueue(new Callback<List<DiabloColor>>() {
            @Override
            public void onResponse(Call<List<DiabloColor>> call, Response<List<DiabloColor>> response) {
                Log.d(LOG_TAG, "success to get color");
                Profile.instance().setColors(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 50;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<DiabloColor>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 51;
                message.sendToTarget();
            }
        });
    }

    private void getSizeGroup(){
        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<List<DiabloSizeGroup>> call = face.listSizeGroup(Profile.instance().getToken());
        call.enqueue(new Callback<List<DiabloSizeGroup>>() {
            @Override
            public void onResponse(Call<List<DiabloSizeGroup>> call, Response<List<DiabloSizeGroup>> response) {
                Log.d(LOG_TAG, "success to get size group");
                Profile.instance().setSizeGroups(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 60;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<DiabloSizeGroup>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 61;
                message.sendToTarget();
            }
        });
    }

    private void getAllMatchStock(){
        StockInterface face = StockClient.getClient().create(StockInterface.class);
        Integer loginShop = Profile.instance().getLoginShop();
        Call<List<MatchStock>> call = face.matchAllStock(
                Profile.instance().getToken(),
                new MatchStockRequest(
                        Profile.instance().getConfig(loginShop, DiabloEnum.START_TIME, DiabloUtils.getInstance().currentDate()),
                        loginShop,
                        DiabloEnum.USE_REPO));
        call.enqueue(new Callback<List<MatchStock>>() {
            @Override
            public void onResponse(Call<List<MatchStock>> call, Response<List<MatchStock>> response) {
                Log.d(LOG_TAG, "success to get match stock");
                Profile.instance().setMatchStocks(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 70;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<MatchStock>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get match stock");
                Message message = Message.obtain(mLoginHandler);
                message.what = 71;
                message.sendToTarget();
            }
        });
    }

    private void getBrand(){
        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<List<DiabloBrand>> call = face.listBrand(Profile.instance().getToken());
        call.enqueue(new Callback<List<DiabloBrand>>() {
            @Override
            public void onResponse(Call<List<DiabloBrand>> call, Response<List<DiabloBrand>> response) {
                Log.d(LOG_TAG, "success to get brand");
                Profile.instance().setBrands(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 80;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<DiabloBrand>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 81;
                message.sendToTarget();
            }
        });
    }

    private void getType(){
        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<List<DiabloType>> call = face.listType(Profile.instance().getToken());
        call.enqueue(new Callback<List<DiabloType>>() {
            @Override
            public void onResponse(Call<List<DiabloType>> call, Response<List<DiabloType>> response) {
                Log.d(LOG_TAG, "success to get type");
                Profile.instance().setDiabloTypes(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 90;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<DiabloType>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 91;
                message.sendToTarget();
            }
        });
    }

    private void getFirm() {
        FirmInterface face = FirmClient.getClient().create(FirmInterface.class);
        Call<List<Firm>> call = face.listFirm(Profile.instance().getToken());
        call.enqueue(new Callback<List<Firm>>() {
            @Override
            public void onResponse(Call<List<Firm>> call, Response<List<Firm>> response) {
                Log.d(LOG_TAG, "success to get firm");
                Profile.instance().setFirms(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 100;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<Firm>> call, Throwable t) {
                Message message = Message.obtain(mLoginHandler);
                message.what = 101;
                message.sendToTarget();
            }
        });
    }

    private void getAllMatchGood(){
        WGoodInterface face = WgoodClient.getClient().create(WGoodInterface.class);
        Call<List<MatchGood>> call = face.matchAllGood(
            Profile.instance().getToken(),
            new MatchGoodRequest(
                Profile.instance().getConfig(DiabloEnum.START_TIME, DiabloUtils.getInstance().currentDate())));

        call.enqueue(new Callback<List<MatchGood>>() {
            @Override
            public void onResponse(Call<List<MatchGood>> call, Response<List<MatchGood>> response) {
                Log.d(LOG_TAG, "success to get match stock");
                Profile.instance().setMatchGoods(response.body());
                Message message = Message.obtain(mLoginHandler);
                message.what = 110;
                message.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<MatchGood>> call, Throwable t) {
                Log.d(LOG_TAG, "failed to get match stock");
                Message message = Message.obtain(mLoginHandler);
                message.what = 111;
                message.sendToTarget();
            }
        });
    }

    private static class LoginHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        private LoginHandler(LoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
                // ...
                switch (msg.what){
                    case 10: // get employee
                        activity.getEmployee();
                        break;
                    case 11:
                        activity.loginError(1199);
                        break;
                    case 20: // get base setting
                        activity.getBaseSetting();
                        break;
                    case 21:
                        activity.loginError(1199);
                        break;
                    case 30:// get retailer
                        activity.getRetailer();
                        break;
                    case 31:
                        activity.loginError(1199);
                        break;
                    case 40: // color
                        activity.getColor();
                        break;
                    case 41:
                        activity.loginError(1199);
                        break;
                    case 50: // size group
                        activity.getSizeGroup();
                        break;
                    case 51:
                        activity.loginError(1199);
                    case 60:
                        activity.getAllMatchStock();
                        break;
                    case 61:
                        activity.loginError(1199);
                        break;
                    case 70:
                        activity.getBrand();
                        break;
                    case 71:
                        activity.loginError(1199);
                        break;
                    case 80:
                        activity.getType();
                        break;
                    case 81:
                        activity.loginError(1199);
                        break;
                    case 90:
                        activity.getFirm();
                        break;
                    case 91:
                        activity.loginError(1199);
                        break;
                    case 100:
                        activity.getAllMatchGood();
                        break;
                    case 101:
                        activity.loginError(1199);
                        break;
                    case 110:
                        activity.gotoMain();
                        break;
                    case 111:
                        activity.loginError(1199);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DiabloDBManager.instance().close();
    }
}