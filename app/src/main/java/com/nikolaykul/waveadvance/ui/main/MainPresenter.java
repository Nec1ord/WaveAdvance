package com.nikolaykul.waveadvance.ui.main;

import android.util.Pair;

import com.nikolaykul.waveadvance.ui.base.Presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    public void computeNewCoordinate(Pair<Float, Float> coordinate) {
        final Observable<Double> obsX =
                Observable.defer(() -> Observable.just(functionX(coordinate)));
        final Observable<Double> obsY =
                Observable.defer(() -> Observable.just(functionY(coordinate)));
        Observable.combineLatest(obsX, obsY, Pair::new)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> getMvpView().showLoading())
                .doAfterTerminate(() -> getMvpView().hideLoading())
                .subscribe(getMvpView()::showNewCoordinate);
    }

    private double functionX(Pair<Float, Float> pair) {
        final float x = pair.first;
        final float y = pair.second;
        return Math.abs(Math.exp(x) + Math.exp(y));
    }

    private double functionY(Pair<Float, Float> pair) {
        final float x = pair.first;
        final float y = pair.second;
        return Math.abs(Math.sin(x) + Math.cos(y));
    }

}
