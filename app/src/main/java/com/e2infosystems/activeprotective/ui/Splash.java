package com.e2infosystems.activeprotective.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Splash extends BaseActivity {

    /*Variable initialization using bind view*/

    @BindView(R.id.parent_lay)
    ViewGroup mSplashViewGroup;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_splash);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(mSplashViewGroup);

        /*next screen move*/
        nextScreenCheck();

    }


    private void nextScreenCheck() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                removeHandler();
                Class<?> nextScreenClass = GeneralWelcome.class;
                if (PreferenceUtil.getBoolPreferenceValue(Splash.this, AppConstants.LOGIN_STATUS)) {
                    nextScreenClass = PreferenceUtil.getBoolPreferenceValue(Splash.this, AppConstants.CURRENT_USER_ADMIN) ? BeltList.class : UserDashboard.class;
                }
                nextScreen(nextScreenClass);

//                nextScreen(WebURL.class);
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler();
    }


}
