package com.vophamtuananh.base.imageloader;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by vophamtuananh on 12/19/17.
 */

public class LoadingImageView extends AppCompatImageView implements ILoadingImageView {

    private static final int FADE_IN_TIME = 400;

    private static int TRANSPARENT_COLOR;

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TRANSPARENT_COLOR = ContextCompat.getColor(context, android.R.color.transparent);
    }

    @Override
    public void setResourceId(int resourceId) {
        setImageResource(resourceId);
    }

    @Override
    public void setDrawable(Drawable drawable) {
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new ColorDrawable(TRANSPARENT_COLOR), drawable});
        setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(FADE_IN_TIME);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
