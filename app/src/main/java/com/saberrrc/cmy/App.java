package com.saberrrc.cmy;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.saberrrc.cmy.common.buildconfig.AppBuildConfig;
import com.saberrrc.cmy.common.crash.Cockroach;
import com.saberrrc.cmy.common.image.okhttp.OkHttpUrlLoader;
import com.saberrrc.cmy.common.net.https.ProvideOkhttpClientTrust;
import com.saberrrc.cmy.common.utils.LogUtil;
import com.saberrrc.cmy.di.component.AppComponent;
import com.saberrrc.cmy.di.component.DaggerAppComponent;
import com.saberrrc.cmy.di.module.AppModule;
import com.saberrrc.cmy.di.module.RetrofitModule;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.InputStream;

public class App extends Application {

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static App app;
    public static boolean isLogin = false;
    private AppComponent appComponent;
    public static final boolean DEBUG = AppBuildConfig.getInstance().isDebug();

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //让Glide能用HTTPS
        Glide.get(this).register(GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(ProvideOkhttpClientTrust.getInstance().getOkhttpClient()));
        app = this;
        initAppComponent();
        initSDK();
        //initLeakCanary();
//        initCrash();
    }

    private void initLeakCanary() {
        //LeakCanary.install(this);
    }

    private void initSDK() {
        // AutoLayoutConifg.getInstance().useDeviceSize();
        // 保存DevicedID
        // JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this);            // 初始化 JPush
        // CommonUtils.netWorkWarranty();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .retrofitModule(new RetrofitModule(this))
                .build();
    }

    private void initCrash() {
        if (DEBUG) {
            return;
        }
        Cockroach.install(new Cockroach.ExceptionHandler() {
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            LogUtil.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }
}