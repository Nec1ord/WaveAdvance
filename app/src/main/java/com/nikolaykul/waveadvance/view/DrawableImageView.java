package com.nikolaykul.waveadvance.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ImageView;

import com.nikolaykul.waveadvance.R;

import java.util.ArrayList;

public class DrawableImageView extends ImageView {
    private static final float RULER_SIZE = 10f;
    private static final float DEFAULT_RULER_TEXT_SIZE = 22f;
    private static final float DEFAULT_LINE_SIZE = 5f;
    private static final float DEFAULT_DOT_SIZE = 7f;
    private static final int DEFAULT_RULER_COLOR = Color.WHITE;
    private static final int DEFAULT_LINE_COLOR = Color.RED;
    private static final int DEFAULT_DOT_COLOR = Color.WHITE;
    private ArrayList<Dot> mDots;
    private float mDy;
    private float mDx;
    private float maxY;
    private float minY;
    private Paint mLinePaint;
    private Paint mRulerPaint;
    private Paint mDotPaint;
    private Rect mTextBounds;

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

        // draw all Dots with ruler
        for (int i = 0; i < mDots.size() - 1; i++) {
            final float x1 = mDots.get(i).getX();
            final float y1 = mDots.get(i).getY();
            final float x2 = mDots.get(i + 1).getX();
            final float y2 = mDots.get(i + 1).getY();
            canvas.drawLine(x1, y1, x2, y2, mLinePaint);
            drawRuler(canvas, x1, y1);
        }

        // draw last Dot, it's ruler and value
        final Dot lastDot = mDots.get(mDots.size() - 1);
        canvas.drawPoint(lastDot.getX(), lastDot.getY(), mDotPaint);
        drawRulerWithText(canvas,
                lastDot.getX(), lastDot.getY(),
                lastDot.getX() + "", lastDot.getY() + "");
    }

    public void addPoint(Pair<Float, Float> point) {
        final Dot dot = new Dot(point.first, point.second);
        updateProperties(dot);
        mDots.add(dot);
        updateOrdinate(dot.getY());
        invalidate();
    }

    public void clearPoints() {
        mDots.clear();
        clearProperties();
        invalidate();
    }

    private void updateOrdinate(float y) {
        if (y > maxY) maxY = y;
        if (y < minY) minY = y;
        final float maxDiff = Math.abs(maxY) - Math.abs(minY);
        mDy = (getHeight() / 2f) - (maxDiff / 2f);
    }

    private void updateAbscissa(float currentX, float previousX) {
        mDx -= Math.abs(currentX - previousX);
    }

    private void drawRuler(Canvas canvas, float x, float y) {
        canvas.drawLine(x, getHeight(), x, getHeight() - RULER_SIZE, mRulerPaint);
        canvas.drawLine(0, y, RULER_SIZE, y, mRulerPaint);
    }

    private void drawRulerWithText(Canvas canvas, float x, float y, String xText, String yText) {
        drawRuler(canvas, x, y);
        final float delta = RULER_SIZE + 10f;
        // x
        mRulerPaint.getTextBounds(xText, 0, xText.length(), mTextBounds);
        canvas.drawText(xText, x - mTextBounds.centerX(), getHeight() - delta, mRulerPaint);
        // y
        mRulerPaint.getTextBounds(yText, 0, yText.length(), mTextBounds);
        canvas.drawText(yText, delta, y - mTextBounds.centerY(), mRulerPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(false);
        setFocusableInTouchMode(false);

        mDots = new ArrayList<>();
        clearProperties();
        mTextBounds = new Rect();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(DEFAULT_LINE_COLOR);
        mLinePaint.setStrokeWidth(DEFAULT_LINE_SIZE);

        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setColor(DEFAULT_RULER_COLOR);
        mRulerPaint.setTextSize(DEFAULT_RULER_TEXT_SIZE);

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(DEFAULT_DOT_COLOR);
        mDotPaint.setStrokeWidth(DEFAULT_DOT_SIZE);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DrawableImageView, 0, 0);
        try {
            float rulerTextSize = ta.getFloat(R.styleable.DrawableImageView_div_ruler_text_size,
                    DEFAULT_RULER_TEXT_SIZE);
            int rulerColor = ta.getColor(R.styleable.DrawableImageView_div_ruler_color,
                    DEFAULT_RULER_COLOR);
            float lineSize = ta.getFloat(R.styleable.DrawableImageView_div_line_size,
                    DEFAULT_LINE_SIZE);
            int lineColor = ta.getInteger(R.styleable.DrawableImageView_div_line_color,
                    DEFAULT_LINE_COLOR);
            float dotSize = ta.getFloat(R.styleable.DrawableImageView_div_dot_size,
                    DEFAULT_DOT_SIZE);
            int dotColor = ta.getColor(R.styleable.DrawableImageView_div_dot_color,
                    DEFAULT_DOT_COLOR);
            mRulerPaint.setTextSize(rulerTextSize);
            mRulerPaint.setColor(rulerColor);
            mLinePaint.setStrokeWidth(lineSize);
            mLinePaint.setColor(lineColor);
            mDotPaint.setStrokeWidth(dotSize);
            mDotPaint.setColor(dotColor);
        } finally {
            ta.recycle();
        }
    }

    private void clearProperties() {
        mDy = 0f;
        mDx = 0f;
        minY = Float.MAX_VALUE;
        maxY = Float.MIN_VALUE;
    }

    private void updateProperties(Dot dot) {
        if (mDots.size() < 2 || dot.getX() < (getWidth() * 4 / 5)) return;
        final Dot removedDot = mDots.get(0);
        updateOrdinate(removedDot.getY());
        updateAbscissa(removedDot.getX(), mDots.get(1).getX());
        mDots.remove(0);
    }


}
