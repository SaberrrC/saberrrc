package com.saberrrc.cmy.common.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

public interface IImageLoader {
    void displayHeadImage(String url, ImageView imageView);

    void displayImage(String url, ImageView imageView);

    void displayImage(File file, ImageView imageView);

    void displayImage(int resId, ImageView imageView);

    void displayImage(Bitmap bitmap, ImageView imageView);

    void displayCircleImage(String url, ImageView imageView);

    void displayCircleImage(File file, ImageView imageView);

    void displayCircleImage(int resId, ImageView imageView);

    void displayCircleImage(Bitmap bitmap, ImageView imageView);

}
