package com.nikolaykul.waveadvance.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nikolaykul.waveadvance.R;
import com.nikolaykul.waveadvance.event.OnTapListener;

public class TouchableImageView extends ImageView {
    private Dot mDot;
    private Dot mSourceDot;
    private Paint mPaint;
    private OnTapListener mListener;
    private GestureDetector.OnGestureListener mGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent event) {
                    final float x = event.getX();
                    final float y = event.getY();
                    if (x != mDot.getX() || y != mDot.getY()) {
                        mDot.setX(x);
                        mDot.setY(y);
                        TouchableImageView.this.invalidate();
                        if (null != mListener) mListener.onSingleTap(mDot);
                    }
                    return true;
                }

                @Override public boolean onDoubleTap(MotionEvent event) {
                    final float x = event.getX();
                    final float y = event.getY();
                    if (x != mSourceDot.getX() || y != mSourceDot.getY()) {
                        mSourceDot.setX(x);
                        mSourceDot.setY(y);
                        TouchableImageView.this.invalidate();
                        if (null != mListener) mListener.onDoubleTap(mSourceDot);
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

    public void setSourceDot(float x, float y) {
        mSourceDot.setX(x);
        mSourceDot.setY(y);
        this.invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) return;

        setImageDrawable(getBackground());
        mDot.drawSelf(canvas, mPaint);
        mSourceDot.drawSelf(canvas, mPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        initOnTouchCallback(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mDot = new Dot();
        mSourceDot = new Dot();

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TouchableImageView, 0, 0);
        try {
            int dotRadius = ta.getInteger(R.styleable.TouchableImageView_dot_radius, 20);
            int dotColor = ta.getColor(R.styleable.TouchableImageView_dot_color,
                    ContextCompat.getColor(context, R.color.colorAccent));
            int sourceRadius = ta.getInteger(R.styleable.TouchableImageView_source_dot_radius, 25);
            int sourceColor = ta.getColor(R.styleable.TouchableImageView_source_dot_color,
                    ContextCompat.getColor(context, R.color.purple));
            mDot.setColor(dotColor);
            mDot.setRadius(dotRadius);
            mSourceDot.setColor(sourceColor);
            mSourceDot.setRadius(sourceRadius);
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
