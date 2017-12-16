package vophamtuananh.com.basetest;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.vophamtuananh.base.activity.BaseActivity;
import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.viewmodel.ActivityViewModel;

import vophamtuananh.com.basetest.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding, ActivityViewModel<LifecycleOwner>> {

    private ImageLoader imageLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityComponent component = DaggerMainActivityComponent.builder()
                .testApplicationComponent(TestApplication.get(this).component()).build();
        imageLoader = component.getImageLoader();

        imageLoader.load("http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png")
                .into(mViewDataBinding.ivTest);
    }
}
