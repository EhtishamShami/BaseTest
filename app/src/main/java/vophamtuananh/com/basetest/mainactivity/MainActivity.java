package vophamtuananh.com.basetest.mainactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.recyclerview.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;


import vophamtuananh.com.basetest.NormalApplication;
import vophamtuananh.com.basetest.R;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    //@Inject
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*MainActivityComponent component = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .myApplicationComponent(MyApplication.get(this).component())
                .build();*/

        //component.injectMainActivity(this);

        ImageComparator imageComparator = new ImageComparator();

        ImageLoader imageLoader = NormalApplication.get(this).imageLoader();

        imageAdapter = new ImageAdapter(imageComparator, this, imageLoader);

        RecyclerView recyclerView = findViewById(R.id.rv_image);

        recyclerView.setAdapter(imageAdapter);

        imageAdapter.update(new ArrayList<>(Arrays.asList(images1)));

    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "Clicked on: " + position, Toast.LENGTH_SHORT).show();
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

}
