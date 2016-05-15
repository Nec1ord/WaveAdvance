package com.nikolaykul.waveadvance.ui.main;

import android.os.Bundle;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainMvpView {
    @Inject MainPresenter mPresenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.initWithView(this);
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
