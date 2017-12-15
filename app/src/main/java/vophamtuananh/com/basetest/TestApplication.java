package vophamtuananh.com.basetest;

import android.app.Activity;
import android.app.Application;

/**
 * Created by vophamtuananh on 12/15/17.
 */

public class TestApplication extends Application {

    public static TestApplication get(Activity activity) {
        return TestApplication.class.cast(activity.getApplication());
    }

    private TestApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerTestApplicationComponent().builder().
    }
}
