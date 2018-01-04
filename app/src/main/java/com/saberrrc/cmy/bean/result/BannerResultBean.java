package com.saberrrc.cmy.bean.result;


import com.saberrrc.cmy.bean.BaseBean;

import java.util.List;

/**
 * Created by dell、 on 2017-9-28.
 {
 "code": "200",
 "data": [
 {
 "imgUrl": "banner/3.png",
 "level": "3",
 "platformId": "3"
 },
 {
 "imgUrl": "banner/2.png",
 "level": "2",
 "platformId": "2"
 },
 {
 "imgUrl": "banner/1.png",
 "level": "1",
 "platformId": "1"
 }
 ],
 "message": "查询成功!",
 "version": "0.0.1"
 }
 */

public class BannerResultBean extends BaseBean {

    /**
     * data : [{"imgUrl":"banner/3.png","level":"3","platformId":"3"},{"imgUrl":"banner/2.png","level":"2","platformId":"2"},{"imgUrl":"banner/1.png","level":"1","platformId":"1"}]
     * version : 0.0.1
     */

    private String         version;
    private List<DataBean> data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imgUrl : banner/3.png
         * level : 3
         * platformId : 3
         */

        private String imgUrl;
        private String level;
        private String platformId;
        private String activityId;

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        private String bannerUrl;


        public String getBannerUrl() {
            return bannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPlatformId() {
            return platformId;
        }

        public void setPlatformId(String platformId) {
            this.platformId = platformId;
        }
    }
}
