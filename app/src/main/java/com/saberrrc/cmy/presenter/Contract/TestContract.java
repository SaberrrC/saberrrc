package com.saberrrc.cmy.presenter.Contract;

import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.BasePresenter;
import com.saberrrc.cmy.common.base.BaseView;

public interface TestContract {
    interface View extends BaseView {
        void onGetBannerListSuccess(String code, BannerResultBean data, String msg);

        void onGetBannerListFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void getBannerList();
    }
}
