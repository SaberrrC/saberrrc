package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.RxPresenter;
import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.common.utils.NetWorkUtil;
import com.saberrrc.cmy.common.net.SubscriberWrapper;
import com.saberrrc.cmy.presenter.Contract.TestContract;

import javax.inject.Inject;

public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    @Inject
    public TestPresenter(Api apiService) {
        super(apiService);
    }


    @Override
    public void getBannerList() {
        apiService.getBannerList().compose(NetWorkUtil.<BannerResultBean>rxSchedulerHelper()).subscribe(new SubscriberWrapper<BannerResultBean>(new SubscriberWrapper.CallBackListener<BannerResultBean>() {
            @Override
            public void onSuccess(String code, BannerResultBean data, String msg) {
                mView.onGetBannerListSuccess(code, data, msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.onGetBannerListFailed(ex, code, msg);
            }
        }));
    }
}
