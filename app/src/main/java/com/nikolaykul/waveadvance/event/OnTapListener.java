package com.nikolaykul.waveadvance.event;

import com.nikolaykul.waveadvance.view.Dot;

public interface OnTapListener {
    void onSingleTap(Dot dot);
    void onDoubleTap(Dot dot);
}
