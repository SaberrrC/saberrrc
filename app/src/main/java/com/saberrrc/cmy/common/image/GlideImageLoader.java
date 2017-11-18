package com.saberrrc.cmy.common.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

public class GlideImageLoader implements IImageLoader {

    private static class ImageLoaderHolder {
        private static final GlideImageLoader INSTANCE = new GlideImageLoader();
    }

    private ImageProxy proxy;

    private GlideImageLoader() {
        proxy = new ImageProxy();
    }

    public static final GlideImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    @Override
    public void displayHeadImage(String url, ImageView imageView) {
        proxy.displayHeadImage(url, imageView);
    }

    @Override
    public void displayImage(String url, ImageView imageView) {
        proxy.displayImage(url, imageView);
    }

    @Override
    public void displayImage(File file, ImageView imageView) {
        proxy.displayImage(file, imageView);
    }

    @Override
    public void displayImage(int resId, ImageView imageView) {
        proxy.displayImage(resId, imageView);
    }

    @Override
    public void displayImage(Bitmap bitmap, ImageView imageView) {
        proxy.displayImage(bitmap, imageView);
    }

    @Override
    public void displayCircleImage(String url, ImageView imageView) {
        proxy.displayCircleImage(url, imageView);
    }

    @Override
    public void displayCircleImage(File file, ImageView imageView) {
        proxy.displayCircleImage(file, imageView);
    }

    @Override
    public void displayCircleImage(int resId, ImageView imageView) {
        proxy.displayCircleImage(resId, imageView);
    }

    @Override
    public void displayCircleImage(Bitmap bitmap, ImageView imageView) {
        proxy.displayCircleImage(bitmap, imageView);
    }
}