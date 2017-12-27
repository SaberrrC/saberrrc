package com.saberrrc.cmy;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.saberrrc.cmy.common.buildconfig.AppBuildConfig;
import com.saberrrc.cmy.common.crash.Cockroach;
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
        if (!DEBUG) {
            initCrash();
        }
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

    private void initCrash() {

        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            //                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }
}