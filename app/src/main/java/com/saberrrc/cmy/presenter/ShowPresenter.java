package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.presenter.Contract.ShowContract;
import com.saberrrc.cmy.common.base.RxPresenter;

import javax.inject.Inject;

public class ShowPresenter extends RxPresenter<ShowContract.View> implements ShowContract.Presenter {
    @Inject
    public ShowPresenter(Api apiService) {
        super(apiService);
    }
}
