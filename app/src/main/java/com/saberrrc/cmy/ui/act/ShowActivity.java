package com.saberrrc.cmy.ui.act;

import android.content.Intent;
import android.os.Bundle;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.base.BaseActivity;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.presenter.Contract.ShowContract;
import com.saberrrc.cmy.presenter.ShowPresenter;

public class ShowActivity extends BaseActivity<ShowPresenter> implements ShowContract.View {

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_show;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        try {
            Class<BaseFragment> fragmentClass = (Class<BaseFragment>) intent.getSerializableExtra(Constant.SHOW_ACTIVITY.CLASSNAME);
            BaseFragment showFragment = fragmentClass.newInstance();
            Bundle bundle = intent.getBundleExtra(Constant.SHOW_ACTIVITY.BUNDLE);
            if (bundle != null) {
                showFragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_show, showFragment).commitNow();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}