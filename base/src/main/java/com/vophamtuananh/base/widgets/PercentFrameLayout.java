package com.vophamtuananh.base.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.vophamtuananh.base.R;
import com.vophamtuananh.base.utils.DisplayUtil;

/**
 * Created by vophamtuananh on 10/5/17.
 */

public class PercentFrameLayout extends FrameLayout {

    private static final boolean DEFAULT_BASE_ON_LINEAR = true;
    private static final boolean DEFAULT_SHOULD_SQUARE = false;
    private static final int DEFAULT_PERCENT = 0;

    private boolean mMesureBaseOnLinear;
    private boolean mShouldSquare;
    private int mPercent;

    public PercentFrameLayout(@NonNull Context context) {
        super(context);
    }

    public PercentFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PercentFrameLayout, defStyleAttr, 0);

        mMesureBaseOnLinear = a.getBoolean(R.styleable.PercentFrameLayout_pfl_measure_linear, DEFAULT_BASE_ON_LINEAR);
        mShouldSquare = a.getBoolean(R.styleable.PercentFrameLayout_pfl_should_square, DEFAULT_SHOULD_SQUARE);
        mPercent = a.getInt(R.styleable.PercentFrameLayout_pfl_percent, DEFAULT_PERCENT);

        a.recycle();
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
}
