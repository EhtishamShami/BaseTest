package vophamtuananh.com.basetest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vophamtuananh.base.imageloader.LoadingImageView;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public class TestLoadingImageView extends AppCompatImageView implements LoadingImageView {


    public TestLoadingImageView(Context context) {
        super(context);
    }

    public TestLoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLoadingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
