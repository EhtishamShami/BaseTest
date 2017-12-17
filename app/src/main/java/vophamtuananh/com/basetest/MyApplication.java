package vophamtuananh.com.basetest;

import android.app.Activity;
import android.app.Application;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.injection.modules.ContextModule;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public class MyApplication extends Application {

    public static MyApplication get(Activity activity) {
        return MyApplication.class.cast(activity.getApplication());
    }

    private MyApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerMyApplicationComponent.builder()
                .contextModule(new ContextModule(this)).build();

    }

    public MyApplicationComponent component() {
        return component;
    }

}
