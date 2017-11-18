package com.saberrrc.cmy.common.net.callback;

public interface UploadHeadImgListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
