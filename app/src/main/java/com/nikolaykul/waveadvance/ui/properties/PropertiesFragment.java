package com.nikolaykul.waveadvance.ui.properties;

import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseFragment;

public class PropertiesFragment extends BaseFragment {

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
