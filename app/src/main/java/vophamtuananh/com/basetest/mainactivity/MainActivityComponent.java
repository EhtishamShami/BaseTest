package vophamtuananh.com.basetest.mainactivity;

import dagger.Component;
import vophamtuananh.com.basetest.TestApplicationComponent;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Component(modules = MainActivityModule.class, dependencies = TestApplicationComponent.class)
@MainActivtyScope
public interface MainActivityComponent {

    void injectMainActivity(MainActivity mainActivity);
}
