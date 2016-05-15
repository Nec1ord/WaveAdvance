package com.nikolaykul.waveadvance.ui.main;

import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

import timber.log.Timber;

public class MainPresenter extends Presenter<MainMvpView> {

    @Inject public MainPresenter() {
    }

    @Override
    public void init() {
        Timber.i("MainPresenter was initialized.");
    }

    @Override
    public void destroy() {
        Timber.i("MainPresenter was destroyed.");
    }
}
