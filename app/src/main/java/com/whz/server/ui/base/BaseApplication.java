package com.whz.server.ui.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by kevin on 2018/6/24
 */
public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
