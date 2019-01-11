package com.savak.savak.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * @author Aditya Kulkarni
 */

public class App extends Application {

    public static LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
    }
}
