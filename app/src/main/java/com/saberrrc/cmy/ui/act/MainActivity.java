package com.saberrrc.cmy.ui.act;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.bean.event.TabEvent;
import com.saberrrc.cmy.common.base.BaseActivity;
import com.saberrrc.cmy.common.utils.CommonUtils;
import com.saberrrc.cmy.common.utils.MPermissionUtils;
import com.saberrrc.cmy.common.utils.ToastUtils;
import com.saberrrc.cmy.presenter.Contract.MainContract;
import com.saberrrc.cmy.presenter.MainPresenter;
import com.saberrrc.cmy.ui.fragment.RecyclerViewFragment;
import com.saberrrc.cmy.ui.fragment.TestFragment;
import com.saberrrc.cmy.ui.view.CusFragmentTabHost;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private CusFragmentTabHost mFtTab;
    private static Class[]  mFragmentArray = {TestFragment.class, RecyclerViewFragment.class, TestFragment.class};
    private static String[] mTabTextArray  = {"1", "2", "3"};
    private static int[]    imageArray     = {R.drawable.select_home, R.drawable.select_home, R.drawable.select_home};
    private        long     lastTime       = 0;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        setTransAnim(false);
        initTab();
        CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(MainActivity.this);
            }
        });
    }

    private void initTab() {
        mFtTab = (CusFragmentTabHost) findViewById(R.id.ft_tab);
        for (int i = 0; i < 3; i++) {
            mFtTab.setup(this, getSupportFragmentManager(), R.id.fl_content);
            TabHost.TabSpec tabSpec = mFtTab.newTabSpec(String.valueOf(i));
            View tabView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_main_tab, null, false);
            ImageView ivTabIcon = (ImageView) tabView.findViewById(R.id.iv_tab_icon);
            TextView tvTabText = (TextView) tabView.findViewById(R.id.tv_tab_text);
            ivTabIcon.setBackgroundResource(imageArray[i]);
            tvTabText.setText(mTabTextArray[i]);
            tabSpec.setIndicator(tabView);
            mFtTab.addTab(tabSpec, mFragmentArray[i], null);
        }
        mFtTab.getTabWidget().setDividerDrawable(null);
    }

    public void setCurrentTab(int index, int param) {
        mFtTab.setCurrentTab(index);
        EventBus.getDefault().postSticky(new TabEvent(param));
    }

    @Override
    public void onBackPressed() {
        int currentTab = mFtTab.getCurrentTab();
        if (currentTab != 0) {
            mFtTab.setCurrentTab(0);
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime > 2000) {
            ToastUtils.showToast("再按一次退出");
            lastTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}