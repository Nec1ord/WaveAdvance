package com.nikolaykul.waveadvance.di.module;

import android.app.Activity;
import android.content.Context;

import com.nikolaykul.waveadvance.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    Context provideContext() {
        return mActivity;
    }

}
