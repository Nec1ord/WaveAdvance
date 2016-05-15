package com.nikolaykul.waveadvance;

import android.app.Application;
import android.content.Context;

import com.nikolaykul.waveadvance.di.component.ApplicationComponent;
import com.nikolaykul.waveadvance.di.component.DaggerApplicationComponent;
import com.nikolaykul.waveadvance.di.module.ApplicationModule;

import timber.log.Timber;

public class WaveAdvanceApp extends Application {
    private ApplicationComponent mComponent;

    public static ApplicationComponent getApplicationComponent(Context context) {
        return ((WaveAdvanceApp) context.getApplicationContext()).mComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
        initInjector();
    }

    private void initLogging() {
        if (BuildConfig.ENABLE_LOGGING) Timber.plant(new Timber.DebugTree());
    }

    private void initInjector() {
        mComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
