package com.cheng.consult.db.table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cheng on 2017/11/27.
 */

public class Expert implements Serializable {
    //@Id(autoincrement = true)
    private String loginId;//登录id
    private String loginName;//登录名
    private String name;//专家名字
    private String userId;//
    private String userType;//用户类型
    private String status;//是否认证过，200标识已通过认证
    private String imgSrc;
    private String sex;//性别
    private String qq;
    private String phoneNum;
    private String weixin;
    private String age;
    private String area;
    private String expertTime;
    private String goodField;
    private String des;
    private int isFocused;//是否关注该专家，0：未关注；1：已关注

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getExpertTime() {
        return expertTime;
    }

    public void setExpertTime(String expertTime) {
        this.expertTime = expertTime;
    }

    public String getGoodField() {
        return goodField;
    }

    public void setGoodField(String goodField) {
        this.goodField = goodField;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getIsFocused() {
        return isFocused;
    }

    public void setIsFocused(int isFocused) {
        this.isFocused = isFocused;
    }
}
