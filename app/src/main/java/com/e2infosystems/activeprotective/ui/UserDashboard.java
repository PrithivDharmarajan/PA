package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.LoginResponse;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserDashboard extends BaseActivity {

    @BindView(R.id.user_dashboard_parent_lay)
    ViewGroup mUserDashboardViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_center_txt)
    TextView mHeaderCenterTxt;

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    private boolean  mIsDoubleBackToExitAppBool = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_user_dashboard);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mUserDashboardViewGroup);

        mHeaderCenterTxt.setText(getString(R.string.dashboard));

        setHeaderAdjustmentView();
        setFooterView();
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
            mHeaderLay.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderLay.setPadding(0, getStatusBarHeight(UserDashboard.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*Set footer view*/
    private void setFooterView() {
        mFooterFirstImg.setImageResource(R.drawable.logout);
        mFooterFirstImg.setVisibility(View.VISIBLE);
    }

    /*View onClick*/
    @OnClick({R.id.footer_first_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footer_first_img:

                DialogManager.getInstance().showOptionPopup(this, getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick() {
                        LoginResponse userDetailsRes = new LoginResponse();
                        PreferenceUtil.storeUserDetails(UserDashboard.this, userDetailsRes);
                        previousScreen(GeneralWelcome.class);
                    }
                });
                break;
        }
    }

    /*App exit process*/
    private void exitFromApp() {
        if (mIsDoubleBackToExitAppBool) {
            finishAffinity();
            return;
        }

        mIsDoubleBackToExitAppBool = true;
        DialogManager.getInstance().showToast(this, getString(R.string.exit_msg));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mIsDoubleBackToExitAppBool = false;
            }
        }, 2000);


    }

    @Override
    public void onBackPressed() {
        exitFromApp();
    }
}

