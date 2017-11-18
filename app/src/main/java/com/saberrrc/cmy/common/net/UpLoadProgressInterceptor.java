package com.saberrrc.cmy.common.net;

import com.saberrrc.cmy.common.net.callback.UploadHeadImgListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UpLoadProgressInterceptor implements Interceptor {
    private UploadHeadImgListener progressListener;

    public UpLoadProgressInterceptor(UploadHeadImgListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }
        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new CountingRequestBody(originalRequest.body(), progressListener))
                .build();
        return chain.proceed(progressRequest);
    }
}
