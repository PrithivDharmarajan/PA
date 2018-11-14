package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.utils.DialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NetworkSetup extends BaseActivity {

    @BindView(R.id.network_parent_lay)
    ViewGroup mNetworkViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.ssid_edt)
    EditText mSSIDEdt;

    @BindView(R.id.password_edt)
    EditText mPasswordEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_network_setup);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mNetworkViewGroup);

        mHeaderTxt.setText(getString(R.string.connect_to_belt));
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
            mHeaderLay.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderLay.setPadding(0, getStatusBarHeight(NetworkSetup.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay,R.id.save_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                onBackPressed();
                break;
            case R.id.save_btn:
                validateFields();
                break;
        }
    }


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);

        String ssidStr = mSSIDEdt.getText().toString().trim();
        String passwordStr = mPasswordEdt.getText().toString().trim();

       if (ssidStr.isEmpty()) {
            mSSIDEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_ssid), this);
        } else if (passwordStr.isEmpty()) {
            mPasswordEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_password), this);
        }else {
           previousScreen(BeltList.class);
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}

