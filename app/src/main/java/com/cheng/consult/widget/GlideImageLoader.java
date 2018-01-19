package com.cheng.consult.widget;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cheng.consult.R;

import java.io.File;

/**
 * Created by cheng on 2018/1/19.
 */

class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
        Glide.with(context).load(Uri.fromFile(new File(path))).error(R.mipmap.default_image).placeholder(R.drawable.location_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
}
