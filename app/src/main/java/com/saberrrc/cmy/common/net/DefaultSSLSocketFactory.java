package com.saberrrc.cmy.common.net;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class DefaultSSLSocketFactory {

    public static SSLSocketFactory getSSLSocketFactory(Context context, String keyStoreType, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            if (certificates != null && certificates.length > 0) {
                for (int i = 0; i < certificates.length; i++) {
                    InputStream certificate = context.getResources().openRawResource(certificates[i]);
                    keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));
                    if (certificate != null) {
                        certificate.close();
                    }
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(trustManagerFactory.getTrustManagers());
            sslContext.init(null, wrappedTrustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

        return new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return originalTrustManager.getAcceptedIssuers();
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                try {
                    originalTrustManager.checkClientTrusted(certs, authType);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                try {
                    originalTrustManager.checkServerTrusted(certs, authType);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
            }
        }};
    }
}