package com.ycy.accessibilityservicetest;

import android.app.Application;

/**
 * Created by cxm on 2016/10/26.
 */

public class MyApplication extends Application {

    private boolean mFlag;
    private String params;
    private static MyApplication myApplication = null;
    public static MyApplication getInstance(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public boolean getFlag() {
        return mFlag;
    }

    public void setFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
