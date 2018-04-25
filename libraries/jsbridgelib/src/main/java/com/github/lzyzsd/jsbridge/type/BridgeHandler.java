package com.github.lzyzsd.jsbridge.type;

import com.github.lzyzsd.jsbridge.core.CallBackFunction;

public interface BridgeHandler {
	
	void handler(String data, CallBackFunction function);

}
