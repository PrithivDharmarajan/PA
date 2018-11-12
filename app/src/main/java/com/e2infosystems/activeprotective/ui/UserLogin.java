package com.e2infosystems.activeprotective.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserLogin extends BaseActivity {

    @BindView(R.id.user_login_parent_lay)
    ViewGroup mUserLoginViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_user_login);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mUserLoginViewGroup);

        setHeaderAdjustmentView();
    }

    /*Set header view*/
    private void setHeaderAdjustmentView() {
        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mUserLoginViewGroup.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mUserLoginViewGroup.setPadding(0, getStatusBarHeight(UserLogin.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.admin_login_txt, R.id.scan_to_pair_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_login_txt:
                nextScreen(AdminLogin.class);
                break;
            case R.id.scan_to_pair_btn:
                break;
        }
    }

}

