package vophamtuananh.com.basetest.mainactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.vophamtuananh.base.fragment.BaseFragment;
import com.vophamtuananh.base.fragment.BaseFragmentHelper;
import com.vophamtuananh.base.fragment.FragmentProvider;

import javax.inject.Inject;

import vophamtuananh.com.basetest.MyApplication;
import vophamtuananh.com.basetest.R;
import vophamtuananh.com.basetest.fragment.Fragment1;
import vophamtuananh.com.basetest.fragment.Fragment2;
import vophamtuananh.com.basetest.fragment.PushFragment;

public class MainActivity extends AppCompatActivity implements FragmentProvider<BaseFragment> {

    @Inject
    BaseFragmentHelper<BaseFragment> fragmentHelper;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityComponent component = DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .myApplicationComponent(MyApplication.get(this).component())
                .build();

        component.injectMainActivity(this);

        Button button = findViewById(R.id.btn_switch);
        button.setOnClickListener(view -> {
            if (index == 0) {
                fragmentHelper.showFragment(1);
                index = 1;
            } else {
                fragmentHelper.showFragment(0);
                index = 0;
            }
        });

        Button buttonPush = findViewById(R.id.btn_push);
        buttonPush.setOnClickListener(view -> {
            fragmentHelper.pushFragment(new PushFragment());
        });
    }

    @Override
    public void onBackPressed() {
        if (fragmentHelper.popFragment())
            return;
        super.onBackPressed();
    }

    @Override
    public BaseFragment[] getFragments() {
        return new BaseFragment[]{new Fragment1(), new Fragment2()};
    }

    @Override
    public int getContentLayoutId() {
        return R.id.fl_content;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }
}
