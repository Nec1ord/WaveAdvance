package com.nikolaykul.waveadvance.ui.main;

import android.util.Pair;

import com.nikolaykul.waveadvance.ui.base.MvpView;

public interface MainMvpView extends MvpView {
    void showNewCoordinate(Pair<Double, Double> coordinate);
}