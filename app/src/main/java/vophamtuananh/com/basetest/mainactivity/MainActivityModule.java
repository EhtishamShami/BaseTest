package vophamtuananh.com.basetest.mainactivity;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.recyclerview.ItemComparator;
import com.vophamtuananh.base.recyclerview.RecyclerAdapter;

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
    @MainActivtyScope
    public RecyclerAdapter.OnItemClickListener onItemClickListener() {
        return mainActivity;
    }
}
