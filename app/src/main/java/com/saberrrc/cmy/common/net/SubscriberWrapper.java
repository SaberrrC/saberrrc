package com.saberrrc.cmy.common.net;

import android.text.TextUtils;

import com.saberrrc.cmy.App;
import com.saberrrc.cmy.bean.BaseBean;
import com.saberrrc.cmy.common.utils.CommonUtils;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import retrofit2.HttpException;

public class SubscriberWrapper<T extends BaseBean> implements FlowableSubscriber<T> {

    public static final String  ERR_NETWORK_MSG  = "网络异常，请重试";
    public static final String  TOKEN_ERROR      = "用户已在别处登录，请重新登录";
    public static       String  ERR_NETWORK_CODE = "0";
    public              boolean jumpLogin        = true;

    private CallBackListener<T> callBackListener;

    public SubscriberWrapper(CallBackListener<T> callBackListener) {
        this.callBackListener = callBackListener;
    }

    public SubscriberWrapper(CallBackListener<T> callBackListener, boolean jumpLogin) {
        this.callBackListener = callBackListener;
        this.jumpLogin = jumpLogin;
    }

    @Override
    public void onNext(T t) {
        String code = t.getCode();
        String msg = t.getMessage();
        if (TextUtils.equals(code, "401")) {//token失效
            App.isLogin = false;
            if (!jumpLogin) {
                callBackListener.onSuccess(code, t, msg);
                return;
            } else {
                toLoginActivity();
                return;
            }
        }
        if (TextUtils.equals(code, "200") || TextUtils.equals(code, "204")) {
            callBackListener.onSuccess(code, t, msg);
            return;
        }
        callBackListener.onFailed(null, code, msg);
    }

    public void setJumpLogin(boolean isjumpLogin) {
        jumpLogin = isjumpLogin;
    }

    private void toLoginActivity() {
//        ToastUtils.showToast(TOKEN_ERROR);
//        CommonUtils.startFragment(LoginFragment.class, null);
    }

    @Override
    public void onError(Throwable ex) {
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (ex instanceof HttpException) {
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (!CommonUtils.checkNet()) {
            callBackListener.onFailed(ex, ERR_NETWORK_CODE, "网络错误");
            return;
        }
        callBackListener.onFailed(ex, ERR_NETWORK_CODE, ERR_NETWORK_MSG);
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(@NonNull Subscription s) {
        s.request(Long.MAX_VALUE);//表示请求的数量
    }

    public interface CallBackListener<T> {
        void onSuccess(String code, T data, String msg);

        void onFailed(Throwable ex, String code, String msg);
    }
}


