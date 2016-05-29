package com.nikolaykul.waveadvance.di.component;

import android.content.Context;

import com.nikolaykul.waveadvance.data.MathManager;
import com.nikolaykul.waveadvance.di.module.ApplicationModule;
import com.nikolaykul.waveadvance.di.scope.AppContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @AppContext Context context();
    MathManager mathManager();
}
