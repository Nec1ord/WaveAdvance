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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.event.OnTapListener;

import timber.log.Timber;

public class TouchableImageView extends ImageView {
    private Pair<Float, Float> mSourceDot;
    private Pair<Float, Float> mDot;
    private int mSourceDotRadius;
    private int mDotRadius;
    private int mSourceDotColor;
    private int mDotColor;
    private Paint mPaint;
    private OnTapListener mListener;
    private GestureDetector.OnGestureListener mGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent event) {
                    final Pair<Float, Float> newDot = new Pair<>(event.getX(), event.getY());
                    if (!newDot.equals(mDot)) {
                        mDot = newDot;
                        TouchableImageView.this.invalidate();
                        if (null != mListener) mListener.onSingleTap(mDot);
                        Timber.i("Dot(%f, %f)", mDot.first, mDot.second);
                    }
                    return true;
                }

                @Override public boolean onDoubleTap(MotionEvent event) {
                    final Pair<Float, Float> newSourceDot = new Pair<>(event.getX(), event.getY());
                    if (!newSourceDot.equals(mSourceDot)) {
                        mSourceDot = newSourceDot;
                        TouchableImageView.this.invalidate();
                        if (null != mListener) mListener.onDoubleTap(mDot);
                        Timber.i("SourceDot(%f, %f)", mSourceDot.first, mSourceDot.second);
                    }
                    return true;
                }
            };

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

    public void setOnTapListener(OnTapListener listener) {
        mListener = listener;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) return;

        setImageDrawable(getBackground());
        drawDot(canvas, mDot, mDotRadius, mDotColor, mPaint);
        drawDot(canvas, mSourceDot, mSourceDotRadius, mSourceDotColor, mPaint);
    }

    private void drawDot(Canvas canvas, Pair<Float, Float> dot, int dotRadius, int paintColor,
                         Paint paint) {
        if (null == dot) return;
        paint.setColor(paintColor);
        canvas.drawCircle(dot.first, dot.second, dotRadius, paint);
    }

    private void init(Context context, AttributeSet attrs) {
        initOnTouchCallback(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TouchableImageView, 0, 0);
        try {
            mSourceDotRadius = ta.getInteger(R.styleable.TouchableImageView_source_dot_radius,
                    25);
            mDotRadius = ta.getInteger(R.styleable.TouchableImageView_dot_radius,
                    20);
            mSourceDotColor = ta.getColor(R.styleable.TouchableImageView_source_dot_color,
                    ContextCompat.getColor(context, R.color.purple));
            mDotColor = ta.getColor(R.styleable.TouchableImageView_dot_color,
                    ContextCompat.getColor(context, R.color.colorAccent));
        } finally {
            ta.recycle();
        }
    }

    private void initOnTouchCallback(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context, mGestureListener);
        setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

}
