package com.vophamtuananh.base.widgets.loading;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.utils.DisplayUtil;

/**
 * Created by vophamtuananh on 12/5/17.
 */

public class LoadingView extends View {

    private static final String TAG = "LoadingView";

    private static final boolean DEFAULT_BASE_ON_LINEAR = true;
    private static final boolean DEFAULT_SHOULD_SQUARE = false;
    private static final int DEFAULT_PERCENT = 0;

    private boolean mMesureBaseOnLinear;
    private boolean mShouldSquare;
    private int mPercent;

    private static final int DEFAULT_COLOR = Color.parseColor("#DDFFFFFF");
    private static final FiveBallIndicator DEFAULT_INDICATOR = new FiveBallIndicator();

    private static final int MIN_SHOW_TIME = 500;
    private static final int MIN_DELAY = 500;

    private long mStartTime = -1;

    private boolean mPostedHide = false;

    private boolean mPostedShow = false;

    private boolean mDismissed = false;

    private final Runnable mDelayedHide = () -> {
        mPostedHide = false;
        mStartTime = -1;
        setVisibility(View.GONE);
    };

    private final Runnable mDelayedShow = () -> {
        mPostedShow = false;
        if (!mDismissed) {
            mStartTime = System.currentTimeMillis();
            setVisibility(View.VISIBLE);
        }
    };

    private Indicator mIndicator;
    private int mIndicatorColor;

    private boolean mShouldStartAnimationDrawable;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, defStyleAttr, 0);

        String indicatorName = a.getString(R.styleable.LoadingView_indicatorName);
        mIndicatorColor = a.getColor(R.styleable.LoadingView_indicatorColor, DEFAULT_COLOR);
        setIndicator(indicatorName);
        if (mIndicator == null) {
            setIndicator(DEFAULT_INDICATOR);
        }
        mMesureBaseOnLinear = a.getBoolean(R.styleable.LoadingView_lv_measure_linear, DEFAULT_BASE_ON_LINEAR);
        mShouldSquare = a.getBoolean(R.styleable.LoadingView_lv_should_square, DEFAULT_SHOULD_SQUARE);
        mPercent = a.getInt(R.styleable.LoadingView_lv_percent, DEFAULT_PERCENT);
        
        a.recycle();
    }

    public Indicator getIndicator() {
        return mIndicator;
    }

    public void setIndicator(Indicator d) {
        if (mIndicator != d) {
            if (mIndicator != null) {
                mIndicator.setCallback(null);
                unscheduleDrawable(mIndicator);
            }

            mIndicator = d;
            setIndicatorColor(mIndicatorColor);
            if (d != null) {
                d.setCallback(this);
            }
            postInvalidate();
        }
    }

    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;
        mIndicator.setColor(color);
    }

    public void setIndicator(String indicatorName) {
        if (TextUtils.isEmpty(indicatorName)) {
            return;
        }
        StringBuilder drawableClassName = new StringBuilder();
        drawableClassName.append(indicatorName);
        try {
            Class<?> drawableClass = Class.forName(drawableClassName.toString());
            Indicator indicator = (Indicator) drawableClass.newInstance();
            setIndicator(indicator);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Didn't find your class , check the name again !");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        setVisibility(VISIBLE);
    }

    public void smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        setVisibility(GONE);
    }

    public void hide() {
        mDismissed = true;
        removeCallbacks(mDelayedShow);
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            setVisibility(View.GONE);
        } else {
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    public void show() {
        mStartTime = -1;
        mDismissed = false;
        removeCallbacks(mDelayedHide);
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable drawable) {
        return drawable == mIndicator || super.verifyDrawable(drawable);
    }

    void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }

        mShouldStartAnimationDrawable = true;
        postInvalidate();
    }

    void stopAnimation() {
        mIndicator.stop();
        mShouldStartAnimationDrawable = false;
        postInvalidate();
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (verifyDrawable(drawable)) {
            final Rect dirty = drawable.getBounds();
            final int scrollX = getScrollX() + getPaddingLeft();
            final int scrollY = getScrollY() + getPaddingTop();

            invalidate(dirty.left + scrollX, dirty.top + scrollY,
                    dirty.right + scrollX, dirty.bottom + scrollY);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();

        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;

        if (mIndicator != null) {
            final int intrinsicWidth = mIndicator.getIntrinsicWidth();
            final int intrinsicHeight = mIndicator.getIntrinsicHeight();
            final float intrinsicAspect = (float) intrinsicWidth / intrinsicHeight;
            final float boundAspect = (float) w / h;
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    final int width = (int) (h * intrinsicAspect);
                    left = (w - width) / 2;
                    right = left + width;
                } else {
                    final int height = (int) (w * (1 / intrinsicAspect));
                    top = (h - height) / 2;
                    bottom = top + height;
                }
            }
            mIndicator.setBounds(left, top, right, bottom);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    void drawTrack(Canvas canvas) {
        final Drawable d = mIndicator;
        if (d != null) {
            final int saveCount = canvas.save();

            canvas.translate(getPaddingLeft(), getPaddingTop());

            d.draw(canvas);
            canvas.restoreToCount(saveCount);

            if (mShouldStartAnimationDrawable) {
                ((Animatable) d).start();
                mShouldStartAnimationDrawable = false;
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mPercent == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int screenSize = mMesureBaseOnLinear ?
                DisplayUtil.getDeviceWidth(getContext()) : DisplayUtil.getDeviceHeight(getContext());
        final int size = (screenSize * mPercent) / 100;
        if (mMesureBaseOnLinear) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                    mShouldSquare ? MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY) : heightMeasureSpec);
        } else {
            super.onMeasure(mShouldSquare ? MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY) :
                    widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        final int[] state = getDrawableState();
        if (mIndicator != null && mIndicator.isStateful()) {
            mIndicator.setState(state);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (mIndicator != null) {
            mIndicator.setHotspot(x, y);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
        removeCallbacks();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        removeCallbacks(mDelayedHide);
        removeCallbacks(mDelayedShow);
    }
}
