package com.saberrrc.cmy.presenter;

import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.RxPresenter;
import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.common.net.SubscriberWrapper;
import com.saberrrc.cmy.common.utils.NetWorkUtil;
import com.saberrrc.cmy.presenter.Contract.RecyclerViewContract;

import javax.inject.Inject;

public class RecyclerViewPresenter extends RxPresenter<RecyclerViewContract.View> implements RecyclerViewContract.Presenter {
    @Inject
    public RecyclerViewPresenter(Api apiService) {
        super(apiService);
    }


    @Override
    public void getBannerList(final int position) {
        apiService.getBannerList().compose(NetWorkUtil.<BannerResultBean>rxSchedulerHelper()).subscribe(new SubscriberWrapper<BannerResultBean>(new SubscriberWrapper.CallBackListener<BannerResultBean>() {
            @Override
            public void onSuccess(String code, BannerResultBean data, String msg) {
                mView.onGetBannerListSuccess(code, data, msg, position);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.onGetBannerListFailed(ex, code, msg, position);
            }
        }));
    }
}
