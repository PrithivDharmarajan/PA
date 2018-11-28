package com.e2infosystems.activeprotective.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserQRBarcodeScanner extends BaseActivity {

    @BindView(R.id.scan_qr_bar_code_lay)
    ViewGroup mScanQRBarCodeViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_start_txt)
    TextView mHeaderStartTxt;

    @BindView(R.id.header_center_txt)
    TextView mHeaderCenterTxt;

    @BindView(R.id.header_end_txt)
    TextView mHeaderEndTxt;

    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;

    @BindView(R.id.qr_img)
    ImageView mQRImg;


    private CameraSource mCameraSource;
    private Handler mHandler;
    private Runnable mRunnable;
    private boolean mIsScannedBool = false;
    private String mDeviceIDStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_user_qr_barcode_scan);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sysOut("QR onResume");
        initialiseDetectorsAndSources();
    }


    @Override
    protected void onPause() {
        super.onPause();
        sysOut("QR onPause");
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource.stop();
        }
    }

    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mScanQRBarCodeViewGroup);

        mHeaderStartTxt.setText(getString(R.string.close));
        mHeaderStartTxt.setVisibility(View.VISIBLE);

        mHeaderCenterTxt.setText(getString(R.string.scan_qr_code));

        mHeaderEndTxt.setText(getString(R.string.close));
        mHeaderEndTxt.setVisibility(View.INVISIBLE);

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
                            mHeaderLay.setPadding(0, getStatusBarHeight(UserQRBarcodeScanner.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_txt, R.id.add_manually_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_txt:
                onBackPressed();
                break;
            case R.id.add_manually_btn:
                nextScreen(AddUserBelt.class);
                break;

        }
    }

    private void initialiseDetectorsAndSources() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        mCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (askPermissions() && ActivityCompat.checkSelfPermission(UserQRBarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        mCameraSource.start(mSurfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                sysOut("QR stop");
                if (mCameraSource != null)
                    mCameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() != 0 && !mIsScannedBool) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            {
                                String scannedFullDataStrArr[] = (barcode.valueAt(0).displayValue).split("!");

                                if (scannedFullDataStrArr[0] != null && !mIsScannedBool) {
                                    String scannedDataStrArr[] = scannedFullDataStrArr[0].split(";");

                                    if (scannedDataStrArr.length == 1 && !scannedDataStrArr[0].isEmpty() && scannedDataStrArr[0].length() == 7 && !mIsScannedBool) {
                                        mIsScannedBool = true;
                                        mDeviceIDStr=scannedDataStrArr[0];
                                        addDeviceAPICall();
                                    } else if (!mIsScannedBool) {
                                        QRErrorColor();
                                    }
                                } else if (!mIsScannedBool) {
                                    QRErrorColor();
                                }

                            }


                        }
                    });


                }
            }
        });
    }


    private void QRErrorColor() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mQRImg.setColorFilter(getResources().getColor(R.color.red));
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mIsScannedBool = false;
                        mQRImg.setColorFilter(getResources().getColor(R.color.blue));
                    }
                };

                mHandler = new Handler();
                mHandler.postDelayed(mRunnable, 1500);
            }
        });

    }

    private void changeQRColor() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mQRImg.setColorFilter(getResources().getColor(R.color.red));
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mQRImg.setColorFilter(getResources().getColor(R.color.blue));
                    }
                };

                mHandler = new Handler();
                mHandler.postDelayed(mRunnable, 1000);
            }
        });

    }

    private void addDeviceAPICall() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (vibrator != null) {
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(500);
                    }
                }
                mQRImg.setColorFilter(getResources().getColor(R.color.green));

                FetchDeviceEntity userLoginEntity = new FetchDeviceEntity();
                userLoginEntity.setDeviceId(mDeviceIDStr);
                APIRequestHandler.getInstance().userWearerLoginAPICall(userLoginEntity,UserQRBarcodeScanner.this);


            }
        });

    }

    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    try {
                        if (askPermissions() && ActivityCompat.checkSelfPermission(UserQRBarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            mCameraSource.start(mSurfaceView.getHolder());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                public void onNegativeClick() {
                    backScreen();
                }
            });
        }

        return addPermission;
    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof UserLoginResponse) {
            UserLoginResponse userLoginResponse = (UserLoginResponse) resObj;
            if(userLoginResponse.getData().getItems().size()>0){
                PreferenceUtil.storeUserDetails(this,userLoginResponse.getData().getItems().get(0));
                PreferenceUtil.storeBoolPreferenceValue(UserQRBarcodeScanner.this, AppConstants.LOGIN_STATUS, true);
                nextScreen(UserDashboard.class);
            }else{
                QRErrorColor();
            }
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        if (t.getMessage() != null && !t.getMessage().isEmpty() && !(t instanceof IOException)) {
            DialogManager.getInstance().showAlertPopup(this, t.getMessage(), new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    QRErrorColor();
                }
            });
        } else if (t instanceof IOException) {
            DialogManager.getInstance().showAlertPopup(this,
                    (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            QRErrorColor();
                        }
                    });


        } else {
            QRErrorColor();
        }

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
        backScreen();
    }
}
