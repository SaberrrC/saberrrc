package com.saberrrc.cmy.di.module;

import com.github.lzyzsd.jsbridge.core.CallBackFunction;

import dagger.Module;

@Module
public interface FindNotificationModule {

     void callHandlerFunction(String handlerName, String data, CallBackFunction callBack);

}
