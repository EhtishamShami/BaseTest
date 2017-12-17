package vophamtuananh.com.basetest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vophamtuananh.base.imageloader.LoadingImageView;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public class MyLoadingImageView extends AppCompatImageView implements LoadingImageView {


    public MyLoadingImageView(Context context) {
        super(context);
    }

    public MyLoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
