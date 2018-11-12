package com.e2infosystems.activeprotective.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.e2infosystems.activeprotective.ui.BeltList;
import com.e2infosystems.activeprotective.ui.GeneralWelcome;
import com.e2infosystems.activeprotective.ui.Splash;
import com.e2infosystems.activeprotective.ui.UserDashboard;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.PreferenceUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ActiveProtectiveApplication extends android.app.Application {

    private static boolean activityVisible;
    private static ActiveProtectiveApplication mInstance;

    public static synchronized ActiveProtectiveApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStopped() {
        activityVisible = false;
    }

    public static void activityFinished() {
        activityVisible = false;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

       // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlytics);

        /*init UncaughtException*/
        Thread.setDefaultUncaughtExceptionHandler(new unCaughtException());

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void registerActivityLifecycleCallbacks(
            ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    /*unCaughtException*/
    private class unCaughtException implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Crashlytics.logException(ex);

            /*Restart application*/
            if (activityVisible) {
                Class<?> nextScreenClass = GeneralWelcome.class;
                if (PreferenceUtil.getBoolPreferenceValue(mInstance, AppConstants.LOGIN_STATUS)) {
                    nextScreenClass = PreferenceUtil.getBoolPreferenceValue(mInstance, AppConstants.CURRENT_USER_ADMIN) ? BeltList.class : UserDashboard.class;
                }

                /*for back screen process*/
                AppConstants.PREVIOUS_SCREEN = new ArrayList<>();
                AppConstants.PREVIOUS_SCREEN.add(nextScreenClass.getCanonicalName());

                Intent intent = new Intent(mInstance, nextScreenClass);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }

                Runtime.getRuntime().exit(0);
            }
        }
    }
}
