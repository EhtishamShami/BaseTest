package vophamtuananh.com.basetest.mainactivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vophamtuananh.base.imageloader.ImageLoader;
import com.vophamtuananh.base.recyclerview.RecyclerAdapter;

import vophamtuananh.com.basetest.databinding.ItemImageBinding;

/**
 * Created by vophamtuananh on 12/16/17.
 */

public class ImageAdapter extends RecyclerAdapter<ImageAdapter.LoadImageHolder, String> {

    private ImageLoader mImageLoader;

    //@Inject
    public ImageAdapter(ImageComparator comparator, OnItemClickListener onItemClickListener, ImageLoader imageLoader) {
        super(comparator, onItemClickListener);
        mImageLoader = imageLoader;
    }

    @Override
    protected LoadImageHolder getViewHolder(ViewGroup parent, int viewType) {
        ItemImageBinding itemImageBinding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LoadImageHolder(itemImageBinding, mImageLoader);
    }

    public static class LoadImageHolder extends RecyclerAdapter.BaseHolder<ItemImageBinding, String> {

        private ImageLoader imageLoader;

        LoadImageHolder(ItemImageBinding boundView, ImageLoader imageLoader) {
            super(boundView);
            this.imageLoader = imageLoader;
        }

        @Override
        protected void bindData(String model) {
            super.bindData(model);
            imageLoader.load(model).into(boundView.ivImage);
        }
    }
}
