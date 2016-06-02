package com.nikolaykul.waveadvance.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Dot {
    private static final float DEFAULT_X = Float.NaN;
    private static final float DEFAULT_Y = Float.NaN;
    private static final int DEFAULT_RADIUS = 20;
    private static final int DEFAULT_COLOR = Color.RED;
    private float mX;
    private float mY;
    private int mRadius;
    private int mColor;

    public Dot() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_RADIUS, DEFAULT_COLOR);
    }

    public Dot(float x, float y, int radius, int color) {
        this.mX = x;
        this.mY = y;
        this.mRadius = radius;
        this.mColor = color;
    }

    public void drawSelf(Canvas canvas, Paint paint) {
        if (DEFAULT_X == mX || DEFAULT_Y == mY) return;

        paint.setColor(mColor);
        canvas.drawCircle(mX, mY, mRadius, paint);
    }

    // getters & setters

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }
}
