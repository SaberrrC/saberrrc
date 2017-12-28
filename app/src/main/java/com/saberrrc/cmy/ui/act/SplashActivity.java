package com.saberrrc.cmy.ui.act;

import android.content.Intent;
import android.view.View;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.base.BaseActivity;
import com.saberrrc.cmy.common.utils.SpUtils;
import com.saberrrc.cmy.common.utils.ThreadUtils;
import com.saberrrc.cmy.presenter.Contract.SplashContract;
import com.saberrrc.cmy.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {


    private final int DELAY_MILLIS = 0;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        //设置状态栏颜色
        //        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        setTransAnim(false);
        //防止白屏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ThreadUtils.runMainDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = SpUtils.getBoolean(SplashActivity.this, "isFirst", true);
                //                if (isFirst) {
                // CommonUtils.startFragment(GuideFragment.class, null);
                // SplashActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                //                } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                //                }
                finish();
            }
        }, DELAY_MILLIS);
    }
}