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
import com.e2infosystems.activeprotective.input.model.AddUserEntity;
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


public class AddUser extends BaseActivity {

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

    @BindView(R.id.title_edt)
    EditText mTitleNumberEdt;

    @BindView(R.id.first_name_edt)
    EditText mFirstNameEdt;

    @BindView(R.id.middle_name_edt)
    EditText mMiddleNameEdt;

    @BindView(R.id.last_name_edt)
    EditText mLastNameEdt;

    @BindView(R.id.address_line_one_edt)
    EditText mAddressLineOneEdt;

    @BindView(R.id.address_line_two_edt)
    EditText mAddressLineTwoEdt;

    @BindView(R.id.zip_code_edt)
    EditText mZipCodeEdt;

    @BindView(R.id.country_code_edt)
    EditText mCountryCodeEdt;

    @BindView(R.id.primary_mobile_number_edt)
    EditText mPrimaryMobileNumberEdt;

    @BindView(R.id.secondary_mobile_number_edt)
    EditText mSecondaryMobileNumberEdt;

    @BindView(R.id.primary_email_edt)
    EditText mPrimaryEmailEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_user);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddBeltViewGroup);

        /*Keypad button action*/
        mPrimaryEmailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        mHeaderStartTxt.setText(getString(R.string.cancel));
        mHeaderStartTxt.setVisibility(View.VISIBLE);

        mHeaderCenterTxt.setText(getString(R.string.add_user));

        mHeaderEndTxt.setText(getString(R.string.save));
        mHeaderEndTxt.setVisibility(View.VISIBLE);

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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AddUser.this), 0, 0);
                        }
                    });
                }
            });
        }


    }


    /*View onClick*/
    @OnClick({R.id.header_start_txt, R.id.header_end_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_txt:
                onBackPressed();
                break;
            case R.id.header_end_txt:
                validateFields();
                break;

        }
    }


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);

        String titleNumberStr = mTitleNumberEdt.getText().toString().trim();
        String firstNameStr = mFirstNameEdt.getText().toString().trim();
        String middleNameStr = mMiddleNameEdt.getText().toString().trim();
        String lastNameStr = mLastNameEdt.getText().toString().trim();
        String addressLineOneStr = mAddressLineOneEdt.getText().toString().trim();
        String addressLineTwoStr = mAddressLineTwoEdt.getText().toString().trim();
        String zipCodeStr = mZipCodeEdt.getText().toString().trim();
        String countryCodeStr = mCountryCodeEdt.getText().toString().trim();
        String primaryMobileNumberStr = mPrimaryMobileNumberEdt.getText().toString().trim();
        String secondaryMobileNumberStr = mSecondaryMobileNumberEdt.getText().toString().trim();
        String primaryEmailStr = mPrimaryEmailEdt.getText().toString().trim();


        if (titleNumberStr.isEmpty()) {
            mTitleNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_title), this);
        } else if (firstNameStr.isEmpty()) {
            mFirstNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_first_name), this);
        } else if (addressLineOneStr.isEmpty()&&addressLineTwoStr.isEmpty()) {
            mAddressLineOneEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_address), this);
        } else if (zipCodeStr.isEmpty()) {
            mZipCodeEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_zip), this);
        } else if (countryCodeStr.isEmpty()) {
            mCountryCodeEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_country_code), this);
        }  else if (primaryMobileNumberStr.isEmpty()) {
            mPrimaryMobileNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_primary_mobile_number), this);
        }  else if (primaryEmailStr.isEmpty()) {
            mPrimaryEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_primary_email), this);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(primaryEmailStr).matches()) {
            mPrimaryEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter__valid_primary_email), this);
        } else {

            LoginResponse userDetailsEntityRes = PreferenceUtil.getUserDetails(this);

            AddUserEntity addUserEntity = new AddUserEntity();

            addUserEntity.setCommunityId(userDetailsEntityRes.getCommunityId());
            addUserEntity.setCommunityName(userDetailsEntityRes.getCommunityName());
            addUserEntity.setAccountId(userDetailsEntityRes.getAccountId());
            addUserEntity.setTitle(titleNumberStr);
            addUserEntity.setFirstName(firstNameStr);
            addUserEntity.setMiddleName(middleNameStr);
            addUserEntity.setLastName(lastNameStr);
            addUserEntity.setAddress1(addressLineOneStr);
            addUserEntity.setAddress2(addressLineTwoStr);
            addUserEntity.setZipCode(zipCodeStr);
            addUserEntity.setCountryCode(countryCodeStr);
            addUserEntity.setPrimNumber(primaryMobileNumberStr);
            addUserEntity.setSecNumber(secondaryMobileNumberStr);
            addUserEntity.setPrimEmail(primaryEmailStr);

            APIRequestHandler.getInstance().addUserAPICall(addUserEntity,this);
        }
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            final CommonResponse addUserResponse = (CommonResponse) resObj;
            DialogManager.getInstance().showAlertPopup(this, addUserResponse.getMessage(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    backScreen();
                }
            });
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

