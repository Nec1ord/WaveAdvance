package com.nikolaykul.waveadvance.ui.base;

import android.support.annotation.NonNull;

public abstract class Presenter<T extends MvpView> {
    private T mMvpView;

    protected T getMvpView() {
        return mMvpView;
    }
    public void attachMvpView(@NonNull T mvpView) {
        mMvpView = mvpView;
    }

    public void initWithView(@NonNull T mvpView) {
        attachMvpView(mvpView);
        init();
    }

    public abstract void init();
    public abstract void destroy();
}
