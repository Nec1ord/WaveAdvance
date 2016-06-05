package com.nikolaykul.waveadvance.ui.main;

import com.nikolaykul.waveadvance.data.MathManager;
import com.nikolaykul.waveadvance.di.scope.PerActivity;
import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

@PerActivity
public class MainPresenter extends Presenter<MainMvpView> {
    private final MathManager mManager;
    private final CompositeSubscription mSubscriptions;

    @Inject public MainPresenter(MathManager manager) {
        this.mManager = manager;
        this.mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void init() {
        Timber.i("MainPresenter was initialized.");
    }

    @Override
    public void destroy() {
        mSubscriptions.unsubscribe();
    }

    public void updateSourcePosition(float x, float y) {
        mManager.updateSourcePosition(x, y);
    }

    public void keepComputingNewCoordinates(float x, float y, long period, double tDelta) {
        mSubscriptions.clear();
        final Subscription subscription = mManager.updateCoordsByTime(x, y, period, tDelta)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::showNewCoordinate);
        mSubscriptions.add(subscription);
    }

    public void computeNewCoordinate(float x, float y) {
        mSubscriptions.clear();
        final Subscription subscription = mManager.updateCoords(x, y)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::showNewCoordinate);
        mSubscriptions.add(subscription);
    }

}
