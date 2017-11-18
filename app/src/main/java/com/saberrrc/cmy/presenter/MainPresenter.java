package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.presenter.Contract.MainContract;
import com.saberrrc.cmy.common.base.RxPresenter;

import javax.inject.Inject;

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    public MainPresenter(Api apiService) {
        super(apiService);
    }
}