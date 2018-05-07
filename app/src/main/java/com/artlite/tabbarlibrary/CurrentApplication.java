package com.artlite.tabbarlibrary;

import android.app.Application;

import com.artlite.bslibrary.core.BSInstance;

public class CurrentApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BSInstance.init(this);
    }
}
