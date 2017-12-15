package vophamtuananh.com.basetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vophamtuananh.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
