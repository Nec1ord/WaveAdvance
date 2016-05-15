package com.nikolaykul.waveadvance.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.nikolaykul.waveadvance.R;

import timber.log.Timber;

public class TouchableImageView extends ImageView implements View.OnTouchListener {
    private Pair<Float, Float> mDot;
    private int mDotRadius;
    private Paint mPaint;

    public TouchableImageView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchableImageView(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public TouchableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public TouchableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) return;

        setImageDrawable(getBackground());
        canvas.drawCircle(mDot.first, mDot.second, mDotRadius, mPaint);
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            final Pair<Float, Float> newDot = new Pair<>(event.getX(), event.getY());
            if (!mDot.equals(newDot)) {
                mDot = newDot;
                Timber.i("Dot(%f, %f)", mDot.first, mDot.second);
                this.invalidate();
            }
        }
        return true;
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TouchableImageView, 0, 0);
        try {
            mDotRadius = ta.getInteger(R.styleable.TouchableImageView_dot_radius,
                    10);
            mPaint.setColor(ta.getColor(R.styleable.TouchableImageView_dot_color,
                    ContextCompat.getColor(context, R.color.colorAccent)));
        } finally {
            ta.recycle();
        }
    }

}
