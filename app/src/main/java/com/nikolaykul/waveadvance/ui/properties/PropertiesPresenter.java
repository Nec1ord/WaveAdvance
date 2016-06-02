package com.nikolaykul.waveadvance.ui.properties;

import com.nikolaykul.waveadvance.data.MathManager;
import com.nikolaykul.waveadvance.di.scope.PerActivity;
import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

@PerActivity
public class PropertiesPresenter extends Presenter<PropertiesMvpView> {
    private MathManager mManager;

    @Inject PropertiesPresenter(MathManager manager) {
        mManager = manager;
    }

    @Override public void init() {
        getMvpView().showProperties(mManager.getProperties());
    }

    @Override public void destroy() {

    }



}
