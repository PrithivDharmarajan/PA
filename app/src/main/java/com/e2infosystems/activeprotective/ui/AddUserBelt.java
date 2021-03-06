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
import com.e2infosystems.activeprotective.input.model.FetchDeviceEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.UserLoginResponse;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddUserBelt extends BaseActivity {

    @BindView(R.id.add_belt_parent_lay)
    ViewGroup mAddBeltViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_start_txt)
    TextView mHeaderStartTxt;

    @BindView(R.id.header_center_txt)
    TextView mHeaderCenterTxt;

    @BindView(R.id.header_end_txt)
    TextView mHeaderEndTxt;



    @BindView(R.id.serial_number_edt)
    EditText mSerialNumberEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_user_belt);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddBeltViewGroup);

        /*Keypad button action*/
        mSerialNumberEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        mHeaderStartTxt.setText(getString(R.string.close));
        mHeaderStartTxt.setVisibility(View.VISIBLE);
        mHeaderCenterTxt.setText(getString(R.string.add_belt));
        mHeaderEndTxt.setText(getString(R.string.close));


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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AddUserBelt.this), 0, 0);
                        }
                    });
                }
            });
        }


    }


    /*View onClick*/
    @OnClick({R.id.header_start_txt,R.id.save_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_txt:
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

        String serialNumberStr = mSerialNumberEdt.getText().toString().trim();
        if (serialNumberStr.isEmpty()) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_serial_number), this);
        } else if (serialNumberStr.length()< 8) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.serial_contains_nine_char), this);
        } else{


            FetchDeviceEntity userLoginEntity = new FetchDeviceEntity();
            userLoginEntity.setDeviceId(serialNumberStr);
            APIRequestHandler.getInstance().userWearerLoginAPICall(userLoginEntity,this);


        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof UserLoginResponse) {
            UserLoginResponse userLoginResponse = (UserLoginResponse) resObj;
            if(userLoginResponse.getData().getItems().size()>0){
                PreferenceUtil.storeUserDetails(this,userLoginResponse.getData().getItems().get(0));
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
                nextScreen(UserDashboard.class);
            }else if(!userLoginResponse.getMessage().isEmpty()){

                DialogManager.getInstance().showInfoPopup(this,userLoginResponse.getMessage());
            }
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
        backScreen();
    }
}

