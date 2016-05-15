package com.nikolaykul.waveadvance.di.module;

import android.app.Application;
import android.content.Context;

import com.nikolaykul.waveadvance.di.scope.AppContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @AppContext
    Context provideAppContext() {
        return mApplication;
    }

}
