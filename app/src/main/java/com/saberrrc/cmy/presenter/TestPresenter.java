package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.common.base.RxPresenter;
import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.presenter.Contract.TestContract;

import javax.inject.Inject;

public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    @Inject
    public TestPresenter(Api apiService) {
        super(apiService);
    }
}
