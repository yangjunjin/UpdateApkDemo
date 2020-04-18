package com.heima.updateapkdemo;

import android.app.Application;

/**
 * author : yangjunjin
 * date : 2020/4/18 16:56
 */
public class App extends Application {
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static App getContext() {
        return context;
    }
}
