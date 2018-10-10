package com.jingcaiwang.mytestdemo.beans;

import java.io.Serializable;

/**
 * Created by fanshixiang on 2017/10/26.
 */

public class UpdateInfoBean implements Serializable {
    private int version;  //版本号
    private String updateNotification;  //更新提示
    private String serviceUrl;   //资源地址

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUpdateNotification() {
        return updateNotification;
    }

    public void setUpdateNotification(String updateNotification) {
        this.updateNotification = updateNotification;
    }


    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }


}
