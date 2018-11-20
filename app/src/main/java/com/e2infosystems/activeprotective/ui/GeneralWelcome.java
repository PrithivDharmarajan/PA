package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.BuildConfig;
import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GeneralWelcome extends BaseActivity {

    @BindView(R.id.welcome_parent_lay)
    ViewGroup mWelcomeViewGroup;

    @BindView(R.id.active_protective_lay)
    LinearLayout mActiveProtectiveLay;

    @BindView(R.id.version_txt)
    TextView mVersionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_generel_welcome);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mWelcomeViewGroup);

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
                            mActiveProtectiveLay.setPadding(0, getStatusBarHeight(GeneralWelcome.this), 0, 0);
                        }
                    });
                }
            });
        }

    }

    /*View onClick*/
    @OnClick({R.id.scan_to_pair_btn, R.id.admin_login_btn, R.id.sign_up_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_to_pair_btn:
                PreferenceUtil.storeBoolPreferenceValue(this,AppConstants.CURRENT_USER_ADMIN,false);
                nextScreen(UserQRBarcodeScanner.class);
                break;
            case R.id.admin_login_btn:
                PreferenceUtil.storeBoolPreferenceValue(this,AppConstants.CURRENT_USER_ADMIN,true);
                nextScreen(AdminLogin.class);
                break;
            case R.id.sign_up_txt:
                break;
        }
    }


}

