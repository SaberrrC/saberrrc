package com.saberrrc.cmy.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.saberrrc.cmy.App;

public class VersionManagementUtil {
    //单例
    private static final VersionManagementUtil INSTANCE = new VersionManagementUtil();

    public static VersionManagementUtil getInstance() {
        return VersionManagementUtil.INSTANCE;
    }


    public static String getVersionName() {
        PackageManager manager = App.getInstance().getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(App.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }

    }

    public static int getVersionCode(Context mContext) {
        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int VersionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2))
            throw new IllegalArgumentException("Invalid parameter!");
        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            int[] number2 = getValue(version2, index2);

            if (number1[0] < number2[0]) {
                return -1;
            } else if (number1[0] > number2[0]) {
                return 1;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if ((index1 < version1.length()))
            return 1;
        else
            return -1;
    }

    private static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;
        return value_index;
    }

}
