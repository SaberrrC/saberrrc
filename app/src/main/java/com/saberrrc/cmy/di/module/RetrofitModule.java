package com.saberrrc.cmy.di.module;

import android.content.res.AssetManager;

import com.saberrrc.cmy.App;
import com.saberrrc.cmy.BuildConfig;
import com.saberrrc.cmy.common.buildconfig.AppBuildConfig;
import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.common.converter.FastJsonConverterFactory;
import com.saberrrc.cmy.common.net.Api;
import com.saberrrc.cmy.common.net.CacheInterceptor;
import com.saberrrc.cmy.common.net.HeadInterceptor;
import com.saberrrc.cmy.common.net.logging.Level;
import com.saberrrc.cmy.common.net.logging.LoggingInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class RetrofitModule {
    private App context;

    public RetrofitModule(App context) {
        this.context = context;
    }

    @Provides
    public OkHttpClient providesClient(CacheInterceptor cacheInterceptor, HeadInterceptor headInterceptor, Cache cache, SSLContext sslContext, HostnameVerifier DO_NOT_VERIFY) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置统一的请求头部参数
        builder.addInterceptor(headInterceptor);
        //使用自定义的Log拦截器
        builder.addInterceptor(new LoggingInterceptor.Builder().loggable(BuildConfig.DEBUG).setLevel(Level.BASIC).log(Platform.INFO).request("Request").response("Response")
                //.addHeader("version", BuildConfig.VERSION_NAME)
                //.addQueryParam("query", "0")
                .build());

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //设置https
        //        SSLSocketFactory sslSocketFactory = setPemTrust("pem214187085930497.pem");
        //        builder.sslSocketFactory(sslSocketFactory);
        //信任全部https
        builder.hostnameVerifier(DO_NOT_VERIFY);
        builder.sslSocketFactory(sslContext.getSocketFactory());
        return builder.build();
    }

    private SSLSocketFactory setPemTrust(String assetsName) {
        AssetManager assets = App.getInstance().getAssets();
        try {
            InputStream is = assets.open(assetsName);
            SSLSocketFactory cer_shanlin = getCertificates(is);
            return cer_shanlin;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    public SSLContext providesSSLContext() {
        //        jumpTrust();
        return jumpTrust();
//        try {
//            KeyStore localTrustStore = KeyStore.getInstance("BKS");
//            InputStream input = context.getResources().openRawResource(R.raw.island_truststore);
//            localTrustStore.load(input, "island".toCharArray());
//            IslandTrustManager trustManager = new IslandTrustManager(localTrustStore);
//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
//            return sslContext;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    private SSLContext jumpTrust() {
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    @Provides
    public CacheInterceptor providesCacheInterceptor() {
        return new CacheInterceptor(context);
    }

    @Provides
    public HeadInterceptor providesHeadInterceptor() {
        return new HeadInterceptor(context);
    }


    @Provides
    public HostnameVerifier providesHostVef() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        };
    }

    @Provides
    public Cache provideCache() {
        File cacheFile = new File(Constant.NETWORK_CACHE_PATH);
        return new Cache(cacheFile, 1024 * 1024 * 1);
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(AppBuildConfig.getInstance().getBaseUrl())
                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    @Provides
    public Api providesApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    public SSLSocketFactory getCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            return socketFactory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}