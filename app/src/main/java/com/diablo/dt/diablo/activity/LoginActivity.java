package com.diablo.dt.diablo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.diablo.dt.diablo.Client.RightClient;
import com.diablo.dt.diablo.Client.WLoginClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloEnum;
import com.diablo.dt.diablo.entity.MainProfile;
import com.diablo.dt.diablo.response.LoginResponse;
import com.diablo.dt.diablo.response.LoginUserInfoResponse;
import com.diablo.dt.diablo.rest.RightInterface;
import com.diablo.dt.diablo.rest.WLoginInterfacae;
import com.diablo.dt.diablo.utils.DiabloError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout loginWrap, passwordWrap;
    Context mContext;
    // private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        mContext = this;
        MainProfile.getInstance().setContext(mContext);

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
                            Log.d("%s", "LOGIN login success");
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
                                MainProfile.getInstance().setToken(response.body().getToken());
                                // get profile from server, include login data, authen data, etc...
                                RightInterface rightInterface = RightClient.getClient().create(RightInterface.class);
                                Call<LoginUserInfoResponse> rightCall = rightInterface.getLoginUserInfo(
                                        MainProfile.getInstance().getToken());

                                rightCall.enqueue(new Callback<LoginUserInfoResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginUserInfoResponse> call, Response<LoginUserInfoResponse> response) {
                                        Log.d("LOGIN: User profile %s", response.toString());

                                    }

                                    @Override
                                    public void onFailure(Call<LoginUserInfoResponse> call, Throwable t) {

                                    }
                                });

                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            String error = DiabloError.getInstance().getError(1199);
                            new MaterialDialog.Builder(mContext)
                                    .title(R.string.user_login)
                                    .content(error)
                                    // .contentColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                                    .positiveText(mContext.getResources().getString(R.string.login_ok))
                                    .positiveColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                                    .show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}