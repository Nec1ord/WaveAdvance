package com.nikolaykul.waveadvance.ui.main;

import com.nikolaykul.waveadvance.di.component.ActivityComponent;
import com.nikolaykul.waveadvance.ui.base.BaseFragment;

public class DrawerFragment extends BaseFragment {

    @Override protected void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
