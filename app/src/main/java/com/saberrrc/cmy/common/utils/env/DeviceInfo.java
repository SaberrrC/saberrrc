package com.saberrrc.cmy.common.utils.env;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.saberrrc.cmy.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class DeviceInfo {

    /**
     * @return String
     * @Title:
     * @Description:移动sim运营商名字
     */

    public static String getSimOperatorName() {
        TelephonyManager telManager = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();

        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 //中国移动
                return "中国移动";

            } else if (imsi.startsWith("46001")) {

                // 中国联通
                return "中国联通";

            } else if (imsi.startsWith("46003")) {

                // 中国电信
                return "中国电信";

            }
        }
        return "unknow";

    }


    /**
     * @return String
     * @Title:
     * @Description:获取手机的IMEI
     */
    public static String getIMEI() {
        String deviceId = "";
        if (App.getInstance().checkCallingOrSelfPermission(permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telManager = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telManager.getDeviceId();
        }
        return deviceId;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取手机型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取手机品牌
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 取得特殊的品牌，比如鼎为的 手机
     *
     * @return
     */
    public static String getSpecilBrand() {
        String version = Build.DISPLAY;
        if (version == null)
            return "";
        String[] arr = version.split("_");
        if (arr == null || arr.length < 2) {
            return "";
        }
        String pid = arr[0];
        /** String cid = arr[1]; **/
        return pid;
    }

    /**
     * 取得特殊的机型，比如鼎为的 手机
     *
     * @return
     */
    public static String getSpecilModel() {
        String version = Build.DISPLAY;
        if (version == null)
            return "";
        String[] arr = version.split("_");
        if (arr == null || arr.length < 2) {
            return "";
        }
        /** String pid = arr[0]; **/
        String cid = arr[1];
        return cid;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取OS版本
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取 国家代码
     */
    public static String getCountoryCode() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

    /**
     * @return String
     * @Title:
     * @Description:获取本机语言类型
     */
    public static String getLanguage() {
        String countoryLanguage = App.getInstance().getResources().getConfiguration().locale.getCountry();
        return countoryLanguage;
    }

    /**
     * @return int
     * @Title:
     * @Description:获取网络类型
     */
    public static int getNetworkType() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

    /**
     * @return int
     * @Title:
     * @Description:获取网络类型
     */
    public static String getNetworkTypeName() {
        ConnectivityManager connManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
        if (networkinfo == null)
            return null;
        int type = networkinfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            String networkName = networkinfo.getExtraInfo();
            if (networkName != null && networkName.length() > 20) {
                networkName = networkName.substring(0, 20);
            }
            return networkName;
        } else {
            return null;
        }
    }

    /**
     * @return String
     * @Title:
     * @Description:移动网路运营商名字
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    /**
     * @return int[], int[0] desity 屏幕密度（像素比例：0.75/1.0/1.5/2.0） , int[1]
     * desityDIP 屏幕密度（每寸像素：120/160/240/320）
     * @Title:
     * @Description:获取屏幕密度
     */
    public static int[] getDesity() {
        int[] desitys = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int desity = (int) dm.density;
        @SuppressWarnings("unused") int desityDPI = dm.densityDpi;
        desitys[0] = desity;
        desitys[1] = desity;
        return desitys;
    }

    /**
     * @return String
     * @Title:
     * @Description:取得SIM 卡的序列号
     */
    public static String getSimNumber() {
        TelephonyManager telManager = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return telManager.getSubscriberId();
    }

    /**
     * 取得设备的标志
     *
     * @return
     * @Title: getDeviceId
     */
    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (null == tm.getDeviceId()) {
            return getSimNumber();
        }
        return tm.getDeviceId();
    }

    public static int[] getDisplayResolution() {
        int[] resolution = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        resolution[0] = width;
        resolution[1] = height;
        return resolution;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取SDK版本
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @return String
     * @Title:
     * @Description:获取本手机号
     */
    public static String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获取CPU核数
     *
     * @return
     */
    public static int getCpuCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Default to return 1 core
        return 1;
    }

    /**
     * 获取Android手机RAM大小
     *
     * @return 返回值的单位是字节
     */
    public static long getTotalMemory() {
        String fileName = "/proc/meminfo";
        String totalMem = "";
        long mTotal = 1;
        BufferedReader localBufferedReader = null;
        try {
            FileReader fr = new FileReader(fileName);
            localBufferedReader = new BufferedReader(fr, 8);
            if ((totalMem = localBufferedReader.readLine()) != null) {
                int begin = totalMem.indexOf(':');
                int end = totalMem.indexOf('k');
                // 采集数量的内存
                totalMem = totalMem.substring(begin + 1, end).trim();
                // 转换为Long型并将得到的内存单位转换为字节
                mTotal = Long.parseLong(totalMem) * 1024;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != localBufferedReader) {
                try {
                    localBufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                localBufferedReader = null;
            }
        }
        return mTotal;
    }
}
