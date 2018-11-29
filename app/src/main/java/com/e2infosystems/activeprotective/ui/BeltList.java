package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.adapter.BeltListAdapter;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.BeltItemListEntityRes;
import com.e2infosystems.activeprotective.output.model.BeltListResponse;
import com.e2infosystems.activeprotective.output.model.DeleteDeviceResponse;
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


public class BeltList extends BaseActivity {

    @BindView(R.id.belt_list_parent_lay)
    ViewGroup mBeltListViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_start_txt)
    TextView mHeaderStartTxt;

    @BindView(R.id.header_center_txt)
    TextView mHeaderCenterTxt;

//    @BindView(R.id.clock_txt)
//    TextClock mClockTxt;

    @BindView(R.id.header_end_txt)
    TextView mHeaderEndTxt;

    @BindView(R.id.no_belt_lay)
    ScrollView mNoBeltLay;

    @BindView(R.id.belt_lay)
    LinearLayout mBeltLay;

    @BindView(R.id.belt_recycler_view)
    RecyclerView mBeltRecyclerView;

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_third_img)
    ImageView mFooterThirdImg;

    private ArrayList<BeltItemListEntityRes> mBeltItemArrListRes = new ArrayList<>();
    private boolean mIsEditListBool = false, mIsDoubleBackToExitAppBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_belt_list);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mBeltListViewGroup);

        mHeaderStartTxt.setText(getString(R.string.edit));
        mHeaderCenterTxt.setText(getString( R.string.belt_list));
        mHeaderEndTxt.setText(getString(R.string.edit));
//        mClockTxt.setFormat24Hour("hh:mm a");
//        mClockTxt.setFormat12Hour("hh:mm a");
        setHeaderAdjustmentView();
        setFooterView();
        beltListAPI();
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
                            mHeaderLay.setPadding(0, getStatusBarHeight(BeltList.this), 0, 0);
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

        mFooterSecondImg.setImageResource(R.drawable.qr);
        mFooterSecondImg.setVisibility(View.INVISIBLE);

        mFooterThirdImg.setImageResource(R.drawable.logout);
        mFooterThirdImg.setVisibility(View.VISIBLE);
    }

    private void beltListAPI() {
        APIRequestHandler.getInstance().beltListAPICall(this);
    }

    /*View onClick*/
    @OnClick({R.id.header_end_txt, R.id.scan_qr_code_btn, R.id.footer_first_img,
            R.id.footer_second_img, R.id.footer_third_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_end_txt:
                if (mBeltItemArrListRes.size() > 0) {
                    mIsEditListBool = !mIsEditListBool;
                    mHeaderEndTxt.setText(getString(mIsEditListBool ? R.string.done : R.string.edit));
                    mHeaderStartTxt.setText(mHeaderEndTxt.getText().toString().trim());
                    setBeltListAdapter(mBeltItemArrListRes);
                }
                break;
            case R.id.footer_first_img:
                AppConstants.TEMP_SCREEN = getString(R.string.settings);
                nextScreen(Temp.class);
                break;
            case R.id.scan_qr_code_btn:
            case R.id.footer_second_img:
                nextScreen(AdminQRBarCodeScanner.class);
                break;
            case R.id.footer_third_img:
                DialogManager.getInstance().showOptionPopup(this, getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick() {
                        AdminLoginResponse userDetailsRes = new AdminLoginResponse();
                        PreferenceUtil.storeAdminDetails(BeltList.this, userDetailsRes);
                        previousScreen(AdminLogin.class);
                    }
                });
                break;

        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof BeltListResponse) {
            BeltListResponse beltListResponse = (BeltListResponse) resObj;
            mBeltItemArrListRes = beltListResponse.getData().getItems();
            setBeltListAdapter(mBeltItemArrListRes);
        } else if (resObj instanceof DeleteDeviceResponse) {
            DeleteDeviceResponse deleteDeviceResponse = (DeleteDeviceResponse) resObj;
            DialogManager.getInstance().showAlertPopup(this, deleteDeviceResponse.getMessage(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    beltListAPI();
                }
            });
        }


    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            if(resObj instanceof BeltListResponse){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoBeltLay.setVisibility (View.VISIBLE);
                        mBeltLay.setVisibility( View.GONE);
                    }
                });
            }

            DialogManager.getInstance().showAlertPopup(this,
                    (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                        }
                    });
        }
    }

    private void setBeltListAdapter(final ArrayList<BeltItemListEntityRes> beltItemArrListRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFooterSecondImg.setVisibility(beltItemArrListRes.size() > 0 ? View.VISIBLE : View.INVISIBLE);
                mHeaderEndTxt.setVisibility(beltItemArrListRes.size() > 0 ? View.VISIBLE : View.INVISIBLE);

                mNoBeltLay.setVisibility(beltItemArrListRes.size() > 0 ? View.GONE : View.VISIBLE);
                mBeltLay.setVisibility(beltItemArrListRes.size() > 0 ? View.VISIBLE : View.GONE);

                mBeltRecyclerView.setLayoutManager(new LinearLayoutManager(BeltList.this));
                mBeltRecyclerView.setAdapter(new BeltListAdapter(beltItemArrListRes, mIsEditListBool, BeltList.this, BeltList.this));
            }
        });
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

