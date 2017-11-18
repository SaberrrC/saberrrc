package com.saberrrc.cmy.common.utils;

import android.widget.Toast;

import com.saberrrc.cmy.App;

public class ToastUtils {

    private static Toast toast;

    public static void showToast(final String text) {
        ThreadUtils.runMain(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(text);
                }
                toast.show();
            }
        });
    }

}
