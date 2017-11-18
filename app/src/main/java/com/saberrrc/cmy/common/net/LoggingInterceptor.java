package com.saberrrc.cmy.common.net;

import com.saberrrc.cmy.common.utils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    Charset charset;

    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();//请求发起的时间
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            try {
                //打印body
                RequestBody body1 = request.body();
                Buffer buffer = new Buffer();
                body1.writeTo(buffer);
                MediaType mediaType = body1.contentType();
                if (mediaType != null) {
                    charset = mediaType.charset(UTF8);
                }
                LogUtil.d("body:" + buffer.readString(charset));
                //打印header
                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        LogUtil.d("headers:" + name + ": " + headers.value(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                LogUtil.d(String.format("发送请求 %s on %s %n%s %nRequestParams:{%s}", request.url(), chain.connection(), request.headers(), sb.toString()));
            }
        } else {
            LogUtil.d(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        LogUtil.d(String.format("接收响应: [%s] %n返回json:【%s】 %.1fms %n%s", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }
}
