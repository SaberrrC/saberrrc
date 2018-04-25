package com.saberrrc.cmy.presenter.Contract;

import com.saberrrc.cmy.common.base.BasePresenter;
import com.saberrrc.cmy.common.base.BaseView;

public interface TestContract {
    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<TestContract.View> {
    }
}
