package com.nikolaykul.waveadvance.ui.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nikolaykul.waveadvance.WaveAdvanceApp;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.di.component.DaggerActivityComponent;
import com.nikolaykul.waveadvance.di.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        injectSelf(createActivityComponent());
    }

    private ActivityComponent createActivityComponent() {
        return DaggerActivityComponent.builder()
                .applicationComponent(WaveAdvanceApp.getApplicationComponent(this))
                .activityModule(new ActivityModule(this))
                .build();
    }

    protected abstract void injectSelf(ActivityComponent component);

}
