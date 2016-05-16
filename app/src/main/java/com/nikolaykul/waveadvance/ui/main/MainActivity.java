package com.nikolaykul.waveadvance.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.databinding.ActivityMainBinding;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainMvpView, View.OnTouchListener {
    @Inject MainPresenter mPresenter;
    private ActivityMainBinding mBinding;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.imageView.setOnTouchListener(this);
        initToolbar(mBinding.toolbar);
        mPresenter.initWithView(this);
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        displayCoordinates(x, y);
        return true;
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    private void displayCoordinates(float x, float y) {
        mBinding.tvX.setText(String.format(Locale.getDefault(), "X = %f", x));
        mBinding.tvY.setText(String.format(Locale.getDefault(), "Y = %f", y));
    }

    private void initToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.activity_main_title);
    }

}
