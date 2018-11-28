package com.e2infosystems.activeprotective.services;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.e2infosystems.activeprotective.input.model.AddBeltEntity;
import com.e2infosystems.activeprotective.input.model.AddUserEntity;
import com.e2infosystems.activeprotective.input.model.DeleteDeviceEntity;
import com.e2infosystems.activeprotective.input.model.FetchDeviceEntity;
import com.e2infosystems.activeprotective.input.model.LoginEntity;
import com.e2infosystems.activeprotective.input.model.AssignUnAssignBeltEntity;
import com.e2infosystems.activeprotective.input.model.UpdateWifiStatusEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.AllUserListResponse;
import com.e2infosystems.activeprotective.output.model.BeltListResponse;
import com.e2infosystems.activeprotective.output.model.CommonResponse;
import com.e2infosystems.activeprotective.output.model.DeleteDeviceResponse;
import com.e2infosystems.activeprotective.output.model.ErrorResponse;
import com.e2infosystems.activeprotective.output.model.AdminLoginResponse;
import com.e2infosystems.activeprotective.output.model.UserLoginResponse;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequestHandler {

    private static APIRequestHandler sInstance = new APIRequestHandler();

    /*Init retrofit for API call*/
    private APICommonInterface mServiceInterface = serviceInterface();

    public static APIRequestHandler getInstance() {
        return sInstance;
    }

    private APICommonInterface serviceInterface() {
        return new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APICommonInterface.class);
    }

    /*Init UnsafeOkHttpClient for API call*/
    private static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*Login API*/
    public void loginAPICall(LoginEntity loginEntity, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.loginAPI(loginEntity).enqueue(new Callback<AdminLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<AdminLoginResponse> call, @NonNull Response<AdminLoginResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new AdminLoginResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdminLoginResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new AdminLoginResponse(), t);
            }
        });
    }

    /*Belt List API*/
    public void beltListAPICall(final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        System.out.println("getAuthorizationToken"+PreferenceUtil.getAuthorizationToken(baseActivity));
        System.out.println("getUserName"+PreferenceUtil.getUserName(baseActivity));
        System.out.println("COMMUNITY_ID"+PreferenceUtil.getStringPreferenceValue(baseActivity, AppConstants.COMMUNITY_ID));
        mServiceInterface.beltListAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), PreferenceUtil.getStringPreferenceValue(baseActivity, AppConstants.COMMUNITY_ID)).enqueue(new Callback<BeltListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BeltListResponse> call, @NonNull Response<BeltListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new BeltListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BeltListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new BeltListResponse(), t);
            }
        });
    }

    /*Delete Device from Belt List API*/
    public void deleteDeviceFromBeltListAPICall(ArrayList<DeleteDeviceEntity> deleteDeviceArrEntityList, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.deleteDeviceFromBeltListAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), deleteDeviceArrEntityList).enqueue(new Callback<DeleteDeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteDeviceResponse> call, @NonNull Response<DeleteDeviceResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new DeleteDeviceResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteDeviceResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new BeltListResponse(), t);
            }
        });
    }

    /*Add Device to Belt List API*/
    public void addDeviceAPICall(ArrayList<AddBeltEntity> addDeviceArrEntityList, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.addDeviceAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), addDeviceArrEntityList).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Fetch Device Details API*/
    public void fetchDeviceAPICall(ArrayList<FetchDeviceEntity> fetchDeviceArrEntityList, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.fetchDeviceAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), fetchDeviceArrEntityList).enqueue(new Callback<BeltListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BeltListResponse> call, @NonNull Response<BeltListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new BeltListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BeltListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new BeltListResponse(), t);
            }
        });
    }

    /*Assign API Call*/
    public void assignBeltAPICall(AssignUnAssignBeltEntity unAssignBeltEntity, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.assignBeltAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), unAssignBeltEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*UnAssign */
    public void unAssignBeltAPICall(AssignUnAssignBeltEntity unAssignBeltEntity, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.unAssignBeltAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), unAssignBeltEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*fetch All users */
    public void fetchAllUserListAPICall( final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.fetchAllUserListAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), PreferenceUtil.getStringPreferenceValue(baseActivity, AppConstants.COMMUNITY_ID)).enqueue(new Callback<AllUserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllUserListResponse> call, @NonNull Response<AllUserListResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new AllUserListResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllUserListResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new AllUserListResponse(), t);
            }
        });
    }


    /*Add User */
    public void addUserAPICall(AddUserEntity addUserEntity, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.addUserAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), addUserEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Network Setup APICall */
    public void networkSetupAPICall(String SSIDStr,String pwdStr, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.networkSetupAPI(AppConstants.NETWORK_SETUP_URL,SSIDStr, pwdStr).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure("", new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure("", t);
            }
        });
    }

    /*updateWifiStatusAPICall */
    public void updateWifiStatusAPICall(UpdateWifiStatusEntity updateWifiStatusEntity, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.updateWifiStatusAPI(PreferenceUtil.getAuthorizationToken(baseActivity), PreferenceUtil.getUserName(baseActivity), updateWifiStatusEntity).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new CommonResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new CommonResponse(), t);
            }
        });
    }

    /*Wearer Login */
    public void userWearerLoginAPICall(FetchDeviceEntity deviceIDResponse, final BaseActivity baseActivity) {
        DialogManager.getInstance().showProgress(baseActivity);
        mServiceInterface.userWearerLoginAPI(deviceIDResponse).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserLoginResponse> call, @NonNull Response<UserLoginResponse> response) {
                DialogManager.getInstance().hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    baseActivity.onRequestSuccess(response.body());
                } else {
                    String errorMsgStr = response.raw().message();
                    try {
                        ErrorResponse errorBodyRes = new Gson().fromJson(Objects.requireNonNull(response.errorBody()).string(), ErrorResponse.class);
                        errorMsgStr = errorBodyRes.getErrorMessage().isEmpty() ? errorMsgStr : errorBodyRes.getErrorMessage();
                    } catch (IOException | JsonParseException e) {
                        e.printStackTrace();
                    }
                    baseActivity.onRequestFailure(new UserLoginResponse(), new Throwable(errorMsgStr));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserLoginResponse> call, @NonNull Throwable t) {
                DialogManager.getInstance().hideProgress();
                baseActivity.onRequestFailure(new UserLoginResponse(), t);
            }
        });
    }
}


