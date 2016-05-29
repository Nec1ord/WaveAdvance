package com.nikolaykul.waveadvance.event;

import android.util.Pair;

public interface OnTapListener {
    void onSingleTap(Pair<Float, Float> dot);
    void onDoubleTap(Pair<Float, Float> dot);
}
