package com.vophamtuananh.base.injection.modules;

import android.content.Context;

import com.vophamtuananh.base.injection.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vophamtuananh on 12/15/17.
 */

@Module
public final class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public Context context() {
        return context;
    }
}
