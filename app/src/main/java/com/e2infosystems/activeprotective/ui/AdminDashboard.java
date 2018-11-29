package com.e2infosystems.activeprotective.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.AdminLoginResponse;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.net.http.SslError.SSL_UNTRUSTED;


public class AdminDashboard extends BaseActivity {

    @BindView(R.id.admin_dashboard_parent_lay)
    ViewGroup mAdminDashboardViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.web_view)
    WebView mWebView;

    private String mWebURLStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_admin_dashboard);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAdminDashboardViewGroup);

        mHeaderTxt.setText(getString(R.string.dashboard));

        setHeaderAdjustmentView();

        mWebURLStr=AppConstants.DASHBOARD_URL;
        if(askPermissions()){
            webURLLoad();
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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AdminDashboard.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                onBackPressed();
                break;
        }
    }
    private void webURLLoad(){
        DialogManager.getInstance().showProgress(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(mWebURLStr);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWebView.setVisibility(View.VISIBLE);
            DialogManager.getInstance().hideProgress();

            String firstKeyNameStr = "accountId";
            String firstKeyValStr = AppConstants.BELT_DETAILS.getAccountId();

            String secondKeyNameStr = "communityId";
            String secondKeyValStr = AppConstants.BELT_DETAILS.getCommunityId();

            String thirdKeyNameStr = "communityName";
            String thirdKeyValStr = AppConstants.BELT_DETAILS.getCommunityName();

            String fourthKeyNameStr = "userId";
            String fourthKeyValStr = AppConstants.BELT_DETAILS.getUserId();

            String fifthKeyNameStr = "FullName";
            String fifthKeyValStr =AppConstants.BELT_DETAILS.getUserName();

            String sixthKeyNameStr = "deviceId";
            String sixthKeyValStr = AppConstants.BELT_DETAILS.getDeviceId();



            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript("localStorage.setItem('" + firstKeyNameStr + "','" + firstKeyValStr + "');" +
                        "localStorage.setItem('" + secondKeyNameStr + "','" + secondKeyValStr + "');" +
                        "localStorage.setItem('" + thirdKeyNameStr + "','" + thirdKeyValStr + "');" +
                        "localStorage.setItem('" + fourthKeyNameStr + "','" + fourthKeyValStr + "');" +
                        "localStorage.setItem('" + fifthKeyNameStr + "','" + fifthKeyValStr + "');" +
                        "localStorage.setItem('" + sixthKeyNameStr + "','" + sixthKeyValStr + "');", null);
            } else {
                mWebView.loadUrl("localStorage.setItem('" + firstKeyNameStr + "','" + firstKeyValStr + "');" +
                        "localStorage.setItem('" + secondKeyNameStr + "','" + secondKeyValStr + "');" +
                        "localStorage.setItem('" + thirdKeyNameStr + "','" + thirdKeyValStr + "');" +
                        "localStorage.setItem('" + fourthKeyNameStr + "','" + fourthKeyValStr + "');" +
                        "localStorage.setItem('" + fifthKeyNameStr + "','" + fifthKeyValStr + "');" +
                        "localStorage.setItem('" + sixthKeyNameStr + "','" + sixthKeyValStr + "');");
            }
        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            stopWebView();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (error.hasError(SSL_UNTRUSTED)) {
                handler.proceed();
            } else {
                super.onReceivedSslError(view, handler, error);
                stopWebView();
            }
        }
    }

    private void stopWebView() {
        DialogManager.getInstance().hideProgress();
        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.loadUrl("");
            mWebView.stopLoading();
        }
    }


    /*To get permission for access read and write storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {

                    webURLLoad();
                }

                public void onNegativeClick() {
                    backScreen();
                }
            });
        }

        return addPermission;
    }
    @Override
    public void onBackPressed() {
        backScreen();
    }
}

