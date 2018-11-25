package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.adapter.AllUserListAdapter;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.AllUserItemListEntityRes;
import com.e2infosystems.activeprotective.output.model.AllUserListResponse;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllUserList extends BaseActivity {

    @BindView(R.id.all_user_parent_lay)
    ViewGroup mAllUserViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.assign_user_list_recycler_view)
    RecyclerView mAssignUserListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_all_user_list);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAllUserViewGroup);

        mHeaderTxt.setText(getString(R.string.assign_user));
        setHeaderAdjustmentView();
        fetchAllUserAPICall();
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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AllUserList.this), 0, 0);
                        }
                    });
                }
            });
        }
    }


    /*Fetch All User API call*/
    private void fetchAllUserAPICall() {
        APIRequestHandler.getInstance().fetchAllUserListAPICall(this);
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay, R.id.add_user_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                backScreen();
                break;
            case R.id.add_user_btn:
                nextScreen(AddUser.class);
                break;

        }
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);

        if (resObj instanceof AllUserListResponse) {
            AllUserListResponse allUserListResponse = (AllUserListResponse) resObj;
            setAllUserListAdapter(allUserListResponse.getData().getItems());
        } else if (resObj instanceof CommonResponse) {
            CommonResponse assignedResponse = (CommonResponse) resObj;

            DialogManager.getInstance().showInfoPopup(this,assignedResponse.getMessage());

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backScreen();
                }
            }, 1000);

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

    private void setAllUserListAdapter(final ArrayList<AllUserItemListEntityRes> allUserItemArrListRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssignUserListRecyclerView.setLayoutManager(new LinearLayoutManager(AllUserList.this));
                mAssignUserListRecyclerView.setAdapter(new AllUserListAdapter(allUserItemArrListRes, AllUserList.this, AllUserList.this));
            }
        });
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}

