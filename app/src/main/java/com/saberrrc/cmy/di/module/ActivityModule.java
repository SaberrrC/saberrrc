package com.saberrrc.cmy.di.module;

import android.app.Activity;

import com.saberrrc.cmy.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }


}
