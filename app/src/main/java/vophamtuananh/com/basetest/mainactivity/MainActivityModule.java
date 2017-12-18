package vophamtuananh.com.basetest.mainactivity;

import com.vophamtuananh.base.fragment.BaseFragment;
import com.vophamtuananh.base.fragment.FragmentProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Module
public class MainActivityModule {

    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    public FragmentProvider<BaseFragment> fragmentProvider() {
        return mainActivity;
    }

    @Provides
    public int shouldPosition() {
        return 0;
    }
}
