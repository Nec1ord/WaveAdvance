package com.nikolaykul.waveadvance.ui.properties;

import com.nikolaykul.waveadvance.data.PropertiesProvider;
import com.nikolaykul.waveadvance.di.scope.PerActivity;
import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

@PerActivity
public class PropertiesPresenter extends Presenter<PropertiesMvpView> {
    private PropertiesProvider mProvider;

    @Inject PropertiesPresenter(PropertiesProvider provider) {
        mProvider = provider;
    }

    @Override public void init() {
        getMvpView().showProperties(mProvider.getAllProperties());
    }

    @Override public void destroy() {

    }


}
