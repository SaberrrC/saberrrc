package com.saberrrc.cmy.common.net;


import com.saberrrc.cmy.App;
import com.saberrrc.cmy.BuildConfig;
import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.common.utils.NetWorkUtil;
import com.saberrrc.cmy.common.utils.SpUtils;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private final OkHttpClient mClient;

    private OkHttpClient provideClient(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
            builder.addInterceptor(loggingInterceptor);
        }
        File cacheFile = new File(Constant.NETWORK_CACHE_PATH);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 1);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtil.isNetworkConnected(App.getInstance())) {
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Response response = chain.proceed(request);
                if (NetWorkUtil.isNetworkConnected(App.getInstance())) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).removeHeader("Pragma").build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).removeHeader("Pragma").build();
                }
                return response;
            }
        };

        //https
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
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        };


        Interceptor apikey = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder().addHeader("token", SpUtils.getString(App.getInstance(), Constant.TOKEN, "")).build();
                return chain.proceed(request);
            }
        };
        //设置统一的请求头部参数
        builder.addInterceptor(apikey);

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
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.hostnameVerifier(DO_NOT_VERIFY);

        return builder.build();

    }

    private Retrofit createRetrofit(OkHttpClient client, String url) {
        return new Retrofit.Builder().baseUrl(url).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
    }


    private static class RetrofitHelperHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    private RetrofitHelper() {
        OkHttpClient client = new OkHttpClient();
        mClient = provideClient(client.newBuilder());

    }

    public static final RetrofitHelper getInstance() {
        return RetrofitHelper.RetrofitHelperHolder.INSTANCE;
    }

    public OkHttpClient getOkClient() {
        if (mClient != null) {
            return mClient;
        } else {
            return null;
        }
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
