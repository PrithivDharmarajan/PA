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
import com.e2infosystems.activeprotective.input.model.AddBeltEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.output.model.AdminLoginResponse;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminQRBarCodeScanner extends BaseActivity {

    @BindView(R.id.scan_qr_bar_code_lay)
    ViewGroup mScanQRBarCodeViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;

    @BindView(R.id.qr_img)
    ImageView mQRImg;


    private CameraSource mCameraSource;

    private Handler mHandler;
    private Runnable mRunnable;
    private boolean mIsScannedBool = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_admin_qr_barcode_scan);
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
        if (mCameraSource != null)
            mCameraSource.stop();


    }


    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mScanQRBarCodeViewGroup);

        mHeaderTxt.setText(getString(R.string.scan_qr_code));
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
                            mHeaderLay.setPadding(0, getStatusBarHeight(AdminQRBarCodeScanner.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay, R.id.add_manually_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
                onBackPressed();
                break;
            case R.id.add_manually_btn:
                AppConstants.ADD_BELT_DETAILS = new AddBeltEntity();
                nextScreen(AddBelt.class);
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
                    if (askPermissions() && ActivityCompat.checkSelfPermission(AdminQRBarCodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                            String scannedFullDataStrArr[] = (barcode.valueAt(0).displayValue).split("!");

                            ArrayList<String> scannedFullDataArrList = new ArrayList<>(Arrays.asList(scannedFullDataStrArr));
                            boolean isValidDataBool=true;
                            for (int i=0;i<scannedFullDataArrList.size();i++){
                                if ( !mIsScannedBool) {
                                    String scannedFirstDataStrArr[] = scannedFullDataArrList.get(0).split(";");

                                    if (scannedFirstDataStrArr.length != 6 || scannedFirstDataStrArr[0].trim().isEmpty() || scannedFirstDataStrArr[0].length() < 9 || scannedFirstDataStrArr[1].trim().isEmpty()
                                            ||  scannedFirstDataStrArr[2].trim().isEmpty() ||  scannedFirstDataStrArr[3].trim().isEmpty()
                                            ||  scannedFirstDataStrArr[4].trim().isEmpty() || scannedFirstDataStrArr[5].trim().isEmpty()) {
                                        isValidDataBool=false;
                                        break;
                                    }

                                    if((i+1)<scannedFullDataArrList.size()){
                                        String scannedSecondDataStrArr[] = scannedFullDataArrList.get(i+1).split(";");
                                        if (scannedSecondDataStrArr.length !=scannedFirstDataStrArr.length  || !scannedSecondDataStrArr[0].trim().equals(scannedFirstDataStrArr[0].trim())  || !scannedSecondDataStrArr[1].trim().equals(scannedFirstDataStrArr[1].trim())
                                                ||  !scannedSecondDataStrArr[2].trim().equals(scannedFirstDataStrArr[2].trim()) ||  !scannedSecondDataStrArr[3].trim().equals(scannedFirstDataStrArr[3].trim())
                                                ||  !scannedSecondDataStrArr[4].trim().equals(scannedFirstDataStrArr[4].trim()) || !scannedSecondDataStrArr[5].equals(scannedFirstDataStrArr[5].trim())) {
                                            isValidDataBool=false;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (isValidDataBool && !mIsScannedBool) {
                                String scannedDataStrArr[] = scannedFullDataStrArr[0].split(";");
                                     mIsScannedBool = true;
                                    AppConstants.ADD_BELT_DETAILS = new AddBeltEntity();
                                    AppConstants.ADD_BELT_DETAILS.setDeviceId(scannedDataStrArr[0] != null ? scannedDataStrArr[0] : "");
                                    AppConstants.ADD_BELT_DETAILS.setDevMAC(scannedDataStrArr[1] != null ? scannedDataStrArr[1] : "");
                                    AppConstants.ADD_BELT_DETAILS.setDevSSID(scannedDataStrArr[2] != null ? scannedDataStrArr[2] : "");
                                    AppConstants.ADD_BELT_DETAILS.setDevPasswd(scannedDataStrArr[3] != null ? scannedDataStrArr[3] : "");
                                    AppConstants.ADD_BELT_DETAILS.setDevSize(scannedDataStrArr[4] != null ? scannedDataStrArr[4] : "");
                                    AppConstants.ADD_BELT_DETAILS.setDevModal(scannedDataStrArr[5] != null ? scannedDataStrArr[5] : "");
                                    addDeviceAPICall();
                            } else if (!mIsScannedBool) {
                                QRErrorColor();
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

                ArrayList<AddBeltEntity> addDeviceArrEntityList = new ArrayList<>();

                AdminLoginResponse userDetailsEntityRes = PreferenceUtil.getAdminDetails(AdminQRBarCodeScanner.this);
                AddBeltEntity addDeviceArrFirstEntity = new AddBeltEntity();
//                AddBeltEntity addDeviceArrSecondEntity = new AddBeltEntity();

                addDeviceArrFirstEntity.setCommunityId(userDetailsEntityRes.getCommunityId());
                addDeviceArrFirstEntity.setCommunityName(userDetailsEntityRes.getCommunityName());
                addDeviceArrFirstEntity.setAccountId(userDetailsEntityRes.getAccountId());
                addDeviceArrFirstEntity.setDevMAC(AppConstants.ADD_BELT_DETAILS.getDevMAC());
                addDeviceArrFirstEntity.setDevModal(AppConstants.ADD_BELT_DETAILS.getDevModal());
                addDeviceArrFirstEntity.setDevPasswd(AppConstants.ADD_BELT_DETAILS.getDevPasswd());
                addDeviceArrFirstEntity.setDevSSID(AppConstants.ADD_BELT_DETAILS.getDevSSID());
                addDeviceArrFirstEntity.setDevSize(AppConstants.ADD_BELT_DETAILS.getDevSize());
                addDeviceArrFirstEntity.setDeviceId(AppConstants.ADD_BELT_DETAILS.getDeviceId());
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

//                addDeviceArrSecondEntity.setCommunityId(userDetailsEntityRes.getCommunityId());
//                addDeviceArrSecondEntity.setCommunityName(userDetailsEntityRes.getCommunityName());
//                addDeviceArrSecondEntity.setAccountId(userDetailsEntityRes.getAccountId());
//                addDeviceArrSecondEntity.setDevMAC(AppConstants.BELT_DETAILS.getDevMAC());
//                addDeviceArrSecondEntity.setDevModal(AppConstants.BELT_DETAILS.getDevModal());
//                addDeviceArrSecondEntity.setDevPasswd(AppConstants.BELT_DETAILS.getDevPasswd());
//                addDeviceArrSecondEntity.setDevSSID(AppConstants.BELT_DETAILS.getDevSSID());
//                addDeviceArrSecondEntity.setDevSize(AppConstants.BELT_DETAILS.getDevSize());
//                addDeviceArrSecondEntity.setDeviceId(AppConstants.BELT_DETAILS.getDeviceId());
//                addDeviceArrSecondEntity.setLedIntensity(4);
//                addDeviceArrSecondEntity.setSystemAlert(1);
//                addDeviceArrSecondEntity.setUnBuckleAlert(0);
//                addDeviceArrSecondEntity.setBuckleAlert(0);
//                addDeviceArrSecondEntity.setDeviceAlwaysOn(1);
//                addDeviceArrSecondEntity.setUserAlert(1);
//                addDeviceArrSecondEntity.setVibrationLevel(4);
//                addDeviceArrSecondEntity.setVolumeLevel(4);
//                addDeviceArrSecondEntity.setWiFiConfiguredStatus(4);

                addDeviceArrEntityList.add(addDeviceArrFirstEntity);
                addDeviceArrEntityList.add(addDeviceArrFirstEntity);

                APIRequestHandler.getInstance().addDeviceAPICall(addDeviceArrEntityList, AdminQRBarCodeScanner.this);
            }
        });

    }

    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
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
                        if (askPermissions() && ActivityCompat.checkSelfPermission(AdminQRBarCodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            AppConstants.BELT_DEVICE_ID = AppConstants.ADD_BELT_DETAILS.getDeviceId();
            AppConstants.IS_FROM_BELT_LIST_BOOL = false;
            nextScreen(BeltDetails.class);
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
