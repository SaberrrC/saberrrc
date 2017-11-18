package com.saberrrc.cmy.di.component;

import android.content.Context;

import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.di.module.AppModule;
import com.saberrrc.cmy.di.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface AppComponent {
    Context getContext();
    Api getApi();
}
