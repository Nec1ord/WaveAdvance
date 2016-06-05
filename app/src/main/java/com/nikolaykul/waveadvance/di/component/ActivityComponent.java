package com.nikolaykul.waveadvance.di.component;

import android.app.Activity;
import android.content.Context;

import com.nikolaykul.waveadvance.di.module.ActivityModule;
import com.nikolaykul.waveadvance.di.scope.PerActivity;
import com.nikolaykul.waveadvance.ui.properties.PropertiesFragment;
import com.nikolaykul.waveadvance.ui.main.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(PropertiesFragment fragment);

    Activity activity();

    Context context();
}
