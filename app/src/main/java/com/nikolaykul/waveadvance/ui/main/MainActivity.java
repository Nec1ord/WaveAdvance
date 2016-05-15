package com.nikolaykul.waveadvance.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.databinding.ActivityMainBinding;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, View.OnTouchListener {
    @Inject MainPresenter mPresenter;
    private ActivityMainBinding mBinding;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        mBinding.imageView.setOnTouchListener(this);
        mPresenter.initWithView(this);
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        Timber.i("\nX = %f,\nY = %f", x, y);
        return false;
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
