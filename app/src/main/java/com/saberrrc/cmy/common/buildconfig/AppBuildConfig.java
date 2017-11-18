package com.saberrrc.cmy.common.buildconfig;

import com.saberrrc.cmy.common.constants.Constant;

public class AppBuildConfig {

    private static HttpConfigType httpConfigType = HttpConfigType.PRD;

    private static AppBuildConfig mInstance;
    public String BASEURL = "";

    /**
     * 请求服务器
     */
    //开发环境
    public static final String BASEURL_DEV = Constant.BASEURL_DEV;
    //测试环境
    public static final String BASEURL_QA  = Constant.BASEURL_QA;
    //正式环境
    public static final String BASEURL_PRD = Constant.BASEURL_PRD;

    public enum HttpConfigType {
        DEV, QA, UAT, PRE, PRD
    }

    public boolean isDebug() {
        switch (httpConfigType) {
            case DEV:
            case QA:
            case UAT:
                return true;
            case PRE:
            case PRD:
                return false;
            default:
                return false;
        }
    }


    public static AppBuildConfig getInstance() {
        if (mInstance == null) {
            mInstance = new AppBuildConfig();
        }
        return mInstance;
    }

    private AppBuildConfig() {
        chooseHttpType(httpConfigType);
    }

    public HttpConfigType getConfigType() {
        return httpConfigType;
    }

    public void chooseHttpType(HttpConfigType httpConfigType) {
        AppBuildConfig.httpConfigType = httpConfigType;
        switch (httpConfigType) {
            case DEV:
                BASEURL = BASEURL_DEV;
                break;
            case QA:
                BASEURL = BASEURL_QA;
                break;
            case UAT:
                //			dsHttp = HTTP_DS_UAT;
                //			break;
            case PRE:
                //			dsHttp = HTTP_DS_PRE;
                //			break;
            case PRD:
                BASEURL = BASEURL_PRD;
                break;
            default:
                break;
        }
    }

    public String getBaseUrl() {
        return BASEURL;
    }
}