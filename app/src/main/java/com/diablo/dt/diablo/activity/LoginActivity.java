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
import com.diablo.dt.diablo.Client.BaseSettingClient;
import com.diablo.dt.diablo.Client.EmployeeClient;
import com.diablo.dt.diablo.Client.RetailerClient;
import com.diablo.dt.diablo.Client.RightClient;
import com.diablo.dt.diablo.Client.StockClient;
import com.diablo.dt.diablo.Client.WLoginClient;
import com.diablo.dt.diablo.Client.WgoodClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.BaseSetting;
import com.diablo.dt.diablo.entity.DiabloColor;
import com.diablo.dt.diablo.entity.DiabloSizeGroup;
import com.diablo.dt.diablo.entity.Employee;
import com.diablo.dt.diablo.entity.MatchStock;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.entity.Retailer;
import com.diablo.dt.diablo.request.MatchStockRequest;
import com.diablo.dt.diablo.response.LoginResponse;
import com.diablo.dt.diablo.response.LoginUserInfoResponse;
import com.diablo.dt.diablo.rest.BaseSettingInterface;
import com.diablo.dt.diablo.rest.EmployeeInterface;
import com.diablo.dt.diablo.rest.RetailerInterface;
import com.diablo.dt.diablo.rest.RightInterface;
import com.diablo.dt.diablo.rest.StockInterface;
import com.diablo.dt.diablo.rest.WGoodInterface;
import com.diablo.dt.diablo.rest.WLoginInterfacae;
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

    Button btnLogin;
    TextInputLayout loginWrap, passwordWrap;
    Context mContext;

    private final LoginHandler mLoginHandler = new LoginHandler(this);
    // private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        mContext = this;
        Profile.instance().setContext(this.getApplicationContext());

        loginWrap = (TextInputLayout) findViewById(R.id.login_name_holder);
        passwordWrap = (TextInputLayout) findViewById(R.id.login_password_holder);

        // InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginWrap.getEditText().getText().toString();
                String password = passwordWrap.getEditText().getText().toString();
                if (name.trim().equals(""))
                    loginWrap.getEditText().setError("请输入用户名");
                else if (password.trim().equals(""))
                    passwordWrap.getEditText().setError("请输入用户密码");
                 else {
                    // login
                    WLoginInterfacae loginInterfacae = WLoginClient.getClient().create(WLoginInterfacae.class);
                    Call<LoginResponse> call = loginInterfacae.login(name, password, DiabloEnum.TABLET);

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Log.d(LOG_TAG, "success to login");
                            // AlertDialogLayout dialog = (AlertDialogLayout)findViewById(R.id.alert_dialog);
                            if (0 != response.body().getCode()){
                                String error = DiabloError.getInstance().getError(response.body().getCode());
                                new MaterialDialog.Builder(mContext)
                                        .title(R.string.user_login)
                                        .content(error)
                                        // .contentColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                                        .positiveText(mContext.getResources().getString(R.string.login_ok))
                                        .positiveColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                                        .show();
                            } else {
                                Profile.instance().setToken(response.body().getToken());
                                // get profile from server, include login data, authen data, etc...
                                getLoginUserInfo();
                                // getEmployee();
                                // getRetailer();
                                // getBaseSetting();

//                                Intent intent = new Intent(mContext, MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            loginError(1199);
                        }
                    });
                }
            }
        });
    }

    public void gotoMain(){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void loginError(Integer ecode){
        String error = DiabloError.getInstance().getError(ecode);
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

    private void getBrand(){

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


    private static class LoginHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        private LoginHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
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
                        activity.gotoMain();
                        break;
                    case 71:
                        activity.loginError(1199);
                        break;

                    default:
                        break;
                }
            }
        }
    }
}