package vophamtuananh.com.basetest.mainactivity;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.view.View;

import com.vophamtuananh.base.activity.BaseActivity;
import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.recyclerview.RecyclerAdapter;
import com.vophamtuananh.base.viewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import vophamtuananh.com.basetest.R;
import vophamtuananh.com.basetest.TestApplication;
import vophamtuananh.com.basetest.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding, ActivityViewModel<LifecycleOwner>> implements RecyclerAdapter.OnItemClickListener {

    @Inject
    ImageLoader imageLoader;

    @Inject
    ImageAdapter imageAdapter;

    int index = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityComponent component = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .testApplicationComponent(TestApplication.get(this).component())
                .build();

        component.injectMainActivity(this);


        mViewDataBinding.rvImage.setAdapter(imageAdapter);
        imageAdapter.update(new ArrayList<>(Arrays.asList(images1)));

        mViewDataBinding.btnChange.setOnClickListener(view -> {
            if (index == 0) {
                imageAdapter.update(new ArrayList<>(Arrays.asList(images2)));
                index = 1;
            } else {
                imageAdapter.appenItems(new ArrayList<>(Arrays.asList(images1)));
                index = 0;
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        imageLoader.load(imageAdapter.getDatas().get(position)).into(mViewDataBinding.ivImage);
    }

    private String[] images1 = {
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png"
    };

    private String[] images2 = {
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png",
            "http://admin.minimomentsapp.com//FileUpload/Background/Background_Boho_Announcement_20173207A073211.142452.png"
    };

}
