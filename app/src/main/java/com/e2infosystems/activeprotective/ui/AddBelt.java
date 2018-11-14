package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.input.model.AddBeltEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.output.model.LoginResponse;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceBtnCallback;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddBelt extends BaseActivity {

    @BindView(R.id.add_belt_parent_lay)
    ViewGroup mAddBeltViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.serial_number_edt)
    EditText mSerialNumberEdt;

    @BindView(R.id.mac_address_edt)
    EditText mMacAddressEdt;

    @BindView(R.id.ssid_edt)
    EditText mSSIDEdt;

    @BindView(R.id.password_edt)
    EditText mPasswordEdt;

    @BindView(R.id.xs_img)
    ImageView mXsImg;

    @BindView(R.id.xs_txt)
    TextView mXsTxt;

    @BindView(R.id.s_img)
    ImageView mSImg;

    @BindView(R.id.s_txt)
    TextView mSTxt;

    @BindView(R.id.m_img)
    ImageView mMImg;

    @BindView(R.id.m_txt)
    TextView mMTxt;

    @BindView(R.id.l_img)
    ImageView mLImg;

    @BindView(R.id.l_txt)
    TextView mLTxt;

    @BindView(R.id.xl_img)
    ImageView mXlImg;

    @BindView(R.id.xl_txt)
    TextView mXlTxt;

    @BindView(R.id.model_edt)
    EditText mModelEdt;

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    private String mBeltSizeStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_belt);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddBeltViewGroup);

        /*Keypad button action*/
        mModelEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        mHeaderTxt.setText(getString(R.string.add_belt));
        setHeaderAdjustmentView();
        setData();
        setFooterView();
    }

    /*Screen orientation changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderAdjustmentView();
    }


    private void setData() {

        AddBeltEntity beltDetailsEntity = AppConstants.BELT_DETAILS;
        mSerialNumberEdt.setText(beltDetailsEntity.getDeviceId());
        mSerialNumberEdt.setSelection(beltDetailsEntity.getDeviceId().length());
        mMacAddressEdt.setText(beltDetailsEntity.getDevMAC());
        mSSIDEdt.setText(beltDetailsEntity.getDevSSID());
        mPasswordEdt.setText(beltDetailsEntity.getDevPasswd());
        selectBeltSize(beltDetailsEntity.getDevSize().isEmpty() ? getString(R.string.s) : beltDetailsEntity.getDevSize());
        mModelEdt.setText(beltDetailsEntity.getDevModal());

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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AddBelt.this), 0, 0);
                        }
                    });
                }
            });
        }


    }

    /*Set footer view*/
    private void setFooterView() {
        mFooterFirstImg.setImageResource(R.drawable.settings);
        mFooterFirstImg.setVisibility(View.VISIBLE);

        mFooterSecondImg.setImageResource(R.drawable.logout);
        mFooterSecondImg.setVisibility(View.VISIBLE);

    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay, R.id.xs_lay, R.id.s_lay, R.id.m_lay, R.id.l_lay, R.id.xl_lay,
            R.id.save_btn, R.id.footer_first_img, R.id.footer_second_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                onBackPressed();
                break;
            case R.id.xs_lay:
                selectBeltSize(getString(R.string.xs));
                break;
            case R.id.s_lay:
                selectBeltSize(getString(R.string.s));
                break;
            case R.id.m_lay:
                selectBeltSize(getString(R.string.m));
                break;
            case R.id.l_lay:
                selectBeltSize(getString(R.string.l));
                break;
            case R.id.xl_lay:
                selectBeltSize(getString(R.string.xl));
                break;
            case R.id.save_btn:
                validateFields();
                break;
            case R.id.footer_first_img:
                AppConstants.TEMP_SCREEN = getString(R.string.settings);
                nextScreen(Temp.class);
                break;
            case R.id.footer_second_img:
                DialogManager.getInstance().showOptionPopup(this, getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick() {
                        LoginResponse userDetailsRes = new LoginResponse();
                        PreferenceUtil.storeUserDetails(AddBelt.this, userDetailsRes);
                        previousScreen(GeneralWelcome.class);
                    }
                });
                break;
        }
    }

    /*Select belt*/
    private void selectBeltSize(String beltSizeStr) {
        mBeltSizeStr = beltSizeStr;

        mXsImg.setImageResource(R.drawable.circle_white_gray);
        mXsTxt.setTextColor(ContextCompat.getColor(this, R.color.gray));

        mSImg.setImageResource(R.drawable.circle_white_gray);
        mSTxt.setTextColor(ContextCompat.getColor(this, R.color.gray));

        mMImg.setImageResource(R.drawable.circle_white_gray);
        mMTxt.setTextColor(ContextCompat.getColor(this, R.color.gray));

        mLImg.setImageResource(R.drawable.circle_white_gray);
        mLTxt.setTextColor(ContextCompat.getColor(this, R.color.gray));

        mXlImg.setImageResource(R.drawable.circle_white_gray);
        mXlTxt.setTextColor(ContextCompat.getColor(this, R.color.gray));

        if (beltSizeStr.equalsIgnoreCase(getString(R.string.xs))) {
            mXsImg.setImageResource(R.drawable.circle_blue);
            mXsTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (beltSizeStr.equalsIgnoreCase(getString(R.string.s))) {
            mSImg.setImageResource(R.drawable.circle_blue);
            mSTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (beltSizeStr.equalsIgnoreCase(getString(R.string.m))) {
            mMImg.setImageResource(R.drawable.circle_blue);
            mMTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (beltSizeStr.equalsIgnoreCase(getString(R.string.l))) {
            mLImg.setImageResource(R.drawable.circle_blue);
            mLTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if (beltSizeStr.equalsIgnoreCase(getString(R.string.xl))) {
            mXlImg.setImageResource(R.drawable.circle_blue);
            mXlTxt.setTextColor(ContextCompat.getColor(this, R.color.white));
        }


    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);

        String serialNumberStr = mSerialNumberEdt.getText().toString().trim();
        String macAddressStr = mMacAddressEdt.getText().toString().trim();
        String ssidStr = mSSIDEdt.getText().toString().trim();
        String passwordStr = mPasswordEdt.getText().toString().trim();
        String modelStr = mModelEdt.getText().toString().trim();

        if (serialNumberStr.isEmpty()) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_serial_number), this);
        }else if (serialNumberStr.length()< 9) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.serial_contains_eight_char), this);
        } else if (macAddressStr.isEmpty()) {
            mMacAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_mac_address), this);
        } else if (ssidStr.isEmpty()) {
            mSSIDEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_ssid), this);
        } else if (passwordStr.isEmpty()) {
            mPasswordEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_password), this);
        } else if (modelStr.isEmpty()) {
            mModelEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_model), this);
        } else {

            AppConstants.BELT_DETAILS = new AddBeltEntity();
            AppConstants.BELT_DETAILS.setDeviceId(serialNumberStr);
            AppConstants.BELT_DETAILS.setDevMAC(macAddressStr);
            AppConstants.BELT_DETAILS.setDevSSID(ssidStr);
            AppConstants.BELT_DETAILS.setDevPasswd(passwordStr);
            AppConstants.BELT_DETAILS.setDevSize(mBeltSizeStr);
            AppConstants.BELT_DETAILS.setDevModal(modelStr);

            ArrayList<AddBeltEntity> addDeviceArrEntityList = new ArrayList<>();

            LoginResponse userDetailsEntityRes = PreferenceUtil.getUserDetails(this);
            AddBeltEntity addDeviceArrFirstEntity = new AddBeltEntity();
            AddBeltEntity addDeviceArrSecondEntity = new AddBeltEntity();

            addDeviceArrFirstEntity.setCommunityId(userDetailsEntityRes.getCommunityId());
            addDeviceArrFirstEntity.setCommunityName(userDetailsEntityRes.getCommunityName());
            addDeviceArrFirstEntity.setAccountId(userDetailsEntityRes.getAccountId());
            addDeviceArrFirstEntity.setDevMAC(macAddressStr);
            addDeviceArrFirstEntity.setDevModal(modelStr);
            addDeviceArrFirstEntity.setDevPasswd(passwordStr);
            addDeviceArrFirstEntity.setDevSSID(ssidStr);
            addDeviceArrFirstEntity.setDevSize(mBeltSizeStr);
            addDeviceArrFirstEntity.setDeviceId(serialNumberStr);
            addDeviceArrFirstEntity.setLedIntensity(4);
            addDeviceArrFirstEntity.setSystemAlert(1);
            addDeviceArrFirstEntity.setUnBuckleAlert(0);
            addDeviceArrFirstEntity.setBuckleAlert(0);
            addDeviceArrFirstEntity.setDeviceAlwaysOn(1);
            addDeviceArrFirstEntity.setUserAlert(1);
            addDeviceArrFirstEntity.setVibrationLevel(4);
            addDeviceArrFirstEntity.setVolumeLevel(4);
            addDeviceArrFirstEntity.setWiFiConfiguredStatus(4);
            addDeviceArrFirstEntity.setPersButton(0);

//            addDeviceArrSecondEntity.setDeviceId(serialNumberStr);
//            addDeviceArrSecondEntity.setCommunityId(userDetailsEntityRes.getCommunityId());
//            addDeviceArrSecondEntity.setCommunityName(userDetailsEntityRes.getCommunityName());
//            addDeviceArrSecondEntity.setAccountId(userDetailsEntityRes.getAccountId());
//            addDeviceArrSecondEntity.setDevMAC(macAddressStr);
//            addDeviceArrSecondEntity.setDevModal(modelStr);
//            addDeviceArrSecondEntity.setDevPasswd(passwordStr);
//            addDeviceArrSecondEntity.setDevSSID(ssidStr);
//            addDeviceArrSecondEntity.setDevSize(mBeltSizeStr);
//            addDeviceArrSecondEntity.setDeviceId(serialNumberStr);
//            addDeviceArrSecondEntity.setLedIntensity(4);
//            addDeviceArrSecondEntity.setSystemAlert(1);
//            addDeviceArrSecondEntity.setUnBuckleAlert(0);
//            addDeviceArrSecondEntity.setBuckleAlert(0);
//            addDeviceArrSecondEntity.setDeviceAlwaysOn(1);
//            addDeviceArrSecondEntity.setUserAlert(1);
//            addDeviceArrSecondEntity.setVibrationLevel(4);
//            addDeviceArrSecondEntity.setVolumeLevel(4);
//            addDeviceArrSecondEntity.setWiFiConfiguredStatus(4);
//            addDeviceArrFirstEntity.setPersButton(0);


            addDeviceArrEntityList.add(addDeviceArrFirstEntity);
            addDeviceArrEntityList.add(addDeviceArrFirstEntity);

            APIRequestHandler.getInstance().addDeviceAPICall(addDeviceArrEntityList, this);
        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            AppConstants.BELT_DEVICE_ID = AppConstants.BELT_DETAILS.getDeviceId();
            AppConstants.IS_FROM_BELT_LIST_BOOL = false;
            nextScreen(BeltDetails.class);
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

