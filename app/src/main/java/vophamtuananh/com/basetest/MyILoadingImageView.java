package vophamtuananh.com.basetest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.vophamtuananh.base.imageloader.ILoadingImageView;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public class MyILoadingImageView extends AppCompatImageView implements ILoadingImageView {


    public MyILoadingImageView(Context context) {
        super(context);
    }

    public MyILoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyILoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setResourceId(int resourceId) {
        setImageResource(resourceId);
    }

    @Override
    public void setDrawable(Drawable drawable) {
        setImageDrawable(drawable);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
