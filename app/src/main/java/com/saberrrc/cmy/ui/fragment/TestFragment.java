package com.saberrrc.cmy.ui.fragment;

import android.view.View;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.presenter.Contract.TestContract;
import com.saberrrc.cmy.presenter.TestPresenter;

public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View {

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_test);
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initData() {
        mPresenter.getBannerList();
    }

    @Override
    public void onGetBannerListSuccess(String code, BannerResultBean data, String msg) {

    }

    @Override
    public void onGetBannerListFailed(Throwable ex, String code, String msg) {

    }
}
