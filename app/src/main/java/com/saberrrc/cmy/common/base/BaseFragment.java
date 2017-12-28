package com.saberrrc.cmy.common.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saberrrc.cmy.App;
import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.common.utils.ActManager;
import com.saberrrc.cmy.common.utils.CommonUtils;
import com.saberrrc.cmy.common.utils.MPermissionUtils;
import com.saberrrc.cmy.common.utils.StatusBarUtils;
import com.saberrrc.cmy.di.component.DaggerFragmentComponent;
import com.saberrrc.cmy.di.component.FragmentComponent;
import com.saberrrc.cmy.di.module.FragmentModule;
import com.saberrrc.cmy.ui.act.ShowActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    @Inject
    protected T        mPresenter;
    private   Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtils.setColor(getActivity(), Color.TRANSPARENT);
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        View view = createView();
        mUnBinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder().appComponent(App.getInstance().getAppComponent()).fragmentModule(getFragmentModule()).build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }

    public abstract View createView();

    protected abstract void initInject();

    public abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    public View creatViewFromId(int id) {
        return LayoutInflater.from(getActivity()).inflate(id, null, false);
    }

    public void finish() {
        getActivity().finish();
    }

    public void killActivity(Class<?> cls) {
        ActManager.getInstance().killActivity(cls);
    }

    public void killActivity(AppCompatActivity activity) {
        ActManager.getInstance().killActivity(activity);
    }

    public void killFragment(Class<?>... cls) {
        ActManager.getInstance().killFragment(cls);
    }

    public static void startFragment(Class clss, Bundle bundle) {
        Intent intent = new Intent(App.getInstance(), ShowActivity.class);
        intent.putExtra(Constant.SHOW_ACTIVITY.BUNDLE, bundle);
        intent.putExtra(Constant.SHOW_ACTIVITY.CLASSNAME, clss);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }

    public void checkPermission(MPermissionUtils.OnPermissionListener listener) {
        CommonUtils.checkPermission(getActivity(), listener);
    }

    public void setTransAnim(boolean transAnim) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setTransAnim(transAnim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}