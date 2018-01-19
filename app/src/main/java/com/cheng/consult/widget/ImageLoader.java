package com.cheng.consult.widget;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by cheng on 2018/1/19.
 */

public interface ImageLoader {
    void displayImage(Context context, String path, ImageView imageView, int width, int height);
}
