package com.saberrrc.cmy.common.utils;

import android.content.Context;

public abstract class DefaultOnPermissionListener implements MPermissionUtils.OnPermissionListener {

    private Context mContext;

    public DefaultOnPermissionListener(Context context) {
        this.mContext = context;
    }

    public void onPermissionDenied() {
        MPermissionUtils.showTipsDialog(mContext);
    }

}
