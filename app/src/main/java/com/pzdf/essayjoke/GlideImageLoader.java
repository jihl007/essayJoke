package com.pzdf.essayjoke;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pzdf.framelibrary.recyclerview.adapter.ViewHolder;

/**
 * @author jihl
 */
public class GlideImageLoader extends ViewHolder.HolderImageLoader {

    public GlideImageLoader(String imagePath) {
        super(imagePath);
    }

    @Override
    public void displayImage(Context context, ImageView imageView, String imagePath) {
        // Glide 加载图片
        Glide.with(context).load(imagePath).placeholder(R.drawable.ic_discovery_default_channel)
                .centerCrop().into(imageView);
    }
}
