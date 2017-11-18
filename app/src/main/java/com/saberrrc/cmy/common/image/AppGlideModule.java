package com.saberrrc.cmy.common.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.saberrrc.cmy.common.net.glide.MyOkHttpUrlLoader;

import java.io.InputStream;

public class AppGlideModule extends OkHttpGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new MyOkHttpUrlLoader.Factory());
        //        super.registerComponents(context, glide);
    }
}