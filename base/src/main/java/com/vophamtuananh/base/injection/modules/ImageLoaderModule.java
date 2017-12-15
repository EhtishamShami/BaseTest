package com.vophamtuananh.base.injection.modules;

import android.content.Context;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.injection.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Module(includes = ContextModule.class)
public final class ImageLoaderModule {

    @Provides
    @ApplicationScope
    public ImageLoader imageLoader(Context context) {
        return new ImageLoader(context);
    }
}
