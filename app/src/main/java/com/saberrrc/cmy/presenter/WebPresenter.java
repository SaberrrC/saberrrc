package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.presenter.Contract.WebContract;
import com.saberrrc.cmy.common.base.RxPresenter;

import javax.inject.Inject;

public class WebPresenter extends RxPresenter<WebContract.View> implements WebContract.Presenter {
    @Inject
    public WebPresenter(Api apiService) {
        super(apiService);
    }
}
