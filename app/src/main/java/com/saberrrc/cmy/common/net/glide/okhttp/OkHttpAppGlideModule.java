package com.saberrrc.cmy.common.net.glide.okhttp;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.saberrrc.cmy.common.net.https.ProvideOkhttpClientTrust;

import java.io.InputStream;

@GlideModule
public class OkHttpAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Log.d("cmy", "registerComponents: ");
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProvideOkhttpClientTrust.getInstance().getOkhttpClient()));
    }

}