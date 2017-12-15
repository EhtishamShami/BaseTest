package vophamtuananh.com.basetest;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.injection.modules.ImageLoaderModule;
import com.vophamtuananh.base.injection.scopes.ApplicationScope;

import dagger.Component;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@ApplicationScope
@Component(modules = ImageLoaderModule.class)
public interface TestApplicationComponent {

    ImageLoader getImageloader();
}
