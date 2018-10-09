package com.jingcaiwang.mytestdemo.beans;

import java.io.Serializable;

/**
 * Created by fanshixiang on 2017/10/26.
 */

public class UpdateInfoBean implements Serializable {
    private int version;  //版本号
    private String updateNotification;  //更新提示
    private boolean ifUpdate ;   //是否强制更新
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

    public Boolean getIfUpdate() {
        return ifUpdate;
    }

    public void setIfUpdate(Boolean ifUpdate) {
        this.ifUpdate = ifUpdate;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public String toString() {
        return "UpdateInfoBean{" +
                "version=" + version +
                ", updateNotification='" + updateNotification + '\'' +
                ", ifUpdate=" + ifUpdate +
                ", serviceUrl='" + serviceUrl + '\'' +
                '}';
    }
}
