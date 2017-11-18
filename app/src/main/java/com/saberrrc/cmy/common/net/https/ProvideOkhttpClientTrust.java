package com.saberrrc.cmy.common.net.https;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;

public class ProvideOkhttpClientTrust {
    private ProvideOkhttpClientTrust() {
    }

    private static ProvideOkhttpClientTrust privder;

    public static ProvideOkhttpClientTrust getInstance() {
        if (privder == null) {
            synchronized (ProvideOkhttpClientTrust.class) {
                if (privder == null) {
                    privder = new ProvideOkhttpClientTrust();
                }
            }
        }
        return privder;
    }
    public OkHttpClient getOkhttpClient(){
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        OkHttpClient client = mBuilder.build();
        return client;
    }
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            // 在这处理证书
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }
}