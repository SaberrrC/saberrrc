package com.saberrrc.cmy.common.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.saberrrc.cmy.App;
import com.saberrrc.cmy.R;

import java.io.File;

final class ImageProxy implements IImageLoader {

    private static final int HEAD_PLACEHOLDER  = R.mipmap.ic_launcher;
    private static final int IMAGE_PLACEHOLDER = R.mipmap.holder_img;
    private static final int ERROR             = R.mipmap.error_img;

    @Override
    public void displayHeadImage(String url, ImageView imageView) {
        Glide.with(App.getInstance()).load(url).placeholder(HEAD_PLACEHOLDER).error(HEAD_PLACEHOLDER).transform(new GlideCircleTransform(App.getInstance())).into(imageView);
    }

    @Override
    public void displayImage(String url, ImageView imageView) {
        Glide.with(App.getInstance()).load(url).placeholder(IMAGE_PLACEHOLDER).error(ERROR).into(imageView);
    }

    @Override
    public void displayImage(File file, ImageView imageView) {
        Glide.with(App.getInstance()).load(file).placeholder(IMAGE_PLACEHOLDER).error(ERROR).into(imageView);
    }

    @Override
    public void displayImage(int resId, ImageView imageView) {
        Glide.with(App.getInstance()).load(resId).placeholder(IMAGE_PLACEHOLDER).error(ERROR).into(imageView);
    }

    @Override
    public void displayImage(Bitmap bitmap, ImageView imageView) {
        Glide.with(App.getInstance()).load(bitmap).placeholder(IMAGE_PLACEHOLDER).error(ERROR).into(imageView);
    }

    @Override
    public void displayCircleImage(String url, ImageView imageView) {
        Glide.with(App.getInstance()).load(url).placeholder(IMAGE_PLACEHOLDER).error(ERROR).transform(new GlideCircleTransform(App.getInstance())).into(imageView);
    }

    @Override
    public void displayCircleImage(File file, ImageView imageView) {
        Glide.with(App.getInstance()).load(file).placeholder(IMAGE_PLACEHOLDER).error(ERROR).transform(new GlideCircleTransform(App.getInstance())).into(imageView);
    }

    @Override
    public void displayCircleImage(int resId, ImageView imageView) {
        Glide.with(App.getInstance()).load(resId).placeholder(IMAGE_PLACEHOLDER).error(ERROR).transform(new GlideCircleTransform(App.getInstance())).into(imageView);
    }

    @Override
    public void displayCircleImage(Bitmap bitmap, ImageView imageView) {
        Glide.with(App.getInstance()).load(bitmap).placeholder(IMAGE_PLACEHOLDER).error(ERROR).transform(new GlideCircleTransform(App.getInstance())).into(imageView);
    }
}
