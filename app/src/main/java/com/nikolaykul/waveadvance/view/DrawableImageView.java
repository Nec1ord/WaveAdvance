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
    private static final int DEFAULT_RULER_COLOR = Color.WHITE;
    private static final int DEFAULT_LINE_COLOR = Color.RED;
    private ArrayList<Dot> mDots;
    private Paint mLinePaint;
    private Paint mRulerPaint;
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

        // find dx, dy
        final Dot lastDot = mDots.get(mDots.size() - 1);
        final float dx = (getWidth() / 2) - lastDot.getX();
        final float dy = (getHeight() / 2) - lastDot.getY();

        // draw last Dot's ruler and value
        drawRulerWithText(canvas,
                lastDot.getX() + dx, lastDot.getY() + dy,
                lastDot.getX() + "", lastDot.getY() + "");

        // draw all Dots with ruler
        for (int i = 0; i < mDots.size() - 1; i++) {
            final float x1 = mDots.get(i).getX() + dx;
            final float y1 = mDots.get(i).getY() + dy;
            final float x2 = mDots.get(i + 1).getX() + dx;
            final float y2 = mDots.get(i + 1).getY() + dy;
            canvas.drawLine(x1, y1, x2, y2, mLinePaint);
            drawRuler(canvas, x1, y1);
        }
    }

    public void addPoint(Pair<Float, Float> point) {
        mDots.add(new Dot(point.first, point.second));
        invalidate();
    }

    public void clearPoints() {
        mDots.clear();
        invalidate();
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
        mTextBounds = new Rect();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(DEFAULT_LINE_COLOR);
        mLinePaint.setStrokeWidth(5f);

        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setColor(DEFAULT_RULER_COLOR);
        mRulerPaint.setTextSize(DEFAULT_RULER_TEXT_SIZE);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DrawableImageView, 0, 0);
        try {
            float rulerTextSize = ta.getFloat(R.styleable.DrawableImageView_ruler_text_size,
                    DEFAULT_RULER_TEXT_SIZE);
            int rulerColor = ta.getColor(R.styleable.DrawableImageView_ruler_color,
                    DEFAULT_RULER_COLOR);
            float lineSize = ta.getFloat(R.styleable.DrawableImageView_line_size,
                    DEFAULT_LINE_SIZE);
            int lineColor = ta.getInteger(R.styleable.DrawableImageView_line_color,
                    DEFAULT_LINE_COLOR);
            mRulerPaint.setTextSize(rulerTextSize);
            mRulerPaint.setColor(rulerColor);
            mLinePaint.setStrokeWidth(lineSize);
            mLinePaint.setColor(lineColor);
        } finally {
            ta.recycle();
        }
    }


}
