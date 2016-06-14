package com.nikolaykul.waveadvance.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ImageView;

import com.nikolaykul.waveadvance.R;

import java.util.ArrayList;

public class DrawableImageView extends ImageView {
    private static final float DEFAULT_LINE_SIZE = 5f;
    private static final int DEFAULT_LINE_COLOR = Color.RED;
    private ArrayList<Dot> mDots;
    private float mSy;
    private float mDy;
    private float mDx;
    private float maxY;
    private float minY;
    private Paint mLinePaint;

    public DrawableImageView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawableImageView(Context context, AttributeSet attrs, int defStyleAttr,
                             int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public DrawableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public DrawableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mDots.isEmpty()) return;

        canvas.translate(mDx, mDy);

        // draw all Dots
        for (int i = 0; i < mDots.size() - 1; i++) {
            final float x1 = mDots.get(i).getX();
            final float y1 = mDots.get(i).getY() * mSy;
            final float x2 = mDots.get(i + 1).getX();
            final float y2 = mDots.get(i + 1).getY() * mSy;
            canvas.drawLine(x1, y1, x2, y2, mLinePaint);
        }
    }

    public void addPoint(Pair<Float, Float> point) {
        final Dot dot = new Dot(point.first, point.second);
        mDots.add(dot);
        removeLastDot(dot);
        updateProperties();
        invalidate();
    }

    public void clearPoints() {
        mDots.clear();
        clearProperties();
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(false);
        setFocusableInTouchMode(false);

        mDots = new ArrayList<>();
        clearProperties();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(DEFAULT_LINE_COLOR);
        mLinePaint.setStrokeWidth(DEFAULT_LINE_SIZE);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DrawableImageView, 0, 0);
        try {
            float lineSize = ta.getFloat(R.styleable.DrawableImageView_div_line_size,
                    DEFAULT_LINE_SIZE);
            int lineColor = ta.getInteger(R.styleable.DrawableImageView_div_line_color,
                    DEFAULT_LINE_COLOR);
            mLinePaint.setStrokeWidth(lineSize);
            mLinePaint.setColor(lineColor);
        } finally {
            ta.recycle();
        }
    }

    private void clearProperties() {
        mSy = 1f;
        mDy = 0f;
        mDx = 0f;
        minY = Float.MAX_VALUE;
        maxY = Float.MIN_VALUE;
    }

    private void removeLastDot(Dot newDot) {
        if (newDot.getX() < getWidth() || mDots.size() < 2) return;
        updateAbscissa(mDots.get(0).getX(), mDots.get(1).getX());
        mDots.remove(0);
    }

    private void updateProperties() {
        if (mDots.size() < 1) return;
        final Dot lastDot = mDots.get(mDots.size() - 1);
        updateMaxMin(lastDot.getY());
        updateOrdinate();
        updateScale();
    }

    private void updateMaxMin(float y) {
        if (y > maxY) maxY = y;
        if (y < minY) minY = y;
    }

    private void updateOrdinate() {
        if (maxY == Float.MIN_VALUE && minY == Float.MAX_VALUE) return;
        final float maxDiff = Math.abs(maxY) - Math.abs(minY);
        mDy = (getHeight() - maxDiff) / 2f;
    }

    private void updateAbscissa(float currentX, float previousX) {
        mDx -= Math.abs(currentX - previousX);
    }

    private void updateScale() {
        float newSy = (float) Math.pow(10, getScaleFactor());
        if (newSy != 0f) mSy = newSy;
    }

    private int getScaleFactor() {
        if (maxY == Float.MIN_VALUE && minY == Float.MAX_VALUE) return 0;

        final float maxValue = Math.max(Math.abs(maxY), Math.abs(minY));
        final float maxTranslatedValue = maxValue + mDy;

        if (maxTranslatedValue >= getHeight()) {
            return getScaleReduceFactor(maxValue, 10);
        } else if (maxValue < 10) {
            return getScaleIncreaseFactor(maxValue, 10);
        }
        return 0;
    }

    private int getScaleReduceFactor(float number, float lowerBound) {
        if (Math.abs(number) <= lowerBound || number == 0f) return 0;
        return -1 - getScaleReduceFactor(number % 10, lowerBound);
    }

    private int getScaleIncreaseFactor(float number, float upperBound) {
        if (Math.abs(number) >= upperBound || number == 0f) return 0;
        return 1 + getScaleIncreaseFactor(number * 10, upperBound);
    }

}
