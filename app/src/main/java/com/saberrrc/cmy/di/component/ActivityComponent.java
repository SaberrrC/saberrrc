package com.saberrrc.cmy.di.component;

import android.app.Activity;

import com.saberrrc.cmy.di.module.ActivityModule;
import com.saberrrc.cmy.ui.act.MainActivity;
import com.saberrrc.cmy.ui.act.ShowActivity;
import com.saberrrc.cmy.di.PerActivity;
import com.saberrrc.cmy.ui.act.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();
    void inject(MainActivity activity);

    void inject(ShowActivity showActivity);

    void inject(SplashActivity splashActivity);
}
