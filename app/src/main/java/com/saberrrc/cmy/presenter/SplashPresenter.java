package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.common.base.RxPresenter;
import com.saberrrc.cmy.presenter.Contract.SplashContract;

import javax.inject.Inject;

public class SplashPresenter extends RxPresenter<SplashContract.View> implements SplashContract.Presenter {
    @Inject
    public SplashPresenter(Api apiService) {
        super(apiService);
    }
}
