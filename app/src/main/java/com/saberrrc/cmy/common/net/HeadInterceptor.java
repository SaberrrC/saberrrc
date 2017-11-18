package com.saberrrc.cmy.common.net;

import android.content.Context;

import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.common.utils.SpUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {
    private Context context;

    public HeadInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader(Constant.NET.HEAD_PARAM, SpUtils.getString(context, Constant.TOKEN, "")).build();
        return chain.proceed(request);
    }
}