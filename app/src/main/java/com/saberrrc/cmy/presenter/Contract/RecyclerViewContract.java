package com.saberrrc.cmy.presenter.Contract;

import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.BasePresenter;
import com.saberrrc.cmy.common.base.BaseView;

public interface RecyclerViewContract {
    interface View extends BaseView {
        void onGetBannerListSuccess(String code, BannerResultBean data, String msg, int position);

        void onGetBannerListFailed(Throwable ex, String code, String msg, int position);
    }

    interface Presenter extends BasePresenter<RecyclerViewContract.View> {
        void getBannerList(int position);
    }
}
