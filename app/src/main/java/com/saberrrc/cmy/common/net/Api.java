package com.saberrrc.cmy.common.net;

import com.saberrrc.cmy.bean.result.BannerResultBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface Api {

    //获取banner图
    @GET("banner/list")
    Flowable<BannerResultBean> getBannerList();
}
