package com.saberrrc.cmy.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.saberrrc.cmy.App;
import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.utils.ActManager;
import com.saberrrc.cmy.common.utils.MPermissionUtils;
import com.saberrrc.cmy.di.component.ActivityComponent;
import com.saberrrc.cmy.di.component.DaggerActivityComponent;
import com.saberrrc.cmy.di.module.ActivityModule;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BasePresenter> extends AutoLayoutActivity implements BaseView {

    @Inject
    protected T        mPresenter;
    protected Activity mContext;
    private   Unbinder mUnBinder;

    public static final String ACTION_HIDE_KEYBROAD = "ACTION_HIDE_KEYBROAD";
    private OnHideKeyBroadListener mOnHideKeyBroadListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = initLayout();
        if (layoutId == 0) {
            throw new RuntimeException("请在 initLayout 方法中传入有效的布局id");
        }
        setContentView(layoutId);
        ActManager.getInstance().addActivity(this);
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initInject();
        // StatusBarUtils.setColor(this, Color.TRANSPARENT);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    public void setOnHideKeyBroadListener(OnHideKeyBroadListener mOnHideKeyBroadListener) {
        this.mOnHideKeyBroadListener = mOnHideKeyBroadListener;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            sendBroadcast(new Intent(ACTION_HIDE_KEYBROAD));
            if (mOnHideKeyBroadListener != null)
                mOnHideKeyBroadListener.onKeyBroadHide();
        }
    }

    protected abstract void initInject();

    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder().appComponent(App.getInstance().getAppComponent()).activityModule(getActivityModule()).build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    /**
     * 初始化布局
     */
    public abstract int initLayout();

    /**
     * 初始化数据
     */
    public abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActManager.getInstance().killActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mUnBinder.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean transAnim = true;

    public void setTransAnim(boolean transAnim) {
        this.transAnim = transAnim;
    }

    @Override
    public void finish() {
        super.finish();
        if (transAnim) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public interface OnHideKeyBroadListener {
        void onKeyBroadHide();
    }
}