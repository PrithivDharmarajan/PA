package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.input.model.AddBeltEntity;
import com.e2infosystems.activeprotective.input.model.AssignUnAssignBeltEntity;
import com.e2infosystems.activeprotective.input.model.FetchDeviceEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.BeltItemListEntityRes;
import com.e2infosystems.activeprotective.output.model.BeltListResponse;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.output.model.AdminLoginResponse;
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


public class BeltDetails extends BaseActivity {

    @BindView(R.id.belt_details_parent_lay)
    ViewGroup mBeltDetailsViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_end_img_lay)
    RelativeLayout mHeaderEndImgLay;

    @BindView(R.id.header_end_img)
    ImageView mHeaderEndImg;

    @BindView(R.id.serial_number_edt)
    EditText mSerialNumberEdt;

    @BindView(R.id.mac_address_edt)
    EditText mMacAddressEdt;

    @BindView(R.id.ssid_edt)
    EditText mSSIDEdt;

    @BindView(R.id.password_edt)
    EditText mPasswordEdt;

    @BindView(R.id.size_edt)
    EditText mSizeEdt;

    @BindView(R.id.model_edt)
    EditText mModelEdt;

    @BindView(R.id.assigned_user_lay)
    LinearLayout mAssignedUserLay;

    @BindView(R.id.assigned_user_edt)
    EditText mAssignedUserEdt;

    @BindView(R.id.belt_status_btn)
    Button mBeltStatusBtn;

    @BindView(R.id.configure_status_btn)
    Button mConfigureStatusBtn;

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_third_img)
    ImageView mFooterThirdImg;

    @BindView(R.id.footer_fourth_img)
    ImageView mFooterFourthImg;

    private BeltItemListEntityRes mBeltDetailsEntity = new BeltItemListEntityRes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_belt_details);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mBeltDetailsViewGroup);

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

        mHeaderTxt.setText(getString(R.string.belt_details));
        mHeaderEndImgLay.setVisibility(View.VISIBLE);
        mHeaderEndImg.setImageResource(R.drawable.dashboard);
        mHeaderEndImg.setColorFilter(getResources().getColor(R.color.white));

        mSerialNumberEdt.setSelected(false);
        mSerialNumberEdt.setFocusableInTouchMode(false);

        mMacAddressEdt.setSelected(false);
        mMacAddressEdt.setFocusableInTouchMode(false);

        mSSIDEdt.setSelected(false);
        mSSIDEdt.setFocusableInTouchMode(false);

        mPasswordEdt.setSelected(false);
        mPasswordEdt.setFocusableInTouchMode(false);

        mSizeEdt.setSelected(false);
        mSizeEdt.setFocusableInTouchMode(false);

        mModelEdt.setSelected(false);
        mModelEdt.setFocusableInTouchMode(false);

        mAssignedUserEdt.setSelected(false);
        mAssignedUserEdt.setFocusableInTouchMode(false);

        mBeltStatusBtn.setVisibility(AppConstants.IS_FROM_BELT_LIST_BOOL ? View.GONE : View.VISIBLE);


        setHeaderAdjustmentView();
        fetchDeviceDetailsAPICall();
        setFooterView();
    }

    /*Fetch all details API call*/
    private void fetchDeviceDetailsAPICall() {
        ArrayList<FetchDeviceEntity> fetchDeviceArrEntityList = new ArrayList<>();
        FetchDeviceEntity fetchDeviceEntity = new FetchDeviceEntity();
        fetchDeviceEntity.setDeviceId(AppConstants.BELT_DEVICE_ID);
        fetchDeviceArrEntityList.add(fetchDeviceEntity);

        APIRequestHandler.getInstance().fetchDeviceAPICall(fetchDeviceArrEntityList, this);
    }

    /*UnAssign API call*/
    private void UnAssignAPICall() {
        AssignUnAssignBeltEntity unAssignBeltEntity = new AssignUnAssignBeltEntity();
        unAssignBeltEntity.setCommunityId(mBeltDetailsEntity.getCommunityId());
        unAssignBeltEntity.setDeviceId(mBeltDetailsEntity.getDeviceId());
        unAssignBeltEntity.setUserId(mBeltDetailsEntity.getUserId());
        unAssignBeltEntity.setUserName(mBeltDetailsEntity.getUserName());

        APIRequestHandler.getInstance().unAssignBeltAPICall(unAssignBeltEntity, this);
    }

    private void setData(BeltItemListEntityRes beltDetailsEntity) {

        AppConstants.BELT_DETAILS=beltDetailsEntity;

        mSerialNumberEdt.setText(beltDetailsEntity.getDeviceId());
//        mSerialNumberEdt.setSelected(beltDetailsEntity.getDeviceId().isEmpty());
//        mSerialNumberEdt.setFocusableInTouchMode(beltDetailsEntity.getDeviceId().isEmpty());

        mMacAddressEdt.setText(beltDetailsEntity.getDevMAC());
//        mMacAddressEdt.setSelected(beltDetailsEntity.getDevMAC().isEmpty());
//        mMacAddressEdt.setFocusableInTouchMode(beltDetailsEntity.getDevMAC().isEmpty());

        mSSIDEdt.setText(beltDetailsEntity.getDevSSID());
//        mSSIDEdt.setSelected(beltDetailsEntity.getDevSSID().isEmpty());
//        mSSIDEdt.setFocusableInTouchMode(beltDetailsEntity.getDevSSID().isEmpty());

        mPasswordEdt.setText(beltDetailsEntity.getDevPasswd());
//        mPasswordEdt.setSelected(beltDetailsEntity.getDevPasswd().isEmpty());
//        mPasswordEdt.setFocusableInTouchMode(beltDetailsEntity.getDevPasswd().isEmpty());

        mSizeEdt.setText(beltDetailsEntity.getDevSize());
//        mSizeEdt.setSelected(beltDetailsEntity.getDevSize().isEmpty());
//        mSizeEdt.setFocusableInTouchMode(beltDetailsEntity.getDevSize().isEmpty());

        mModelEdt.setText(beltDetailsEntity.getDevModal());
//        mModelEdt.setSelected(beltDetailsEntity.getDevModal().isEmpty());
//        mModelEdt.setFocusableInTouchMode(beltDetailsEntity.getDevModal().isEmpty());

        mAssignedUserEdt.setText(beltDetailsEntity.getUserName());
//        mAssignedUserEdt.setSelected(beltDetailsEntity.getUserName().isEmpty());
//        mAssignedUserEdt.setFocusableInTouchMode(beltDetailsEntity.getUserName().isEmpty());

        if (AppConstants.IS_FROM_BELT_LIST_BOOL) {
            mAssignedUserLay.setVisibility(beltDetailsEntity.getAssignStatus() == AppConstants.SUCCESS_CODE ? View.VISIBLE : View.GONE);
            mBeltStatusBtn.setText(getString(beltDetailsEntity.getAssignStatus() == AppConstants.SUCCESS_CODE ? R.string.un_assign : R.string.assign));
            mConfigureStatusBtn.setText(getString(beltDetailsEntity.getWiFiConfiguredStatus() == 1 ? R.string.reconfigure : R.string.configure));
            mBeltStatusBtn.setVisibility(View.VISIBLE);
        }

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
                            mHeaderLay.setPadding(0, getStatusBarHeight(BeltDetails.this), 0, 0);
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

        mFooterSecondImg.setImageResource(R.drawable.belt_settings);
        mFooterSecondImg.setVisibility(View.VISIBLE);

        mFooterThirdImg.setImageResource(R.drawable.alert);
        mFooterThirdImg.setVisibility(View.VISIBLE);

        mFooterFourthImg.setImageResource(R.drawable.logout);
        mFooterFourthImg.setVisibility(View.VISIBLE);
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay, R.id.header_end_img_lay, R.id.belt_status_btn, R.id.configure_status_btn,
            R.id.footer_first_img, R.id.footer_second_img, R.id.footer_third_img, R.id.footer_fourth_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                onBackPressed();
                break;
            case R.id.header_end_img_lay:
                nextScreen(AdminDashboard.class);
                break;
            case R.id.belt_status_btn:
                String beltStatusStr = mBeltStatusBtn.getText().toString().trim();
                if (beltStatusStr.equalsIgnoreCase(getString(R.string.assign))) {
                    nextScreen(AllUserList.class);
                } else if (beltStatusStr.equalsIgnoreCase(getString(R.string.un_assign))) {
                    DialogManager.getInstance().showOptionPopup(this, String.format(getString(R.string.un_assign_msg), mSerialNumberEdt.getText().toString().trim(), mBeltDetailsEntity.getUserName()), getString(R.string.un_assign), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onNegativeClick() {

                        }

                        @Override
                        public void onPositiveClick() {
                            UnAssignAPICall();
                        }
                    });
                } else {
                    previousScreen(BeltList.class);
                }
                break;
            case R.id.configure_status_btn:
                validateFields();
                break;
            case R.id.footer_first_img:
                AppConstants.TEMP_SCREEN = getString(R.string.settings);
                nextScreen(Temp.class);
                break;
            case R.id.footer_second_img:
                AppConstants.IS_FROM_BELT_SETTINGS_BOOL = true;
                nextScreen(WebURL.class);
                break;
            case R.id.footer_third_img:
                AppConstants.IS_FROM_BELT_SETTINGS_BOOL = false;
                nextScreen(WebURL.class);
                break;
            case R.id.footer_fourth_img:
                DialogManager.getInstance().showOptionPopup(this, getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick() {
                        AdminLoginResponse userDetailsRes = new AdminLoginResponse();
                        PreferenceUtil.storeAdminDetails(BeltDetails.this, userDetailsRes);
                        previousScreen(AdminLogin.class);
                    }
                });
                break;
        }
    }


    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);

        String serialNumberStr = mSerialNumberEdt.getText().toString().trim();
        String macAddressStr = mMacAddressEdt.getText().toString().trim();
        String ssidStr = mSSIDEdt.getText().toString().trim();
        String passwordStr = mPasswordEdt.getText().toString().trim();
        String sizeStr = mSizeEdt.getText().toString().trim();
        String modelStr = mModelEdt.getText().toString().trim();

        if (serialNumberStr.isEmpty()) {
            mSerialNumberEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_serial_number), this);
        } else if (macAddressStr.isEmpty()) {
            mMacAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_mac_address), this);
        } else if (ssidStr.isEmpty()) {
            mSSIDEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_ssid), this);
        } else if (passwordStr.isEmpty()) {
            mPasswordEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_password), this);
        } else if (sizeStr.isEmpty()) {
            mSizeEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_size), this);
        } else if (modelStr.isEmpty()) {
            mModelEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.plz_enter_model), this);
        } else {

            sysOut("setDeviceId---"+serialNumberStr);

            nextScreen(Instructions.class);
        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof BeltListResponse) {
            BeltListResponse beltListResponse = (BeltListResponse) resObj;
            mBeltDetailsEntity = new BeltItemListEntityRes();
            if (beltListResponse.getData().getItems().size() > 0) {
                mBeltDetailsEntity = beltListResponse.getData().getItems().get(0);
            }
            setData(mBeltDetailsEntity);
        } else if (resObj instanceof CommonResponse) {
            final CommonResponse beltUnAssignResponse = (CommonResponse) resObj;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBeltStatusBtn.setText(getString(R.string.assign));
                    mAssignedUserLay.setVisibility(View.GONE);

                    DialogManager.getInstance().showInfoPopup(BeltDetails.this,beltUnAssignResponse.getMessage());

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

