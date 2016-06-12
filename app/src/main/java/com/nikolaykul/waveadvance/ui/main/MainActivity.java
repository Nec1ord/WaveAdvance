package com.nikolaykul.waveadvance.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
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
    private Menu mMenu;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setListeners();
        initToolbar(mBinding.toolbar);
        mPresenter.initWithView(this);
        mBinding.ivTouchable.postDelayed(this::initSource, 1000);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        onResumeUpdating();
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_resume:
                clearViews();
                mPresenter.resumeUpdating();
                break;
            case R.id.action_stop:
                mPresenter.stopUpdating();
                break;
        }
        return true;
    }

    @Override protected void onStop() {
        mPresenter.stopUpdating();
        super.onStop();
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

    @Override public void onResumeUpdating() {
        if (null == mMenu) return;
        mMenu.findItem(R.id.action_resume).setVisible(false);
        mMenu.findItem(R.id.action_stop).setVisible(true);
    }

    @Override public void onStopUpdating() {
        if (null == mMenu) return;
        mMenu.findItem(R.id.action_resume).setVisible(true);
        mMenu.findItem(R.id.action_stop).setVisible(false);
    }

    @Override public void onSingleTap(Dot dot) {
        clearViews();
        mPresenter.keepComputingNewCoordinates(dot.getX(), dot.getY(), 50, 50);
    }

    @Override public void onDoubleTap(Dot dot) {
        clearViews();
        mPresenter.updateSourcePosition(dot.getX(), dot.getY());
    }

    @Override protected void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    private void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(R.string.activity_main_title);
        }
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
        mPresenter.stopUpdating();
        mBinding.tvMax.setText("");
        mBinding.ivDrawable.clearPoints();
    }

}
