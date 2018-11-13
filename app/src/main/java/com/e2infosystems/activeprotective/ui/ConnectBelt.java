package com.e2infosystems.activeprotective.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectBelt extends BaseActivity {

    @BindView(R.id.connect_belt_parent_lay)
    ViewGroup mConnectBeltViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    private BroadcastReceiver mBroadcastReceiver;
    private boolean mIsCorrectWifiBool = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_connect_belt);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mConnectBeltViewGroup);

        mHeaderTxt.setText(getString(R.string.connect_to_belt));

        setHeaderAdjustmentView();


        if (askPermissions()) {
            connectToAP(AppConstants.BELT_DETAILS.getDevSSID(), AppConstants.BELT_DETAILS.getDevPasswd());
//            connectToAP("JioFi2_D3DABA_NAKUL", "Vino@5577");
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
                            mHeaderLay.setPadding(0, getStatusBarHeight(ConnectBelt.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay, R.id.cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
            case R.id.cancel_btn:
                onBackPressed();
                break;
        }
    }

    public void connectToAP(String SSIDStr, String passwordStr) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = Objects.requireNonNull(wifiManager).isWifiEnabled();
        if (!wifiEnabled) {
            wifiManager.setWifiEnabled(true);
        }
        wifiConfiguration.SSID = String.format("\"%s\"", SSIDStr);
        wifiConfiguration.preSharedKey = String.format("\"%s\"", passwordStr);

        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.disconnect();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String connectedWifiSSIDStr = wifiManager.getConnectionInfo().getSSID().replaceAll("\"","");
                if (!mIsCorrectWifiBool && (AppConstants.BELT_DETAILS.getDevSSID().matches(connectedWifiSSIDStr))) {
                    mIsCorrectWifiBool = true;
                    nextScreen(NetworkSetup.class);
                } else if (!mIsCorrectWifiBool) {
                    DialogManager.getInstance().showToast(ConnectBelt.this, "Failed to connect the Wifi");
                }


            }
        };

        wifiManager.enableNetwork(netId, true);
        registerReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        wifiManager.reconnect();

    }


    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int accessCorePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (accessFinePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (accessCorePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    connectToAP(AppConstants.BELT_DETAILS.getDevSSID(), AppConstants.BELT_DETAILS.getDevPasswd());
                }

                public void onNegativeClick() {
                    backScreen();
                }
            });
        }

        return addPermission;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null)
            unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}

