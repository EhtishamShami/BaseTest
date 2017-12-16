package vophamtuananh.com.basetest.mainactivity;

import android.databinding.BindingAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.recyclerview.ItemComparator;
import com.vophamtuananh.base.recyclerview.RecyclerAdapter;

import javax.inject.Inject;

import vophamtuananh.com.basetest.TestLoadingImageView;
import vophamtuananh.com.basetest.databinding.ItemImageBinding;

/**
 * Created by vophamtuananh on 12/16/17.
 */

public class ImageAdapter extends RecyclerAdapter<ImageAdapter.LoadImageHolder, String> {

    private static ImageLoader mImageLoader;

    @Inject
    public ImageAdapter(ImageComparator comparator, OnItemClickListener onItemClickListener, ImageLoader imageLoader) {
        super(comparator, onItemClickListener);
        mImageLoader = imageLoader;
    }

    @Override
    protected LoadImageHolder getViewHolder(ViewGroup parent, int viewType) {
        ItemImageBinding itemImageBinding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LoadImageHolder(itemImageBinding);
    }

    public static class LoadImageHolder extends RecyclerAdapter.BaseHolder<ItemImageBinding, String> {

        public LoadImageHolder(ItemImageBinding boundView) {
            super(boundView);
        }
    }

    @BindingAdapter("app:load_image")
    public static void loadImage(TestLoadingImageView imageView, String imageUrl) {
        mImageLoader.load(imageUrl).into(imageView);
    }
}
