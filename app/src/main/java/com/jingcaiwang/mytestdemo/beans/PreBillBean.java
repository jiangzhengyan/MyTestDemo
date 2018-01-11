package com.jingcaiwang.mytestdemo.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiang_yan on 2017/11/24.
 * <p>
 * 预缴费的  modle
 */

public class PreBillBean implements Serializable {
    private String houseId; // 799,
    private String houseName; // 2,
    private String parkId; // 16,
    private List<PreBillItemBean> billList;

    public List<PreBillItemBean> getBillList() {
        return billList;
    }

    public void setBillList(List<PreBillItemBean> billList) {
        this.billList = billList;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public class PreBillItemBean implements Serializable {

        private String address;  // 北京市北京市大兴区经开电商经开大厦B座12-002,
        private String lowerLimit;  // 50.00,
        private String meterCode;  // 613132132132131,
        private String payMoney;  // 200.00,
        private String prepaymentIcon;  // http;  //private String testjcgj.jingcaiwang.cn;  //8886/upload/image/20171204/20171204163002_991.jpg,
        private String prepaymentId;  // 0,
        private String prepaymentName;  // 中水费,
        private String price;  // 55.00,
        private String surplusNum;  // 23.9,
        private String unit;  // 0,
        private String upperLimit;  // 5000.00,
        private String userIdentityName;  // 咖喱咖喱

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLowerLimit() {
            return lowerLimit;
        }

        public void setLowerLimit(String lowerLimit) {
            this.lowerLimit = lowerLimit;
        }

        public String getMeterCode() {
            return meterCode;
        }

        public void setMeterCode(String meterCode) {
            this.meterCode = meterCode;
        }

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getPrepaymentIcon() {
            return prepaymentIcon;
        }

        public void setPrepaymentIcon(String prepaymentIcon) {
            this.prepaymentIcon = prepaymentIcon;
        }

        public String getPrepaymentId() {
            return prepaymentId;
        }

        public void setPrepaymentId(String prepaymentId) {
            this.prepaymentId = prepaymentId;
        }

        public String getPrepaymentName() {
            return prepaymentName;
        }

        public void setPrepaymentName(String prepaymentName) {
            this.prepaymentName = prepaymentName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSurplusNum() {
            return surplusNum;
        }

        public void setSurplusNum(String surplusNum) {
            this.surplusNum = surplusNum;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(String upperLimit) {
            this.upperLimit = upperLimit;
        }

        public String getUserIdentityName() {
            return userIdentityName;
        }

        public void setUserIdentityName(String userIdentityName) {
            this.userIdentityName = userIdentityName;
        }
    }
}
