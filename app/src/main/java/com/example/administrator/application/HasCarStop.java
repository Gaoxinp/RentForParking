package com.example.administrator.application;

import android.app.Application;
import android.content.Context;

import cn.smssdk.SMSSDK;

/**
 * Created by 高信朋 on 2017/4/10.
 */

public class HasCarStop extends Application {
    public static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "1cd7c8c8dea88", "ecac6bc25c61fe2a976332213ad0a4c3");
        appContext = getApplicationContext();
    }
    public static Context getAppContext() {
        return appContext;
    }
}
