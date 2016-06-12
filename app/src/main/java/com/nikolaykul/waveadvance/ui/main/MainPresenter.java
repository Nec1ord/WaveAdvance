package com.nikolaykul.waveadvance.ui.main;

import android.util.Pair;

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
    private double mMaxU;
    private double mMaxV;
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
        clearMaxValues();
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
        resumeUpdating();
    }

    public void computeV() {
        mLastCoordinate = MathManager.Coordinate.V;
        resumeUpdating();
    }

    public void keepComputingNewCoordinates(float x, float y, long period, double tDelta) {
        keepComputingNewCoordinates(mLastCoordinate, x, y, period, tDelta);
    }

    public void clearMaxValues() {
        mMaxU = Double.MIN_VALUE;
        mMaxV = Double.MIN_VALUE;
    }

    public void stopUpdating() {
        mSubscriptions.clear();
    }

    public void resumeUpdating() {
        keepComputingNewCoordinates(mLastCoordinate, mLastX, mLastY, mLastPeriod, mLastDeltaT);
    }

    private void keepComputingNewCoordinates(MathManager.Coordinate which,
                                            float x, float y,
                                            long period, double tDelta) {
        stopUpdating();
        rememberLastData(x, y, period, tDelta);
        final Subscription subscription = mManager.updateCoordsByTime(which, x, y, period, tDelta)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showNewCoordinate,
                        t -> Timber.e(t, "Some error"));
        mSubscriptions.add(subscription);
    }

    private void showNewCoordinate(Pair<Double, Double> coordinate) {
        // show max
        final double newVal = coordinate.second;
        if (mLastCoordinate == MathManager.Coordinate.U && mMaxU < newVal) {
            mMaxU = newVal;
            getMvpView().showMaxU(mMaxU);
        } else if (mLastCoordinate == MathManager.Coordinate.V && mMaxV < newVal) {
            mMaxV = newVal;
            getMvpView().showMaxV(mMaxV);
        }
        // show current
        getMvpView().showNewCoordinate(coordinate);
    }

    private void rememberLastData(float x, float y, long period, double tDelta) {
        mLastX = x;
        mLastY = y;
        mLastPeriod = period;
        mLastDeltaT = tDelta;
    }

}
