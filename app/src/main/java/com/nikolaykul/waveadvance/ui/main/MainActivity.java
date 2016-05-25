package com.nikolaykul.waveadvance.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.databinding.ActivityMainBinding;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.event.OnTapListener;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainMvpView, OnTapListener {
    @Inject MainPresenter mPresenter;
    private ActivityMainBinding mBinding;
    private MaterialDialog mStub;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.imageView.setOnTapListener(this);
        initToolbar(mBinding.toolbar);
        mStub = new MaterialDialog.Builder(this)
                .content(R.string.message_loading)
                .cancelable(false)
                .build();
        mPresenter.initWithView(this);
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public void showLoading() {
        mStub.show();
    }

    @Override public void hideLoading() {
        mStub.hide();
    }

    @Override public void showNewCoordinate(Pair<Double, Double> coordinate) {
        final double x = coordinate.first;
        final double y = coordinate.second;
        final String msg = String.format(Locale.getDefault(), "X = %f, Y = %f", x, y);
        mBinding.tvResult.setText(msg);
    }

    @Override public void onSingleTap(Pair<Float, Float> dot) {
        displayCoordinates(dot.first, dot.second);
    }

    @Override public void onDoubleTap(Pair<Float, Float> dot) {
        displayCoordinates(dot.first, dot.second);
        mPresenter.computeNewCoordinate(dot);
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
