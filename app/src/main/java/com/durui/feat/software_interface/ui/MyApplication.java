package com.durui.feat.software_interface.ui;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import timber.log.Timber;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Google工程师推荐的日志插件，接管TAG + Logcat
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //在搭载 Android 5 之前版本的设备上部署包含多个 dex 文件的应用
        MultiDex.install(this);
    }
}
