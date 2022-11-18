package com.hustcsproject.BananaPlayer;

import android.app.Application;
import android.content.Context;

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
    }
}
