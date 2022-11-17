package com.hustcsproject.BananaPlayer;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Describe:
 * <p></p>
 *
 */
public class MyApp extends Application {
    public static MyApp instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUm();
    }

    private void initUm() {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.preInit(this, "5fb738981e29ca3d7bdef32a", "Hust301");
        //友盟正式初始化
        UMConfigure.init(this, "5fb738981e29ca3d7bdef32a", "Hust301", UMConfigure.DEVICE_TYPE_PHONE, "");
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
        //选择AUTO页面采集模式，统计SDK基础指标无需手动埋点可自动采集
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

}
