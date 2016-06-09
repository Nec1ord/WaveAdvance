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
    private float mLastX;
    private float mLastY;
    private long mLastPeriod;
    private double mLastDeltaT;
    private MathManager.Coordinate mLastCoordinate;

    @Inject public MainPresenter(MathManager manager) {
        this.mManager = manager;
        this.mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void init() {
        mLastCoordinate = MathManager.Coordinate.U;
    }

    @Override
    public void destroy() {
        mSubscriptions.unsubscribe();
    }

    public void updateSourcePosition(float x, float y) {
        mManager.updateSourcePosition(x, y);
    }

    public void computeU() {
        mLastCoordinate = MathManager.Coordinate.U;
        keepComputingNewCoordinates(mLastX, mLastY, mLastPeriod, mLastDeltaT);
    }

    public void computeV() {
        mLastCoordinate = MathManager.Coordinate.V;
        keepComputingNewCoordinates(mLastX, mLastY, mLastPeriod, mLastDeltaT);
    }

    public void keepComputingNewCoordinates(float x, float y, long period, double tDelta) {
        stopUpdating();
        rememberLastData(x, y, period, tDelta);
        final Subscription subscription = mManager.updateCoordsByTime(mLastCoordinate,
                x, y, period, tDelta)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        getMvpView()::showNewCoordinate,
                        t -> Timber.e(t, "Some error"));
        mSubscriptions.add(subscription);
    }

    public void stopUpdating() {
        mSubscriptions.clear();
    }

    private void rememberLastData(float x, float y, long period, double tDelta) {
        mLastX = x;
        mLastY = y;
        mLastPeriod = period;
        mLastDeltaT = tDelta;
    }

}
