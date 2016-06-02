package com.nikolaykul.waveadvance.ui.base;

import android.app.Fragment;
import android.os.Bundle;

import com.nikolaykul.waveadvance.di.HasActivityComponent;
import com.nikolaykul.waveadvance.di.component.ActivityComponent;

public abstract class BaseFragment extends Fragment {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDi();
    }

    private void initDi() {
        if (getActivity() instanceof HasActivityComponent) {
            injectSelf(((HasActivityComponent) getActivity()).getComponent());
        }
    }

    protected abstract void injectSelf(ActivityComponent component);

}
