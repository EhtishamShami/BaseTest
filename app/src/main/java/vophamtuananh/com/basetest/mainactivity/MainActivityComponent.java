package vophamtuananh.com.basetest.mainactivity;

import dagger.Component;
import vophamtuananh.com.basetest.MyApplicationComponent;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Component(modules = MainActivityModule.class, dependencies = MyApplicationComponent.class)
@MainActivityScope
public interface MainActivityComponent {

    void injectMainActivity(MainActivity mainActivity);
}
