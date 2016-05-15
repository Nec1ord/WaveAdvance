package com.nikolaykul.waveadvance;

import android.app.Application;

import timber.log.Timber;

public class WaveAdvanceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
    }

    private void initLogging() {
        if (BuildConfig.ENABLE_LOGGING) Timber.plant(new Timber.DebugTree());
    }
}
