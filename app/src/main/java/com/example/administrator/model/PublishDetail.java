package com.example.administrator.model;

/**
 * Created by 高信朋 on 2017/4/13.
 */

public class PublishDetail {
    //车位发布者phoneNum
    private String phoneNum = "";


    //省，直辖市
    private String province = " ";
    //市
    private String city = "";

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getEndDate() {
        return endDate;
    }

    //区县
    private String area = "";
    //详细地址
    private String detailPos = "";

    //发布时间
    private String publishDate = "";
    //出租时间，开始时间
    private String beginDate = "";
    //金额
    private String money = "";
    public String getBeginDate() {
        return beginDate;
    }


    public String getDetailPos() {
        return detailPos;
    }

    public String getDuration() {
        return duration;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getMoney() {
        return money;
    }

    //出租结束时间
    private String endDate = "";

    // 时长
    private String duration = "";

    public String getCarPhone() {
        return carPhone;
    }

    private String carPhone = "";

    public PublishDetail(String phoneNum, String province, String city, String area, String detailPos, String publishDate, String beginDate, String endDate, String duration, String money,String carPhone) {
        this.phoneNum = phoneNum;
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailPos = detailPos;
        this.publishDate = publishDate;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = duration;
        this.money = money;
        this.carPhone = carPhone;
    }
    public PublishDetail(String phoneNum, String province, String city, String area, String detailPos, String publishDate, String beginDate, String endDate, String duration, String money) {
        this.phoneNum = phoneNum;
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailPos = detailPos;
        this.publishDate = publishDate;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = duration;
        this.money = money;
    }

    public PublishDetail(String phoneNum, String beginDate, String detailPos, String duration, String money) {
        this.phoneNum = phoneNum;
        this.beginDate = beginDate;
        this.detailPos = detailPos;
        this.duration = duration;
        this.money = money;
    }


}
