package vophamtuananh.com.basetest;

import com.vophamtuananh.base.imageloader.ImageLoader;

import dagger.Component;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Component(modules = MainActivityModule.class, dependencies = TestApplicationComponent.class)
@MainActivtyScope
public interface MainActivityComponent {

    ImageLoader getImageLoader();
}
