package com.saberrrc.cmy.common.constants;


import com.saberrrc.cmy.App;

import java.io.File;

public class Constant {
    /**
     * 接口base url
     * 开发联调：
     * 测试地址：
     */
    //开发环境
    public static final String BASEURL_DEV = "http://120.27.220.25:8089";
    //测试环境
    public static final String BASEURL_QA  = "http://terminal-life-uat.sd-bao.com:8086";
    //正式环境
    public static final String BASEURL_PRD = "https://loanstore.sd-bao.com/app/v1/";

    //图片地址
    public static final String PIC_BASE_URL = "https://loanstore.sd-bao.com/loanstore/";

    public static final String TOKEN              = "TOKEN";
    public static final String NETWORK_CACHE_PATH = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String USER_PHONE_HISTORY = "USER_PHONE_HISTORY";//用于登录界面数据回显
    public static final String USER_PHONE         = "USER_PHONE";//用于登录界面数据回显
    public static final String FORGET_PHONE       = "FORGET_PHONE";
    public static final String NETWORK_ERROR      = "网络错误";
    public static final String NO_MESSAGE         = "暂无消息";
    public static final String MESSAGE_NO_DATA    = "暂无数据";
    public static final String NO_SEARCH          = "暂无搜索结果";
    public static final String SEARCH_HISTORY     = "SEARCH_HISTORY";
    public static final String PLATFORM_INFO      = "PLATFORM_INFO";

    public static class NET {
        public static String HEAD_PARAM = "token";
    }

    public static class SHOW_ACTIVITY {
        public static final String BUNDLE    = "BUNDLE";
        public static final String CLASSNAME = "CLASSNAME";
    }

    public static class LOGIN {
        public static final String TRANSFORM = "TRANSFORM";
        public static final String BACK      = "BACK";
        public static final String FINISH    = "FINISH";
    }

    public static class WebView {
        public static String TITLE = "TITLE";
        public static String URL   = "URL";
    }


}
