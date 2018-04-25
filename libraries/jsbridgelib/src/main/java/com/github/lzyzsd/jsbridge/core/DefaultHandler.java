package com.github.lzyzsd.jsbridge.core;

import com.github.lzyzsd.jsbridge.type.BridgeHandler;

public class DefaultHandler implements BridgeHandler {

	String TAG = "DefaultHandler";
	
	@Override
	public void handler(String data, CallBackFunction function) {
		if(function != null){
			function.onCallBack("DefaultHandler response data");
		}
	}

}
