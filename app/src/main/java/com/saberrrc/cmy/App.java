package com.saberrrc.cmy;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.saberrrc.cmy.common.buildconfig.AppBuildConfig;
import com.saberrrc.cmy.common.net.https.ProvideOkhttpClientTrust;
import com.saberrrc.cmy.di.component.AppComponent;
import com.saberrrc.cmy.di.component.DaggerAppComponent;
import com.saberrrc.cmy.di.module.AppModule;
import com.saberrrc.cmy.di.module.RetrofitModule;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.InputStream;

public class App extends Application {

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
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProvideOkhttpClientTrust.getInstance().getOkhttpClient()));
        app = this;
        initAppComponent();
        initSDK();
        initAutoLayout();
        //initLeakCanary();
    }

    private void initLeakCanary() {
        //LeakCanary.install(this);
    }

    private void initAutoLayout() {
        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    private void initSDK() {
        // AutoLayoutConifg.getInstance().useDeviceSize();
        // 保存DevicedID
        // JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this);            // 初始化 JPush
        // CommonUtils.netWorkWarranty();
        // CrashHandler.getInstance().setCustomCrashHanler(app);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).retrofitModule(new RetrofitModule(this)).build();
    }
}