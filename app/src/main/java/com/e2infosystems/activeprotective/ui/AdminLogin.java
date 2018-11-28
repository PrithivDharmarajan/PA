package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.BuildConfig;
import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.input.model.LoginEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.AdminLoginResponse;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AdminLogin extends BaseActivity {

    @BindView(R.id.admin_login_parent_lay)
    ViewGroup mAdminLoginViewGroup;

    @BindView(R.id.active_protective_lay)
    LinearLayout mActiveProtectiveLay;

    @BindView(R.id.email_user_id_edt)
    EditText mEmailUserIdEdt;

    @BindView(R.id.password_edt)
    EditText mPasswordEdt;

    @BindView(R.id.version_txt)
    TextView mVersionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_admin_login);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAdminLoginViewGroup);

        /*Keypad button action*/
        mPasswordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        mVersionTxt.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
        setHeaderAdjustmentView();
    }

    /*Screen orientation changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderAdjustmentView();
    }

    /*Set header view*/
    private void setHeaderAdjustmentView() {
        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActiveProtectiveLay.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActiveProtectiveLay.setPadding(0, getStatusBarHeight(AdminLogin.this), 0, 0);
                        }
                    });
                }
            });
        }


    }


    /*View onClick*/
    @OnClick({R.id.submit_btn, R.id.user_login_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                validateFields();
                break;
            case R.id.user_login_txt:
                onBackPressed();
                break;

        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String emailUserIdStr = mEmailUserIdEdt.getText().toString().trim();
        String passwordStr = mPasswordEdt.getText().toString().trim();

        if (emailUserIdStr.isEmpty()) {
            mEmailUserIdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_email_user_id), this);
        } else if (passwordStr.isEmpty()) {
            mPasswordEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_password), this);
        } else {
            LoginEntity loginEntity=new LoginEntity();
            loginEntity.setUsername(emailUserIdStr);
            loginEntity.setPassword(passwordStr);
            APIRequestHandler.getInstance().loginAPICall(loginEntity, this);
        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof AdminLoginResponse) {
            final AdminLoginResponse loginResponse = (AdminLoginResponse) resObj;

            DialogManager.getInstance().showInfoPopup(this,String.format(getString(R.string.welcome_user),loginResponse.getUser()));

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    PreferenceUtil.storeAdminDetails(AdminLogin.this, loginResponse);
                    nextScreen(BeltList.class);
                }
            }, 1000);

        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showAlertPopup(this,
                    (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                        }
                    });


        }
    }


    @Override
    public void onBackPressed() {
        previousScreen(GeneralWelcome.class);
    }
}

