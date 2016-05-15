package com.nikolaykul.waveadvance.ui.main;

import android.os.Bundle;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

}
