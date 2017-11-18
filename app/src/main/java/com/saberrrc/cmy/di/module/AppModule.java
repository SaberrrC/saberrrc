package com.saberrrc.cmy.di.module;

import android.content.Context;

import com.saberrrc.cmy.App;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    public Context providesContext(){
        return mApp;
    }


}
