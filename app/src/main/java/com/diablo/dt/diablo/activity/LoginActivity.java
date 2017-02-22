package com.diablo.dt.diablo.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.diablo.dt.diablo.Client.WLoginClient;
import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.entity.DiabloEnum;
import com.diablo.dt.diablo.entity.MainProfile;
import com.diablo.dt.diablo.response.LoginResponse;
import com.diablo.dt.diablo.rest.WLoginInterfacae;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout loginWrap, passwordWrap;
    // private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        // coordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinatorLayout);

        // init profile
        MainProfile.getInstance().setContext(this);

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
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d("%s", "LOGIN login error");
                        }
                    });
                }
            }
        });
    }
}