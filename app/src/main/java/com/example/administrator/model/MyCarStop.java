package com.example.administrator.model;

/**
 * Created by 高信朋 on 2017/4/12.
 */

public class MyCarStop {
    public String getCarPhone() {
        return carPhone;
    }

    private String carPhone;
    //省/直辖市
    private String province;
    //市
    private String city;
    //区县
    private String area;
    //详细地址
    private String detailPos;
    //出租状态
    private String state;


    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getDetailPos() {
        return detailPos;
    }


    public String getState() {
        return state;
    }

    public MyCarStop(String province, String city, String area, String detailPos,String state) {
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailPos = detailPos;
        this.state = state;
    }
    public MyCarStop(String province, String city, String area, String detailPos,String carPhone,String state) {
        this.province = province;
        this.city = city;
        this.area = area;
        this.detailPos = detailPos;
        this.carPhone = carPhone;
        this.state = state;
    }
}
