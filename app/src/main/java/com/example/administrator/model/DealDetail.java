package com.example.administrator.model;

/**
 * Created by 高信朋 on 2017/4/6.
 * 交易详情
 */

public class DealDetail {
    private String beginDate = "";       //交易时间

    public String getPublishDate() {
        return publishDate;
    }

    private String publishDate = "";       //交易时间

    public String getEndDate() {
        return endDate;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getMasterPhoneNum() {
        return masterPhoneNum;
    }

    private String endDate = "";       //交易时间

    public String getCarPhone() {
        return carPhone;
    }

    private String carPhone = "";

    public DealDetail(String publishDate,String beginDate, String endDate, String province, String city, String area, String phoneNum, String masterPhoneNum, String detailePos, String mins, String money) {
        this.publishDate = publishDate;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.province = province;
        this.city = city;
        this.area = area;
        this.phoneNum = phoneNum;
        this.masterPhoneNum = masterPhoneNum;
        this.detailePos = detailePos;
        this.mins = mins;
        this.money = money;
    }
    public DealDetail(String publishDate,String beginDate, String endDate, String province, String city, String area, String phoneNum, String masterPhoneNum, String detailePos, String mins, String money,String carPhone) {
        this.publishDate = publishDate;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.province = province;
        this.city = city;
        this.area = area;
        this.phoneNum = phoneNum;
        this.masterPhoneNum = masterPhoneNum;
        this.detailePos = detailePos;
        this.mins = mins;
        this.money = money;
        this.carPhone = carPhone;
    }

    private String province = "";       //交易时间
    private String city = "";       //交易时间
    private String area = "";       //交易时间
    private String phoneNum = "";       //交易时间
    private String masterPhoneNum = "";       //交易时间

    private String detailePos = "";       //车位所在地
    private String mins = "";           //租用时长
    private String money = "";          //总计金额

    //构造器
    public DealDetail(String time, String position, String mins, String money) {
        this.beginDate = time;
        this.detailePos = position;
        this.mins = mins;
        this.money = money;
    }


    public String getBeginDate() {
        return beginDate;
    }

    public String getDetailePos() {
        return detailePos;
    }

    public String getMins() {
        return mins;
    }

    public String getMoney() {
        return money;
    }


}
