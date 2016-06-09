package com.nikolaykul.waveadvance.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.databinding.ActivityMainBinding;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.event.OnTapListener;
import com.nikolaykul.waveadvance.ui.base.BaseActivity;
import com.nikolaykul.waveadvance.util.ActivityUtil;
import com.nikolaykul.waveadvance.view.Dot;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainMvpView, OnTapListener {
    @Inject MainPresenter mPresenter;
    private ActivityMainBinding mBinding;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setListeners();
        initToolbar(mBinding.toolbar);
        mPresenter.initWithView(this);
        mBinding.ivTouchable.postDelayed(this::initSource, 1000);
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public void showMaxU(double u) {
        mBinding.tvMax.setText(String.format(Locale.getDefault(), "Max U = %f", u));
    }

    @Override public void showMaxV(double v) {
        mBinding.tvMax.setText(String.format(Locale.getDefault(), "Max V = %f", v));
    }

    @Override public void showNewCoordinate(Pair<Double, Double> coordinate) {
        final double x = coordinate.first;
        final double y = coordinate.second;
        mBinding.ivDrawable.addPoint(new Pair<>((float) x, (float) y));
    }

    @Override public void onSingleTap(Dot dot) {
        Pair<Float, Float> formattedCoordinates = formatCoordinates(dot.getX(), dot.getY());
        final float x = formattedCoordinates.first;
        final float y = formattedCoordinates.second;
        clearViews();
        mPresenter.keepComputingNewCoordinates(x, y, 50, 50);
    }

    @Override public void onDoubleTap(Dot dot) {
        Pair<Float, Float> formattedCoordinates = formatCoordinates(dot.getX(), dot.getY());
        final float x = formattedCoordinates.first;
        final float y = formattedCoordinates.second;
        clearViews();
        mPresenter.stopUpdating();
        mPresenter.updateSourcePosition(x, y);
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    private Pair<Float, Float> formatCoordinates(float x, float y) {
        int width = mBinding.ivTouchable.getWidth();
        int height = mBinding.ivTouchable.getHeight();
        final float newX = x / width * 100;
        final float newY = y / height * 100;
        return new Pair<>(newX, newY);
    }

    private void initToolbar(Toolbar toolbar) {
        toolbar.setTitle(R.string.activity_main_title);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_stop:
                    mPresenter.stopUpdating();
                    break;
            }
            return true;
        });
    }

    private void setListeners() {
        mBinding.ivTouchable.setOnTapListener(this);
        mBinding.btnShowU.setOnClickListener(v -> {
            clearViews();
            mPresenter.computeU();
        });
        mBinding.btnShowV.setOnClickListener(v -> {
            clearViews();
            mPresenter.computeV();
        });
        mBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                ActivityUtil.hideKeyboard(MainActivity.this);
            }

            @Override public void onDrawerOpened(View drawerView) {
            }

            @Override public void onDrawerClosed(View drawerView) {
            }

            @Override public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void initSource() {
        final float x0 = mBinding.ivTouchable.getWidth() / 2f;
        final float y0 = mBinding.ivTouchable.getHeight() / 2f;
        mBinding.ivTouchable.setSourceDot(x0, y0);
        this.onDoubleTap(new Dot(x0, y0));
    }

    private void clearViews() {
        // clear max
        mBinding.tvMax.setText("");
        mPresenter.clearMaxValues();
        // clear image
        mBinding.ivDrawable.clearPoints();
    }

}
