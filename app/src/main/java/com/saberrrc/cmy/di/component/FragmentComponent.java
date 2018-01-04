package com.saberrrc.cmy.di.component;

import android.app.Activity;

import com.saberrrc.cmy.ui.fragment.RecyclerViewFragment;
import com.saberrrc.cmy.ui.fragment.WebFragment;
import com.saberrrc.cmy.di.PerFragment;
import com.saberrrc.cmy.di.module.FragmentModule;
import com.saberrrc.cmy.ui.fragment.TestFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject(TestFragment testFragment);

    void inject(WebFragment webFragment);

    void inject(RecyclerViewFragment recyclerViewFragment);
}
