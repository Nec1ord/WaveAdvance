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
    }

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }

    @Override public void showNewCoordinate(Pair<Double, Double> coordinate) {
        final double x = coordinate.first;
        final double y = coordinate.second;
        mBinding.drawableImageView.addPoint(new Pair<>((float) x, (float) y));
    }

    @Override public void onSingleTap(Dot dot) {
        Pair<Float, Float> formattedCoordinates = formatCoordinates(dot.getX(), dot.getY());
        final float x = formattedCoordinates.first;
        final float y = formattedCoordinates.second;
        displayCoordinates(x, y);
        mBinding.drawableImageView.clearPoints();
        mPresenter.keepComputingNewCoordinates(x, y, 1000, 0.05);
    }

    @Override public void onDoubleTap(Dot dot) {
        Pair<Float, Float> formattedCoordinates = formatCoordinates(dot.getX(), dot.getY());
        final float x = formattedCoordinates.first;
        final float y = formattedCoordinates.second;
        displaySourceCoordinates(x, y);
        mPresenter.updateSourcePosition(x, y);
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    private Pair<Float, Float> formatCoordinates(float x, float y) {
        int width = mBinding.imageView.getWidth();
        int height = mBinding.imageView.getHeight();
        final float newX = x / width * 100;
        final float newY = y / height * 100;
        return new Pair<>(newX, newY);
    }

    private void displaySourceCoordinates(float x, float y) {
        mBinding.tvSourceX.setText(String.format(Locale.getDefault(), "Xo = %f", x));
        mBinding.tvSourceY.setText(String.format(Locale.getDefault(), "Yo = %f", y));
    }

    private void displayCoordinates(float x, float y) {
        mBinding.tvX.setText(String.format(Locale.getDefault(), "X = %f", x));
        mBinding.tvY.setText(String.format(Locale.getDefault(), "Y = %f", y));
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
        mBinding.imageView.setOnTapListener(this);
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

}
