package com.github.lzyzsd.jsbridge.module;

import com.github.lzyzsd.jsbridge.core.CallBackFunction;

public interface FindNotificationModule {

     void callHandler(String handlerName, String data, CallBackFunction callBack);

}
