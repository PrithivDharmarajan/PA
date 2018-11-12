package com.e2infosystems.activeprotective.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminWelcome extends BaseActivity {

    /*Variable initialization using bind view*/

    @BindView(R.id.admin_welcome_parent_lay)
    ViewGroup mAdminWelcomeParentLay;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_admin_welcome);
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
        setupUI(mAdminWelcomeParentLay);

        /*next screen move*/
        nextScreen();

    }


    private void nextScreen() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                removeHandler();
                nextScreen(BeltList.class);
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
